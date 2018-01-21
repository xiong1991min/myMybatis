package com.xm.mapper;

import com.xm.annotation.Select;
import com.xm.entity.User;

import java.util.List;

/**
 * Created by xm on 2018/1/21.
 */
public interface UserMapper {

	@Select("select * from USER_TEST_TB")
	public List<User> getUser();

}
