package org.retailsales.voucher.service.Impl;

import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.ReturnDao;
import org.retailsales.voucher.entity.ReturnOrder;
import org.retailsales.voucher.service.ReturnService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReturnServiceImpl extends BaseService<ReturnOrder, ReturnDao> implements ReturnService {

    @Override
    public boolean insert(ReturnOrder entity, boolean ignoreNullValue, String... excluded) {
        Date date = DateUtil.getDateByWord("now");
        String formattedDate = DateUtil.formatDateTime(date);
        entity.setReturnDate(formattedDate);
        return super.insert(entity, ignoreNullValue, excluded);
    }
}
