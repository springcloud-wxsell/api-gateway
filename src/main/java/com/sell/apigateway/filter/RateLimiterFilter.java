package com.sell.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import com.sell.common.enums.ResultEnum;
import com.sell.common.exception.RateLimiterException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author WangWei
 * @Title: RateFilter
 * @ProjectName api-gateway
 * @date 2018/12/17 17:15
 * @description:
 */
@Component
public class RateLimiterFilter extends ZuulFilter {
    private static final RateLimiter Rate_Limiter = RateLimiter.create(100);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 限流拦截器的顺序应该放在所有拦截器之前
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!Rate_Limiter.tryAcquire()) {
            throw new RateLimiterException(ResultEnum.ACQUIRE_TOKEN_ERROR);
        }
        return null;
    }
}
