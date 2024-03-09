package org.retailsales.voucher;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import org.quincy.rock.core.dao.annotation.PrimaryKey;
import org.quincy.rock.core.vo.BaseEntity;


import lombok.Getter;
import lombok.Setter;

/**
 * <b>实体基类。</b>
 * <p><b>详细说明：</b></p>
 * <!-- 在此添加详细说明 -->
 * 无。
 *
 * @version 1.0
 * @author mex2000
 * @since 1.0
 */
@Getter
@Setter
public abstract class Entity extends BaseEntity<Long> {
	/**
	 * serialVersionUID。
	 */
	private static final long serialVersionUID = -9146290789116371771L;

	/**
	 * 主键id。
	 */
	//@ApiModelProperty(value="主键id",position = 0)
	@Schema(description = "主键id", example = "0")
	@PrimaryKey(name = "f_id")
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * id。
	 * @see org.quincy.rock.core.vo.Vo#id()
	 */
	@Override
	public Long id() {
		return id;
	}

	/**
	 * id。
	 * @see org.quincy.rock.core.vo.Vo#id(Object)
	 */
	@Override
	public Entity id(Long id) {
		this.id = id;
		return this;
	}


}
