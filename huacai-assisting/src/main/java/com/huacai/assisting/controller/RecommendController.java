package com.huacai.assisting.controller;

import com.huacai.assisting.domain.Products;
import com.huacai.assisting.service.IRecommendService;
import com.huacai.common.core.controller.BaseController;
import com.huacai.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐服务控制器
 */
@RestController
@RequestMapping("/api/recommend")
public class RecommendController extends BaseController {

    @Autowired
    private IRecommendService recommendService;

    /**
     * 获取用户个性化推荐
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getUserRecommendations(@PathVariable String userId) {
        List<Products> recommendations = recommendService.getUserRecommendations(userId);
        return success(recommendations);
    }

    /**
     * 获取相似商品推荐
     * @param productId 商品ID
     * @return 相似商品列表
     */
    @GetMapping("/similar/{productId}")
    public AjaxResult getSimilarProducts(@PathVariable String productId) {
        List<Products> similarProducts = recommendService.getSimilarProducts(productId);
        return success(similarProducts);
    }

    /**
     * 获取关联商品推荐
     * @param productIds 商品ID列表
     * @return 关联商品列表
     */
    @GetMapping("/related")
    public AjaxResult getRelatedProducts(@RequestParam List<String> productIds) {
        List<Products> relatedProducts = recommendService.getRelatedProducts(productIds);
        return success(relatedProducts);
    }
}
