package com.huacai.assisting.mapper;

import java.util.List;
import com.huacai.assisting.domain.ProductReviewss;

/**
 * 产品评论Mapper接口
 * 
 * @author huacai
 * @date 2026-03-15
 */
public interface ProductReviewssMapper 
{
    /**
     * 查询产品评论
     * 
     * @param reviewId 产品评论主键
     * @return 产品评论
     */
    public ProductReviewss selectProductReviewssByReviewId(String reviewId);

    /**
     * 查询产品评论列表
     * 
     * @param productReviewss 产品评论
     * @return 产品评论集合
     */
    public List<ProductReviewss> selectProductReviewssList(ProductReviewss productReviewss);

    /**
     * 新增产品评论
     * 
     * @param productReviewss 产品评论
     * @return 结果
     */
    public int insertProductReviewss(ProductReviewss productReviewss);

    /**
     * 修改产品评论
     * 
     * @param productReviewss 产品评论
     * @return 结果
     */
    public int updateProductReviewss(ProductReviewss productReviewss);

    /**
     * 删除产品评论
     * 
     * @param reviewId 产品评论主键
     * @return 结果
     */
    public int deleteProductReviewssByReviewId(String reviewId);

    /**
     * 批量删除产品评论
     * 
     * @param reviewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductReviewssByReviewIds(String[] reviewIds);
}
