package com.huacai.assisting.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huacai.common.annotation.Log;
import com.huacai.common.core.controller.BaseController;
import com.huacai.common.core.domain.AjaxResult;
import com.huacai.common.enums.BusinessType;
import com.huacai.assisting.domain.ProductCategories;
import com.huacai.assisting.service.IProductCategoriesService;
import com.huacai.common.utils.poi.ExcelUtil;
import com.huacai.common.core.page.TableDataInfo;

/**
 * 产品分类Controller
 *
 * @author huacai
 * @date 2025-08-12
 */
@RestController
@RequestMapping("/assisting/categories")
public class ProductCategoriesController extends BaseController {
    @Autowired
    private IProductCategoriesService productCategoriesService;

    /**
     * 查询产品分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProductCategories productCategories) {
        startPage();
        List<ProductCategories> list = productCategoriesService.selectProductCategoriesList(productCategories);
        return getDataTable(list);
    }

    /**
     * 导出产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('assisting:categories:export')")
    @Log(title = "产品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductCategories productCategories) {
        List<ProductCategories> list = productCategoriesService.selectProductCategoriesList(productCategories);
        ExcelUtil<ProductCategories> util = new ExcelUtil<ProductCategories>(ProductCategories.class);
        util.exportExcel(response, list, "产品分类数据");
    }

    /**
     * 获取产品分类详细信息
     */
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId) {
        return success(productCategoriesService.selectProductCategoriesByCategoryId(categoryId));
    }

    /**
     * 新增产品分类
     */
    @Log(title = "产品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductCategories productCategories) {
        return toAjax(productCategoriesService.insertProductCategories(productCategories));
    }

    /**
     * 修改产品分类
     */
    @Log(title = "产品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductCategories productCategories) {
        return toAjax(productCategoriesService.updateProductCategories(productCategories));
    }

    /**
     * 删除产品分类
     */
    @Log(title = "产品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds) {
        return toAjax(productCategoriesService.deleteProductCategoriesByCategoryIds(categoryIds));
    }
}