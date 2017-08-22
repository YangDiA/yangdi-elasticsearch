package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc15 on 2017/2/23.
 */
public class At {
    @Test
    public void beanWrapper(){
        AdsEntity entity = new AdsEntity();
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
        bw.setPropertyValue("adId","aaaa");
        System.out.print(entity.getAdId());
    }



    @Test
    public void p(){

String mobiles = "17760005194";
            Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

            Matcher m = p.matcher(mobiles);

            System.out.println(m.matches()+"---");
        try{
            SimpleDateFormat DateFormate =   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.util.Date date1 = new java.util.Date();
            System.out.println(date1+"---");
            java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
            sqlDate.setTime(date1.getTime());
            System.out.println(sqlDate);
            GoodsEntity goodsEntity = new GoodsEntity();
           // goodsEntity.setGoodsUpDate(sqlDate);
            System.out.println(DateFormate.format(sqlDate));
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static boolean isMobileNO(String mobiles){

       Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);


        return m.matches();

     }
}
