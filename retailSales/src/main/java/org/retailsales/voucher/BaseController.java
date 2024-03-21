package org.retailsales.voucher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.quincy.rock.core.dao.DaoUtil;
import org.quincy.rock.core.dao.sql.Predicate;
import org.quincy.rock.core.dao.sql.Sort;
import org.quincy.rock.core.util.MapUtil;
import org.quincy.rock.core.vo.Result;
import org.quincy.rock.core.vo.Vo.Default;
import org.quincy.rock.core.vo.Vo.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

/**
 * <b>基类Controller。</b>
 * <p><b>详细说明：</b></p>
 * <!-- 在此添加详细说明 -->
 * 无。
 *
 * @author mex2000
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(allowCredentials = "true", origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@Slf4j
@Validated
public abstract class BaseController<T extends Entity, S extends Service<T>> {
    @Autowired
    private S service;

    /**
     * <b>获得Service接口。</b>
     * <p><b>详细说明：</b></p>
     * <!-- 在此添加详细说明 -->
     * 无。
     *
     * @return Service接口
     */
    protected S service() {
        return this.service;
    }

    // @ModelAttribute
//	public void checkLogin() throws LoginException {
//		if (AppUtils.useCaptcha && !AppUtils.isLogin())
//			throw new LoginException("未登录!");
//	}

    @Operation(summary = "添加一个实体", description = "该接口继承自BaseController")
    @PostMapping("/add")
    public @ResponseBody Result<Boolean> add(@Validated({Default.class})
                                             @RequestBody T vo,
                                             @Parameter(description = "是否忽略空值", example = "false")
                                             @RequestParam(defaultValue = "false") boolean ignoreNullValue) {
        log.debug("call add");
        boolean result = this.service().insert(vo, ignoreNullValue);
        return Result.of(result);
    }

    @Operation(summary = "添加多个实体", description = "该接口继承自BaseController")
    @PostMapping("/addMultiple")
    public @ResponseBody Result<Boolean> addMultiple(@Validated({Default.class})
                                                     @RequestBody List<T> voList,
                                                     @Parameter(description = "是否忽略空值", example = "false")
                                                     @RequestParam(defaultValue = "false") boolean ignoreNullValue) {
        log.debug("call addMultiple");

        boolean result = true;
        for (T vo : voList) {
            boolean subResult = this.service().insert(vo, ignoreNullValue);
            if (!subResult) {
                result = false;
                break; // 如果其中一个插入失败，则跳出循环
            }
        }
        return Result.of(result);
    }


    @Operation(summary = "更新一个实体", description = "该接口继承自BaseController")
    @PostMapping("/update")
    public @ResponseBody Result<Boolean> update(@Validated({Update.class, Default.class})
                                                @RequestBody T vo,
                                                @Parameter(description = "是否忽略空值", example = "false")
                                                @RequestParam(defaultValue = "false") boolean ignoreNullValue) {
        log.debug("call update");
        boolean result = this.service().update(vo, ignoreNullValue, null, "password");
        return Result.of(result);
    }


    @Operation(summary = "使用Map更新一个实体", description = "该接口继承自BaseController")
    @PostMapping("/updateMap")
    public @ResponseBody Result<Boolean> updateMap(@NotEmpty @RequestBody Map<String, Object> voMap) {
        log.debug("call updateMap");
        Long id = MapUtil.getLong(voMap, "id");
        Predicate where = DaoUtil.and().equal("id", id);
        boolean result = this.service().updateMap(voMap, where);
        return Result.of(result);
    }


    @Operation(summary = "删除一个实体", description = "该接口继承自BaseController")
    @GetMapping("/remove")
    public @ResponseBody Result<Boolean> remove(@Parameter(description = "主键id", required = true)
                                                @NotNull @RequestParam long id) {
        log.debug("call remove");
        boolean result = this.service().delete(id);
        return Result.of(result);
    }


    @Operation(summary = "删除多个实体", description = "该接口继承自BaseController")
    @GetMapping(value = "/removeMore")
    public @ResponseBody Result<Boolean> removeMore(
            @Parameter(description = "多个主键id", required = true, allowEmptyValue = false)
            @RequestParam("id") @NotEmpty Long[] ids
    ) {
        log.debug("call removeMore");
        boolean result = this.service().deleteMore(Arrays.asList(ids));
        return Result.of(result);
    }


    @Operation(summary = "查询一个实体", description = "该接口继承自BaseController")
    @GetMapping(value = "/queryOne")
    public @ResponseBody Result<T> queryOne(
            @Parameter(description = "主键id", required = true)
            @RequestParam @NotNull long id
    ) {
        log.debug("call queryOne");
        T vo = this.service().findOne(id, "password");
        return Result.toResult(vo);
    }


    @Operation(summary = "根据指定的属性名和值返回一条数据", description = "该接口继承自SimpleController")
    @GetMapping(value = "/queryByName")
    public @ResponseBody Result<T> queryByName(
            @Parameter(description = "属性名", required = true) @RequestParam @NotBlank String propName,
            @Parameter(description = "属性值", required = true) @RequestParam @NotBlank String propValue
    ) {
        log.debug("call queryByName");
        T vo = this.service().findByName(propName, propValue, "password");
        return Result.toResult(vo);
    }


    @Operation(summary = "根据主键id查询数据是否存在", description = "该接口继承自BaseController")
    @GetMapping(value = "/exists")
    public @ResponseBody Result<Boolean> exists(
            @Parameter(description = "主键id", required = true) @RequestParam @NotNull long id
    ) {
        log.debug("call exists");
        boolean result = this.service().exist(id);
        return Result.of(result);
    }


    @Operation(summary = "根据指定的属性名和属性值查询数据是否存在", description = "该接口继承自BaseController")
    @GetMapping(value = "/existsByName")
    public @ResponseBody Result<Boolean> existsByName(
            @Parameter(description = "属性名", required = true) @RequestParam @NotBlank String propName,
            @Parameter(description = "属性值", required = true) @RequestParam @NotBlank String propValue,
            @Parameter(description = "忽略的主键id") @RequestParam Long ignoreId
    ) {
        log.debug("call existsByName");
        boolean result = this.service().existByName(propName, propValue, ignoreId);
        return Result.of(result);
    }


    @Operation(summary = "查询所有实体", description = "该接口继承自SimpleController")
    @GetMapping(value = "/queryAll")
    public @ResponseBody Result<List<? extends Entity>> queryAll(
            @Parameter(description = "排序规则字符串") @RequestParam String sort
    ) {
        log.debug("call queryAll");
        List<? extends Entity> list = this.service().findAll(null, Sort.parse(sort));
        return Result.toResult(list);
    }

}
