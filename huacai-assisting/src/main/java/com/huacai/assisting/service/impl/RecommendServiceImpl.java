package com.huacai.assisting.service.impl;

import com.huacai.assisting.domain.Cart;
import com.huacai.assisting.domain.Orders;
import com.huacai.assisting.domain.Products;
import com.huacai.assisting.mapper.CartMapper;
import com.huacai.assisting.mapper.OrdersMapper;
import com.huacai.assisting.mapper.ProductsMapper;
import com.huacai.assisting.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐服务实现类
 */
@Service
public class RecommendServiceImpl implements IRecommendService {

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Products> getUserRecommendations(String userId) {
        // 1. 获取用户购物车数据
        List<Cart> cartList = cartMapper.selectCartListByUserId(userId);

        // 2. 获取用户订单数据
        List<Orders> ordersList = ordersMapper.selectOrdersListByUserId(userId);

        // 3. 计算推荐结果
        List<Products> recommendations = calculateRecommendations(userId, cartList, ordersList);

        return recommendations;
    }

    private List<Products> calculateRecommendations(String userId, List<Cart> cartList, List<Orders> ordersList) {
        List<Products> recommendations = new ArrayList<>();

        // 如果购物车和订单都为空，随机展示12个商品
        if (cartList.isEmpty() && ordersList.isEmpty()) {
            List<Products> allProducts = productsMapper.selectHotProducts(50); // 获取足够多的商品
            Collections.shuffle(allProducts); // 随机打乱
            if (allProducts.size() > 12) {
                return allProducts.subList(0, 12);
            }
            return allProducts;
        }

        // 基于购物车和订单数据的推荐算法
        // 1. 提取用户偏好的商品类别
        List<String> preferredCategoryIds = extractPreferredCategories(cartList, ordersList);

        // 2. 获取热门商品
        List<Products> hotProducts = productsMapper.selectHotProducts(20);

        // 3. 基于类别推荐
        if (!preferredCategoryIds.isEmpty()) {
            for (String categoryId : preferredCategoryIds) {
                Map<String, Object> params = new HashMap<>();
                params.put("categoryId", categoryId);
                params.put("limit", 10);
                List<Products> categoryProducts = productsMapper.selectProductsByCategoryId(params);
                for (Products product : categoryProducts) {
                    if (!recommendations.contains(product)) {
                        recommendations.add(product);
                        if (recommendations.size() >= 12) break;
                    }
                }
                if (recommendations.size() >= 12) break;
            }
        }

        // 4. 补充热门商品
        if (recommendations.size() < 12) {
            for (Products product : hotProducts) {
                if (!recommendations.contains(product)) {
                    recommendations.add(product);
                    if (recommendations.size() >= 12) break;
                }
            }
        }

        return recommendations;
    }

    private List<String> extractPreferredCategories(List<Cart> cartList, List<Orders> ordersList) {
        List<String> categoryIds = new ArrayList<>();

        // 从购物车提取
        for (Cart cart : cartList) {
            Products product = productsMapper.selectProductsById(cart.getProductsId());
            if (product != null && product.getCategoryId() != null) {
                categoryIds.add(product.getCategoryId());
            }
        }

        // 从订单提取
        for (Orders order : ordersList) {
            // 这里需要根据实际的订单商品关联表结构来实现
            // 暂时简化处理
        }

        return categoryIds;
    }

    @Override
    public List<Products> getSimilarProducts(String productId) {
        Products product = productsMapper.selectProductsById(productId);
        if (product == null) {
            return new ArrayList<>();
        }

        // 基于商品类别推荐相似商品
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", product.getCategoryId());
        params.put("limit", 6);
        return productsMapper.selectProductsByCategoryId(params);
    }

    @Override
    public List<Products> getRelatedProducts(List<String> productIds) {
        // 基于关联规则的推荐
        // 暂时返回热门商品
        return productsMapper.selectHotProducts(6);
    }
}
