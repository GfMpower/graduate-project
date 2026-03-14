package com.huacai.assisting.service.impl;

import com.huacai.assisting.domain.ProductCategories;
import com.huacai.assisting.mapper.ProductCategoriesMapper;
import com.huacai.assisting.service.IProductCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoriesServiceImpl implements IProductCategoriesService {
    @Autowired
    private ProductCategoriesMapper productCategoriesMapper;

    @Override
    public List<ProductCategories> selectProductCategoriesList(ProductCategories productCategories) {
        return productCategoriesMapper.selectProductCategoriesList(productCategories);
    }

    @Override
    public ProductCategories selectProductCategoriesByCategoryId(String categoryId) {
        return productCategoriesMapper.selectProductCategoriesByCategoryId(categoryId);
    }

    @Override
    public int insertProductCategories(ProductCategories productCategories) {
        return productCategoriesMapper.insertProductCategories(productCategories);
    }

    @Override
    public int updateProductCategories(ProductCategories productCategories) {
        return productCategoriesMapper.updateProductCategories(productCategories);
    }

    @Override
    public int deleteProductCategoriesByCategoryId(String categoryId) {
        return productCategoriesMapper.deleteProductCategoriesByCategoryId(categoryId);
    }

    @Override
    public int deleteProductCategoriesByCategoryIds(String[] categoryIds) {
        return productCategoriesMapper.deleteProductCategoriesByCategoryIds(categoryIds);
    }
}