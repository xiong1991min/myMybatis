package com.xm.core;

import java.util.List;

/**
 * Created by xm on 2018/1/21.
 * 模拟sqlsession
 */
public interface MySqlSession {

	public <T> T getMapper(Class<T> clazz);

	public <E> List<E> selectList(String query) throws Exception;

}
