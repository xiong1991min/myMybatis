package com.xm.core;

import com.xm.annotation.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by xm on 2018/1/21.
 */
public class SQLSelectProxy implements InvocationHandler {
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		/**
		 * 获得Mapper方法上的Select注解,以此来取得注解中的SQL语句
		 */
		Select select = method.getAnnotation(Select.class);
		if (!method.isAnnotationPresent(Select.class)) {
			throw new RuntimeException("缺少@Select注解!");
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Object obj = null;
		try {
			pstmt = MySqlSessionImpl.connection.prepareStatement(select.value());
			rs = pstmt.executeQuery();

			/**
			 * 获得Method的返回对象类型,此处应当作判断处理,当List的时候,当只返回一个对象的时候.
			 * 为了简单实现功能并与第一节中测试文件不发生冲突起见,此处当作List处理
			 */
			String returnType = method.getGenericReturnType().toString();//java.util.List<com.freud.practice.User>

			if (returnType.startsWith(List.class.getName())) {
				//去掉我们不需要的字符串，得到List中的类型
				returnType = returnType.replace(List.class.getName(), "").replace("<", "").replace(">", "");
			} else {
				// 返回其他对象应当作其他处理，此处为了简单起见，暂不处理
			}
			obj = MySqlSessionImpl.executeQuery(rs, returnType);

		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}

		}
		return obj;
	}

}
