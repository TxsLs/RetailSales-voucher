package org.retailsales.voucher.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.quincy.rock.core.cache.CachePoolRef;
import org.quincy.rock.core.cache.ObjectCache;
import org.quincy.rock.core.util.CaptchaImage;
import org.quincy.rock.core.util.DateUtil;
import org.quincy.rock.core.vo.Result;
import org.retailsales.voucher.AppUtils;
import org.retailsales.voucher.entity.Employee;
import org.retailsales.voucher.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;


@CrossOrigin(allowCredentials = "true", origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@Slf4j
@Controller
@Validated
@RequestMapping("/")
@SuppressWarnings({"unchecked", "rawtypes"})
public class PublicControler {

	@Autowired
	private EmployeeService service;

	@Operation(summary = "用户登录", description = "可以自己添加验证码功能")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "登录成功"),
			@ApiResponse(responseCode = "400", description = "参数错误"),
			@ApiResponse(responseCode = "403", description = "验证码不正确"),
			@ApiResponse(responseCode = "404", description = "账号不存在"),
			@ApiResponse(responseCode = "401", description = "账号或密码不正确"),
			@ApiResponse(responseCode = "406", description = "账户已被封禁"),
			@ApiResponse(responseCode = "500", description = "服务器内部错误")
	})
	@PostMapping("/login")
	public ResponseEntity<Result<Boolean>> login(
			@Parameter(description = "用户代码", required = true) @NotBlank @RequestParam String username,
			@Parameter(description = "密码", required = true) @NotBlank @RequestParam String password,
			@Parameter(description = "验证码") String captcha,
			HttpSession session,
			@Parameter(description = "Session超时时间（分钟）") Integer sessionTimeoutInMinutes) {

		session.removeAttribute(AppUtils.CURRENT_LOGIN_USER_KEY);
		log.debug("call userLogin");

		if (AppUtils.useCaptcha) {
			String code = cacheCaptcha(session);
			if (code == null || !code.equals(captcha)) {
				return ResponseEntity.status(403).body(Result.toResult("1003", "验证码不正确!"));
			}
		}

		Employee user = service.findByCode(username);
		if (user == null) {
			return ResponseEntity.status(404).body(Result.toResult("1020", "该账号不存在，请注册！"));
		} else if (service.checkPassword(username, password) == null) {
			return ResponseEntity.status(401).body(Result.toResult("1001", "账号或密码不正确!"));
		} else if (user.getStatus() == 0) {
			return ResponseEntity.status(406).body(Result.toResult("1066", "您的账户已被封禁，请联系管理员邮箱:1034710773@qq.com!"));
		} else {
			User loginUser = new User(
					user.getCode(), user.getPassword(), Arrays.asList());
			session.setAttribute(AppUtils.CURRENT_LOGIN_USER_KEY, loginUser);
			// 设置Session的有效期
			if (sessionTimeoutInMinutes != null && sessionTimeoutInMinutes > 0) {
				int sessionTimeoutInDays = sessionTimeoutInMinutes * 60 * 24 * 60;
				session.setMaxInactiveInterval(sessionTimeoutInDays);
			}
			log.debug("Session的超时时间设置为：{}秒", session.getMaxInactiveInterval());
			return ResponseEntity.ok().body(Result.of(false));
		}
	}

	@Operation(summary = "注销登录", description = "")
	@GetMapping("/logout")
	public ResponseEntity<Result<Boolean>> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok().body(Result.of(true));
	}

	// 这里使用hidden = true来隐藏loginSuccess方法和loginFail方法
	@Parameter(hidden = true)
	@RequestMapping(value = "/loginSuccess", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Result<Boolean>> loginSuccess() {
		return ResponseEntity.ok().body(Result.of(true));
	}

	@Parameter(hidden = true)
	@RequestMapping(value = "/loginFail", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Result<Boolean>> loginFail() {
		return ResponseEntity.status(400).body(Result.toResult("1001", "用户或密码不正确!"));
	}

	@Operation(summary = "返回当前用户信息", description = "未登录则返回null")
	@GetMapping("/loginUser")
	public ResponseEntity<Result<Employee>> loginUser(HttpSession session) {
		log.debug("call loginUser");
		Employee employee = null;
		session.getAttribute(AppUtils.CURRENT_LOGIN_USER_KEY);
		if (AppUtils.isLogin()) {
			String code = AppUtils.getLoginUser().getUsername();
			employee = service.findByCode(code);
		}
		return ResponseEntity.ok().body(Result.toResult(employee));
	}

	@Operation(summary = "返回验证码图片", description = "")
	@GetMapping("/captcha.jpg")
	public ResponseEntity<byte[]> captchaImg(HttpSession session) throws IOException {
		log.debug("call captcha.jpg");
		CaptchaImage captchaImg = new CaptchaImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		String captcha = captchaImg.generateCaptcha(baos);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(baos.size());
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentDispositionFormData("attachment", "captcha.jpg");
		cacheCaptcha(captcha, session);
		return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
	}

	private void cacheCaptcha(String captcha, HttpSession session) {
		session.setAttribute(AppUtils.PUBLIC_LAST_CAPTCHA_KEY,
				CachePoolRef.createObjectCache(captcha, false, 5 * DateUtil.MILLISECOND_UNIT_MINUTE));
	}

	private String cacheCaptcha(HttpSession session) {
		ObjectCache<String> oc = (ObjectCache) session.getAttribute(AppUtils.PUBLIC_LAST_CAPTCHA_KEY);
		return oc == null ? null : oc.get();
	}
}
