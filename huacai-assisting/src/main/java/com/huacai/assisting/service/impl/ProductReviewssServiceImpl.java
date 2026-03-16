package com.huacai.assisting.service.impl;

import java.util.List;
import com.huacai.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.assisting.mapper.ProductReviewssMapper;
import com.huacai.assisting.domain.ProductReviewss;
import com.huacai.assisting.service.IProductReviewssService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 产品评论Service业务层处理
 *
 * @author huacai
 * @date 2026-03-15
 */
@Service
public class ProductReviewssServiceImpl implements IProductReviewssService
{
    @Autowired
    private ProductReviewssMapper productReviewssMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询产品评论
     *
     * @param reviewId 产品评论主键
     * @return 产品评论
     */
    @Override
    public ProductReviewss selectProductReviewssByReviewId(String reviewId)
    {
        return productReviewssMapper.selectProductReviewssByReviewId(reviewId);
    }

    /**
     * 查询产品评论列表
     *
     * @param productReviewss 产品评论
     * @return 产品评论
     */
    @Override
    public List<ProductReviewss> selectProductReviewssList(ProductReviewss productReviewss)
    {
        return productReviewssMapper.selectProductReviewssList(productReviewss);
    }

    /**
     * 新增产品评论
     *
     * @param productReviewss 产品评论
     * @return 结果
     */
    @Override
    public int insertProductReviewss(ProductReviewss productReviewss)
    {
        productReviewss.setCreateTime(DateUtils.getNowDate());
        return productReviewssMapper.insertProductReviewss(productReviewss);
    }

    /**
     * 批量新增产品评论
     *
     * @param productReviewsss 产品评论List
     * @return 结果
     */
    @Override
    public int batchInsertProductReviewss(List<ProductReviewss> productReviewsss)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(productReviewsss)) {
            try {
                for (int i = 0; i < productReviewsss.size(); i++) {
                    int row = productReviewssMapper.insertProductReviewss(productReviewsss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == productReviewsss.size() - 1;
                    if (bool){
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            }catch (Exception e){
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            }finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改产品评论
     *
     * @param productReviewss 产品评论
     * @return 结果
     */
    @Override
    public int updateProductReviewss(ProductReviewss productReviewss)
    {
        return productReviewssMapper.updateProductReviewss(productReviewss);
    }

    /**
     * 批量删除产品评论
     *
     * @param reviewIds 需要删除的产品评论主键
     * @return 结果
     */
    @Override
    public int deleteProductReviewssByReviewIds(String[] reviewIds)
    {
        return productReviewssMapper.deleteProductReviewssByReviewIds(reviewIds);
    }

    /**
     * 删除产品评论信息
     *
     * @param reviewId 产品评论主键
     * @return 结果
     */
    @Override
    public int deleteProductReviewssByReviewId(String reviewId)
    {
        return productReviewssMapper.deleteProductReviewssByReviewId(reviewId);
    }
}
