

1、数据库、表创建

   
	#创建数据库
	CREATE DATABASE IF NOT EXISTS db_poi CHARACTER SET utf8 COLLATE utf8_general_ci;
    
	#创建表
    CREATE TABLE `tb_poi`(
      `id` bigint(20) unsigned NOT NULL auto_increment,
      `poikey` varchar(100) NOT NULL,
      `poivalue` varchar(200) NOT NULL,
      `poitype` int NOT NULL,
       PRIMARY KEY  (`id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8;

2、项目层级

    poi-parent 
		poi-base				基础包（包含File、excel工具类）
		poi-persistence			POI类型数据处理
		poi-local				读取百度数据到本地
		poi-web 				REST服务提供