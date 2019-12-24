实体类的类名不能有"_"。

配置文件优先级由低到高依次为：
1.装入主配置文件:/org/jrplat/config.properties
2.装入自定义主配置文件：/config.local.properties
3.装入数据库配置文件：/org/jrplat/db.properties
4.装入自定义数据库配置文件：/db.local.properties
5.装入扩展配置文件：/web.jrplat.properties

启动初始化:
    1.等系统启动完成,执行address.sql
    2.注意单位【数据中心】的单位类型，如果为空，执行。
        UPDATE Org O
        SET O.DWLX_id = (SELECT id
                         FROM DicItem D
                         WHERE D.code = '1' AND D.name = '承运商')
        WHERE O.orgName = '数据中心' AND O.id = 1;
    3.完成后重启系统.