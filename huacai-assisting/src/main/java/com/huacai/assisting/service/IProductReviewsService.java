package com.huacai.assisting.service;

import com.huacai.assisting.domain.ProductReviews;
import java.util.List;

public interface IProductReviewsService {
    public List<ProductReviews> selectProductReviewsList(ProductReviews productReviews);

    public ProductReviews selectProductReviewsByReviewId(String reviewId);

    public int insertProductReviews(ProductReviews productReviews);

    public int updateProductReviews(ProductReviews productReviews);

    public int deleteProductReviewsByReviewId(String reviewId);

    public int deleteProductReviewsByReviewIds(String[] reviewIds);
}