package com.jiuzhe.core.copy.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Class02 {
    private Integer id;
    private Long uid;
    private String name;
    private Date date;
    private BigDecimal money;
    private List<Integer> apps;
}