package utils;

/**
 * @author wu
 * @date 2020/4/19 - 17:03
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str))return true;
        return false;
    }
}
