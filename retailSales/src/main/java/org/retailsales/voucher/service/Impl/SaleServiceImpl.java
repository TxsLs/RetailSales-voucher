package org.retailsales.voucher.service.Impl;

import org.apache.ibatis.annotations.Mapper;
import org.quincy.rock.core.util.DateUtil;
import org.retailsales.voucher.BaseService;
import org.retailsales.voucher.dao.SaleDao;
import org.retailsales.voucher.entity.SalesOrder;
import org.retailsales.voucher.service.SaleService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class SaleServiceImpl extends BaseService<SalesOrder, SaleDao> implements SaleService {
    @Override
    public boolean insert(SalesOrder entity, boolean ignoreNullValue, String... excluded) {
        Date date = DateUtil.getDateByWord("now");
        String formattedDate = DateUtil.formatDateTime(date);
        entity.setSaleDate(formattedDate);
        // 生成随机字母和数字作为后缀
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDateTime = dateFormat.format(date);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder cusCodeBuilder = new StringBuilder(formattedDateTime);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {  // 生成6位随机字符
            cusCodeBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        String cusCode = cusCodeBuilder.toString();

        // 更新 entity 的 cus 字段
        entity.setCus(cusCode);
        return super.insert(entity, ignoreNullValue, excluded);
    }
}
