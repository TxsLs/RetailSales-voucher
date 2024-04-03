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
@Schema(description = "销售单实体(在执行更新操作时采取动态更新策略，如果属性值为空，则忽略该属性)")
@Table(name = "t_return_order", alias = "ro")
@JoinTables({@JoinTable(name = "t_product", alias = "p", onExpr = "ro.f_product_id=p.f_id"),
//        @JoinTable(name = "t_sales_order", alias = "so", onExpr = "so.f_product_id=p.f_id"),
        @JoinTable(name = "t_category", alias = "cg", onExpr = "cg.f_id=p.f_category_id"),
//        @JoinTable(name = "t_purchase_order", alias = "po", onExpr = "p.f_id=po.f_product_id"),
//        @JoinTable(name = "t_supplier", alias = "s", onExpr = "po.f_supplier_id=s.f_id")
})
public class ReturnOrder extends Entity {

    @Schema(description = "商品id", required = false, example = "1")
    private Long productId;

    @Schema(description = "商品名称", required = false, example = "5")
    @Column(value = "f_name", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String productName;

    @Schema(description = "分类名称", required = false, example = "6")
    @Column(value = "f_name", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryName;

    @Schema(description = "退货时间", required = false, example = "2")
    private String returnDate;

    @Schema(description = "计件方式", required = false, example = "5")
    @Column(value = "f_unit", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String unitName;

    @Schema(description = "退货数量", required = false, example = "2")
    private Long quantity;

    @Schema(description = "退货原因", required = false, example = "2")
    private String reason;

//    @Schema(description = "销售单价", required = false, example = "3")
//    @Column(value = "f_sale_price", tableAlias = "so", ignoreInsert = true, ignoreUpdate = true)
//    private BigDecimal salePrice;

    @Schema(description = "退货金额", required = false, example = "4")
    private BigDecimal totalPrice;

    /*@Schema(description = "供应商id", required = false, example = "4")
    @Column(value = "f_id", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private Long supplierId;

    @Schema(description = "供应商名称", required = false, example = "7")
    @Column(value = "f_name", tableAlias = "s", ignoreInsert = true, ignoreUpdate = true)
    private String supplierName;*/

    @Schema(description = "操作人", required = false, example = "3")
    private String emp;

    @Schema(description = "顾客代号", required = false, example = "3")
    private String cus;

}
