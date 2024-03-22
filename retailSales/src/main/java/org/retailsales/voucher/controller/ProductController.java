package org.retailsales.voucher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quincy.rock.core.dao.DaoUtil;
import org.quincy.rock.core.dao.sql.Predicate;
import org.quincy.rock.core.dao.sql.Sort;
import org.quincy.rock.core.lang.DataType;
import org.quincy.rock.core.vo.PageSet;
import org.quincy.rock.core.vo.Result;
import org.retailsales.voucher.BaseController;
import org.retailsales.voucher.entity.Product;
import org.retailsales.voucher.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@Schema(description = "商品管理模块")
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

}