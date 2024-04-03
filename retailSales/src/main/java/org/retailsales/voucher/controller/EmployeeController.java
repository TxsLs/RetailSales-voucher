package org.retailsales.voucher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.annotations.Update;
import org.quincy.rock.core.util.IOUtil;
import org.quincy.rock.core.vo.Result;
import org.retailsales.voucher.AppUtils;
import org.retailsales.voucher.BaseController;
import org.retailsales.voucher.entity.Employee;
import org.retailsales.voucher.entity.Photo;
import org.retailsales.voucher.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Tag(name = "员工管理模块")
@Controller
@Validated
@RequestMapping("/employee")
public class EmployeeController extends BaseController<Employee, EmployeeService> {

	@Operation(summary = "添加员工实体", description = "允许文件上传！")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "成功"),
			@ApiResponse(responseCode = "400", description = "无效的请求"),
			@ApiResponse(responseCode = "401", description = "未经授权"),
			@ApiResponse(responseCode = "403", description = "禁止访问"),
			@ApiResponse(responseCode = "404", description = "未找到"),
			@ApiResponse(responseCode = "500", description = "服务器内部错误")
	})
	@PostMapping("/addEmployee")
	public ResponseEntity<Result<Boolean>> addEmployee(@Validated @RequestBody Employee vo,
													   @RequestParam(value = "photo", required = false) MultipartFile file) throws IOException {
		log.debug("call addEmployee");
		boolean exist = this.service().existByName("code", vo.getCode(), null);
		boolean result = false;
		if (!exist) {
			result = this.service().insert(vo, true);
			if (result && file != null && !file.isEmpty()) {
				result = this.updatePhoto(vo.id(), file);
			}
			return ResponseEntity.ok(Result.of(result));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.toResult("1067", "账号名已存在！请重试！"));
		}
	}


	@Operation(summary = "更新员工实体", description = "允许文件上传！")
	@PostMapping(value = "/updateEmployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiResponse(responseCode = "200", description = "成功更新员工",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class)))
	@ApiResponse(responseCode = "400", description = "请求参数有误")
	public @ResponseBody Result<Boolean> updateEmployee(
			@Validated({Update.class, Default.class}) @RequestBody Employee vo,
			@Parameter(description = "员工照片", required = false) @RequestPart(value = "photo", required = false) MultipartFile file) throws IOException {

		log.debug("call updateEmployee");

		// 获取登录用户的代码
		String code = AppUtils.getLoginUser().getUsername();
		// 根据登录用户的代码查找员工
		Employee employee = service().findByCode(code);

		// 判断用户是否登录且为管理员
		if (AppUtils.isLogin() && employee.getAdmin() == 1) {
			boolean exist = this.service().existByName("code", vo.getCode(), vo.getId());
			if (!exist) {
				boolean result = this.service().update(vo, true, null);
				if (result && file != null && !file.isEmpty()) {
					result = this.updatePhoto(vo.getId(), file);
				}
				return Result.of(result);
			} else {
				return Result.toResult("1067", "账号已存在！请重试！");
			}
		} else {
			return Result.toResult("1068", "未登录或权限不够！请返回登录界面！");
		}
	}

	@Operation(summary = "根据员工id更新自己的信息", description = "当前用户可以修改自己少部分个人信息")
	@PostMapping("/updateSelfInfo")
	public ResponseEntity<Result<Boolean>> updateSelfInfo(
			@Parameter(description = "要更新实体的主键id", required = true)
			@RequestParam("id") @Positive Long id,
			@Parameter(description = "账号")
			@RequestParam(value = "code", required = false) String code,
			@Parameter(description = "名称")
			@RequestParam(value = "name", required = false) String name,
			@Parameter(description = "性别(1-男,2-女)")
			@RequestParam(value = "gender", required = false) Integer gender,
			@Parameter(description = "电话号")
			@RequestParam(value = "phone", required = false) Long phone,
			@Parameter(description = "照片", schema = @Schema(type = "string", format = "binary"))
			@RequestPart(value = "photo", required = false) MultipartFile file,
			HttpSession session) throws IOException {

		if (AppUtils.isLogin()) {
			String loginCode = AppUtils.getLoginUser().getUsername();
			Employee employee = service().findByCode(loginCode);
			Long employeeId = employee.getId();

			if (!id.equals(employeeId)) {
				return ResponseEntity.badRequest().body(Result.toResult("1069", "只能更新自己的信息！"));
			}

			Boolean exist = service().existByName("code", code, employeeId);
			if (!exist) {
				Employee vo = new Employee();
				vo.setId(id);
				vo.setCode(code);
				vo.setName(name);
				vo.setGender(gender);
				vo.setPhone(phone);

				boolean result = this.service().update(vo, true, null);
				if (result && file != null && !file.isEmpty()) {
					result = this.updatePhoto(id, file);
				}
				return ResponseEntity.ok(Result.of(result));
			} else {
				return ResponseEntity.badRequest().body(Result.toResult("1067", "账号名已存在！请重试！"));
			}
		} else {
			return ResponseEntity.badRequest().body(Result.toResult("1068", "未登录！请返回登录界面！"));
		}
	}

	@Operation(summary = "员工修改自己的密码", description = "员工修改自己的密码")
	@PostMapping(value = "/changeSelfPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(responseCode = "200", description = "密码修改成功",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class)))
	@ApiResponse(responseCode = "400", description = "请求参数有误")
	public @ResponseBody Result<Boolean> changeSelfPassword(
			@Parameter(description = "旧密码", required = true) @RequestParam @NotBlank String oldPassword,
			@Parameter(description = "新密码", required = true) @RequestParam @NotBlank String newPassword,
			HttpSession session) {

		log.debug("call changeSelfPassword");
		boolean ok = false;
		if (AppUtils.isLogin()) {
			String code = AppUtils.getLoginUser().getUsername();
			ok = this.service().changeSelfPassword(code, oldPassword, newPassword);
		}
		return Result.of(ok);
	}

	@Operation(summary = "员工找回自己的密码", description = "员工找回自己的密码")
	@PostMapping(value = "/findSelfPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(responseCode = "200", description = "密码找回成功",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class)))
	@ApiResponse(responseCode = "400", description = "请求参数有误")
	public @ResponseBody Result<Boolean> findSelfPassword(
			@Parameter(description = "账号", required = true) @RequestParam @NotBlank String code,
			@Parameter(description = "姓名", required = true) @RequestParam @NotBlank String name,
			@Parameter(description = "电话号", required = true) @RequestParam @NotNull Long phone,
			@Parameter(description = "新密码", required = true) @RequestParam @NotBlank String newPassword) {

		log.debug("call changeSelfPassword");
		boolean ok = false;
		Employee employee = this.service().findByCode(code);
		if (employee == null) {
			return Result.toResult("1072", "该账号不存在！");
		} else {
			if (employee.getCode().equals(code) && employee.getName().equals(name) && employee.getPhone().equals(phone)
					&& employee.getStatus().equals(1)) {
				ok = this.service().changePassword(code, newPassword);
			} else if (employee.getStatus().equals(0)) {
				return Result.toResult("1066", "您的账户已被封禁，请联系管理员邮箱:1034710773@qq.com!");
			} else {
				return Result.toResult("1069", "用户信息不匹配，请重试！");
			}
		}
		return Result.of(ok);
	}

	@Operation(summary = "重设员工密码", description = "")
	@PostMapping(value = "/resetPwd")
//	@Secured({ "ROLE_ADMIN" })
	public @ResponseBody Result<Boolean> resetPwd(@NotBlank @RequestParam String code,
												  @NotBlank @RequestParam String password) {
		log.debug("call resetPwd");
		boolean result = this.service().changePassword(code, password);
		return Result.of(result);
	}

	@Operation(summary = "检查密码", description = "")
	@PostMapping(value = "/checkpwd")
	public @ResponseBody Result<Boolean> checkpwd(@NotBlank @RequestParam String code,
												  @NotBlank @RequestParam String password) {
		log.debug("call checkpwd");
		boolean ok = false;
		Employee result = this.service().checkPassword(code, password);
		if (result != null) {
			ok = true;
		}
		return Result.of(ok);
	}

	@Operation(summary = "上传员工照片", description = "照片大小不要超过500K")
	@PostMapping(value = "/uploadPhoto")
	public @ResponseBody Result<Boolean> uploadPhoto(@NotNull @RequestParam long id,
													 @RequestPart(value = "photo", required = false) MultipartFile file) throws IOException {
		log.debug("call uploadPhoto");
		boolean result = this.updatePhoto(id, file);
		return Result.of(result);
	}

	@Operation(summary = "删除员工照片", description = "")
	@GetMapping(value = "/deletePhoto")
	public @ResponseBody Result<Boolean> deletePhoto(@NotNull @RequestParam long id) throws IOException {
		log.debug("call deletePhoto");
		boolean result = this.updatePhoto(id, null);
		return Result.of(result);
	}

	@Operation(summary = "下载员工照片")
	@GetMapping(value = "/photo")
	public ResponseEntity<byte[]> photo(@NotNull @RequestParam long id) {
		log.debug("call photo");
		Photo photo = this.service().getPhoto(id);
		if (Photo.isEmpty(photo))
			return null;
		//
		BodyBuilder builder = ResponseEntity.ok().contentLength(photo.length());
		builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
		builder.header("Content-Disposition", "attachment; filename=" + photo.getPhotoFile());
		return builder.body(photo.getPhoto());
	}

	//更新照片
	private boolean updatePhoto(long id, MultipartFile file) throws IOException {
		Photo photo = new Photo();
		photo.setId(id);
		if (file != null && !file.isEmpty()) {
			photo.setPhoto(file.getBytes());
			String extName = FilenameUtils.getExtension(file.getOriginalFilename());
			String fileName = IOUtil.getUniqueFileName("up", "." + extName);
			photo.setPhotoFile(fileName);
		}
		return this.service().updatePhoto(photo);
	}


}
