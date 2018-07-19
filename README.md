# seckill ----秒杀系统实现总结

1：日志
    slf4j只是规范的接口，
    log4j，logback等等才是具体的实现

2：业务流程
    商家----(添加/调整)/(发货/核账)----库存------(秒杀/预售)/(付款/退货)------用户
    整个秒杀是对库存做的系统
    用户秒杀过程-->减库存 -->记录购买明细-->数据落地
    
3：购买行为：
    记录谁购买了
    成功的时间以及有效期
    付款、发货信息

4：事务机制依然是最有效最可靠的数据落地方案。

5：竞争 (如何高效的处理竞争 --减库存的操作)   
    事务+行级锁
    start tramsaction---update---insert---commit
    
    update table_name set num=num-1 where id=100 and num >1  
    
6：功能
    接口暴露：避免知道接口地址
    执行秒杀：如果处理
    查询：
    
7：数据库设计
CREATE DATABASE	 seckill;
   
USE seckill;
  
CREATE TABLE  seckill(
   `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
   `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
   `number` INT NOT NULL COMMENT '库存数量',
   `start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
   `end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
   `create_time` TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (seckill_id),
   KEY idx_start_time(start_time),
   KEY idx_ent_time(end_time),
   KEY idx_create_time(create_time)
   ) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
    
    INSERT INTO seckill(`name`,`number`,`start_time`,`end_time`)
    VALUES
    ('100秒杀iphoneX',1000,'2018-11-11 00:00:00','2018-11-12 00:00:00'),
    ('10秒杀iphone8',1000,'2018-11-11 00:00:00','2018-11-12 00:00:00'),
    ('1秒杀iphone7',1000,'2018-11-11 00:00:00','2018-11-12 00:00:00');

CREATE TABLE  success_killed(
    `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '秒杀商品Id',
    `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
    `state` TINYINT NOT NULL  DEFAULT -1 COMMENT '状态 -1：无效  0：成功  1：已付款  2：已发货...',
    `create_time` TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (seckill_id,user_phone),
    KEY idx_create_time(create_time)
    ) ENGINE=INNODB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';




