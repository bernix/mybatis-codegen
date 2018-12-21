<#assign classNameLower = className?uncap_first>
package ${basePackage}.service<#if module??>.${module}</#if>;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ${basePackage}.entity<#if module??>.${module}</#if>.${className};
import ${basePackage}.dao<#if module??>.${module}</#if>.${className}Dao;
import com.csut.common.Page;
import com.csut.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ${table.comment!}管理服务
 *
 * @author ${author}
 * @since ${currentDate}
 */
@Service
public class ${className}Service {

    final private ${className}Dao ${classNameLower}Dao;

    @Autowired
    public ${className}Service(${className}Dao ${classNameLower}Dao) {
        this.${classNameLower}Dao = ${classNameLower}Dao;
    }

    /**
     * 保存${table.comment!}
     *
     * @param ${classNameLower} 实体
     */
    public void save${className}(${className} ${classNameLower}) {
        if (${classNameLower}.getId() == null) {
            insert${className}(${classNameLower});
        } else {
            update${className}(${classNameLower});
        }
    }

    /**
     * 更新${table.comment!}
     *
     * @param ${classNameLower} 实体
     */
    public void update${className}(${className} ${classNameLower}) {
        ${classNameLower}Dao.update(${classNameLower});
    }

    /**
     * 添加${table.comment!}
     *
     * @param ${classNameLower} 实体
     */
    public Integer insert${className}(${className} ${classNameLower}) {
        ${classNameLower}Dao.insert(${classNameLower});
        return ${classNameLower}.getId();
    }

    /**
     * 批量删除${table.comment!}
     *
     * @param ids 主键id集合
     */
    public void delete${className}ByIds(List<Integer> ids) {
        ${classNameLower}Dao.deleteByIds(ids);
    }

    /**
     * 删除${table.comment!}
     *
     * @param id 主键 id
     */
    public void delete${className}(Integer id) {
        ${classNameLower}Dao.delete(id);
    }

    /**
     * 获取${table.comment!}
     *
     * @param id 主键 id
     * @return 实体
     */
    public ${className} get${className}(Integer id) {
        return get${className}(id, false);
    }

    /**
     * 获取${table.comment!}
     *
     * @param id   主键 id
     * @param lock 是否加锁
     * @return 实体
     */
    public ${className} get${className}(Integer id, boolean lock) {
        if (lock) {
            return ${classNameLower}Dao.lock(id);
        }
        return ${classNameLower}Dao.get(id);
    }

    /**
     * 根据过滤条件查找
     *
     * @param filter 过滤条件
     * @return 实体集合
     */
    public List<${className}> find${className}(Map<String, Object> filter) {
        return ${classNameLower}Dao.find(filter);
    }

    /**
     * 获取所有${table.comment!}
     *
     * @return 实体集合
     */
    public List<${className}> getAll${className}() {
        return find${className}(Maps.newHashMap());
    }

    /**
     * 根据过滤条件计算数量
     *
     * @param filter 过滤条件
     * @return 统计总数
     */
    public long count${className}(Map<String, Object> filter) {
        return ${classNameLower}Dao.count(filter);
    }

    /**
     * 查找分页信息
     *
     * @param pageRequest 分页请求
     * @param filter      过滤条件
     * @return 分页实体
     */
    public Page<${className}> find${className}Page(PageRequest pageRequest, Map<String, Object> filter) {
        long count = count${className}(filter);
        List<${className}> data;
        if (count > 0) {
            filter.putAll(pageRequest.getOffsetMap());
            data = find${className}(filter);
        } else {
            // no need to hit the database if the total count is 0
            data = Lists.newArrayList();
        }
        return new Page<>(pageRequest, data, count);
    }
}