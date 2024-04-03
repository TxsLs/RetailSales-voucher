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
import org.retailsales.voucher.entity.OutboundOrder;
import org.retailsales.voucher.entity.Product;
import org.retailsales.voucher.service.InboundService;
import org.retailsales.voucher.service.OutboundService;
import org.retailsales.voucher.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "出库单管理")
@Controller
@RequestMapping("/outbound")
public class OutboundController extends BaseController<OutboundOrder, OutboundService> {
    @Autowired
    ProductService productservice;
    @Autowired
    InboundService inboundService;

    @Operation(summary = "条件分页查询", description = "")
    @GetMapping("/queryPage")
    public @ResponseBody Result<PageSet<OutboundOrder>> queryPage(
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
        log.debug("call outbound");
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
            where.between("outboundDate", joinTime, endTime); // created_time为时间字段名
        }

        PageSet<OutboundOrder> ps = this.service().findPage(where, Sort.parse(sort), pageNum, pageSize);
        return Result.toResult(ps);
    }

    @Operation(summary = "加chu库单", description = "")
    @PostMapping("/addOutbound")
    public @ResponseBody Result<Boolean> addOutbound(@RequestBody OutboundOrder vo) {
        log.debug("call addOutbound!");
        boolean re = inboundService.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有入库");
        }
        Product product = productservice.findByName("id", vo.getProductId());
        boolean result;
        if (product.getStockQuantity() >= vo.getQuantity()) {
            result = this.service().insert(vo, true);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "出库的商品数量不能大于库存数量！*^____^*");
        }

    }

    @Operation(summary = "修改出库单", description = "")
    @PostMapping("/updateOutbound")
    public @ResponseBody Result<Boolean> updateInbound(@RequestBody OutboundOrder vo) {
        log.debug("call updateInbound!");
        boolean re = inboundService.existByName("productId", vo.getProductId(), null);

        if (re == false) {
            return Result.toResult("1077", "此商品没有入库");
        }
        Product product = productservice.findByName("id", vo.getProductId());
        boolean result;
        if (product.getStockQuantity() >= 0) {
            result = this.service().update(vo, true, null);
            return Result.of(result);
        } else {
            return Result.toResult("1077", "出库的商品数量不能大于进货数量！*^____^*");
        }

    }

}
