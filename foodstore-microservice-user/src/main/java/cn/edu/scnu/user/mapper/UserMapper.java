package cn.edu.scnu.user.mapper;

import com.easymall.common.pojo.User;

public interface UserMapper {

	Integer queryUsername(String userName);

	void userSave(User user);

	User queryUserByNameAndPassword(User user);

}
