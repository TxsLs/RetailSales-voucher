package org.retailsales.voucher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quincy.rock.core.dao.DaoUtil;
import org.quincy.rock.core.dao.sql.Predicate;
import org.quincy.rock.core.dao.sql.Sort;
import org.quincy.rock.core.vo.PageSet;
import org.quincy.rock.core.vo.Result;
import org.retailsales.voucher.BaseController;
import org.retailsales.voucher.entity.Product;
import org.retailsales.voucher.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@Tag(name = "商品管理模块")
@RequestMapping("/product")
public class ProductController extends BaseController<Product, ProductService> {

    @Operation(summary = "条件分页查询", description = "")
    @GetMapping("/queryPage")
    public @ResponseBody Result<PageSet<Product>> queryPage(
            @Parameter(description = "名称(支持like)，允许null") @RequestParam(required = false) String name,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String sort,
            @Parameter(description = "categoryId") @RequestParam(required = false) String categoryId,
            @Parameter(description = "price") @RequestParam(required = false) String price,
            @Parameter(description = "stockQuantity") @RequestParam(required = false) String stockQuantity,
            @Parameter(description = "页码", required = true) @RequestParam long pageNum,
            @Parameter(description = "页大小", required = true) @RequestParam int pageSize) {
        log.debug("call queryPage");
        Predicate where = DaoUtil.and();
        if (StringUtils.isNotEmpty(name))
            where.like("name", name);
        if (StringUtils.isNotEmpty(categoryId))
            where.like("categoryId", categoryId);
        if (StringUtils.isNotEmpty(price))
            where.like("price", price);
        if (StringUtils.isNotEmpty(stockQuantity))
            where.like("stockQuantity", stockQuantity);

        //	if (workstateId != null)
        //		where.equal(DataType.LONG, "workstateId", workstateId.toString());
        PageSet<Product> ps = this.service().findPage(where, Sort.parse(sort), pageNum, pageSize);
        return Result.toResult(ps);
    }

    @Operation(summary = "加product", description = "")
    @PostMapping("/addProduct")
    public @ResponseBody Result<Boolean> addCategory(@RequestBody Product vo) {
        log.debug("call addProduct!");
        boolean exist = this.service().existByName("name", vo.getName(), null);
        boolean result;
        if (!exist) {//注册账户时先判断输入的名是否存在
            result = this.service().insert(vo, true);

            return Result.of(result);
        } else {
            return Result.toResult("1075", "此商品已存在！请换一个吧！*^____^*");
        }

    }

}
