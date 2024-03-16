package org.retailsales.voucher.controller;

import jakarta.validation.ValidationException;

import org.mybatis.spring.MyBatisSystemException;
import org.quincy.rock.core.vo.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>ControllerExceptionAdvice。</b>
 * <p><b>详细说明：</b></p>
 * <!-- 在此添加详细说明 -->
 * 无。
 *
 * @author Txs
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public Result<?> defaultExceptionHandler(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionDetails = sw.toString();

        System.out.println("捕获异常详情：");
        System.out.println(exceptionDetails);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", "1000");
        errorDetails.put("errorMessage", "未知异常!");
        errorDetails.put("exceptionDetails", exceptionDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("errorDetails", errorDetails);

        return Result.toResult(result);
    }


    @ExceptionHandler(BindException.class)
    public Result<?> bindExceptionHandler(BindException ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionDetails = sw.toString();

        System.out.println("绑定异常详情：");
        System.out.println(exceptionDetails);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", "1998");
        errorDetails.put("errorMessage", "绑定异常!");
        errorDetails.put("exceptionDetails", exceptionDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("errorDetails", errorDetails);

        return Result.toResult(result);
    }

    @ExceptionHandler(ValidationException.class)
    public Result<?> validationExceptionHandler(ValidationException ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionDetails = sw.toString();

        System.out.println("校验异常详情：");
        System.out.println(exceptionDetails);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", "1999");
        errorDetails.put("errorMessage", "校验异常!");
        errorDetails.put("exceptionDetails", exceptionDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("errorDetails", errorDetails);

        return Result.toResult(result);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> missingParamExceptionHandler(MissingServletRequestParameterException ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionDetails = sw.toString();

        System.out.println("缺少请求参数异常详情：");
        System.out.println(exceptionDetails);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", "2000");
        errorDetails.put("errorMessage", "缺少请求参数异常!");
        errorDetails.put("exceptionDetails", exceptionDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("errorDetails", errorDetails);

        return Result.toResult(result);
    }


    @ExceptionHandler(MyBatisSystemException.class)
    public Result<?> myBatisExceptionHandler(MyBatisSystemException ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionDetails = sw.toString(); // 获取异常堆栈信息

        // 打印异常信息到控制台
        System.out.println("MyBatis 系统异常：");
        System.out.println(exceptionDetails);

        // 构造返回结果
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", "3000");
        errorDetails.put("errorMessage", "MyBatis 系统异常");
        errorDetails.put("exceptionDetails", exceptionDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("errorDetails", errorDetails);

        return Result.toResult(result);
    }


}
