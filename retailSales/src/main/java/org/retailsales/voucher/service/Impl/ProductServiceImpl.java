package org.retailsales.voucher.service.Impl;

import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.ProductDao;
import org.retailsales.voucher.entity.Product;
import org.retailsales.voucher.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends BaseService<Product, ProductDao> implements ProductService {

}
