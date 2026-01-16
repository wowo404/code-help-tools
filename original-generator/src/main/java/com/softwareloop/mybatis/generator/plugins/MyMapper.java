package com.softwareloop.mybatis.generator.plugins;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用mapper
 *
 * @author liuzhangsheng
 * @create 2019/3/13
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
