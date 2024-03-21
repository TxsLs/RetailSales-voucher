package org.retailsales.voucher.service.Impl;

import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.PurchaseOrderDao;
import org.retailsales.voucher.entity.PurchaseOrder;
import org.retailsales.voucher.service.PurchaseOrderService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class PurchaseOrderServiceImpl extends BaseService<PurchaseOrder, PurchaseOrderDao> implements PurchaseOrderService {

    @Override
    public boolean insert(PurchaseOrder entity, boolean ignoreNullValue, String... excluded) {
        Date date = DateUtil.getDateByWord("now");
        String formattedDate = DateUtil.formatDateTime(date);
        entity.setPurchaseDate(formattedDate);
        return super.insert(entity, ignoreNullValue, excluded);
    }

}
