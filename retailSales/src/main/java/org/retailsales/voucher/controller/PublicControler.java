//package org.retailsales.voucher.controller;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Arrays;
//
//import javax.servlet.http.HttpSession;
//import javax.validation.constraints.NotBlank;
//
//import org.boot.hf.admin.AppUtils;
//import org.boot.hf.admin.entity.Employee;
//import org.boot.hf.admin.service.EmployeeService;
//import org.quincy.rock.core.cache.CachePoolRef;
//import org.quincy.rock.core.cache.ObjectCache;
//import org.quincy.rock.core.util.CaptchaImage;
//import org.quincy.rock.core.util.DateUtil;
//import org.quincy.rock.core.vo.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.CacheControl;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity.BodyBuilder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import springfox.documentation.annotations.ApiIgnore;
//
//@CrossOrigin(allowCredentials = "true", origins = { "http://127.0.0.1:5500", "http://localhost:5500" })
//@Slf4j
//@Api(tags = "公用模块")
//@Controller
//@Validated
//@RequestMapping("/")
//@SuppressWarnings({ "unchecked", "rawtypes" })
//public class PublicControler {
//
//	@Autowired
//	private EmployeeService service;
//
//	@ApiOperation(value = "用户登录", notes = "可以自己添加验证码功能")
//	@ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "用户代码", required = true),
//			@ApiImplicitParam(name = "password", value = "密码", required = true),
//			@ApiImplicitParam(name = "captcha", value = "验证码") })
//	@RequestMapping(value = "/login", method = { RequestMethod.POST })
//	public @ResponseBody Result<Boolean> login(@NotBlank @RequestParam String username,
//			@NotBlank @RequestParam String password, String captcha, @ApiIgnore HttpSession session,
//			Integer sessionTimeoutInMinutes) {
//		session.removeAttribute(AppUtils.CURRENT_LOGIN_USER_KEY);
//		log.debug("call userLogin");
//		if (AppUtils.useCaptcha) {
//			String code = cacheCaptcha(session);
//			if (code == null || !code.equals(captcha)) {
//				return Result.toResult("1003", "验证码不正确!");
//			}
//		}
//		Employee user = service.findByCode(username);
//		if (user == null) {
//			return Result.toResult("1020", "该账号不存在，请注册！");
//		} else if (service.checkPassword(username, password) == null) {
//			return Result.toResult("1001", "账号或密码不正确!");
//
//		} else if (user.getStatus() == 0) {
//			return Result.toResult("1066", "您的账户已被封禁，请联系管理员邮箱:1034710773@qq.com!");
//		} else if (user.getAdmin() == 0) {
//			User loginUser = new User(//spring安全的uer
//					user.getCode(), user.getPassword(), Arrays.asList());
//
//			session.setAttribute(AppUtils.CURRENT_LOGIN_USER_KEY, loginUser);
//			// 设置Session的有效期
//			if (sessionTimeoutInMinutes != null && sessionTimeoutInMinutes > 0) {
//				int sessionTimeoutInDays = sessionTimeoutInMinutes * 60 * 24 * 60;
//				session.setMaxInactiveInterval(sessionTimeoutInDays);
//			}
//			log.debug("Session的超时时间设置为：{}秒", session.getMaxInactiveInterval());
//			return Result.of(false);
//		} else {
//			User loginUser = new User(//spring安全的uer
//					user.getCode(), user.getPassword(), Arrays.asList());
//			session.setAttribute(AppUtils.CURRENT_LOGIN_USER_KEY, loginUser);
//			// 设置Session的有效期
//			if (sessionTimeoutInMinutes != null && sessionTimeoutInMinutes > 0) {
//				int sessionTimeoutInDays = sessionTimeoutInMinutes * 60 * 24 * 60;
//				session.setMaxInactiveInterval(sessionTimeoutInDays);
//			}
//			log.debug("Session的超时时间设置为：{}秒", session.getMaxInactiveInterval());
//			return Result.of(true);
//		}
//
//	}
//
//	@ApiOperation(value = "注销登录", notes = "")
//	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
//	public @ResponseBody Result<Boolean> logout(@ApiIgnore HttpSession session) {
//		session.invalidate();
//		return Result.of(true);
//	}
//
//	@ApiIgnore
//	@RequestMapping(value = "/loginSuccess", method = { RequestMethod.GET, RequestMethod.POST })
//	public @ResponseBody Result<Boolean> loginSuccess() {
//		return Result.of(true);
//	}
//
//	@ApiIgnore
//	@RequestMapping(value = "/loginFail", method = { RequestMethod.GET, RequestMethod.POST })
//	public @ResponseBody Result<Boolean> loginFail() {
//		return Result.toResult("1001", "用户或密码不正确!");
//	}
//
//	@ApiOperation(value = "返回当前用户信息", notes = "未登录则返回null")
//	@RequestMapping(value = "/loginUser", method = { RequestMethod.GET })
//	public @ResponseBody Result<Employee> loginUser(@ApiIgnore HttpSession session) {
//		log.debug("call loginUser");
//		Employee employee = null;
//		session.getAttribute(AppUtils.CURRENT_LOGIN_USER_KEY);
//		if (AppUtils.isLogin()) {
//			String code = AppUtils.getLoginUser().getUsername();
//			employee = service.findByCode(code);
//		}
//		return Result.toResult(employee);
//	}
//
//	@ApiOperation(value = "返回验证码图片", notes = "")
//	@RequestMapping(value = "/captcha.jpg", method = { RequestMethod.GET })
//	public ResponseEntity<byte[]> captchaImg(@ApiIgnore HttpSession session) throws IOException {
//		log.debug("call captcha.jpg");
//		CaptchaImage captchaImg = new CaptchaImage();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
//		String captcha = captchaImg.generateCaptcha(baos);
//		BodyBuilder builder = ResponseEntity.ok().contentLength(baos.size()).cacheControl(CacheControl.noCache())
//				.contentType(MediaType.IMAGE_JPEG);
//		builder.header("Content-Disposition", "attachment; filename=captcha.jpg");
//		cacheCaptcha(captcha, session);
//		return builder.body(baos.toByteArray());
//	}
//
//	private void cacheCaptcha(String captcha, HttpSession session) {
//		session.setAttribute(AppUtils.PUBLIC_LAST_CAPTCHA_KEY,
//				CachePoolRef.createObjectCache(captcha, false, 5 * DateUtil.MILLISECOND_UNIT_MINUTE));
//	}
//
//	private String cacheCaptcha(HttpSession session) {
//		ObjectCache<String> oc = (ObjectCache) session.getAttribute(AppUtils.PUBLIC_LAST_CAPTCHA_KEY);
//		return oc == null ? null : oc.get();
//	}
//}
