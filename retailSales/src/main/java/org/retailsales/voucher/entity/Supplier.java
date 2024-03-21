package org.retailsales.voucher.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;

@Getter
@Setter
@Table(name = "t_supplier", alias = "s")
public class Supplier extends Entity {

    @Schema(description = "供应商名称", required = false, example = "1")
    private String name;

    @Schema(description = "供应商名称", required = false, example = "2")
    private Long phone;

}
