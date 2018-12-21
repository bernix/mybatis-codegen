package cn.bning.codegen.engine;

import cn.bning.codegen.config.CodegenConfig;
import cn.bning.codegen.db.TableInfo;
import cn.bning.codegen.exception.GeneratorException;
import cn.bning.codegen.utils.FileHelper;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernix Ning
 * @date 2018-12-17
 */
public class FreemarkerTemplateEngine {

    private static final Logger log = LoggerFactory.getLogger(FreemarkerTemplateEngine.class);

    private Configuration configuration;
    private CodegenConfig config;
    private Map<String, Object> extDataModel;

    public static FreemarkerTemplateEngine newInstance(CodegenConfig config) {
        return new FreemarkerTemplateEngine(config);
    }

    private FreemarkerTemplateEngine(CodegenConfig config) {
        this.config = config;
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setNumberFormat("###########");
    }

    public FreemarkerTemplateEngine extDataModel(Map<String, Object> extDataModel) {
        this.extDataModel = extDataModel;
        return this;
    }

    public void generate(String templateRootDir, TableInfo tableInfo) {
        if (config == null) {
            throw new GeneratorException("未指定CodegenConfig");
        }
        if (StringUtils.isBlank(templateRootDir)) {
            throw new GeneratorException("未指定模板目录");
        }
        if (StringUtils.isBlank(config.getOutputDir())) {
            throw new GeneratorException("未指定输出目录CodegenConfig.outputDir");
        }
        File templateRoot = FileHelper.getFile(templateRootDir);
        Collection<File> templateFiles = FileUtils.listFiles(templateRoot, new String[]{"ftl"}, true);
        if (templateFiles.isEmpty()) {
            log.warn(">> 没有任何ftl文件: {}", templateRoot.getAbsolutePath());
            return;
        }
        String handlingTemplateFile = null;
        try {
            File outputDir = new File(config.getOutputDir());
            log.info(">> 文件输出目录: {}", outputDir.getAbsolutePath());
            if (!outputDir.exists() || !outputDir.isDirectory()) {
                log.info(">> 创建文件输出目录...");
                outputDir.mkdir();
            } else if (config.isDeleteOutput()) {
                log.info(">> 清理文件输出目录...");
                FileUtils.cleanDirectory(outputDir);
            }

            Path templateRootDirPath = Paths.get(templateRoot.getPath());
            String basePackagePath = config.getBasePackage().replace(".", File.separator);

            configuration.setTemplateLoader(new FileTemplateLoader(templateRoot));

            for (File templateFile : templateFiles) {
                handlingTemplateFile = templateFile.getPath();
                // 模板文件的绝对路径
                Path fullTemplateFilePath = templateFile.toPath();
                // 模板文件的相对路径(相对于templateRootDir)
                Path templateFilePath = fullTemplateFilePath.subpath(templateRootDirPath.getNameCount(), fullTemplateFilePath.getNameCount());

                // 输出文件
                String outputFilePath = outputDir.getAbsolutePath() + File.separator + templateFilePath.toString();
                // 替换输出文件的路径变量和文件名变量
                String outputFileName = resolveOutputFile(basePackagePath, outputFilePath);

                // 若输出文件路径不存在，则需要创建
                File outputFile = new File(outputFileName);
                File outputFileDir = outputFile.getParentFile();
                if (outputFileDir != null && !outputFileDir.exists()) {
                    FileUtils.forceMkdir(outputFileDir);
                }

                // 开始进行模板处理
                Template template = configuration.getTemplate(templateFilePath.toString().replaceAll("\\\\", "/"));
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                template.process(buildDataModel(tableInfo), new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
                fileOutputStream.close();
                log.info(">> 模板文件处理完成: {} => {}", templateFile.getName(), outputFile);
            }
        } catch (TemplateException e) {
            log.error(">> 处理模板文件失败: {}", handlingTemplateFile);
        } catch (IOException e) {
            log.error(">> 生成代码出错: {}", templateRootDir, e);
        }
    }

    private Map<String, Object> buildDataModel(TableInfo tableInfo) {
        Map<String, Object> model;
        if (extDataModel != null) {
            model = new HashMap<>(extDataModel.size() + 16);
            model.putAll(extDataModel);
        } else {
            model = new HashMap<>(16);
        }
        model.put("basePackage", config.getBasePackage());
        model.put("module", config.getModule());
        model.put("className", config.getClassName());
        model.put("mapperName", config.getMapperName());
        model.put("table", tableInfo);
        model.put("author", config.getAuthor());
        model.put("currentDate", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
        return model;
    }

    private String resolveOutputFile(String basePackagePath, String templateFileName) {
        // get rid of '.ftl' extension
        String outputFile = templateFileName.substring(0, templateFileName.length() - 4);
        // resolve the basePackage in the path
        if (outputFile.contains("${basePackage}")) {
            outputFile = outputFile.replace("${basePackage}", basePackagePath);
        }
        // resolve the module in the path
        if (outputFile.contains("${module}")) {
            if (StringUtils.isBlank(config.getModule())) {
                outputFile = outputFile.replace("${module}" + File.separator, "");
            } else {
                outputFile = outputFile.replace("${module}", config.getModule());
            }
        }
        // resolve the mapperName in the path
        if (outputFile.contains("${mapperName}") && StringUtils.isNotBlank(config.getMapperName())) {
            outputFile = outputFile.replace("${mapperName}", config.getMapperName());
        }
        // resolve the className in the path
        if (outputFile.contains("${className}") && StringUtils.isNotBlank(config.getClassName())) {
            outputFile = outputFile.replace("${className}", config.getClassName());
        }
        return outputFile;
    }

}
