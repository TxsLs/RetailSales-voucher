package org.retailsales.voucher.service.Impl;

import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.SupplierDao;
import org.retailsales.voucher.entity.Supplier;
import org.retailsales.voucher.service.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl extends BaseService<Supplier, SupplierDao> implements SupplierService {
}
