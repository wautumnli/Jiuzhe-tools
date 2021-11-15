package com.jiuzhe.core.copy;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * CGLIB对象拷贝
 *
 * @author wanqiuli
 */
public class BeanCopyUtil {

    /**
     * 缓存BeanCopier使用
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 类型转换器,如果不使用,无法自动拆箱和装箱
     */
    private static final TypeConverter TYPE_CONVERTER = new TypeConverter();


    /**
     * 单对象拷贝,现存在不同类型转换不能成功的问题
     *
     * @param source 源对象
     * @param target 目的对象
     */
    public static <S, T> T copy(S source, T target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException("bean copier null object exception!");
        }
        BeanCopier copier = getCopier(source.getClass(), target.getClass());
        copier.copy(source, target, TYPE_CONVERTER);
        return target;
    }

    /**
     * 对象集合拷贝, 现存在不同类型转换不能成功的问题
     *
     * @param sourceList  源对象集合
     * @param targetClass 目标函数new
     * @param <S>         源对象类型
     * @param <T>         目的对象类型
     * @return 目的对象集合
     */
    public static <S, T> List<T> batchCopy(List<S> sourceList, Supplier<T> targetClass) {
        if (Objects.isNull(sourceList)) {
            throw new IllegalArgumentException("bean copier list null object exception");
        }
        if (sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        BeanCopier beanCopier = getCopier(sourceList.get(0).getClass(), targetClass.get().getClass());
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S s : sourceList) {
            T target = targetClass.get();
            beanCopier.copy(s, target, TYPE_CONVERTER);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * 自定义类型转换类
     */
    static class TypeConverter implements Converter {

        /**
         * 转换方法处理
         * 支持同类型转换，包装类型与基础类型转换，集合转换
         *
         * @param value   源字段值
         * @param target  目标类
         * @param context 目标类调用方法
         * @return 转换结果
         */
        @Override
        public Object convert(Object value, Class target, Object context) {
            // 如果源值为null且目标类是基础数据类型 则返回0 否者返回空
            if (Objects.isNull(value)) {
                // 如果是布尔默认值false，其他基础数据类型默认值0
                return target.isPrimitive() ? getDefaultValue(target) : null;
            }
            // 集合拷贝处理
            if (((value instanceof List) && Objects.equals(target, List.class)) ||
                    ((value instanceof Set) && Objects.equals(target, Set.class)) ||
                    ((value instanceof Map) && Objects.equals(target, Map.class))) {
                return value;
            }
            // 如果是同类型或是包装类型与基础类型之间转换直接返回值
            if (Objects.equals(value.getClass(), target) ||
                    Objects.equals(getBasicType(value), target)) {
                return value;
            }
            // 特殊情况转换 Byte - Integer互相转换
            if ((value instanceof Byte) && Objects.equals(target, Integer.class)) {
                return ((Byte) value).intValue();
            }
            if ((value instanceof Integer) && Objects.equals(target, Byte.class)) {
                return ((Integer) value).byteValue();
            }
            return null;
        }

        /**
         * 获取该基础数据类型默认值
         * boolean默认值false, char默认值\u0000, 其他为0
         *
         * @param clazz 基础数据类型
         * @return 默认值
         */
        private Object getDefaultValue(Class<?> clazz) {
            String type = clazz.toString();
            if (Objects.equals(type, "boolean")) {
                return false;
            }
            if (Objects.equals(type, "char")) {
                return '\u0000';
            }
            return 0;
        }

        /**
         * 判断是否存在基础数据类型，并返回基础数据类型，不存在返回空
         *
         * @return 基础数据类型类 如 Integer会获取int List没有基础数据类型会返回空
         */
        private Object getBasicType(Object value) {
            Field[] fields = value.getClass().getFields();
            if (fields.length <= 0) {
                return null;
            }
            try {
                for (Field field : fields) {
                    if (Objects.equals(field.getName(), "TYPE")) {
                        return field.get(null);
                    }
                }
            } catch (IllegalAccessException e) {
                return null;
            }
            return null;
        }
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
            beanCopier = BeanCopier.create(sourceClass, targetClass, Boolean.TRUE);
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