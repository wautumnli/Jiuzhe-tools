package com.ql.test.fieldfill;

import com.jd.base.common.annotation.base.FieldAnalysis;
import lombok.Data;

import java.util.Map;

/**
 * @author wanqiuli
 */
@Data
@FieldAnalysis
public class Color extends Base {
    private String type;
}
