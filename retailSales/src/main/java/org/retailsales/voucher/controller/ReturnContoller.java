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
import org.retailsales.voucher.entity.InboundOrder;
import org.retailsales.voucher.entity.ReturnOrder;
import org.retailsales.voucher.entity.SalesOrder;
import org.retailsales.voucher.service.ProductService;
import org.retailsales.voucher.service.ReturnService;
import org.retailsales.voucher.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "退货单管理", description = "tuihuodan")
@Slf4j
@Controller
@RequestMapping("/return")
public class ReturnContoller extends BaseController<ReturnOrder, ReturnService> {
    @Autowired
    SaleService saleService;
    @Autowired
    ProductService productservice;

    @Operation(summary = "条件分页查询", description = "")
    @GetMapping("/queryPage")
    public @ResponseBody Result<PageSet<ReturnOrder>> queryPage(
            @Parameter(description = "名称(支持like)，允许null") @RequestParam(required = false) String productName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String sort,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String totalPrice,
            @Parameter(description = "categoryid") @RequestParam(required = false) String categoryId,
            @Parameter(description = "supplierId") @RequestParam(required = false) String supplierId,
            @Parameter(description = "categoryName") @RequestParam(required = false) String categoryName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String quantity,
            @Parameter(description = "起始时间") @RequestParam(required = false) String joinTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
            @Parameter(description = "页码", required = true) @RequestParam long pageNum,
            @Parameter(description = "页大小", required = true) @RequestParam int pageSize) {
        log.debug("call purchase");
        Predicate where = DaoUtil.and();
        if (StringUtils.isNotEmpty(productName))
            where.like("productName", productName);
        if (StringUtils.isNotEmpty(totalPrice))
            where.equal("totalPrice", totalPrice);
        if (StringUtils.isNotEmpty(quantity))
            where.equal("quantity", quantity);
        if (StringUtils.isNotEmpty(categoryId))
            where.like("categoryId", categoryId);
        if (StringUtils.isNotEmpty(supplierId))
            where.like("supplierId", supplierId);
        if (StringUtils.isNotEmpty(categoryName))
            where.like("categoryName", categoryName);
        if (StringUtils.isNotEmpty(joinTime) && StringUtils.isNotEmpty(endTime)) {
            where.between("saleDate", joinTime, endTime); // created_time为时间字段名
        }

        PageSet<ReturnOrder> ps = this.service().findPage(where, Sort.parse(sort), pageNum, pageSize);
        return Result.toResult(ps);
    }

    @Operation(summary = "加chu库单", description = "")
    @PostMapping("/addReturn")
    public @ResponseBody Result<Boolean> addOutbound(@RequestBody ReturnOrder vo) {
        log.debug("call addReturn!");
        boolean re = saleService.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有出售");
        }
        List<SalesOrder> salesOrders = saleService.findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantity = 0;
// 遍历 purchase 列表并累加数量
        for (SalesOrder order : salesOrders) {
            totalQuantity += order.getQuantity();
        }

        List<ReturnOrder> inboundOrders = this.service().findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantityin = 0;
// 遍历 purchase 列表并累加数量
        for (ReturnOrder order : inboundOrders) {
            totalQuantityin += order.getQuantity();
        }

        boolean result;
        if (totalQuantity >= vo.getQuantity() && vo.getQuantity() <= (totalQuantity - totalQuantityin) && (totalQuantity - totalQuantityin) >= 0) {
            result = this.service().insert(vo, true);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "退货的商品数量不能大于销售数量！*^____^*");
        }

    }

    @Operation(summary = "修改单", description = "")
    @PostMapping("/updateReturn")
    public @ResponseBody Result<Boolean> updateInbound(@RequestBody ReturnOrder vo) {
        log.debug("call updateReturn!");
        boolean re = saleService.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有出售");
        }
        List<SalesOrder> salesOrders = saleService.findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantity = 0;
// 遍历 purchase 列表并累加数量
        for (SalesOrder order : salesOrders) {
            totalQuantity += order.getQuantity();
        }
        ReturnOrder inbound = this.service().findByName("id", vo.getId());
        boolean result;
        if ((totalQuantity - inbound.getQuantity() + vo.getQuantity()) <= totalQuantity) {
            result = this.service().update(vo, true, null);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "退货的商品数量不能大于销售数量！*^____^*");
        }

    }

}
