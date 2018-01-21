package com.xm.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xm on 2018/1/21.
 */
public class MySqlSessionImpl implements MySqlSession {
	/**
	 * DB connection
	 */
	public static Connection connection;

	private Map<String, Mapper> xmlSQLs;

	private List<String> annotationClasses;

	public MySqlSessionImpl() {
		/**
		 * driverString 和 connString 应该是从配置文件读取，这里简化了
		 */
		final String driverString = "com.mysql.jdbc.Driver";
		final String connString = "jdbc:mysql://localhost:3306/mybatis";
		try {
			Class.forName(driverString);
			/** 获得DB连接 */
			connection = DriverManager.getConnection(connString,"root","root");
		} catch (Exception e) {
			System.out.println("获取DBConnection出错！");
		}
	}

	/**
	 * 基于Annotation的数据库操作
	 */
	public <T> T getMapper(Class<T> clazz) {
		T clazzImpl =
				(T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new SQLSelectProxy());

		return clazzImpl;
	}

	/**
	 * 基于XML的查询操作
	 */
	public <E> List<E> selectList(String query) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			/** 简单的PreparedStateme JDBC实现 */
			pstmt = connection.prepareStatement(xmlSQLs.get(query).getQuerySql());
			rs = pstmt.executeQuery();
			/** 执行查询操作 */
			return executeQuery(rs, xmlSQLs.get(query).getResultType());
		} finally {
			if (!rs.isClosed()) {
				rs.close();
			}

			if (!pstmt.isClosed()) {
				pstmt.close();
			}
		}
	}

	/**
	 * 执行查询操作，并将查询到的结果与配置中的ResultType根据变量名一一对应，通过反射调用Set方法注入各个变量的值
	 *
	 * @param rs
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static <E> List<E> executeQuery(ResultSet rs, String type) throws Exception {
		int count = rs.getMetaData().getColumnCount();

		List<String> columnNames = new ArrayList<String>();

		for (int i = 1; i <= count; i++) {
			columnNames.add(rs.getMetaData().getColumnName(i));
		}

		final List list = new ArrayList<Object>();

		while (rs.next()) {
			Class modelClazz = Class.forName(type);
			Object obj = modelClazz.newInstance();
			for (Method setMethods : modelClazz.getMethods()) {
				for (String columnName : columnNames) {
					if (setMethods.getName().equalsIgnoreCase("set" + columnName)) {
						setMethods.invoke(obj, rs.getString(columnName));
					}
				}
			}
			list.add(obj);
		}

		return list;
	}

	public Map<String, Mapper> getXmlSQLs() {
		return xmlSQLs;
	}

	public void setXmlSQLs(Map<String, Mapper> xmlSQLs) {
		this.xmlSQLs = xmlSQLs;
	}

	public List<String> getAnnotationClasses() {
		return annotationClasses;
	}

	public void setAnnotationClasses(List<String> annotationClasses) {
		this.annotationClasses = annotationClasses;
	}
}
