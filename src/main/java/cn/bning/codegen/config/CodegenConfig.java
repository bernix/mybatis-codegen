package cn.bning.codegen.config;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
@Getter
@Builder
public class CodegenConfig {

    private String basePackage;
    private String module;
    /**
     * entity class name
     */
    private String className;
    private String author;
    /**
     * mapper file name
     */
    private String mapperName;
    private String outputDir;
    private boolean deleteOutput;

}
