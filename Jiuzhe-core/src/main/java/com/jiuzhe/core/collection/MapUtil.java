package com.jiuzhe.core.collection;

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
        Map<K, V> map = new HashMap<>(objects.length);
        if (objects.length % 2 != 0) {
            throw new RuntimeException("无法构建Map,参数数量有误!");
        }
        Class<?> k = objects[0].getClass();
        Class<?> v = objects[1].getClass();
        for (int i = 0; i < objects.length - 1; i += 2) {
            if (!Objects.equals(k, objects[i].getClass()) || !Objects.equals(v, objects[i + 1].getClass())) {
                throw new RuntimeException("无法构建Map,参数类型有误!");
            }
            map.put((K) objects[i], (V) objects[i + 1]);
        }
        return map;
    }
}
