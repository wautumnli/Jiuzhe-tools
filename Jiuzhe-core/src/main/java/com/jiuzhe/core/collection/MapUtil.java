package com.jiuzhe.core.collection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Map工具
 *
 * @author tianzhenkun
 */
public class MapUtil {
    public static <K, V> Map<K, V> asMap(Object... objects) {
        Map<K, V> map = initHashMap(objects.length / 2);
        // key, value成对，所以传入数量为偶数
        if (Objects.equals(objects.length & 1, 1)) {
            throw new RuntimeException("无法构建Map,参数数量有误!");
        }
        // 初始化key value类型
        Class<?> kClass = null;
        Class<?> vClass = null;
        for (int i = 0; i < objects.length; i++) {
            if (Objects.nonNull(objects[i])) {
                if ((i & 1) == 1) {
                    vClass = initKeyValueClass(vClass, objects[i]);
                } else {
                    kClass = initKeyValueClass(kClass, objects[i]);
                }
            }
        }
        for (int i = 0; i < objects.length - 1; i += 2) {
            map.put((K) objects[i], (V) objects[i + 1]);
        }
        return map;
    }

    /**
     * 初始化HashMap大小
     *
     * @param initialCapacity 初始化大小
     * @param <K>             Key
     * @param <V>             Value
     * @return HashMap
     */
    public static <K, V> Map<K, V> initHashMap(int initialCapacity) {
        // 初始化大小，考虑扩容的原因，且HashMap大小为2的幂次，所有初始化大小 = 自己所需大小 / 0.75 + 1
        return new HashMap<>((int) ((float) initialCapacity / 0.75F + 1.0F));
    }

    /**
     * 初始化key value类型
     *
     * @param clazz  class类
     * @param object 对象
     * @return key value类型
     */
    private static Class<?> initKeyValueClass(Class<?> clazz, Object object) {
        if (Objects.isNull(clazz)) {
            return object.getClass();
        } else if (!Objects.equals(clazz, object.getClass())) {
            // 如果出现不同类型了，不能转map
            throw new RuntimeException("无法构建Map,参数类型有误!");
        }
        return null;
    }
}
