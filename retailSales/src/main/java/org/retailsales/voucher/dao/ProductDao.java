package org.retailsales.voucher.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.retailsales.voucher.Dao;
import org.retailsales.voucher.entity.Product;

import java.util.Map;

@Mapper
public interface ProductDao extends Dao<Product> {


}
