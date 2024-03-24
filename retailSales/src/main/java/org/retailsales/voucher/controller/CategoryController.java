package org.retailsales.voucher.controller;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quincy.rock.core.vo.Result;
import org.retailsales.voucher.BaseController;
import org.retailsales.voucher.entity.Category;
import org.retailsales.voucher.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Tag(name = "分类管理模块")
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController<Category, CategoryService> {

    @Operation(summary = "加分类", description = "")
    //@Parameter(name = "vo", description = "分类", required = true)
    @PostMapping("/addCategory")
    public @ResponseBody Result<Boolean> addCategory(@RequestBody Category vo) {
        log.debug("call addCategory!");
        boolean exist = this.service().existByName("name", vo.getName(), null);
        boolean result;
        if (!exist) {//注册账户时先判断输入的名是否存在
            result = this.service().insert(vo, true);

            return Result.of(result);
        } else {
            return Result.toResult("1075", "此分类已存在！请换一个吧！*^____^*");
        }

    }


}
