package com.codingapi.filter.zuul.service.impl;

import com.codingapi.filter.core.token.TokenService;
import com.codingapi.filter.zuul.em.VerificationState;
import com.codingapi.filter.zuul.exception.VerificationException;
import com.codingapi.filter.zuul.helper.FilterZuulCheckHelper;
import com.codingapi.filter.zuul.service.PreRequestVerificationService;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by lorne on 2017/7/8.
 */
@Service
public class PreRequestVerificationServiceImpl implements PreRequestVerificationService {


    private final static String charsetName = "utf-8";

    private Logger logger = LoggerFactory.getLogger(PreRequestVerificationServiceImpl.class);

    @Autowired
    private TokenService tokenService;


    @Autowired
    private FilterZuulCheckHelper filterZuulCheckHelper;

    @Override
    public void execute(HttpServletRequest request, String url) throws VerificationException {

        //不需要任何验证的地址
        logger.debug("url:" + url);
        if (!filterZuulCheckHelper.needUrlVerifyCheck(url)) {
            return ;
        }

        //检查请求方式是否正确
        String method = request.getMethod();
        logger.debug("method:" + method);
        if (!"POST".equals(method)) {
            throw new VerificationException(VerificationState.STATE_METHOD_ERROR);
        }

        //检查token是否有效
        String token = ServletRequestUtils.getStringParameter(request, "token", "");
        logger.debug("token:" + token);
        tokenCheck(url,token);

        //检查sign数据格式
        String sign = ServletRequestUtils.getStringParameter(request, "sign", "");
        logger.debug("sign:" + sign);
        signCheck(sign,token,request);


    }


    private void tokenCheck(String url,String token) throws VerificationException{
        if (!filterZuulCheckHelper.needUrlTokenCheck(url)) {
            if (StringUtils.isNotEmpty(token)) {
                String tokenValue = tokenService.getToken(token);
                if (StringUtils.isEmpty(tokenValue)) {
                    throw new VerificationException(VerificationState.STATE_TOKEN_ERROR);
                }
            } else {
                throw new VerificationException(VerificationState.STATE_TOKEN_ERROR);
            }
        }
    }


    private void signCheck(String sign,String token,HttpServletRequest request) throws VerificationException{
        try {
            String json = IOUtils.toString(request.getInputStream(), charsetName);
            String checkSign;
            if (StringUtils.isNotEmpty(token)) {
                checkSign = MD5Util.md5(("token=" + token + "&content=" + json).getBytes(charsetName));
            } else {
                String value = "content=" + json;
                checkSign = MD5Util.md5((value).getBytes(charsetName));
            }

            if (!sign.equals(checkSign)) {
                throw new VerificationException(VerificationState.STATE_SIGN_ERROR);
            }
        }catch (Exception e){
            throw new VerificationException(VerificationState.STATE_SIGN_ERROR);
        }
    }


}
