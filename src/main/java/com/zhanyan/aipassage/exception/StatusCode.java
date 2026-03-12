package com.zhanyan.aipassage.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 状态码枚举定义
 *
 * @author qyd
 * @date 2022-10-13
 */
public enum StatusCode
{

    SUCCESS(1000, "请求成功"),
    INVALID_PARAM(1001, "非法字段"),
    SYSTEM_BUSY(1002, "系统忙"),
    INVALID_MASTER_KEY(1003, "无接口访问权限"),
    FAILURE(1004, "请求失败"),
    UNAUTHORIZED(1005, "未授权"),
    INVALID_TOKEN(2001, "TOKEN失效"),
    CONNECT_TIMED_OUT(3001, "请求超时"),
    HTTP_REQ_ERROR(3002, "HTTP请求出错");

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误描述信息
     */
    private final String msg;

    StatusCode(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public String getCode()
    {
        return this.code + "";
    }

    public int getCodeValue()
    {
        return this.code;
    }

    /**
     * 转为Map集合数据
     *
     * @return 枚举对象Map集合
     */
    public static Map<Integer, String> toMap()
    {
        Map<Integer, String> map = new HashMap<>(32);
        for (StatusCode value : StatusCode.values())
        {
            map.put(value.getCodeValue(), value.getMsg());
        }
        return map;
    }

    /**
     * 转为List集合数据
     *
     * @return 枚举对象List集合
     */
    public static List<Map<String, String>> toList()
    {
        List<Map<String, String>> list = new ArrayList<>(32);
        Map<String, String> map = null;
        for (StatusCode item : StatusCode.values())
        {
            map = new HashMap<>();
            map.put("code", item.getCode());
            map.put("msg", item.getMsg());
            list.add(map);
        }
        map = null;
        return list;
    }
}
