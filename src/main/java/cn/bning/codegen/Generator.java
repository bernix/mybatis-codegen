package cn.bning.codegen;

import cn.bning.codegen.config.CodegenConfig;
import cn.bning.codegen.config.DatasourceConfig;
import cn.bning.codegen.converter.FieldInfo;
import cn.bning.codegen.db.DbInfoReader;
import cn.bning.codegen.db.TableInfo;
import cn.bning.codegen.engine.FreemarkerTemplateEngine;
import cn.bning.codegen.utils.HashMapBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
public class Generator {

    public static void main(String[] args) {
        TableInfo tableInfo = DbInfoReader.newInstance(datasourceConfig(), fieldInfoMap())
                .read("user");
        if (StringUtils.isBlank(tableInfo.getComment())) {
            tableInfo.setComment("用户信息");
        }

        FreemarkerTemplateEngine.newInstance(globalConfig())
                // .extDataModel() // 设置freemarker模板中要用到的其他参数值
                .generate("example-templates", tableInfo);
    }

    private static CodegenConfig globalConfig() {
        return CodegenConfig.builder()
                // 基础包名
                .basePackage("com.csut.admin")
                // 模块名
                .module("user")
                // 数据库映射的类名
                .className("User")
                .author("Bernix Ning")
                // mybatis mapper文件名
                .mapperName("user-mapper")
                // 指定生成代码文件的输出目录
                .outputDir("./generate-out")
                // 代码生成前清理代码输出目录
                .deleteOutput(true)
                .build();
    }

    private static DatasourceConfig datasourceConfig() {
        return DatasourceConfig.builder()
                .url("jdbc:mysql://localhost:3306/userdb?useUnicode=true&useSSL=false&serverTimezone=GMT&characterEncoding=utf8")
                .username("root")
                .pwd("12345678")
                .db("userdb")
                .build();
    }

    private static Map<String, FieldInfo> fieldInfoMap() {
        return HashMapBuilder
                .newMap("F01", FieldInfo.builder().name("id").build())
                .put("F02", FieldInfo.builder().name("username").comment("用户名").build())
                .put("F03", FieldInfo.builder().name("password").comment("登录密码").build())
                .put("F04", FieldInfo.builder().name("realname").comment("真实姓名").build())
                .put("F05", FieldInfo.builder().name("department").comment("部门").build())
                .put("F06", FieldInfo.builder().name("mobile").comment("手机号码").build())
                .put("F07", FieldInfo.builder().name("nickname").comment("昵称").build())
                .put("F08", FieldInfo.builder().name("status").build())
                .put("F09", FieldInfo.builder().name("registerDatetime").build())
                .put("F10", FieldInfo.builder().name("lastLoginDatetime").build())
                .build();
    }

}
