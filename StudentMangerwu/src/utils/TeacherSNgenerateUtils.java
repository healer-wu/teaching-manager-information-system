package utils;

/**
 * @author wu
 * @date 2020/4/29 - 8:38
 */
public class TeacherSNgenerateUtils {
    public static String getSN(int id){
//        String yyyyMMdd = DataFormatUtils.getDateFormat(new Date(), "yyyyMMdd");
        return "t"+id+System.currentTimeMillis();
    }
}
