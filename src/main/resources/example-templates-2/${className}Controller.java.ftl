<#assign classNameLower = className?uncap_first>
package ${basePackage}.controller<#if module??>.${module}</#if>;

import ${basePackage}.entity<#if module??>.${module}</#if>.${className};
import ${basePackage}.service<#if module??>.${module}</#if>.${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${table.comment!}Rest API
 *
 * @author ${author}
 * @since ${currentDate}
 */
@RestController
@RequestMapping("/${classNameLower}")
public class ${className}Controller {

    final private ${className}Service ${classNameLower}Service;

    @Autowired
    public ${className}Controller(${className}Service ${classNameLower}Service) {
        this.${classNameLower}Service = ${classNameLower}Service;
    }

    @PostMapping("/get")
    public ${className} get(@RequestParam("id") Integer id) {
        return ${classNameLower}Service.get${className}(id);
    }
}