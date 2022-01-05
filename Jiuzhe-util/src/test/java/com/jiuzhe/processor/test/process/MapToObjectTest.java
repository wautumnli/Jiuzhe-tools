package com.jiuzhe.processor.test.process;


import com.jiuzhe.processor.test.bean.App;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


public class MapToObjectTest {

    @Test
    public void exec() {
        App app = new App(
                1,
                "tzk",
                BigDecimal.valueOf(1.5D),
                new Date());
        Map<String, Object> stringObjectMap = app.objectToMap();
        stringObjectMap.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
