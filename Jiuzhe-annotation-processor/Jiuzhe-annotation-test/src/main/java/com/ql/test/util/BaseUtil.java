package com.ql.test.util;

import com.ql.test.baseinfo.HttpServletRequest;
import com.ql.test.baseinfo.UserInfo;

/**
 * @author wanqiuli
 */
public class BaseUtil {

    public static UserInfo getUserInfo(HttpServletRequest a) {
        return new UserInfo(a);
    }
}
