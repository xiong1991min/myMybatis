package com.xm;

import com.xm.core.MySqlSession;
import com.xm.core.MySqlSessionFactory;
import com.xm.core.MySqlSessionImpl;
import com.xm.core.SqlSessionFactoryBuilder;
import com.xm.entity.User;
import com.xm.mapper.UserMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 */
public class App {
	/**
	 * 配置文件
	 */
	private String source;

	private InputStream inputStream;

	private MySqlSessionFactory sqlSessionFactory;

	@Before
	public void setUp() {
		source = "test-mybatis-configuration.xml";
	}

	/**
	 * 基于XML格式配置的测试方法
	 */
	@Test
	public void testXMLConfingure() {

		try {
			/**
			 * 获得Session
			 */
			inputStream = App.class.getClassLoader().getResourceAsStream(source);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			MySqlSession session = sqlSessionFactory.openSession();

			/**
			 * 执行Ｑｕｅｒｙ操作
			 */
			List<User> users = (List) session.selectList("com.xm.mapper.UserMapper.getUser");
			System.out.println("Query by XML configuration...");

			/**
			 * 打印结果
			 */
			this.printUsers(users);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 基于Annotation配置的测试方法
	 */
	@Test
	public void testAnnotationConfingure() {
		try {
			inputStream = App.class.getClassLoader().getResourceAsStream(source);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			MySqlSession session = sqlSessionFactory.openSession();

			UserMapper userMapper = session.getMapper(UserMapper.class);
			System.out.println("\r\nQuery by annotation configuration...");
			this.printUsers(userMapper.getUser());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void clearUp() throws SQLException {
		if (MySqlSessionImpl.connection != null && !MySqlSessionImpl.connection.isClosed()) {
			MySqlSessionImpl.connection.close();
		}
	}

	private void printUsers(final List<User> users) {
		int count = 0;

		for (User user : users) {
			System.out.println(MessageFormat.format("==User[{0}]=================", ++count));
			System.out.println("User Id: " + user.getId());
			System.out.println("User UserName: " + user.getUsername());
			System.out.println("User Password: " + user.getPassword());
			System.out.println("User nickname: " + user.getNickname());
		}
	}
}
