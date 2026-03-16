package com.huacai.assisting.service.impl;

import java.util.List;
import com.huacai.common.utils.DateUtils;
import com.huacai.common.utils.SecurityUtils;
import com.huacai.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.assisting.mapper.ProductReviewsMapper;
import com.huacai.assisting.domain.ProductReviews;
import com.huacai.assisting.service.IProductReviewsService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.huacai.common.utils.SecurityUtils.getUserId;

/**
 * 产品评论Service业务层处理
 *
 * @author huacai
 * @date 2026-03-14
 */
@Service
public class ProductReviewsServiceImpl implements IProductReviewsService {
    
    @Autowired
    private ProductReviewsMapper productReviewsMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询产品评论
     *
     * @param reviewId 产品评论主键
     * @return 产品评论
     */
    @Override
    public ProductReviews selectProductReviewsByReviewId(String reviewId) {
        return productReviewsMapper.selectProductReviewsByReviewId(reviewId);
    }

    /**
     * 查询产品评论列表（带数据隔离）
     * 管理员：查看所有评论
     * 农户：只查看自己产品的评论
     * 普通用户：查看所有商品的评论（不做数据隔离）
     *
     * @param productReviews 产品评论
     * @return 产品评论列表
     */
    @Override
    public List<ProductReviews> selectProductReviewsList(ProductReviews productReviews) {
        // 获取当前登录用户ID
        Long loginUserId = getUserId();
        
        // 只有农户角色才需要数据隔离（根据实际业务逻辑调整）
        // 这里暂时注释掉数据隔离，让所有用户都能查看评论
        // if (!SecurityUtils.isAdmin(loginUserId)) {
        //     productReviews.setFarmersUserId(String.valueOf(loginUserId));
        // }
        
        return productReviewsMapper.selectProductReviewsList(productReviews);
    }

    /**
     * 新增产品评论
     *
     * @param productReviews 产品评论
     * @return 结果
     */
    @Override
    public int insertProductReviews(ProductReviews productReviews) {
        productReviews.setCreateTime(DateUtils.getNowDate());
        productReviews.setReviewId(IdUtils.fastSimpleUUID());
        productReviews.setUserId(String.valueOf(getUserId()));
        productReviews.setStatus(1); // 默认显示
        return productReviewsMapper.insertProductReviews(productReviews);
    }

    /**
     * 批量新增产品评论
     *
     * @param productReviewss 产品评论List
     * @return 结果
     */
    @Override
    public int batchInsertProductReviews(List<ProductReviews> productReviewss) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(productReviewss)) {
            try {
                for (int i = 0; i < productReviewss.size(); i++) {
                    // 插入主键ID和用户ID
                    for (ProductReviews productReviews : productReviewss) {
                        productReviews.setReviewId(IdUtils.fastSimpleUUID());
                        productReviews.setUserId(String.valueOf(getUserId()));
                        productReviews.setCreateTime(DateUtils.getNowDate());
                        productReviews.setStatus(1);
                    }
                    int row = productReviewsMapper.insertProductReviews(productReviewss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == productReviewss.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改产品评论
     *
     * @param productReviews 产品评论
     * @return 结果
     */
    @Override
    public int updateProductReviews(ProductReviews productReviews) {
        // 如果是回复操作，设置回复时间
        if (productReviews.getReplyContent() != null) {
            productReviews.setReplyTime(DateUtils.getNowDate());
        }
        return productReviewsMapper.updateProductReviews(productReviews);
    }

    /**
     * 批量删除产品评论
     *
     * @param reviewIds 需要删除的产品评论主键
     * @return 结果
     */
    @Override
    public int deleteProductReviewsByReviewIds(String[] reviewIds) {
        return productReviewsMapper.deleteProductReviewsByReviewIds(reviewIds);
    }

    /**
     * 删除产品评论信息
     *
     * @param reviewId 产品评论主键
     * @return 结果
     */
    @Override
    public int deleteProductReviewsByReviewId(String reviewId) {
        return productReviewsMapper.deleteProductReviewsByReviewId(reviewId);
    }
}
