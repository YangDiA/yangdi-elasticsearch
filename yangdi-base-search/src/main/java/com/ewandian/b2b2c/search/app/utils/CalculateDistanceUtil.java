package com.ewandian.b2b2c.search.app.utils;

import com.ewandian.platform.util.StringUtil;

/**
 * Created by YangDi on 2017/3/29.
 * 根据经纬度计算距离
 */
public class CalculateDistanceUtil {
    public static  double calculateDistance(String IPCoordinateX,String IPCoordinateY,String shopCoordinateX,String shopCoordinateY) {
        if(StringUtil.isNullOrEmpty(shopCoordinateX) || StringUtil.isNullOrEmpty(shopCoordinateY)) {
            return Double.MAX_VALUE;
        }
        Double x = Double.valueOf(IPCoordinateX) - Double.valueOf(shopCoordinateX);
        Double y = Double.valueOf(IPCoordinateY) - Double.valueOf(shopCoordinateY);
        Double z = Math.abs(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
        return z;
    }
}
