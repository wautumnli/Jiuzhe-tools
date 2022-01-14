package com.ql.test.baseinfo;

import com.jiuzhe.annotation.BaseInfo;

/**
 * @author wanqiuli
 */
public class Test01 {

    @BaseInfo
    public void info(HttpServletRequest request, Test02 b) throws Exception {
        String a_ = b.getTenantId();
        String b_ = b.getWarehouseNo();
        System.out.println(a_ + b_);
    }
}
