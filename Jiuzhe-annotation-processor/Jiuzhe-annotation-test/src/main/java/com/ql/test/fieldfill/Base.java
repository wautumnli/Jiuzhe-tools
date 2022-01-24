package com.ql.test.fieldfill;

import com.jd.base.common.annotation.base.FieldAnalysis;
import com.jd.base.common.annotation.util.AnnotationFunction;
import lombok.Data;

/**
 * @author wanqiuli
 */
@Data
@FieldAnalysis
public class Base implements AnnotationFunction {
    private String id;
    private String msg;
}
