package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wu
 * @date 2020/4/25 - 19:33
 */
public class DataFormatUtils {
    public static String getDateFormat(Date date,String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s);
        String format = simpleDateFormat.format(date);
        return format;
    }
}
