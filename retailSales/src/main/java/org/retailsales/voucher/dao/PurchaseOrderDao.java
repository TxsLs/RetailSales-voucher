package org.retailsales.voucher.dao;

import org.apache.ibatis.annotations.Mapper;
import org.retailsales.voucher.Dao;
import org.retailsales.voucher.entity.PurchaseOrder;

@Mapper
public interface PurchaseOrderDao extends Dao<PurchaseOrder> {
}
