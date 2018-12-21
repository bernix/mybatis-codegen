package cn.bning.codegen.converter;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
public class DbTypeConverter {

    public static String mysqlType2JavaType(String type) {
        String t = type.toLowerCase();
        if (t.contains("bigint")) {
            return "Long";
        } else if (t.contains("tinyint(1)") || t.contains("bit")) {
            return "Boolean";
        } else if (t.contains("int")) {
            return "Integer";
        } else if (t.contains("text")) {
            return "String";
        } else if (t.contains("decimal")) {
            return "BigDecimal";
        } else if (t.contains("clob") || t.contains("blob") || t.contains("binary")) {
            return "byte[]";
        } else if (t.contains("float")) {
            return "Float";
        } else if (t.contains("double")) {
            return "Double";
        } else if (t.contains("json") || t.contains("enum")) {
            return "String";
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return "Date";
        }
        return "String";
    }

    public static String mysqlType2MybatisJdbcType(String type) {
        String t = type.toLowerCase();
        if (t.contains("bigint")) {
            return "BIGINT";
        } else if (t.contains("tinyint")) {
            return "TINYINT";
        } else if (t.contains("smallint")) {
            return "SMALLINT";
        } else if (t.contains("bit")) {
            return "BIT";
        } else if (t.contains("int")) {
            return "INTEGER";
        } else if (t.contains("text")) {
            return "VARCHAR";
        } else if (t.contains("decimal")) {
            return "DECIMAL";
        } else if (t.contains("clob") ) {
            return "CLOB";
        } else if (t.contains("blob")) {
            return "BLOB";
        } else if (t.contains("binary")) {
            return "BINARY";
        } else if (t.contains("float")) {
            return "FLOAT";
        } else if (t.contains("double")) {
            return "DOUBLE";
        } else if (t.contains("json") || t.contains("enum")) {
            return "VARCHAR";
        } else if (t.contains("datetime") || t.contains("timestamp")) {
            return "TIMESTAMP";
        } else if (t.contains("date")) {
            return "DATE";
        } else if (t.contains("time")) {
            return "TIME";
        }
        return "VARCHAR";
    }
}
