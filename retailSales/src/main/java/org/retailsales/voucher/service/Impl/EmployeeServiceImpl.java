package org.retailsales.voucher.service.Impl;

import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.EmployeeDao;
import org.retailsales.voucher.entity.Employee;
import org.retailsales.voucher.entity.Photo;
import org.retailsales.voucher.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EmployeeServiceImpl extends BaseService<Employee, EmployeeDao> implements EmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Employee findByCode(String code) {
		return this.dao().findByName("code", code);
	}

    /*@Override
    @Transactional
    public boolean updateSelfInfo(Employee vo) {
        return dao().updateSelfInfo(vo) > 0;
    }*/

	@Override
	@Transactional
	public boolean changeSelfPassword(String code, String oldPassword, String newPassword) {
		Employee vo = this.findByCode(code);
		if (vo == null || !passwordEncoder.matches(oldPassword, vo.getPassword()))
			return false;
		else {
			String encodedPassword = passwordEncoder.encode(newPassword);
			return dao().changePassword(code, encodedPassword) > 0;
		}
	}

	@Override
	@Transactional
	public boolean changePassword(String code, String newPassword) {
		String encodedPassword = passwordEncoder.encode(newPassword);
		return dao().changePassword(code, encodedPassword) > 0;
	}

	@Override
	public Employee checkPassword(String code, String password) {
		Employee vo = this.findByCode(code);
		if (vo == null || !passwordEncoder.matches(password, vo.getPassword()))
			return null;
		else {
			return vo;
		}
	}

	@Override
	public Photo getPhoto(long id) {
		return this.dao().getPhoto(id);
	}

	@Override
	@Transactional
	public boolean updatePhoto(Photo photo) {
		return dao().updatePhoto(photo) > 0;
	}
    /*@Override
    @Transactional
    public boolean updateSelfInfo(Employee vo) {
        return dao().updateSelfInfo(vo) > 0;
    }*/

	@Override
	public boolean insert(Employee entity, boolean ignoreNullValue, String... excluded) {
		Date date = DateUtil.getDateByWord("now");
		entity.setJoinTime(date);
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		return super.insert(entity, ignoreNullValue, excluded);
	}

}
