

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
		
3、项目构建

mvn archetype:generate -DgroupId=com.andieguo.poi -DartifactId=poi-mapreduce -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local

4、MySQL数据库构建

	#创建数据库
	CREATE DATABASE IF NOT EXISTS db_poi CHARACTER SET utf8 COLLATE utf8_general_ci;
    
	#创建表
	#name,address,telephone,lat,lng,levelOne,levelTwo,levelThree
    CREATE TABLE `tb_poidata`(
      `id` bigint(20) unsigned NOT NULL auto_increment,
      `name` varchar(50),
      `address` varchar(100),
      `telephone` varchar(20),
      `lat` double NOT NULL,
      `lng` double NOT NULL,
      `levelOne` varchar(10) NOT NULL,
      `levelTwo` varchar(10) NOT NULL,
      `levelThree` varchar(10) NOT NULL,
      `city` varchar(10) NOT NULL,
       PRIMARY KEY  (`id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
    
    #索引构建
    ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name (`city`);
	ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name1 (`levelOne`);
	ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name2 (`levelTwo`);
	ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name3 (`levelThree`);
	ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name4 (`lat`);
	ALTER  TABLE  `tb_poidata`  ADD  INDEX index_name5 (`lng`);