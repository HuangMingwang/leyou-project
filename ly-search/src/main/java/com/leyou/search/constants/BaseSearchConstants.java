package com.leyou.search.constants;

/**
 * @author Huang Mingwang
 * @create 2021-05-30 11:37 上午
 */
public abstract class BaseSearchConstants {
    /**
     * 自动补全的模板id
     */
    public static final String SUGGESTION_TEMPLATE_ID = "goods_suggest_template";
    /**
     * 自动补全的模板参数1名称
     */
    public static final String SUGGESTION_PARAM_PREFIX_KEY = "prefix_key";
    /**
     * 自动补全的模板参数2名称
     */
    public static final String SUGGESTION_PARAM_FIELD = "suggest_field";
    /**
     * 自动补全的自动名
     */
    public static final String SUGGESTION_FIELD = "suggestion";

    /**
     * 默认的查询字段
     */
    public static final String DEFAULT_SEARCH_FIELD = "title";

    /**
     * 分类字段名称
     */
    public static final String CATEGORY_FIELD_NAME = "categoryId";
    /**
     * 品牌字段名称
     */
    public static final String BRAND_FIELD_NAME = "brandId";
    /**
     * 规格参数字段路径
     */
    public static final String SPEC_FIELD_PATH = "specs";

    /**
     * 规格参数名称字段的name
     */
    public static final String SPEC_NAME_FIELD_NAME = "specs.name";
    /**
     * 规格参数值字段路径name
     */
    public static final String SPEC_VALUE_FIELD_NAME = "specs.value";
    /**
     * 默认查询的source字段
     */
    public static final String[] DEFAULT_SOURCE_FIELD = new String[]{"id", "title", "image", "prices", "sold"};

    /**
     * 高亮时的标签
     */
    public static final String DEFAULT_PRE_TAG = "<am>";
    /**
     * 高亮时的标签
     */
    public static final String DEFAULT_POST_TAG = "</am>";
}
