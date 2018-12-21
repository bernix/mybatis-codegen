<#assign classNameLower = className?uncap_first>
<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro mapperElType propertyName,jdbcType>${r"#{"}${propertyName}, jdbcType=${jdbcType}}</#macro>
<#macro mapperElColumn column>${r"#{"}${column.propertyName}, jdbcType=${column.jdbcType}}</#macro>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="${basePackage}.dao<#if module??>.${module}</#if>.${className}Dao">

    <!-- 通用查询映射结果 -->
    <resultMap id="baseResultMap" type="${classNameLower}">
    <#if table.pkColumn??><#--生成主键排在第一位-->
        <id property="${table.pkColumn.propertyName}" jdbcType="${table.pkColumn.jdbcType}" column="${table.pkColumn.name}" />
    </#if>
    <#list table.notPkColumns as column><#--生成公共字段 -->
        <#if !column.pk>
        <result property="${column.propertyName}" jdbcType="${column.jdbcType}" column="${column.name}" />
        </#if>
    </#list>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="baseColumnList">
        ${table.columnNames}
    </sql>

    <sql id="selectCondition">
        <where>
            <trim suffixOverrides="AND">
                <!-- 在这里添加需要过滤查询的字段 -->
            <#list table.columns as column>
                <#if column.javaType == 'byte[]'><#continue></#if>
                <if test="${column.propertyName} != null<#if column.stringType> and ${column.propertyName} != ''</#if>">
                    AND ${column.name} = <@mapperElColumn column/>
                </if>
            </#list>
            </trim>
            <!--复杂的查询，使用sql完成-->
            <trim suffixOverrides="AND">
                <if test="whereSql != null">
                    AND <@mapperEl 'whereSql' />
                </if>
            </trim>
        </where>
    </sql>

    <select id="get" parameterType="int" resultMap="baseResultMap">
        SELECT <include refid="baseColumnList" />
        FROM ${table.fullName}
        WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> = <@mapperEl 'id' />
    </select>

    <select id="lock" parameterType="int" resultMap="baseResultMap">
        SELECT <include refid="baseColumnList" />
        FROM ${table.fullName}
        WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> = <@mapperEl 'id' />
        FOR UPDATE
    </select>

    <insert id="insert" parameterType="${classNameLower}" useGeneratedKeys="<#if table.hasAutoIncrementPk>true<#else>false</#if>"<#if table.hasAutoIncrementPk> keyProperty="id"</#if>>
        INSERT INTO ${table.fullName}
        <trim prefix="(" suffix=")" suffixOverrides="," >
        <#list table.columns as column>
            <#if column.pk && column.autoIncrement><#continue></#if>
            ${column.name},
        </#list>
        </trim>
        <trim prefix=" VALUES (" suffix=")" suffixOverrides=",">
        <#list table.columns as column>
            <#if column.pk && column.autoIncrement><#continue></#if>
            <@mapperElColumn column />,
        </#list>
        </trim>
    </insert>

    <insert id="insertSelective" parameterType="${classNameLower}" useGeneratedKeys="<#if table.hasAutoIncrementPk>true<#else>false</#if>"<#if table.hasAutoIncrementPk> keyProperty="id"</#if>>
        INSERT INTO ${table.fullName}
        <trim prefix="(" suffix=")" suffixOverrides="," >
        <#list table.columns as column>
            <#if column.pk && column.autoIncrement><#continue></#if>
            <if test="${column.propertyName} != null">${column.name},</if>
        </#list>
        </trim>
        <trim prefix=" VALUES (" suffix=")" suffixOverrides=",">
        <#list table.columns as column>
            <#if column.pk && column.autoIncrement><#continue></#if>
            <if test="${column.propertyName} != null"><@mapperElColumn column />,</if>
        </#list>
        </trim>
    </insert>

    <update id="update" parameterType="${classNameLower}">
        UPDATE ${table.fullName}
        <set>
        <#list table.columns as column>
            <#if column.pk><#continue></#if>
            ${column.name} = <@mapperElColumn column />,
        </#list>
        </set>
        WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> = <@mapperEl 'id' />
    </update>

    <update id="updateSelective" parameterType="${classNameLower}">
        UPDATE ${table.fullName}
        <set>
        <#list table.columns as column>
            <#if column.pk><#continue></#if>
            <if test="${column.propertyName} != null">${column.name} = <@mapperElColumn column />,</if>
        </#list>
        </set>
        WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> = <@mapperEl 'id' />
    </update>

    <delete id="delete" parameterType="int">
        DELETE FROM ${table.fullName} WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> = <@mapperEl 'id' />
    </delete>

    <delete id="deleteByIds" parameterType="collection">
        DELETE FROM ${table.fullName} WHERE <#if table.pkColumn??>${table.pkColumn.name}</#if> IN
        <foreach collection="list" item="item" separator="," open="(" close=")"><@mapperElType 'item','INTEGER' /></foreach>
    </delete>

    <select id="find" parameterType="map" resultMap="baseResultMap">
        SELECT <include refid="baseColumnList" />
        FROM ${table.fullName}
        <include refid="selectCondition" />
        <#if table.pkColumn??>ORDER BY ${table.pkColumn.name} DESC</#if>
        <if test="first != null and last != null and  first &gt;= 0 and last &gt; 0 ">
        LIMIT <@mapperEl 'first' />, <@mapperEl 'last' />
        </if>
    </select>

    <select id="count" parameterType="map" resultType="long">
        SELECT COUNT(1) FROM ${table.fullName} <include refid="selectCondition" />
    </select>

</mapper>