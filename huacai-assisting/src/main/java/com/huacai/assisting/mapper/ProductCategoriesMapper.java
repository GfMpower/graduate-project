package com.huacai.assisting.mapper;

import com.huacai.assisting.domain.ProductCategories;
import java.util.List;

public interface ProductCategoriesMapper {
    public List<ProductCategories> selectProductCategoriesList(ProductCategories productCategories);

    public ProductCategories selectProductCategoriesByCategoryId(String categoryId);

    public int insertProductCategories(ProductCategories productCategories);

    public int updateProductCategories(ProductCategories productCategories);

    public int deleteProductCategoriesByCategoryId(String categoryId);

    public int deleteProductCategoriesByCategoryIds(String[] categoryIds);
}