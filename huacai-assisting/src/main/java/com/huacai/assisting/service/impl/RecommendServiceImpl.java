package com.huacai.assisting.service.impl;

import com.huacai.assisting.domain.Cart;
import com.huacai.assisting.domain.Orders;
import com.huacai.assisting.domain.OrdersProducts;
import com.huacai.assisting.domain.Products;
import com.huacai.assisting.mapper.CartMapper;
import com.huacai.assisting.mapper.OrdersMapper;
import com.huacai.assisting.mapper.ProductsMapper;
import com.huacai.assisting.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    // 配置比例：购物车商品数与推荐商品数的比例
    private static final int CART_TO_RECOMMEND_RATIO = 8;
    // 有效订单时间范围（90天）
    private static final long VALID_ORDER_DAYS = 90;
    // 价格区间百分比
    private static final double PRICE_RANGE_PERCENTAGE = 0.2;
    // 缓存有效期（10分钟）
    private static final long CACHE_EXPIRY_MINUTES = 10;
    // 过滤已购买商品的天数（30天）
    private static final long PURCHASED_FILTER_DAYS = 30;

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
        Set<String> addedProductIds = new HashSet<>(); // 用于去重

        // 计算有效订单数（近90天有消费记录的订单）
        int validOrderCount = countValidOrders(ordersList);
        // 计算推荐商品总量
        int totalSize = calculateTotalRecommendSize(cartList.size(), validOrderCount);

        // 无订单 + 无购物车 → 随机推荐8个商品（2排，一排4个）
        if (cartList.isEmpty() && ordersList.isEmpty()) {
            return getRandomRecommendations(8);
        }

        // 有订单（无论是否有购物车）→ 按优先级匹配特征
        if (!ordersList.isEmpty()) {
            // 1. 一级优先级：已完成订单（权重60%）
            List<Products> completedOrderProducts = getProductsFromCompletedOrders(ordersList, userId);
            int completedCount = (int) (totalSize * 0.6);
            addProductsToRecommendations(completedOrderProducts, recommendations, addedProductIds, completedCount);
            if (recommendations.size() >= totalSize) {
                return recommendations;
            }

            // 2. 二级优先级：非已完成订单（权重30%）
            List<Products> otherOrderProducts = getProductsFromOtherOrders(ordersList, userId);
            int otherCount = (int) (totalSize * 0.3);
            addProductsToRecommendations(otherOrderProducts, recommendations, addedProductIds, otherCount);
            if (recommendations.size() >= totalSize) {
                return recommendations;
            }
        }

        // 3. 三级优先级：购物车商品特征推荐（权重10%或100%）
        int cartCount = ordersList.isEmpty() ? totalSize : (int) (totalSize * 0.1);
        List<Products> cartCategoryProducts = getProductsFromCartCategories(cartList);
        addProductsToRecommendations(cartCategoryProducts, recommendations, addedProductIds, cartCount);
        if (recommendations.size() >= totalSize) {
            return recommendations;
        }

        // 4. 补充热门商品
        if (recommendations.size() < totalSize) {
            List<Products> hotProducts = productsMapper.selectHotProducts(100);
            addProductsToRecommendations(hotProducts, recommendations, addedProductIds, totalSize - recommendations.size());
        }

        return recommendations;
    }

    /**
     * 计算有效订单数（近90天有消费记录的订单）
     */
    private int countValidOrders(List<Orders> ordersList) {
        long cutoffTime = System.currentTimeMillis() - (VALID_ORDER_DAYS * 24 * 60 * 60 * 1000);
        return (int) ordersList.stream()
                .filter(order -> order.getCreateTime().getTime() >= cutoffTime)
                .count();
    }

    /**
     * 计算推荐商品总量
     */
    private int calculateTotalRecommendSize(int cartSize, int validOrderCount) {
        int initialSize = (cartSize * CART_TO_RECOMMEND_RATIO) + (validOrderCount * 2);
        // 确保是8的整数倍，且不小于8（2排，一排4个）
        int totalSize = Math.max(8, ((initialSize + 7) / 8) * 8);
        return totalSize;
    }

    /**
     * 从已完成订单中获取推荐商品
     */
    private List<Products> getProductsFromCompletedOrders(List<Orders> ordersList, String userId) {
        List<Products> products = new ArrayList<>();
        Set<String> productIds = new HashSet<>();
        Map<String, Integer> categoryFrequency = new HashMap<>();
        Map<String, BigDecimal> priceRange = new HashMap<>();

        // 过滤已完成订单
        List<Orders> completedOrders = ordersList.stream()
                .filter(order -> "已完成".equals(order.getStatus()))
                .collect(Collectors.toList());

        // 提取订单特征
        for (Orders order : completedOrders) {
            List<OrdersProducts> ordersProductsList = order.getOrdersProductsList();
            if (ordersProductsList != null && !ordersProductsList.isEmpty()) {
                for (OrdersProducts ordersProduct : ordersProductsList) {
                    Products product = productsMapper.selectProductsByProductsId(ordersProduct.getProductsId());
                    if (product != null) {
                        // 统计类别频次
                        if (product.getCategoryId() != null) {
                            categoryFrequency.put(product.getCategoryId(), 
                                    categoryFrequency.getOrDefault(product.getCategoryId(), 0) + 1);
                        }
                        // 记录价格范围
                        priceRange.put(product.getProductsId(), ordersProduct.getPrice());
                        
                        // 添加订单商品
                        if (!productIds.contains(product.getProductsId())) {
                            products.add(product);
                            productIds.add(product.getProductsId());
                        }
                    }
                }
            }
        }

        // 按类别频次排序，优先推荐高频类别
        List<Products> prioritizedProducts = new ArrayList<>(products);
        prioritizedProducts.sort((p1, p2) -> {
            int freq1 = categoryFrequency.getOrDefault(p1.getCategoryId(), 0);
            int freq2 = categoryFrequency.getOrDefault(p2.getCategoryId(), 0);
            return Integer.compare(freq2, freq1);
        });

        // 添加同类别商品
        for (Map.Entry<String, Integer> entry : categoryFrequency.entrySet()) {
            String categoryId = entry.getKey();
            List<Products> categoryProducts = getProductsByCategory(categoryId, priceRange.values());
            for (Products product : categoryProducts) {
                if (!productIds.contains(product.getProductsId())) {
                    prioritizedProducts.add(product);
                    productIds.add(product.getProductsId());
                }
            }
        }

        return prioritizedProducts;
    }

    /**
     * 从非已完成订单中获取推荐商品
     */
    private List<Products> getProductsFromOtherOrders(List<Orders> ordersList, String userId) {
        List<Products> products = new ArrayList<>();
        Set<String> productIds = new HashSet<>();

        // 过滤非已完成订单
        List<Orders> otherOrders = ordersList.stream()
                .filter(order -> !"已完成".equals(order.getStatus()) && !"已取消".equals(order.getStatus()))
                .collect(Collectors.toList());

        // 提取订单商品
        for (Orders order : otherOrders) {
            List<OrdersProducts> ordersProductsList = order.getOrdersProductsList();
            if (ordersProductsList != null && !ordersProductsList.isEmpty()) {
                for (OrdersProducts ordersProduct : ordersProductsList) {
                    Products product = productsMapper.selectProductsByProductsId(ordersProduct.getProductsId());
                    if (product != null && !productIds.contains(product.getProductsId())) {
                        products.add(product);
                        productIds.add(product.getProductsId());
                    }
                }
            }
        }

        return products;
    }

    /**
     * 从购物车类别中获取商品
     */
    private List<Products> getProductsFromCartCategories(List<Cart> cartList) {
        List<Products> products = new ArrayList<>();
        Set<String> categoryIds = new HashSet<>();
        Set<String> productIds = new HashSet<>();

        // 提取购物车中的商品类别
        for (Cart cart : cartList) {
            Products product = productsMapper.selectProductsByProductsId(cart.getProductsId());
            if (product != null && product.getCategoryId() != null && !product.getCategoryId().isEmpty()) {
                categoryIds.add(product.getCategoryId());
                productIds.add(product.getProductsId()); // 排除购物车中已有的商品
            }
        }

        // 从每个类别中获取商品
        for (String categoryId : categoryIds) {
            List<Products> categoryProducts = getProductsByCategory(categoryId, null);
            for (Products product : categoryProducts) {
                if (!productIds.contains(product.getProductsId())) {
                    products.add(product);
                    productIds.add(product.getProductsId());
                }
            }
        }

        return products;
    }

    /**
     * 根据类别获取商品，并考虑价格区间
     */
    private List<Products> getProductsByCategory(String categoryId, Collection<BigDecimal> referencePrices) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("limit", 30);
        List<Products> categoryProducts = productsMapper.selectProductsByCategoryId(params);

        // 如果有参考价格，按价格区间过滤
        if (referencePrices != null && !referencePrices.isEmpty()) {
            BigDecimal avgPrice = referencePrices.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(referencePrices.size()), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal minPrice = avgPrice.multiply(new BigDecimal(1 - PRICE_RANGE_PERCENTAGE));
            BigDecimal maxPrice = avgPrice.multiply(new BigDecimal(1 + PRICE_RANGE_PERCENTAGE));

            categoryProducts = categoryProducts.stream()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0 && 
                            product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        }

        return categoryProducts;
    }

    /**
     * 获取随机推荐商品
     */
    private List<Products> getRandomRecommendations(int count) {
        List<Products> allProducts = productsMapper.selectHotProducts(100);
        Collections.shuffle(allProducts);
        if (allProducts.size() > count) {
            return allProducts.subList(0, count);
        }
        return allProducts;
    }

    /**
     * 将商品添加到推荐列表，确保不重复且不超过总量
     */
    private void addProductsToRecommendations(List<Products> sourceProducts, List<Products> recommendations, 
                                           Set<String> addedProductIds, int count) {
        int addedCount = 0;
        for (Products product : sourceProducts) {
            if (!addedProductIds.contains(product.getProductsId())) {
                recommendations.add(product);
                addedProductIds.add(product.getProductsId());
                addedCount++;
                if (addedCount >= count) {
                    break;
                }
            }
        }
    }

    @Override
    public List<Products> getSimilarProducts(String productId) {
        Products product = productsMapper.selectProductsByProductsId(productId);
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