package com.ql.test;

import com.jiuzhe.annotation.FieldAnalysis;
import lombok.Data;

/**
 * @author wanqiuli
 */
@Data
@FieldAnalysis
public class MyTest extends Base{
    private String id;
    private String name;

    public void test() {
    }
}
