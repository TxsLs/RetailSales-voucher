package org.retailsales.voucher.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;

@Getter
@Setter
@Table(name = "t_category", alias = "ca")
public class Category extends Entity {

    @Schema(description = "分类名称", required = true, example = "1")
    private String name;


}
