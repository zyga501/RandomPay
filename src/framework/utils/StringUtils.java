package framework.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class StringUtils {
    public static String convertNullableString(Object object) {
        if (object == null) {
            return new String();
        }

        return object.toString();
    }

    public static String convertNullableString(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        return object.toString();
    }

    public static String generateRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String generateDate(String pattern, String timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(new Date(Long.valueOf(System.currentTimeMillis()).longValue()));
    }

    public static String saltEncode(String sourcestring) {
        byte[] b = null;
        String s = null;
        String chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        try {

            b = sourcestring.substring(0,sourcestring.length()-1).concat(String.valueOf(sourcestring.charAt(sourcestring.length()-1)^3)).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return chars.charAt((int)(Math.random() * 62))+s;
    }

    public static String saltDecode(String sourcestring) {
        byte[] b ;
        String result = null;
        if (sourcestring != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(sourcestring.substring(1));
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.substring(0,result.length()-1);
    }
}
