package com.huacai.assisting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huacai.common.annotation.Excel;
import com.huacai.common.core.domain.BaseEntity;

import java.util.Date;

public class ProductCategories extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String categoryId;

    @Excel(name = "分类名称")
    private String categoryName;

    @Excel(name = "父分类ID")
    private String parentId;

    @Excel(name = "分类级别")
    private Integer level;

    @Excel(name = "排序")
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}