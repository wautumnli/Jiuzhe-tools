package com.jiuzhe.core.copy;

import lombok.Data;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wautumnli
 * @date: 2021-11-12 14:49
 **/
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3)
public class BeanCopierJMH {

    private static SourceClass sourceClass;

    @Data
    static class SourceClass {
        private Integer id;
        private Long uid;
        private String name;
        private Byte jByte;
        private Date date;
        private BigDecimal bigDecimal;
        private List<Integer> intList;
        private Set<Long> longSet;
        private Map<String, String> stringMap;
    }

    @Data
    static class TargetClass {
        private Integer id;
        private Long uid;
        private String name;
        private Byte jByte;
        private Date date;
        private BigDecimal bigDecimal;
        private List<Integer> intList;
        private Set<Long> longSet;
        private Map<String, String> stringMap;
    }

    static {
        sourceClass = new SourceClass();
        sourceClass.setId(1);
        sourceClass.setUid(1L);
        sourceClass.setName("name");
        sourceClass.setJByte((byte) 1);
        sourceClass.setBigDecimal(new BigDecimal(1));
        sourceClass.setIntList(Arrays.asList(1, 2, 3));
        HashSet<Long> longSet = new HashSet<>(1);
        longSet.add(1L);
        sourceClass.setLongSet(longSet);
        HashMap<String, String> stringMap = new HashMap<>(1);
        stringMap.put("1", "2");
        sourceClass.setStringMap(stringMap);
    }


    @Benchmark
    public void beanCopier() {
        for (int i = 0; i < 10000; i++) {
            TargetClass targetClass = new TargetClass();
            BeanCopierUtil.copy(sourceClass, targetClass);
        }
    }

    @Benchmark
    public void springBeanUtils() {
        for (int i = 0; i < 1000000; i++) {
            TargetClass targetClass = new TargetClass();
            BeanUtils.copyProperties(sourceClass, targetClass);
        }
    }

    public void apacheBeanUtils() throws InvocationTargetException, IllegalAccessException {
        org.apache.commons.beanutils.BeanUtils.setDebug(1);
        for (int i = 0; i < 1000000; i++) {
            TargetClass targetClass = new TargetClass();
            org.apache.commons.beanutils.BeanUtils.copyProperties(targetClass, sourceClass);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BeanCopierJMH.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
