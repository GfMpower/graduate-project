package com.huacai.assisting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huacai.common.annotation.Excel;
import com.huacai.common.core.domain.BaseEntity;

import java.util.Date;

public class ProductReviews extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String reviewId;

    @Excel(name = "产品ID")
    private String productsId;

    @Excel(name = "用户ID")
    private String userId;

    private String userName;

    @Excel(name = "评论内容")
    private String content;

    @Excel(name = "评分")
    private Integer rating;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getProductsId() {
        return productsId;
    }

    public void setProductsId(String productsId) {
        this.productsId = productsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}