package cn.bning.codegen.db;

import cn.bning.codegen.config.DatasourceConfig;
import cn.bning.codegen.converter.DbTypeConverter;
import cn.bning.codegen.converter.FieldInfo;
import cn.bning.codegen.exception.GeneratorException;
import cn.bning.codegen.utils.JacksonUtils;
import cn.bning.codegen.utils.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
public class DbInfoReader {

    private static final Logger log = LoggerFactory.getLogger(DbInfoReader.class);

    private static final String QUERY_TABLE = "show table status from {db} like '{table}'";
    private static final String QUERY_COLUMN = "show full fields from {db}.{table}";
    private static final String PK_KEY = "PRI";
    private static final String AUTO_INCREMENT_EXTRA = "auto_increment";
    private static final String NULL_YES = "YES";

    private DatasourceConfig ds;
    private Map<String, FieldInfo> fieldInfoMap;

    public static DbInfoReader newInstance(DatasourceConfig ds) {
        return newInstance(ds, null);
    }

    public static DbInfoReader newInstance(DatasourceConfig ds, Map<String, FieldInfo> fieldInfoMap) {
        return new DbInfoReader(ds, fieldInfoMap);
    }

    private DbInfoReader(DatasourceConfig ds, Map<String, FieldInfo> fieldInfoMap) {
        this.ds = ds;
        this.fieldInfoMap = fieldInfoMap;
    }

    public TableInfo read(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            throw new GeneratorException("未指定表名");
        }
        if (StringUtils.isBlank(ds.getDb())) {
            throw new GeneratorException("未指定数据库名,请设置Datasource.db");
        }

        String fullTableName = ds.getDb() + "." + tableName;

        String tableSql = QUERY_TABLE.replace("{db}", ds.getDb()).replace("{table}", tableName);
        String columnSql = QUERY_COLUMN.replace("{db}", ds.getDb()).replace("{table}", tableName);
        Connection conn = ds.getConnection();

        Statement stmt = null;
        ResultSet rs = null;
        try {
            TableInfo tableInfo;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(tableSql);
            if (rs.next()) {
                tableInfo = TableInfo.builder()
                        .db(ds.getDb())
                        .name(tableName)
                        .comment(rs.getString("Comment"))
                        .build();
            } else {
                throw new GeneratorException("未找到表信息: " + fullTableName);
            }
            rs.close();
            log.info(">> 表信息读取成功: {}\n{}", fullTableName, JacksonUtils.pojo2json(tableInfo));

            List<ColumnInfo> columns = new ArrayList<>(50);
            rs = stmt.executeQuery(columnSql);
            while (rs.next()) {
                String name = rs.getString("Field");
                // type is something like:  "int(11) unsigned"   "varchar(32)" "datetime"
                String type = rs.getString("Type");
                columns.add(ColumnInfo.builder()
                        .pk(PK_KEY.equals(rs.getString("Key")))
                        .autoIncrement(AUTO_INCREMENT_EXTRA.equals(rs.getString("Extra")))
                        .nullable(NULL_YES.equals(rs.getString("Null")))
                        .name(name)
                        .type(getType(type))
                        .max(getMax(type))
                        .propertyName(getPropertyName(name))
                        .jdbcType(DbTypeConverter.mysqlType2MybatisJdbcType(type))
                        .javaType(DbTypeConverter.mysqlType2JavaType(type))
                        .comment(getComment(name, rs.getString("Comment")))
                        .build());
            }
            log.info(">> 字段信息读取成功: {}\n{}", fullTableName, JacksonUtils.pojo2json(columns));

            tableInfo.setColumns(columns);
            return tableInfo;

        } catch (SQLException e) {
            log.error(">> 从数据库读取表信息和字段信息失败: {}", fullTableName, e);
            throw new GeneratorException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    private String getPropertyName(String columnName) {
        if (fieldInfoMap != null && fieldInfoMap.get(columnName) != null) {
            return fieldInfoMap.get(columnName).getName();
        }
        boolean hasUnderScore = columnName.contains("_");
        return hasUnderScore ? StringHelper.toCamelCase(columnName) : StringUtils.uncapitalize(columnName);
    }

    private String getComment(String columnName, String columnComment) {
        String comment = null;
        if (fieldInfoMap != null && fieldInfoMap.get(columnName) != null) {
            comment = fieldInfoMap.get(columnName).getComment();
        }
        return comment == null ? columnComment : comment;
    }

    private String getType(String type) {
        int bracketStart = type.indexOf('(');
        if (bracketStart < 0) {
            return type;
        }
        return type.substring(0, bracketStart);
    }

    private int getMax(String type) {
        if ("text".equals(type.toLowerCase())) {
            return 65535;
        }
        int bracketStart = type.indexOf('(');
        if (bracketStart < 0) {
            return 0;
        }
        if (type.indexOf(',') > 0) {
            return 0;
        }
        return Integer.parseInt(type.substring(bracketStart + 1, type.indexOf(')')));
    }
}
