package com.huacai.assisting.service;

import com.huacai.assisting.domain.Products;

import java.util.List;

/**
 * 推荐服务接口
 */
public interface IRecommendService {

    /**
     * 获取用户个性化推荐
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    List<Products> getUserRecommendations(String userId);

    /**
     * 获取相似商品推荐
     * @param productId 商品ID
     * @return 相似商品列表
     */
    List<Products> getSimilarProducts(String productId);

    /**
     * 获取关联商品推荐
     * @param productIds 商品ID列表
     * @return 关联商品列表
     */
    List<Products> getRelatedProducts(List<String> productIds);
}
