package com.leyou.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:25 下午
 */
@Data
@NoArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SpuDTO extends BaseDTO {

    private Long id;
    private Long brandId;
    /**
     * 一级类目
     */
    private Long cid1;
    /**
     * 二级类目
     */
    private Long cid2;
    /**
     * 三级类目
     */
    private Long cid3;
    /**
     * 名称
     */
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否上架
     */
    private Boolean saleable;
    /**
     * 商品分类名称拼接
     */
    private String categoryName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 商品详情
     */
    private SpuDetailDTO spuDetail;
    /**
     * spu下的sku的集合
     */
    private List<SkuDTO> skus;
    /**
     * 方便同时获取3级分类
     * @return 3级分类的id集合
     */
    @JsonIgnore
    public List<Long> getCategoryIds(){
        return Arrays.asList(cid1, cid2, cid3);
    }


    public SpuDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SpuDTO> convertEntityList(Collection<T> list){
        return list.stream().map(SpuDTO::new).collect(Collectors.toList());
    }



}
