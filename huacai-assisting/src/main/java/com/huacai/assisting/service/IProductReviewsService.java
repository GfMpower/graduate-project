package com.huacai.assisting.service;

import java.util.List;
import com.huacai.assisting.domain.ProductReviews;

/**
 * 产品评论Service接口
 *
 * @author huacai
 * @date 2026-03-14
 */
public interface IProductReviewsService
{
    /**
     * 查询产品评论
     *
     * @param reviewId 产品评论主键
     * @return 产品评论
     */
    public ProductReviews selectProductReviewsByReviewId(String reviewId);

    /**
     * 查询产品评论列表
     *
     * @param productReviews 产品评论
     * @return 产品评论集合
     */
    public List<ProductReviews> selectProductReviewsList(ProductReviews productReviews);

    /**
     * 新增产品评论
     *
     * @param productReviews 产品评论
     * @return 结果
     */
    public int insertProductReviews(ProductReviews productReviews);

    /**
     * 批量新增产品评论
     *
     * @param productReviewss 产品评论List
     * @return 结果
     */
    public int batchInsertProductReviews(List<ProductReviews> productReviewss);

    /**
     * 修改产品评论
     *
     * @param productReviews 产品评论
     * @return 结果
     */
    public int updateProductReviews(ProductReviews productReviews);

    /**
     * 批量删除产品评论
     *
     * @param reviewIds 需要删除的产品评论主键集合
     * @return 结果
     */
    public int deleteProductReviewsByReviewIds(String[] reviewIds);

    /**
     * 删除产品评论信息
     *
     * @param reviewId 产品评论主键
     * @return 结果
     */
    public int deleteProductReviewsByReviewId(String reviewId);
}
