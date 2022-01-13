package com.ql.test.baseinfo;

import com.jiuzhe.annotation.BaseInfo;
import com.ql.test.baseinfo.UserInfo;

/**
 * @author wanqiuli
 */
public class Test01 {

    @BaseInfo
    public void info(HttpServletRequest request, Test02 b) throws Exception {
    }
}
