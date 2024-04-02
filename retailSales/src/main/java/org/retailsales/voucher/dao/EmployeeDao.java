package org.retailsales.voucher.dao;

import org.apache.ibatis.annotations.Mapper;

import org.quincy.rock.core.dao.DaoUtil;
import org.quincy.rock.core.util.MapUtil;
import org.retailsales.voucher.Dao;
import org.retailsales.voucher.entity.Employee;


@Mapper
public interface EmployeeDao extends Dao<Employee> {

	/**
	 * <b>更新个人信息。</b>
	 * <p><b>详细说明：</b></p>
	 * <!-- 在此添加详细说明 -->
	 * 根据用户id更新用户信息，值对象的id必须有效。
	 * 可以修改自己少部分个人信息。
	 * @param vo　个人用户信息
	 * @return　更新数据条数
	 */
	//public int updateSelfInfo(Employee vo);

	/**
	 * <b>修改密码。</b>
	 * <p><b>详细说明：</b></p>
	 * <!-- 在此添加详细说明 -->
	 * 无。
	 *
	 * @param code        用户登录名
	 * @param newPassword 加密后的新密码
	 * @return 更新数据条数
	 */
	default int changePassword(String code, String newPassword) {
		return updateMap(MapUtil.asMap("password", newPassword), DaoUtil.and().equal("code", code));
	}


}
