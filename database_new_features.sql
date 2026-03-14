-- 用户评论反馈表
CREATE TABLE `product_reviews` (
  `review_id` varchar(32) NOT NULL COMMENT '评论ID',
  `products_id` varchar(32) NOT NULL COMMENT '产品ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` int(1) NOT NULL COMMENT '评分(1-5星)',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  PRIMARY KEY (`review_id`),
  KEY `idx_products_id` (`products_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品评论表';

-- 农产品分类表
CREATE TABLE `product_categories` (
  `category_id` varchar(32) NOT NULL COMMENT '分类ID',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` varchar(32) DEFAULT '0' COMMENT '父分类ID',
  `level` int(1) NOT NULL COMMENT '分类级别(1-3)',
  `sort` int(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`category_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';

-- 修改产品表，添加分类字段
ALTER TABLE `products` ADD COLUMN `category_id` varchar(32) DEFAULT NULL COMMENT '分类ID' AFTER `user_id`;
ALTER TABLE `products` ADD KEY `idx_category_id` (`category_id`);

-- 插入初始分类数据
INSERT INTO `product_categories` (`category_id`, `category_name`, `parent_id`, `level`, `sort`, `create_time`) VALUES
('1', '蔬菜类', '0', 1, 1, NOW()),
('2', '水果类', '0', 1, 2, NOW()),
('3', '粮食类', '0', 1, 3, NOW()),
('4', '畜禽类', '0', 1, 4, NOW()),
('5', '水产类', '0', 1, 5, NOW()),
('6', '菌菇类', '0', 1, 6, NOW()),
('7', '叶菜类', '1', 2, 1, NOW()),
('8', '根茎类', '1', 2, 2, NOW()),
('9', '瓜果类', '1', 2, 3, NOW()),
('10', '热带水果', '2', 2, 1, NOW()),
('11', '温带水果', '2', 2, 2, NOW()),
('12', '浆果类', '2', 2, 3, NOW());