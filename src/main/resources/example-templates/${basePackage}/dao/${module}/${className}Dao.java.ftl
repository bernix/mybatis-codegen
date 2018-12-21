package ${basePackage}.dao<#if module??>.${module}</#if>;

import com.csut.common.dao.CurdDaoSupport;
import ${basePackage}.entity<#if module??>.${module}</#if>.${className};
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ${table.fullName}
 * ${table.comment!}数据库操作API
 *
 * @author ${author}
 * @since ${currentDate}
 */
@Repository
@Mapper
public interface ${className}Dao extends CurdDaoSupport<${className}, Integer> {

    /**
     * 根据ID列表批量删除
     *
     * @param ids ID列表
     * @return 影响的行数
     **/
    int deleteByIds(List<Integer> ids);
}