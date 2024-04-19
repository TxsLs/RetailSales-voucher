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
import org.retailsales.voucher.entity.PurchaseOrder;
import org.retailsales.voucher.service.InboundService;
import org.retailsales.voucher.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "入库单管理")
@Controller
@RequestMapping("/inbound")
public class InboundController extends BaseController<InboundOrder, InboundService> {
    @Autowired
    PurchaseOrderService Purchaseservice;

    @Operation(summary = "条件分页查询", description = "")
    @GetMapping("/queryPage")
    public @ResponseBody Result<PageSet<InboundOrder>> queryPage(
            @Parameter(description = "名称(支持like)，允许null") @RequestParam(required = false) String productName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String sort,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String price,
            @Parameter(description = "categoryid") @RequestParam(required = false) String categoryId,
            @Parameter(description = "supplierId") @RequestParam(required = false) String supplierId,
            @Parameter(description = "categoryName") @RequestParam(required = false) String categoryName,
            @Parameter(description = "排序规则字符串") @RequestParam(required = false) String quantity,
            @Parameter(description = "起始时间") @RequestParam(required = false) String joinTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
            @Parameter(description = "页码", required = true) @RequestParam long pageNum,
            @Parameter(description = "页大小", required = true) @RequestParam int pageSize) {
        log.debug("call inbound");
        Predicate where = DaoUtil.and();
        if (StringUtils.isNotEmpty(productName))
            where.like("productName", productName);
        if (StringUtils.isNotEmpty(price))
            where.equal("price", price);
        if (StringUtils.isNotEmpty(quantity))
            where.equal("quantity", quantity);
        if (StringUtils.isNotEmpty(categoryName))
            where.like("categoryName", categoryName);
        if (StringUtils.isNotEmpty(categoryId))
            where.like("categoryId", categoryId);
        if (StringUtils.isNotEmpty(supplierId))
            where.like("supplierId", supplierId);
        if (StringUtils.isNotEmpty(categoryName))
            where.like("categoryName", categoryName);
        if (StringUtils.isNotEmpty(joinTime) && StringUtils.isNotEmpty(endTime)) {
            where.between("inboundDate", joinTime, endTime); // created_time为时间字段名
        }

        PageSet<InboundOrder> ps = this.service().findPage(where, Sort.parse(sort), pageNum, pageSize);
        return Result.toResult(ps);
    }

    @Operation(summary = "加入库单", description = "")
    @PostMapping("/addInbound")
    public @ResponseBody Result<Boolean> addInbound(@RequestBody InboundOrder vo) {
        log.debug("call addInbound!");
        boolean re = Purchaseservice.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有进货!");
        }
        List<PurchaseOrder> purchase = Purchaseservice.findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantity = 0;
// 遍历 purchase 列表并累加数量
        for (PurchaseOrder order : purchase) {
            totalQuantity += order.getQuantity();
        }

        List<InboundOrder> inboundOrders = this.service().findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantityin = 0;
// 遍历 purchase 列表并累加数量
        for (InboundOrder order : inboundOrders) {
            totalQuantityin += order.getQuantity();
        }

        boolean result;
        if (totalQuantity >= vo.getQuantity() && vo.getQuantity() <= (totalQuantity - totalQuantityin) && (totalQuantity - totalQuantityin) >= 0) {
            result = this.service().insert(vo, true);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "入库的商品总数量不能大于进货数量！*^____^*");
        }

    }

    @Operation(summary = "修改库单", description = "")
    @PostMapping("/updateInbound")
    public @ResponseBody Result<Boolean> updateInbound(@RequestBody InboundOrder vo) {
        log.debug("call updateInbound!");
        boolean re = Purchaseservice.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有进货");
        }
        List<PurchaseOrder> purchase = Purchaseservice.findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantity = 0;
// 遍历 purchase 列表并累加数量
        for (PurchaseOrder order : purchase) {
            totalQuantity += order.getQuantity();
        }

        InboundOrder inbound = this.service().findByName("id", vo.getId());
        List<InboundOrder> inboundOrders = this.service().findAllByName("productId", vo.getProductId(), null);
        // 初始化总数量为0
        int totalQuantityin = 0;
// 遍历 purchase 列表并累加数量
        for (InboundOrder order : inboundOrders) {
            totalQuantityin += order.getQuantity();
        }

        boolean result;
        if (totalQuantity >= totalQuantityin && (totalQuantity - totalQuantityin >= 0) && ((vo.getQuantity() < inbound.getQuantity()) || Math.abs(vo.getQuantity() - inbound.getQuantity()) <= totalQuantity - totalQuantityin)) {
            result = this.service().update(vo, true, null);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "入库的商品总数量不能大于进货数量！*^____^*");
        }

    }

}
