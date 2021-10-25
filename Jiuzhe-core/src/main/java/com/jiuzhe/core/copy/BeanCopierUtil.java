package com.jiuzhe.core.copy;

import org.springframework.cglib.beans.BeanCopier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Bean拷贝工具, 使用CGlib方式
 *
 * @author wanqiuli
 */
public class BeanCopierUtil {

    /**
     * 缓存BeanCopier使用
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 单对象拷贝
     *
     * @param source 源对象
     * @param target 目的对象
     */
    public static <S, T> T copy(S source, T target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException("bean copier null object!");
        }
        BeanCopier copier = getCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
        return target;
    }

    /**
     * 对象集合拷贝
     *
     * @param sourceList  源对象集合
     * @param targetClass 目标函数new
     * @param <S>         源对象类型
     * @param <T>         目的对象类型
     * @return 目的对象集合
     */
    public static <S, T> List<T> batchCopy(List<S> sourceList, Supplier<T> targetClass) {
        if (Objects.isNull(sourceList)) {
            throw new IllegalArgumentException("bean copier list null object!");
        }
        if (sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        BeanCopier beanCopier = getCopier(sourceList.get(0).getClass(), targetClass.get().getClass());
        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (S s : sourceList) {
                T target = targetClass.get();
                beanCopier.copy(s, target, null);
                targetList.add(target);
            }
        } catch (Exception e) {
            throw new RuntimeException("bean copier copy exception");
        }
        return targetList;
    }

    /**
     * 寻找是否存在已经生成的copier,有的话返回,没有生成一个并缓存起来
     *
     * @param sourceClass 源对象类
     * @param targetClass 目的对象类
     * @return 由源对象拷贝到目的对象的BeanCopier
     */
    private static BeanCopier getCopier(Class<?> sourceClass, Class<?> targetClass) {
        BeanCopier beanCopier;
        String copierKey = genCopierKey(sourceClass, targetClass);
        if (BEAN_COPIER_MAP.containsKey(copierKey)) {
            beanCopier = BEAN_COPIER_MAP.get(copierKey);
        } else {
            beanCopier = BeanCopier.create(sourceClass, targetClass, Boolean.FALSE);
            BEAN_COPIER_MAP.put(copierKey, beanCopier);
        }
        return beanCopier;
    }

    /**
     * 生成source到target的key
     *
     * @param source 源对象类
     * @param target 目的对象类
     * @return 源对象类+目的对象类 key
     */
    private static String genCopierKey(Class<?> source, Class<?> target) {
        return source.toString() + target.toString();
    }
}