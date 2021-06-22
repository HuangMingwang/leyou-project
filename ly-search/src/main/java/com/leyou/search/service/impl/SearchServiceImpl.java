package com.leyou.search.service.impl;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.starter.elastic.entity.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.leyou.search.constants.BaseSearchConstants.*;

/**
 * @author Huang Mingwang
 * @create 2021-05-31 10:02 上午
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final ItemClient itemClient;

    private final GoodsRepository goodsRepository;

    public SearchServiceImpl(ItemClient itemClient, GoodsRepository goodsRepository) {
        this.itemClient = itemClient;
        this.goodsRepository = goodsRepository;
    }

    @Override
    public void createIndexAndMapping() {
        try {
            goodsRepository.deleteIndex();
        } catch (Exception e) {
            log.error("删除失败,索引库可能不存在哦");
        }

        goodsRepository.createIndex("{\n" +
                "  \"settings\": {\n" +
                "    \"analysis\": {\n" +
                "      \"analyzer\": {\n" +
                "        \"my_pinyin\": {\n" +
                "          \"tokenizer\": \"ik_smart\",\n" +
                "          \"filter\": [\n" +
                "            \"py\"\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"filter\": {\n" +
                "        \"py\": {\n" +
                "\t\t  \"type\": \"pinyin\",\n" +
                "          \"keep_full_pinyin\": true,\n" +
                "          \"keep_joined_full_pinyin\": true,\n" +
                "          \"keep_original\": true,\n" +
                "          \"limit_first_letter_length\": 16,\n" +
                "          \"remove_duplicated_term\": true\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"suggestion\": {\n" +
                "        \"type\": \"completion\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"title\":{\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"image\":{\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": false\n" +
                "      },\n" +
                "      \"updateTime\":{\n" +
                "        \"type\": \"date\"\n" +
                "      },\n" +
                "      \"specs\":{\n" +
                "        \"type\": \"nested\",\n" +
                "        \"properties\": {\n" +
                "          \"name\":{\"type\": \"keyword\" },\n" +
                "          \"value\":{\"type\": \"keyword\" }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }


    @Override
    public void loadData() {
        int page = 1, size = 100;

        while (true) {
            PageDTO<SpuDTO> pageDTO = itemClient.querySpuByPage(page, size, true, null, null, null);
            List<Goods> goods = pageDTO.getItems().stream()
                    .map(this::spuDTOToGoods)
                    .collect(Collectors.toList());
            goodsRepository.saveAll(goods);
            page++;
            if (page > pageDTO.getTotalPage()) {
                break;
            }
        }
    }

    @Override
    public Mono<List<String>> getSuggestion(String key) {
        /**
         * 根据模板查询自动补全
         */
        HashMap<String, Object> map = new HashMap<>();
        map.put(SUGGESTION_PARAM_PREFIX_KEY, key);
        map.put(SUGGESTION_PARAM_FIELD, SUGGESTION_FIELD);
        return goodsRepository.suggestByTemplate(SUGGESTION_TEMPLATE_ID, map);
    }

    public SearchSourceBuilder getCommonSearch(SearchParamDTO searchParamDTO) {
        //先是一个Bool查询
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分词查询
        if (StringUtils.isEmpty(searchParamDTO.getKey())) {
            //如果为空,查询所有
            sourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery(DEFAULT_SEARCH_FIELD, searchParamDTO.getKey()));
        sourceBuilder.query(boolQuery);
        if (CollectionUtils.isEmpty(searchParamDTO.getFilters())) {
            // 没有，直接返回
            return sourceBuilder;
        }
        /**
         * 加上过滤项查询, 过滤项分为品牌\分类\及spec
         * spec用nested中
         * 品牌和分类用match
         */
        Map<String, String> filters = searchParamDTO.getFilters();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("分类".equals(key)) {
                boolQuery.filter(QueryBuilders.termQuery(CATEGORY_FIELD_NAME, value));
            } else if ("品牌".equals(key)) {
                boolQuery.filter(QueryBuilders.termQuery(BRAND_FIELD_NAME, value));
            } else {
                //一个filter下能放多个查询,就和上面的原理是一样的
                BoolQueryBuilder nestedBool = QueryBuilders.boolQuery();
                nestedBool.must(QueryBuilders.termQuery(SPEC_NAME_FIELD_NAME, key));
                nestedBool.must(QueryBuilders.termQuery(SPEC_VALUE_FIELD_NAME, value));
                boolQuery.filter(QueryBuilders.nestedQuery("specs", nestedBool, ScoreMode.None));
            }
        }

        return sourceBuilder;
    }

    @Override
    public Mono<PageInfo<Goods>> searchGoods(SearchParamDTO searchParamDTO) {
        /**
         * 获得了页数, 关键字(title),  过滤字段, 排序字段, 升降序
         * 前提/source过滤字段
         * 1. 关键字分词查询
         * 2.排序 ,  判断是否为空, 不为空进行排序
         * 3.过滤字段,判断是否为空,不为空进行过虑
         * 4.分页
         * 5.高亮
         */
        //共有的---词条查询
        SearchSourceBuilder sourceBuilder = getCommonSearch(searchParamDTO);

        sourceBuilder.fetchSource(DEFAULT_SOURCE_FIELD, new String[]{});
        //2.分页
        sourceBuilder.from(searchParamDTO.getFrom());
        sourceBuilder.size(searchParamDTO.getSize());
        //3.排序
        if (StringUtils.isNotEmpty(searchParamDTO.getSortBy())) {
            sourceBuilder.sort(searchParamDTO.getSortBy(), searchParamDTO.getDesc() ? SortOrder.DESC : SortOrder.ASC);
        }
        //4.高亮
        sourceBuilder.highlighter(new HighlightBuilder().field(DEFAULT_SEARCH_FIELD, 20).preTags(DEFAULT_PRE_TAG).postTags(DEFAULT_POST_TAG));

        return goodsRepository.queryBySourceBuilderForPageHighlight(sourceBuilder);


    }

    @Override
    public Mono<Map<String, List<?>>> getFilters(SearchParamDTO searchParamDTO) {


        /**
         * 普通查询 + 聚合
         */
        SearchSourceBuilder sourceBuilder = getCommonSearch(searchParamDTO);
        sourceBuilder.size(0);
        //对当前词条下的所有结果进行聚合处理
        //brand      category
        sourceBuilder.aggregation(AggregationBuilders.terms("brandAgg").field(BRAND_FIELD_NAME));
        sourceBuilder.aggregation(AggregationBuilders.terms("categoryAgg").field(CATEGORY_FIELD_NAME));

        sourceBuilder.aggregation(AggregationBuilders.nested("specsAgg", SPEC_FIELD_PATH)
                .subAggregation(AggregationBuilders.terms("nameAgg").field(SPEC_NAME_FIELD_NAME)
                        .subAggregation(AggregationBuilders.terms("valueAgg").field(SPEC_VALUE_FIELD_NAME))));
        Mono<Aggregations> mono = goodsRepository.aggregationBySourceBuilder(sourceBuilder);
        return mono.map(this::parseAggregationResult);
    }

    @Override
    public void saveGoodsById(Long spuId) {
        SpuDTO spuDTO = itemClient.queryGoodsById(spuId);
        goodsRepository.save(spuDTOToGoods(spuDTO));
    }

    @Override
    public void deleteGoodsById(Long spuId) {
        goodsRepository.deleteById(spuId);
    }


    /**
     * 将聚合结果解析为Map集合
     *
     * @param aggregations 聚合结果
     * @return map
     */
    private Map<String, List<?>> parseAggregationResult(Aggregations aggregations) {
        HashMap<String, List<?>> filters = new HashMap<>();
        Terms categoryAgg = aggregations.get("categoryAgg");
        List<Long> list = categoryAgg.getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        List<CategoryDTO> category = itemClient.queryCategoryByIds(list);
        filters.put("分类", category);
        filters.put("品牌", itemClient.queryBrandByIds(((Terms) aggregations
                .get("brandAgg")).getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList())));
        ((Terms) ((Nested) aggregations.get("specsAgg")).getAggregations().get("nameAgg"))
                .getBuckets()
                .forEach(bucket -> {
                            Terms valueAgg = bucket.getAggregations().get("valueAgg");
                            LinkedList<String> values = new LinkedList<>();
                            for (Terms.Bucket bucket1 : valueAgg.getBuckets()) {
                                values.add(bucket1.getKeyAsString());
                            }
                            filters.put(bucket.getKeyAsString(), values);
                        }
                );
        return filters;
    }

    public Goods spuDTOToGoods(SpuDTO spuDTO) {

        Goods goods = new Goods();
        List<SkuDTO> skus = spuDTO.getSkus();
        if (CollectionUtils.isEmpty(skus)) {
            skus = itemClient.querySkuBySpuId(spuDTO.getId());
        }
        // 设置自动补全搜索, 分类名, 品牌名, 商品名称
        ArrayList<String> list = new ArrayList<>();
        list.add(spuDTO.getBrandName());
        list.add(spuDTO.getName());
        list.addAll(Arrays.asList(spuDTO.getCategoryName().split("/")));
        goods.setUpdateTime(new Date());
        //标题
        goods.setTitle(spuDTO.getTitle() + StringUtils.join(list, " "));
        goods.setSuggestion(list);
        //设置规格参数
        List<SpecParamDTO> paramDTOList = itemClient.querySpecValues(spuDTO.getId(), true);
        ArrayList<Map<String, Object>> spec = new ArrayList<>();
        for (SpecParamDTO paramDTO : paramDTOList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", paramDTO.getName());
            map.put("value", chooseSegment(paramDTO));
            spec.add(map);
        }
        goods.setSpecs(spec);
        //spu下所有sku的销量集合
        goods.setSold(skus.stream().mapToLong(SkuDTO::getSold).sum());
        //价格的集合,用set去重
        goods.setPrices(skus.stream().map(SkuDTO::getPrice).collect(Collectors.toSet()));
        //取sku中任意一个
        goods.setImage(StringUtils.substringBefore(skus.get(0).getImages(), ","));
        goods.setId(spuDTO.getId());
        goods.setCategoryId(spuDTO.getCid3());
        goods.setBrandId(spuDTO.getBrandId());
        return goods;
    }

    private Object chooseSegment(SpecParamDTO p) {
        Object value = p.getValue();
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        if (!p.getNumeric() || StringUtils.isBlank(p.getSegments()) || value instanceof Collection) {
            return value;
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

}
