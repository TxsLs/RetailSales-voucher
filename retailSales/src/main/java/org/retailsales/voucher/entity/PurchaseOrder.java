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
import java.util.Date;

@Getter
@Setter
@Table(name = "t_purchase_order", alias = "po")
@JoinTables({@JoinTable(name = "t_product", alias = "p", onExpr = "po.f_product_id=p.f_id"),
        @JoinTable(name = "t_category", alias = "cg", onExpr = "cg.f_id=p.f_category_id"),
        @JoinTable(name = "t_supplier", alias = "s", onExpr = "s.f_id=po.f_supplier_id")
})


public class PurchaseOrder extends Entity {

    @Schema(description = "商品id", required = true, example = "1")
    private Long productId;

    @Schema(description = "进货时间", required = true, example = "2")
    private String PurchaseDate;

    @Schema(description = "进货数量", required = true, example = "3")
    private Long quantity;

    @Schema(description = "进货单价", required = true, example = "3")
    private BigDecimal price;

    @Schema(description = "进货总价", required = false, example = "4")
    private BigDecimal totalPrice;

    @Schema(description = "商品名称", required = false, example = "5")
    @Column(value = "f_name", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String productName;

    @Schema(description = "分类名称", required = false, example = "6")
    @Column(value = "f_name", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryName;

    @Schema(description = "供应商名称", required = false, example = "7")
    @Column(value = "f_name", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private String supplierName;

    @Schema(description = "供应商名称", required = false, example = "8")
    @Column(value = "f_phone", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private Long supplierPhone;
}
