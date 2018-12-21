<#assign classNameLower = className?uncap_first>
package ${basePackage}.entity<#if module??>.${module}</#if>;

import com.csut.common.IdEntity;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ${table.fullName!}
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${currentDate}
 */
@Alias("${classNameLower}")
public class ${className} extends IdEntity<<#if table.pkColumn??>${table.pkColumn.javaType}<#else>Integer</#if>> {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.columns as column>
    <#if column.propertyName == 'id'>
        <#continue />
    </#if>

    /**
     * ${table.name}.${column.name}
     * ${column.comment!}
     */
    <#if column.notBlank>
    @NotBlank
    </#if>
    <#if column.notNull>
    @NotNull
    </#if>
    <#if column.hasLength>
    @Length(max = ${column.max})
    </#if>
    private ${column.javaType} ${column.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#-- ----------  BEGIN Getter/Setter  ---------->
<#list table.columns as field>
    <#if field.propertyName == 'id'>
        <#continue />
    </#if>

    <#if field.javaType == "Boolean">
        <#assign getprefix="is"/>
    <#else>
        <#assign getprefix="get"/>
    </#if>
    public ${field.javaType} ${getprefix}${field.capitalPropertyName}() {
        return ${field.propertyName};
    }

    public void set${field.capitalPropertyName}(${field.javaType} ${field.propertyName}) {
        this.${field.propertyName} = ${field.propertyName};
    }
</#list>
<#-- ----------  END Getter/Setter  ---------->

}