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
import com.huacai.assisting.domain.ProductReviews;
import com.huacai.assisting.service.IProductReviewsService;
import com.huacai.common.utils.poi.ExcelUtil;
import com.huacai.common.core.page.TableDataInfo;

/**
 * 产品评论Controller
 *
 * @author huacai
 * @date 2025-08-12
 */
@RestController
@RequestMapping("/assisting/reviews")
public class ProductReviewsController extends BaseController {
    @Autowired
    private IProductReviewsService productReviewsService;

    /**
     * 查询产品评论列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProductReviews productReviews) {
        startPage();
        List<ProductReviews> list = productReviewsService.selectProductReviewsList(productReviews);
        return getDataTable(list);
    }

    /**
     * 导出产品评论列表
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:export')")
    @Log(title = "产品评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductReviews productReviews) {
        List<ProductReviews> list = productReviewsService.selectProductReviewsList(productReviews);
        ExcelUtil<ProductReviews> util = new ExcelUtil<ProductReviews>(ProductReviews.class);
        util.exportExcel(response, list, "产品评论数据");
    }

    /**
     * 获取产品评论详细信息
     */
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") String reviewId) {
        return success(productReviewsService.selectProductReviewsByReviewId(reviewId));
    }

    /**
     * 新增产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductReviews productReviews) {
        return toAjax(productReviewsService.insertProductReviews(productReviews));
    }

    /**
     * 修改产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductReviews productReviews) {
        return toAjax(productReviewsService.updateProductReviews(productReviews));
    }

    /**
     * 删除产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable String[] reviewIds) {
        return toAjax(productReviewsService.deleteProductReviewsByReviewIds(reviewIds));
    }
}