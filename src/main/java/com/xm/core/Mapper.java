package com.xm.core;

/**
 * Created by xm on 2018/1/21.
 */
public class Mapper {
	/**
	 * 返回类型
	 */
	private String resultType;

	/**
	 * 查询ＳＱＬ
	 */
	private String querySql;

	public String getResultType()
	{
		return resultType;
	}

	public void setResultType(String resultType)
	{
		this.resultType = resultType;
	}

	public String getQuerySql()
	{
		return querySql;
	}

	public void setQuerySql(String querySql)
	{
		this.querySql = querySql;
	}
}
