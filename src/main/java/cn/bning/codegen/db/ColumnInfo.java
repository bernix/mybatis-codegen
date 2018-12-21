package cn.bning.codegen.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
@Getter
@Setter
@Builder
public class ColumnInfo {

    private boolean pk;
    private boolean autoIncrement;
    private boolean nullable;
    private String name;
    private String type;
    private String comment;
    private int max;
    private String jdbcType;
    // entity property
    private String propertyName;
    private String javaType;

    public boolean isNotBlank() {
        return !nullable && isStringType();
    }

    public boolean isNotNull() {
        return !nullable && !isStringType();
    }

    public boolean isHasLength() {
        return max > 0 && isStringType();
    }

    public boolean isStringType() {
        return "String".equals(javaType);
    }

    public String getCapitalPropertyName() {
        return StringUtils.capitalize(propertyName);
    }
}
