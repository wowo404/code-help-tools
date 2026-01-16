package org.justing.generator;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emmet on 17-3-29.
 * desc:
 */
public class Generate {

    /**
     * @param host        数据库IP
     * @param port        数据库端口号
     * @param database    数据库名
     * @param username    数据库用户名
     * @param password    数据库密码
     * @param tableName   表名
     * @param entityName  实体类名
     * @param basePackage 实体类包
     * @param entityPath  实体类路径
     * @throws Exception
     */
    public static void generate(String host, String port, String database, String username, String password,
                                String tableName, String entityName, String basePackage,
                                String entityPath, String mapperPath, String servicePath, String serviceInterfacePath,
                                String controllerPath, String entitySuffix, String requestMappingPrefix) throws Exception {
        String entityPackage = basePackage + ".domain";
        String mapperPackage = basePackage + ".mapper";
        String servicePackage = basePackage + ".service.impl";
        String serviceInterfacePackage = basePackage + ".service";
        String controllerPackage = basePackage + ".controller";
        //读取表结构
        TableInfo tableInfo = initTable(host, port, database, username, password, tableName);
        //生成实体类--会覆盖已存在文件
        GenerateEntity.generate(entityName + entitySuffix, entityPackage, entityPath, database,
                tableName, tableInfo.getColnames(), tableInfo.getColTypes(), tableInfo.getColComment(),
                tableInfo.getExtra(), tableInfo.getImportDate(), tableInfo.getImportDateTime(), tableInfo.getImportBigDecimal());
        ///生成Mapper--不会覆盖
        GenerateMapper.generate(entityName, entityPackage, mapperPackage, mapperPath, entitySuffix);
        //生成Service接口--不会覆盖
        GenerateServiceInterface.generate(entityName, entityPackage, mapperPackage, serviceInterfacePackage, serviceInterfacePath, entitySuffix);
        //生成Service impl--不会覆盖
        GenerateService.generate(entityName, entityPackage, mapperPackage, servicePackage, servicePath, serviceInterfacePackage, entitySuffix);
        //生成Controller--不会覆盖
        GenerateController.generate(entityName, tableInfo, entityPackage, mapperPackage, serviceInterfacePackage, servicePath, controllerPackage,
                controllerPath, entitySuffix, requestMappingPrefix);
    }


    public static void main(String[] args) throws Exception {
        String basePackage = "me.zhengjie";
        String basePath = "";
        String property = System.getProperty("user.name");
        if ("liuzhangsheng".equals(property)) {
            basePath = "D:\\work\\projects\\chenwenbi\\code\\eladmin\\";
        } else {
            return;
        }
        String entitySuffix = "";//po类的后缀
        String requestMappingPrefix = "/app/api";//requestMapping的前缀，即/api/**
        String entityPath = basePath + "eladmin-common-service/src/main/java/me/zhengjie/domain/";
        String mapperPath = basePath + "eladmin-common-service/src/main/java/me/zhengjie/mapper/";
        String servicePath = basePath + "eladmin-common-service/src/main/java/me/zhengjie/service/impl/";
        String serviceInterfacePath = basePath + "eladmin-common-service/src/main/java/me/zhengjie/service/";
        String controllerPath = basePath + "eladmin-app/src/main/java/me/zhengjie/modules/user/rest/";
        Map<String, String> names = new HashMap<>();
//        names.put("bis_image", "BisImage");
//        names.put("bis_recharge_config", "BisRechargeConfig");
//        names.put("bis_recharge_record", "BisRechargeRecord");
//        names.put("bis_special_product", "BisSpecialProduct");
//        names.put("bis_special_product_category", "BisSpecialProductCategory");
//        names.put("ex_user_points_flow", "ExUserPointsFlow");
//        names.put("pay_order", "PayOrder");
//        names.put("pay_order_points", "PayOrderPoints");
//        names.put("pay_order_channel", "PayOrderChannel");
        names.put("ex_identity", "Identity");
        for (Map.Entry<String, String> entry : names.entrySet()) {
            generate("118.178.142.156", "3306", "eladmin", "root", "88GteF34iM2H8g01x",
                    entry.getKey(), entry.getValue(), basePackage, entityPath, mapperPath, servicePath, serviceInterfacePath,
                    controllerPath, entitySuffix, requestMappingPrefix);
        }
    }

    private static TableInfo initTable(String host, String port, String database, String username, String password, String tableName) throws Exception {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", host, port, database);
        Connection conn = DriverManager.getConnection(url, username, password);
        String tableSql = "select TABLE_COMMENT from information_schema.tables where table_name = '" + tableName + "'";
        PreparedStatement preparedStatement = conn.prepareStatement(tableSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tableInfo.setTableComment(resultSet.getString(1));
        }
        String strsql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_KEY, COLUMN_COMMENT, EXTRA  FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + database + "' AND TABLE_NAME = '" + tableName + "'" + " ORDER BY ORDINAL_POSITION"; //读一行记录;
        PreparedStatement pstmt = conn.prepareStatement(strsql);
        ResultSet result = pstmt.executeQuery();
        while (result.next()) {
            tableInfo.getColnames().add(result.getString(1));
            tableInfo.getColTypes().add(result.getString(2));
            tableInfo.getColComment().add(result.getString(4));
            tableInfo.getExtra().add(result.getString(5));
            if ("date".equals(result.getString(2)) || "datetime".equals(result.getString(2)) || "timestamp".equals(result.getString(2)))
                tableInfo.setImportDate(true);
            if ("decimal".equals(result.getString(2))) tableInfo.setImportBigDecimal(true);
        }
        if (conn != null) conn.close();
        return tableInfo;
    }

    @Getter
    @Setter
    public static class TableInfo {
        private String tableName;
        private String tableComment;
        private List<String> colnames = new ArrayList<>();
        private List<String> colTypes = new ArrayList<>();
        private List<String> colComment = new ArrayList<>();
        private List<String> extra = new ArrayList<>();
        private Boolean importDate = false;
        private Boolean importDateTime = false;
        private Boolean importBigDecimal = false;
    }

}
