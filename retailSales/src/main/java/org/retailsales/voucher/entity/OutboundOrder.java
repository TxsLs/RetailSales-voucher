package org.retailsales.voucher.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Column;
import org.quincy.rock.core.dao.annotation.JoinTable;
import org.quincy.rock.core.dao.annotation.JoinTables;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;


@Getter
@Setter
@Table(name = "t_outbound_order", alias = "oo")
@JoinTables({@JoinTable(name = "t_product", alias = "p", onExpr = "oo.f_product_id=p.f_id"),
        @JoinTable(name = "t_category", alias = "cg", onExpr = "cg.f_id=p.f_category_id"),
})
public class OutboundOrder extends Entity {

    @Schema(description = "商品id", required = true, example = "1")
    private Long productId;

    @Schema(description = "商品名称", required = false, example = "5")
    @Column(value = "f_name", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private String productName;

    @Schema(description = "分类名称", required = false, example = "6")
    @Column(value = "f_name", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryName;

    @Schema(description = "出库时间", required = true, example = "2")
    private String outboundDate;


    @Schema(description = "库存数量", required = true, example = "3")
    @Column(value = "f_stock_quantity", tableAlias = "p", ignoreInsert = true, ignoreUpdate = true)
    private Long stockQuantity;

    @Schema(description = "出库数量", required = true, example = "3")
    private Long quantity;


    @Schema(description = "操作人", required = true, example = "3")
    private String emp;


}
