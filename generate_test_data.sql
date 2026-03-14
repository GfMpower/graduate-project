-- 生成测试数据
-- 为每个分类创建测试产品

-- 蔬菜类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('1', '有机胡萝卜', '新鲜有机胡萝卜，营养丰富', 5.99, 'https://example.com/carrot.jpg', '山东寿光', '山东寿光', 100, '500g/袋', '30天', '冷藏储存', '生食或烹饪', '有机胡萝卜，无农药残留，口感甜美', 1, '1', 'testuser', NOW()),
('1', '有机西红柿', '新鲜有机西红柿，酸甜可口', 8.99, 'https://example.com/tomato.jpg', '山东寿光', '山东寿光', 100, '500g/盒', '15天', '冷藏储存', '生食或烹饪', '有机西红柿，无农药残留，口感酸甜', 1, '1', 'testuser', NOW()),
('1', '有机黄瓜', '新鲜有机黄瓜，清脆爽口', 6.99, 'https://example.com/cucumber.jpg', '山东寿光', '山东寿光', 100, '500g/袋', '10天', '冷藏储存', '生食或烹饪', '有机黄瓜，无农药残留，口感清脆', 1, '1', 'testuser', NOW());

-- 水果类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('2', '有机苹果', '新鲜有机苹果，清脆多汁', 12.99, 'https://example.com/apple.jpg', '陕西洛川', '陕西洛川', 100, '2.5kg/箱', '60天', '冷藏储存', '生食', '有机苹果，无农药残留，口感清脆多汁', 1, '1', 'testuser', NOW()),
('2', '有机梨', '新鲜有机梨，肉质细腻', 9.99, 'https://example.com/pear.jpg', '河北赵县', '河北赵县', 100, '2.5kg/箱', '45天', '冷藏储存', '生食', '有机梨，无农药残留，肉质细腻', 1, '1', 'testuser', NOW()),
('2', '有机葡萄', '新鲜有机葡萄，香甜可口', 19.99, 'https://example.com/grape.jpg', '新疆吐鲁番', '新疆吐鲁番', 100, '500g/串', '7天', '冷藏储存', '生食', '有机葡萄，无农药残留，香甜可口', 1, '1', 'testuser', NOW());

-- 粮食类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('3', '有机大米', '东北有机大米，口感香糯', 29.99, 'https://example.com/rice.jpg', '黑龙江五常', '黑龙江五常', 100, '5kg/袋', '365天', '阴凉干燥处', '烹饪', '有机大米，无农药残留，口感香糯', 1, '1', 'testuser', NOW()),
('3', '有机小麦粉', '山东有机小麦粉，粉质细腻', 19.99, 'https://example.com/flour.jpg', '山东滨州', '山东滨州', 100, '5kg/袋', '365天', '阴凉干燥处', '烹饪', '有机小麦粉，无农药残留，粉质细腻', 1, '1', 'testuser', NOW()),
('3', '有机玉米', '东北有机玉米，香甜可口', 12.99, 'https://example.com/corn.jpg', '吉林长春', '吉林长春', 100, '500g/袋', '365天', '阴凉干燥处', '烹饪', '有机玉米，无农药残留，香甜可口', 1, '1', 'testuser', NOW());

-- 畜禽类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('4', '有机鸡蛋', '散养有机鸡蛋，营养丰富', 19.99, 'https://example.com/egg.jpg', '山东德州', '山东德州', 100, '10枚/盒', '30天', '冷藏储存', '烹饪', '有机鸡蛋，散养，营养丰富', 1, '1', 'testuser', NOW()),
('4', '有机鸡肉', '散养有机鸡肉，肉质鲜嫩', 39.99, 'https://example.com/chicken.jpg', '山东德州', '山东德州', 100, '1kg/袋', '7天', '冷藏储存', '烹饪', '有机鸡肉，散养，肉质鲜嫩', 1, '1', 'testuser', NOW());

-- 水产类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('5', '有机鲫鱼', '新鲜有机鲫鱼，肉质鲜美', 29.99, 'https://example.com/carp.jpg', '江苏苏州', '江苏苏州', 100, '500g/条', '2天', '冷藏储存', '烹饪', '有机鲫鱼，新鲜，肉质鲜美', 1, '1', 'testuser', NOW()),
('5', '有机虾', '新鲜有机虾，肉质Q弹', 49.99, 'https://example.com/shrimp.jpg', '江苏苏州', '江苏苏州', 100, '500g/盒', '1天', '冷藏储存', '烹饪', '有机虾，新鲜，肉质Q弹', 1, '1', 'testuser', NOW());

-- 菌菇类产品
INSERT INTO products (category_id, name, subtitle, price, image, origin, shipFrom, inventory, specs, expire, storage, edible, detail, status, user_id, user_name, create_time) VALUES
('6', '有机香菇', '新鲜有机香菇，香味浓郁', 15.99, 'https://example.com/mushroom.jpg', '浙江庆元', '浙江庆元', 100, '500g/袋', '5天', '冷藏储存', '烹饪', '有机香菇，新鲜，香味浓郁', 1, '1', 'testuser', NOW()),
('6', '有机平菇', '新鲜有机平菇，口感滑嫩', 9.99, 'https://example.com/oyster-mushroom.jpg', '浙江庆元', '浙江庆元', 100, '500g/袋', '3天', '冷藏储存', '烹饪', '有机平菇，新鲜，口感滑嫩', 1, '1', 'testuser', NOW()),
('6', '有机金针菇', '新鲜有机金针菇，口感脆嫩', 12.99, 'https://example.com/enoki-mushroom.jpg', '浙江庆元', '浙江庆元', 100, '500g/袋', '3天', '冷藏储存', '烹饪', '有机金针菇，新鲜，口感脆嫩', 1, '1', 'testuser', NOW());

-- 验证测试数据
SELECT category_id, COUNT(*) as product_count FROM products GROUP BY category_id;
