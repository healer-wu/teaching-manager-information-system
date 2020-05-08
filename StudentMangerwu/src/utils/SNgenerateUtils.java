package utils;

import java.util.Date;

/**
 * @author wu
 * @date 2020/4/25 - 19:32
 */
public class SNgenerateUtils {
    public static String getSN(int id){
//        String yyyyMMdd = DataFormatUtils.getDateFormat(new Date(), "yyyyMMdd");
        return "s"+id+(short)System.currentTimeMillis();
    }
}
