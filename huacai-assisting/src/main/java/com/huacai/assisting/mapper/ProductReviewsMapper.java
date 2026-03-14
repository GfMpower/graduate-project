package com.huacai.assisting.mapper;

import java.util.List;
import com.huacai.assisting.domain.ProductReviews;

/**
 * 产品评论Mapper接口
 * 
 * @author huacai
 * @date 2026-03-14
 */
public interface ProductReviewsMapper 
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
     * 修改产品评论
     * 
     * @param productReviews 产品评论
     * @return 结果
     */
    public int updateProductReviews(ProductReviews productReviews);

    /**
     * 删除产品评论
     * 
     * @param reviewId 产品评论主键
     * @return 结果
     */
    public int deleteProductReviewsByReviewId(String reviewId);

    /**
     * 批量删除产品评论
     * 
     * @param reviewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductReviewsByReviewIds(String[] reviewIds);
}
