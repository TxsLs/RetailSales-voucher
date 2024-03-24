package org.retailsales.voucher.service.Impl;

import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.OutboundDao;
import org.retailsales.voucher.entity.OutboundOrder;
import org.retailsales.voucher.service.OutboundService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OutboundServiceImpl extends BaseService<OutboundOrder, OutboundDao> implements OutboundService {

    @Override
    public boolean insert(OutboundOrder entity, boolean ignoreNullValue, String... excluded) {
        Date date = DateUtil.getDateByWord("now");
        String formattedDate = DateUtil.formatDateTime(date);
        entity.setOutboundDate(formattedDate);
        return super.insert(entity, ignoreNullValue, excluded);
    }
}
