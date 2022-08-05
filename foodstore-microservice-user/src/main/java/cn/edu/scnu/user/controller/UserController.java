package cn.edu.scnu.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easymall.common.pojo.User;
import com.easymall.common.utils.CookieUtils;
import com.easymall.common.vo.SysResult;

import cn.edu.scnu.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/user/manage/checkUserName")
	public SysResult checkUsername(String userName) {
		Integer exist = this.userService.checkUsername(userName);

		if (exist == 0) {
			return SysResult.ok();
		} else {
			return SysResult.build(201, "已存在", null);
		}
	}

	@RequestMapping("/user/manage/save")
	public SysResult userSave(User user) {
		// 1.检查用户是否存在
		Integer a = this.userService.checkUsername(user.getUserName());
		if (a > 0) {
			return SysResult.build(201, "用户名已存在", null);
		}

		// 2.注册
		try {
			this.userService.userSave(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}

	}

	// 用户登录的校验功能，校验成功，将数据保存在redis供后续访问
	@RequestMapping("/user/manage/login")
	public SysResult doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		String ticket = this.userService.doLogin(user);
		// System.out.println("=================");
		if (StringUtils.isNotEmpty(ticket)) {
			CookieUtils.setCookie(request, response, "EM_TICKET", ticket);
			// System.out.println("ok");
			return SysResult.ok();
		} else {
			return SysResult.build(201, "登录失败", null);
		}
	}

	@RequestMapping("/user/manage/query/{ticket}")
	public SysResult checkLoginUser(@PathVariable String ticket) {
		String userJson = this.userService.queryUserJson(ticket);
		if (StringUtils.isNotEmpty(userJson)) {
			return SysResult.build(200, "ok", userJson);
		} else {
			return SysResult.build(201, "", null);
		}
	}

	@RequestMapping("user/manage/logout")
	public SysResult doLogout(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		String ticket = null;
		for (Cookie cookie : cookies) {
			// 将cookie.setMaxAge(0)表示删除cookie.
			if(cookie.getName().equals("EM_TICKET")){
				ticket = cookie.getValue();
			}
			//删除前端cookies
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);		
//			System.out.println(cookie.getName());
//			System.out.println(cookie.getValue());
		}
		//删除用户redis
		if(ticket != null){
			this.userService.delUserRedis(ticket);
		}

		httpSession.invalidate();
		return SysResult.ok();
	}

}
