package org.retailsales.voucher.entity;

import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.dao.annotation.Column;
import org.quincy.rock.core.dao.annotation.IgnoreUpdate;
import org.quincy.rock.core.dao.annotation.Table;
import org.retailsales.voucher.Entity;

import java.util.Date;

@Getter
@Setter

@Table(name = "t_employee", alias = "e", resultMap = "resultMap")
//@JoinTables({ @JoinTable(name = "t_ban", alias = "b", onExpr = "e.f_id=b.f_user_id") })
public class Employee extends Entity {
	/**
	 * serialVersionUID。
	 */
	private static final long serialVersionUID = 6839230938734074806L;


	private String code;


	private String name;


	private Integer admin;


	private Integer gender;

	@IgnoreUpdate
	private String password;

	private Date joinTime;

	private Long phone;

	private Integer status;


	@Column(value = "f_photo_file is not null", calculated = true)
	private Boolean hasPhoto;

}
