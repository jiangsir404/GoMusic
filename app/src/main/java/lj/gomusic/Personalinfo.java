package lj.gomusic;

/**
 * Created by lj on 17-12-18.
 */

public class Personalinfo {
    private static String username;
    private static String phonenum;
    private static String password;
    private static String text;
    private static String sex;

    public static String getSex() {
        return sex;
    }

    public static void setSex(String sex) {
        Personalinfo.sex = sex;
    }


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Personalinfo.username = username;
    }

    public static String getPhonenum() {
        return phonenum;
    }

    public static void setPhonenum(String phonenum) {
        Personalinfo.phonenum = phonenum;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Personalinfo.password = password;
    }

    public static String getText() {
        return text;
    }

    public static void setText(String text) {
        Personalinfo.text = text;
    }
}
