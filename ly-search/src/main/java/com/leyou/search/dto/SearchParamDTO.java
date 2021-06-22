package com.leyou.search.dto;

import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-31 10:02 上午
 */
public class SearchParamDTO {

        /**
         * 每页大小，不从页面接收，而是固定大小
         */
        private static final Integer DEFAULT_SIZE = 20;
        /**
         * 默认页
         */
        private static final int DEFAULT_PAGE = 1;
        /**
         * 页码最大不能超过100
         */
        private static final int PAGE_MAX_VALUE = 100;
        /**
         * 搜索的关键字
         */
        private String key;
        /**
         * 当前页码
         */
        private Integer page = DEFAULT_PAGE;
        /**
         * 排序字段
         */
        private String sortBy;
        /**
         * 是否为降序
         */
        private Boolean desc;
        /**
         * 过滤参数
         */
        private Map<String,String> filters;
        /**
         * 开始索引
         */
        private Integer from;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getPage() {
            if(page == null){
                return DEFAULT_PAGE;
            }
            return page;
        }

        public void setPage(Integer page) {
            this.page = Math.min(Math.max(DEFAULT_PAGE, page), PAGE_MAX_VALUE);
        }

        public int getSize() {
            return DEFAULT_SIZE;
        }

        public Map<String, String> getFilters() {
            return filters;
        }

        public void setFilters(Map<String, String> filters) {
            this.filters = filters;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public Boolean getDesc() {
            return desc;
        }

        public void setDesc(Boolean desc) {
            this.desc = desc;
        }

        public Integer getFrom() {
            return (page - 1) * getSize();
        }

}
