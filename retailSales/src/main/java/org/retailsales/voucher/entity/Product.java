package org.retailsales.voucher.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Column;
import org.quincy.rock.core.dao.annotation.IgnoreInsertUpdate;
import org.quincy.rock.core.dao.annotation.JoinTable;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "商品实体(在执行更新操作时采取动态更新策略，如果属性值为空，则忽略该属性)")
@Table(name = "t_product", alias = "p")
@JoinTable(name = "t_category", alias = "cg", onExpr = "cg.f_id=p.f_category_id")
public class Product extends Entity {


    @Schema(description = "商品名称", required = true, example = "1")
    private String name;

    @Schema(description = "分类名称", required = false, example = "2")
    @Column(value = "f_name", tableAlias = "cg", ignoreInsert = true, ignoreUpdate = true)
    private String categoryName;

   /* @Schema(description = "商品价格", required = true, example = "3")
    private BigDecimal price;*/

    @Schema(description = "商品库存", required = true, example = "4")
    private Long stockQuantity;

    @Schema(description = "商品分类编号", required = true, example = "5")
    private Long categoryId;

   /* @IgnoreInsertUpdate
    @Schema(description = "商品状态", required = false, example = "6")
    private Integer status;*/


}
