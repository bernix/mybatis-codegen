package cn.bning.codegen.converter;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Bernix Ning
 * @date 2018-12-18
 */
@Getter
@Builder
public class FieldInfo {

    private String name;
    private String comment;
}
