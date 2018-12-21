package cn.bning.codegen.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
@Getter
@Setter
@Builder
public class TableInfo {

    private String db;
    private String name;
    private String comment;
    private List<ColumnInfo> columns;
    private ColumnInfo pkColumn;
    private List<ColumnInfo> notPkColumns;

    public boolean isHasAutoIncrementPk() {
        return getPkColumn() != null && getPkColumn().isAutoIncrement();
    }

    public String getFullName() {
        return db + "." + name;
    }

    public ColumnInfo getPkColumn() {
        if (pkColumn == null && columns != null) {
            for (ColumnInfo column : columns) {
                if (column.isPk()) {
                    pkColumn = column;
                    break;
                }
            }
        }
        return pkColumn;
    }

    public List<ColumnInfo> getNotPkColumns() {
        if (notPkColumns == null && columns != null) {
            notPkColumns = columns.stream().filter(column -> !column.isPk()).collect(Collectors.toList());
        }
        return notPkColumns;
    }

    public String getColumnNames() {
        if (columns != null) {
            return columns.stream().map(ColumnInfo::getName).collect(Collectors.joining(","));
        }
        return null;
    }

}
