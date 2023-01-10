package com.kobe.xt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 各微服务给前端返回的统一的DTO
 * @date 2022/9/14 17:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> implements Serializable {


    private static final long serialVersionUID = 9017236002997196251L;

    /**
     * 返回信息描述
     */
    private String message;

    /**
     * 返回给前端的业务码
     */
    private long respCode;

    /**
     * 返回给前端的调用结果
     */
    private boolean result;

    /**
     * 返回给前端的JSON串
     */
    private T respData;

}
