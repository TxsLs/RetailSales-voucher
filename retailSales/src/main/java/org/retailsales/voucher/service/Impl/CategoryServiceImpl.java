package org.retailsales.voucher.service.Impl;

import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.CategoryDao;
import org.retailsales.voucher.entity.Category;
import org.retailsales.voucher.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseService<Category, CategoryDao> implements CategoryService {
}
