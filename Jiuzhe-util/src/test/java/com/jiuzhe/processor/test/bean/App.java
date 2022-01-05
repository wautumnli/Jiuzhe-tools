package com.jiuzhe.processor.test.bean;

import com.jiuzhe.processor.ProcessorFunction;
import com.jiuzhe.processor.annotation.MapToObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wautumnli
 */
@Data
@MapToObject
public class App implements ProcessorFunction {
    private Integer id;
    private String name;
    private BigDecimal money;
    private Date date;

    public App(Integer id, String name, BigDecimal money, Date date) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.date = date;
    }
}