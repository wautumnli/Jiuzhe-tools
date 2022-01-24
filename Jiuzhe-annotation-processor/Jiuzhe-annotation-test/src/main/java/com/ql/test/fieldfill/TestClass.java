package com.ql.test.fieldfill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {
    public static void main(String[] args) {
        Color color = new Color();
        color.setType("111");
        color.setId("222");
        color.setMsg("3333");
        Map<String, String> stringStringMap = color.fieldAnalysis();
        for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
