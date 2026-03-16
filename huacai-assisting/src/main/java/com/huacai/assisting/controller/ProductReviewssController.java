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
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.huacai.assisting.domain.ProductReviewss;
import com.huacai.assisting.service.IProductReviewssService;
import com.huacai.common.utils.poi.ExcelUtil;
import com.huacai.common.core.page.TableDataInfo;

/**
 * 产品评论Controller
 *
 * @author huacai
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/assisting/reviewss")
public class ProductReviewssController extends BaseController
{
    @Autowired
    private IProductReviewssService productReviewssService;

    /**
     * 查询产品评论列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProductReviewss productReviewss)
    {
        startPage();
        List<ProductReviewss> list = productReviewssService.selectProductReviewssList(productReviewss);
        return getDataTable(list);
    }

    /**
     * 导出产品评论列表
     */
    @Log(title = "产品评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductReviewss productReviewss)
    {
        List<ProductReviewss> list = productReviewssService.selectProductReviewssList(productReviewss);
        ExcelUtil<ProductReviewss> util = new ExcelUtil<ProductReviewss>(ProductReviewss.class);
        util.exportExcel(response, list, "产品评论数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<ProductReviewss> util = new ExcelUtil<ProductReviewss>(ProductReviewss.class);
        util.importTemplateExcel(response, "产品评论数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "产品评论", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<ProductReviewss> util = new ExcelUtil<ProductReviewss>(ProductReviewss.class);
        InputStream inputStream = file.getInputStream();
        List<ProductReviewss> list = util.importExcel(inputStream );
        inputStream.close();
        int count = productReviewssService.batchInsertProductReviewss(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取产品评论详细信息
     */
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") String reviewId)
    {
        return success(productReviewssService.selectProductReviewssByReviewId(reviewId));
    }

    /**
     * 新增产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductReviewss productReviewss)
    {
        return toAjax(productReviewssService.insertProductReviewss(productReviewss));
    }

    /**
     * 修改产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductReviewss productReviewss)
    {
        return toAjax(productReviewssService.updateProductReviewss(productReviewss));
    }

    /**
     * 删除产品评论
     */
    @Log(title = "产品评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable String[] reviewIds)
    {
        return toAjax(productReviewssService.deleteProductReviewssByReviewIds(reviewIds));
    }
}
