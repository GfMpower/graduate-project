-- 删除所有子类分类（level=2的分类）
DELETE FROM product_categories WHERE level = 2;

-- 更新产品分类，确保每个产品都对应到父类分类
-- 这里假设产品的分类ID可能指向子类分类，需要将其更新为对应的父类分类
UPDATE products SET category_id = '1' WHERE category_id IN ('7', '8', '9'); -- 蔬菜类的子类
UPDATE products SET category_id = '2' WHERE category_id IN ('10', '11', '12'); -- 水果类的子类

-- 为没有分类的产品分配默认分类
UPDATE products SET category_id = '1' WHERE category_id IS NULL OR category_id = '';

-- 验证分类数据
SELECT * FROM product_categories;

-- 验证产品分类数据
SELECT category_id, COUNT(*) as product_count FROM products GROUP BY category_id;