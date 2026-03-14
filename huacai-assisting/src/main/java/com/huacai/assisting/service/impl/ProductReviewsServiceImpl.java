package com.huacai.assisting.service.impl;

import com.huacai.assisting.domain.ProductReviews;
import com.huacai.assisting.mapper.ProductReviewsMapper;
import com.huacai.assisting.service.IProductReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewsServiceImpl implements IProductReviewsService {
    @Autowired
    private ProductReviewsMapper productReviewsMapper;

    @Override
    public List<ProductReviews> selectProductReviewsList(ProductReviews productReviews) {
        return productReviewsMapper.selectProductReviewsList(productReviews);
    }

    @Override
    public ProductReviews selectProductReviewsByReviewId(String reviewId) {
        return productReviewsMapper.selectProductReviewsByReviewId(reviewId);
    }

    @Override
    public int insertProductReviews(ProductReviews productReviews) {
        return productReviewsMapper.insertProductReviews(productReviews);
    }

    @Override
    public int updateProductReviews(ProductReviews productReviews) {
        return productReviewsMapper.updateProductReviews(productReviews);
    }

    @Override
    public int deleteProductReviewsByReviewId(String reviewId) {
        return productReviewsMapper.deleteProductReviewsByReviewId(reviewId);
    }

    @Override
    public int deleteProductReviewsByReviewIds(String[] reviewIds) {
        return productReviewsMapper.deleteProductReviewsByReviewIds(reviewIds);
    }
}