package org.retailsales.voucher.service.Impl;

import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.InboundDao;
import org.retailsales.voucher.entity.InboundOrder;
import org.retailsales.voucher.service.InboundService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InboundServiceImpl extends BaseService<InboundOrder, InboundDao> implements InboundService {
    @Override
    public boolean insert(InboundOrder entity, boolean ignoreNullValue, String... excluded) {
        Date date = DateUtil.getDateByWord("now");
        String formattedDate = DateUtil.formatDateTime(date);
        entity.setInboundDate(formattedDate);
        return super.insert(entity, ignoreNullValue, excluded);
    }
}
