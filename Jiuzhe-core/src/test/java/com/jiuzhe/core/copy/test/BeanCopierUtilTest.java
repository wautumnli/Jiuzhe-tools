package com.jiuzhe.core.copy.test;

import com.jiuzhe.core.copy.BeanCopierUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class BeanCopierUtilTest {

    @Test
    public void singleBeanCopyTest() {
        Class01 class01 = Class01.builder()
                .id(1)
                .uid(2L)
                .name("test_single_copy")
                .date(new Date())
                .money(new BigDecimal("1.5"))
                .apps(Arrays.asList(1, 2, 3))
                .build();
        Class02 class02 = new Class02();
        System.out.println(BeanCopierUtil.copy(class01, class02));
    }

    @Test
    public void multiBeanCopyTest() {
        for (int i = 0; i < 10; i++) {
            Class01 class01 = Class01.builder()
                    .id(i)
                    .uid((long) i)
                    .name("test_single_copy" + i)
                    .date(new Date(i))
                    .money(new BigDecimal(i))
                    .apps(Collections.singletonList(i))
                    .build();
            Class02 class02 = new Class02();
            System.out.println(BeanCopierUtil.copy(class01, class02));
        }
    }
}
