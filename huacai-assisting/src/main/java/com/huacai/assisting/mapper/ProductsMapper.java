package com.huacai.assisting.mapper;

import java.util.List;
import java.util.Map;
import com.huacai.assisting.domain.Products;

/**
 * 农户产品Mapper接口
 * 
 * @author huacai
 * @date 2025-08-12
 */
public interface ProductsMapper 
{
    /**
     * 查询农户产品
     * 
     * @param productsId 农户产品主键
     * @return 农户产品
     */
    public Products selectProductsByProductsId(String productsId);

    /**
     * 查询农户产品列表
     * 
     * @param products 农户产品
     * @return 农户产品集合
     */
    public List<Products> selectProductsList(Products products);

    /**
     * 新增农户产品
     * 
     * @param products 农户产品
     * @return 结果
     */
    public int insertProducts(Products products);

    /**
     * 修改农户产品
     * 
     * @param products 农户产品
     * @return 结果
     */
    public int updateProducts(Products products);

    /**
     * 删除农户产品
     * 
     * @param productsId 农户产品主键
     * @return 结果
     */
    public int deleteProductsByProductsId(String productsId);

    /**
     * 批量删除农户产品
     * 
     * @param productsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductsByProductsIds(String[] productsIds);

    /**
     * 查询热门商品
     * 
     * @param limit 限制数量
     * @return 热门商品列表
     */
    public List<Products> selectHotProducts(int limit);

    /**
     * 根据类别查询商品
     * 
     * @param params 参数包含categoryId和limit
     * @return 商品列表
     */
    public List<Products> selectProductsByCategoryId(Map<String, Object> params);

    /**
     * 查询商品通过ID
     * 
     * @param productsId 商品ID
     * @return 商品
     */
    public Products selectProductsById(String productsId);
}
