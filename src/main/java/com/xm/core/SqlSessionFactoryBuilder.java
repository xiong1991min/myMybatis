package com.xm.core;

import java.io.InputStream;

/**
 * Created by xm on 2018/1/21.
 * 模拟SqlSessionFactoryBuilder
 */
public class SqlSessionFactoryBuilder {
	public MySqlSessionFactory build(InputStream is) {
		MySqlSessionFactory sessionFactory = new MySqlSessionFactory();
		sessionFactory.setConfiguration(is);
		return sessionFactory;
	}
}
