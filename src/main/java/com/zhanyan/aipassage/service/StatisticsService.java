package com.zhanyan.aipassage.service;


import com.zhanyan.aipassage.model.vo.StatisticsVO;

/**
 * 统计服务
 *
 * @author xiaoh
 */
public interface StatisticsService {

    /**
     * 获取系统统计数据
     *
     * @return 统计数据
     */
    StatisticsVO getStatistics();
}
