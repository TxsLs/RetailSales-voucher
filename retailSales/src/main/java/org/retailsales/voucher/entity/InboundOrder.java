package org.retailsales.voucher.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Column;
import org.quincy.rock.core.dao.annotation.JoinTable;
import org.quincy.rock.core.dao.annotation.JoinTables;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "t_inbound_order", alias = "io")
@JoinTables({@JoinTable(name = "t_product", alias = "p", onExpr = "io.f_product_id=p.f_id"),
        @JoinTable(name = "t_category", alias = "cg", onExpr = "cg.f_id=p.f_category_id"),
        @JoinTable(name = "t_purchase_order", alias = "po", onExpr = "p.f_id=po.f_product_id"),
        @JoinTable(name = "t_supplier", alias = "s", onExpr = "po.f_supplier_id=s.f_id")
})
public class InboundOrder extends Entity {

    @Schema(description = "商品id", required = true, example = "1")
    private Long productId;

    @Schema(description = "商品名称", required = false, example = "5")
    @Column(value = "f_name", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String productName;

    @Schema(description = "分类名称", required = false, example = "6")
    @Column(value = "f_id", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryId;

    @Schema(description = "分类名称", required = false, example = "6")
    @Column(value = "f_name", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryName;

    @Schema(description = "入库时间", required = true, example = "2")
    private String inboundDate;

    @Schema(description = "进货时间", required = false, example = "2")
    @Column(value = "f_purchase_date", tableAlias = "po", ignoreInsert = true, ignoreUpdate = true)
    private String purchaseDate;

    @Schema(description = "库存数量", required = true, example = "3")
    @Column(value = "f_stock_quantity", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private Long stockQuantity;

    @Schema(description = "入库数量", required = true, example = "3")
    private Long quantity;

    @Schema(description = "计件方式", required = false, example = "5")
    @Column(value = "f_unit", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String unitName;

    @Schema(description = "进货单价", required = false, example = "3")
    @Column(value = "f_price", tableAlias = "po", ignoreInsert = true, ignoreUpdate = true)
    private BigDecimal price;

    /*@Schema(description = "进货总价", required = false, example = "4")
    @Column(value = "f_total_price", tableAlias = "po", ignoreInsert = true, ignoreUpdate = true)
    private BigDecimal totalPrice;*/

    @Schema(description = "供应商id", required = true, example = "4")
    @Column(value = "f_id", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private Long supplierId;

    @Schema(description = "供应商名称", required = false, example = "7")
    @Column(value = "f_name", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private String supplierName;

    @Schema(description = "供应商名称", required = false, example = "8")
    @Column(value = "f_phone", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private Long supplierPhone;

    @Schema(description = "操作人", required = true, example = "3")
    private String emp;
}
