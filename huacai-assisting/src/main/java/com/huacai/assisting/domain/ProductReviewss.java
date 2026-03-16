package com.huacai.assisting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huacai.common.annotation.Excel;
import lombok.*;
import com.huacai.common.core.domain.BaseEntity;

/**
 * 产品评论对象 product_reviewss
 *
 * @author huacai
 * @date 2026-03-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewss extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private String reviewId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private String productsId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 评价等级 */
    @Excel(name = "评价等级")
    private Long rating;

    /** 订单ID */
    @Excel(name = "订单ID")
    private String orderId;

    /** 农户用户ID */
    @Excel(name = "农户用户ID")
    private String farmersUserId;

    /** 商家回复内容 */
    @Excel(name = "商家回复内容")
    private String replyContent;

    /** 商家回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "商家回复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    /** 状态：0-隐藏，1-显示 */
    @Excel(name = "状态：0-隐藏，1-显示")
    private Long status;


}
