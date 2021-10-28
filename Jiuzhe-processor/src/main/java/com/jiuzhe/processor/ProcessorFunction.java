package com.jiuzhe.processor;

import java.util.Map;

/**
 * @author wautumnli
 */
public interface ProcessorFunction {

    /**
     * 对象转Map
     *
     * @return Map
     */
    default Map<String, Object> objectToMap() {
        return null;
    }
}
