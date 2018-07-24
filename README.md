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

########################################## 接口设计+sql编写 ###############################################
依赖关系：
    SeckillService --> SeckillDao/SuccessKilledDao --> SqlSessionFactory -> DataSource

依赖注入：
    xml：
        1:初始化的类来自第三放的类库，比如DataSource
        2:命名空间的配置 context aop mvc
    注解：
        1：自身开发的类 @service @controller
    java配置类：
        1：不常见，自定义修改依赖类库（加入逻辑程序）
        
事务的传播行为：事务传播行为就是多个事务方法相互调用时，事务如何在这些方法间传播
    1：propagation_requierd：如果当前没有事务，就新建一个事务，如果已存在一个事务中，加入到这个事务中，这是最常见的选择。
    2：propagation_supports：支持当前事务，如果没有当前事务，就以非事务方法执行。
    3：propagation_mandatory：使用当前事务，如果没有当前事务，就抛出异常。
    4：propagation_required_new：新建事务，如果当前存在事务，把当前事务挂起。
    5：propagation_not_supported：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
    6：propagation_never：以非事务方式执行操作，如果当前事务存在则抛出异常。
    7：propagation_nested：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与propagation_required类似的操作
    
Spring 默认的事务传播行为是 PROPAGATION_REQUIRED，它适合于绝大多数的情况。
    假设 ServiveX#methodX() 都工作在事务环境下（即都被 Spring 事务增强了），
    假设程序中存在如下的调用链：Service1#method1()->Service2#method2()->Service3#method3()，
    那么这 3 个服务类的 3 个方法通过 Spring 的事务传播机制都工作在同一个事务中。
    
注意：
    只有抛出运行期异常时才会回滚
    小心不当的try--catch catch之后spring无法感知异常了就，会出现部分成功部分失败的情况。
    ######################################################################################

restful规范：
    GET--查询操作
    POST--添加、修改操作
    PUT--修改操作            幂等性：post非幂等性操作  put幂等性操作
    DELETE--删除操作
    
URL： /模块/资源/{标识}/集合......
    
spring MVC运行流程：
    
    1：client发送请求
    
    2：DispatcherServlet 拦截所有的请求
            默认使用DefaultAnnotationHandlerMapping：用来映射URL，那个URL对应到哪一个具体的handler
            
    3:映射完成之后：会使用默认的DefaultAnnotationHandlerAdapter:用来做handler适配工作，最终衔接我们的 controller
        此处有拦截器也会加入其中
        
    4：产出 ModelAndView (/sss/sss/sss/xdfr/...)
        同时会交互到DispatcherServlet中，
        
    5：发现应用的是一个 InteralResourceViewResolver：默认jsp 的view，会把我们的Model和jsp相结合返回用户。

深度优化：完全避免网络延迟，使用存储过程(插入购买明细与减库存全部放在存储过程中，看看就行了)
    降低行级锁到commit的时间。
    
    DELIMITER $$
    CREATE PROCEDURE `seckill`.`execute_seckill`
    (in v_seckill_id bitint , in v_phone bigint, in v_kill_time timestamp,out r_result int)
    
    BEGIN
        DECLARE insert_count int default  0;
        START TRANSACTION;
            insert ignore into success_killed(seckill_id,user_phone,create_time)
                value (v_seckill_id,v_phone,v_kill_time);
            select  roe_count() into insert_count;
            IF (insert_count=0) THEN
                ROLLBACK;
                SET r_result=-1;
            ELSEIF(insert_count<0) THEN
                  SET r_result=-2;
            ELSE
                UPDATE seckill set num=num-1 where seckill_id=v_seckill_if 
                        and  end_time>v_kill_time
                        and start_time<v_kill_time
                        and number>0
                select  roe_count() into insert_count; 
                  IF (insert_count=0) THEN
                     ROLLBACK;
                     SET r_result=0;
                  ELSEIF(insert_count<0) THEN
                     ROLLBACK;
                     SET r_result=-2;   
                  ELSE
                     COMMIT;
                     SET r_result=1;
                  END IF;
            END IF;
    END
    $$
    DELIMITER ;
    set @r_result=-3;
    call execute_seckill(XX,XX,XX,XX);
    select @r_result;

部署：CDN+ nginx+jetty+redis+mysql