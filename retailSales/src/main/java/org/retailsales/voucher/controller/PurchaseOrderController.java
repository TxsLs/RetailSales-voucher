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
import org.retailsales.voucher.entity.PurchaseOrder;
import org.retailsales.voucher.service.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Tag(name = "进货单管理")
@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController extends BaseController<PurchaseOrder, PurchaseOrderService> {

    @Operation(summary = "条件分页查询", description = "")
    @GetMapping("/queryPage")
    public @ResponseBody Result<PageSet<PurchaseOrder>> queryPage(
            @Parameter(description = "名称(支持like)，允许null") @RequestParam(required = false) String productName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String sort,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String price,
            @Parameter(description = "categoryid") @RequestParam(required = false) String categoryId,
            @Parameter(description = "supplierId") @RequestParam(required = false) String supplierId,
            @Parameter(description = "categoryName") @RequestParam(required = false) String categoryName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String stockQuantity,
            @Parameter(description = "起始时间") @RequestParam(required = false) String joinTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
            @Parameter(description = "页码", required = true) @RequestParam long pageNum,
            @Parameter(description = "页大小", required = true) @RequestParam int pageSize) {
        log.debug("call purchase");
        Predicate where = DaoUtil.and();
        if (StringUtils.isNotEmpty(productName))
            where.like("productName", productName);
        if (StringUtils.isNotEmpty(price))
            where.like("price", price);
        if (StringUtils.isNotEmpty(stockQuantity))
            where.like("stockQuantity", stockQuantity);
        if (StringUtils.isNotEmpty(categoryId))
            where.like("categoryId", categoryId);
        if (StringUtils.isNotEmpty(supplierId))
            where.like("supplierId", supplierId);
        if (StringUtils.isNotEmpty(categoryName))
            where.like("categoryName", categoryName);
        if (StringUtils.isNotEmpty(joinTime) && StringUtils.isNotEmpty(endTime)) {
            where.between("purchaseDate", joinTime, endTime); // created_time为时间字段名
        }

       /* if (StringUtils.isNotEmpty(status))
            where.like("status", status);
        if (StringUtils.isNotEmpty(empStatus))
            where.like("empStatus", empStatus);
        if (merchantId != null)
            where.equal(DataType.LONG, "merchantId", merchantId.toString());*/
        //	if (workstateId != null)
        //		where.equal(DataType.LONG, "workstateId", workstateId.toString());
        PageSet<PurchaseOrder> ps = this.service().findPage(where, Sort.parse(sort), pageNum, pageSize);
        return Result.toResult(ps);
    }

}
