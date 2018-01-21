package com.xm.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xm on 2018/1/21.
 */
public class MySqlSessionFactory {
	private InputStream configuration;

	public MySqlSession openSession() throws IOException {
		MySqlSessionImpl session = new MySqlSessionImpl();
		loadConfigurations(session);
		return session;
	}

	/**
	 * 通过Ｄｏｍ４ｊ读取配置文件信息
	 *
	 * @param session
	 * @throws IOException
	 */
	private void loadConfigurations(final MySqlSessionImpl session) throws IOException {
		try {
			Document document = new SAXReader().read(configuration);
			Element root = document.getRootElement();

			List<Element> mappers = root.element("mappers").elements("mapper");
			for (Element mapper : mappers) {
				if (mapper.attribute("resource") != null) {
					session.setXmlSQLs(loadXMLConfiguration(mapper.attribute("resource").getText()));
				}

				if (mapper.attribute("class") != null) {

				}
			}
		} catch (Exception e) {
			System.out.println("读取配置文件错误!");
		} finally {
			configuration.close();
		}
	}

	/**
	 * 通过ｄｏｍ４ｊ读取Ｍａｐｐｅｒ．ｘｍｌ中的信息
	 *
	 * @param resource
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private Map<String, Mapper> loadXMLConfiguration(String resource) throws DocumentException, IOException {
		Map<String, Mapper> map = new HashMap<String, Mapper>();
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(resource);

			Document document = new SAXReader().read(is);
			Element root = document.getRootElement();
			if (root.getName().equalsIgnoreCase("mapper")) {

				String namespace = root.attribute("namespace").getText();

				for (Element select : (List<Element>) root.elements("select")) {
					Mapper mapperModel = new Mapper();
					mapperModel.setResultType(select.attribute("resultType").getText());
					mapperModel.setQuerySql(select.getText().trim());

					map.put(namespace + "." + select.attribute("id").getText(), mapperModel);
				}
			}
		} finally {
			is.close();
		}
		return map;
	}

	public InputStream getConfiguration() {
		return configuration;
	}

	public void setConfiguration(InputStream configuration) {
		this.configuration = configuration;
	}
}
