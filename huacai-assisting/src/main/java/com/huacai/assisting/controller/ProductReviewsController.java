package com.huacai.assisting.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huacai.common.annotation.Log;
import com.huacai.common.core.controller.BaseController;
import com.huacai.common.core.domain.AjaxResult;
import com.huacai.common.enums.BusinessType;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.huacai.assisting.domain.ProductReviews;
import com.huacai.assisting.service.IProductReviewsService;
import com.huacai.common.utils.poi.ExcelUtil;
import com.huacai.common.core.page.TableDataInfo;

/**
 * 产品评论Controller
 *
 * @author huacai
 * @date 2026-03-14
 */
@RestController
@RequestMapping("/assisting/reviews")
public class ProductReviewsController extends BaseController {
    
    @Autowired
    private IProductReviewsService productReviewsService;

    /**
     * 查询产品评论列表（带权限控制）
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:list')")
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
        ExcelUtil<ProductReviews> util = new ExcelUtil<>(ProductReviews.class);
        util.exportExcel(response, list, "产品评论数据");
    }

    /**
     * 下载模板
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<ProductReviews> util = new ExcelUtil<>(ProductReviews.class);
        util.importTemplateExcel(response, "产品评论数据");
    }

    /**
     * 导入数据
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:import')")
    @Log(title = "产品评论", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<ProductReviews> util = new ExcelUtil<>(ProductReviews.class);
        InputStream inputStream = file.getInputStream();
        List<ProductReviews> list = util.importExcel(inputStream);
        inputStream.close();
        int count = productReviewsService.batchInsertProductReviews(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取产品评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:query')")
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") String reviewId) {
        return success(productReviewsService.selectProductReviewsByReviewId(reviewId));
    }

    /**
     * 新增产品评论（前台用户调用，不需要权限注解）
     */
    @Log(title = "产品评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductReviews productReviews) {
        return toAjax(productReviewsService.insertProductReviews(productReviews));
    }

    /**
     * 修改产品评论（回复/状态修改）
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:edit')")
    @Log(title = "产品评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductReviews productReviews) {
        return toAjax(productReviewsService.updateProductReviews(productReviews));
    }

    /**
     * 删除产品评论
     */
    @PreAuthorize("@ss.hasPermi('assisting:reviews:remove')")
    @Log(title = "产品评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable String[] reviewIds) {
        return toAjax(productReviewsService.deleteProductReviewsByReviewIds(reviewIds));
    }
}
