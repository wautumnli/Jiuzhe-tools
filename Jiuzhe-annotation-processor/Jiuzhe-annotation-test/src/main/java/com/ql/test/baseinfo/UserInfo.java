package com.ql.test.baseinfo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanqiuli
 */
@Data
@NoArgsConstructor
public class UserInfo {

    private String tenantId;

    private String warehouseNo;

    public UserInfo(HttpServletRequest request) {
        this.tenantId = "111";
        this.warehouseNo = "222";
    }
}
