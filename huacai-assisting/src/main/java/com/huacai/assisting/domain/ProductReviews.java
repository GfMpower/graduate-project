package com.huacai.assisting.domain;

import com.huacai.common.annotation.Excel;
import lombok.*;
import com.huacai.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 产品评论对象 product_reviews
 *
 * @author huacai
 * @date 2026-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviews extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private String reviewId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private String productsId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private String orderId;

    /** 农户用户ID（用于数据隔离） */
    @Excel(name = "农户用户ID")
    private String farmersUserId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 评分(1-5星) */
    @Excel(name = "评分")
    private Long rating;

    /** 商家回复内容 */
    @Excel(name = "商家回复")
    private String replyContent;

    /** 商家回复时间 */
    @Excel(name = "回复时间")
    private Date replyTime;

    /** 状态：0-隐藏，1-显示 */
    @Excel(name = "状态")
    private Integer status;

    // 扩展字段（非数据库字段）
    /** 产品名称 */
    private String productName;
    
    /** 用户昵称 */
    private String userName;
    
    /** 农户名称 */
    private String farmersName;
}
