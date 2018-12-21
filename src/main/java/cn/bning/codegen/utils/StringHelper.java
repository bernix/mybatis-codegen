package cn.bning.codegen.utils;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
public class StringHelper {

    /**
     * 将下划线分隔的字符串转换为驼峰变量名
     *
     * @return "hello_world" -> "HelloWorld"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 将下划线分隔的字符串转换为驼峰命名
     *
     * @return "hello_world" -> "HelloWorld"
     */
    public static String toCapitalCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 将驼峰命名转换为下划线分隔的小写字符串
     *
     * @return "helloWorld" -> "hello_world"
     */
    public static String toUnderscoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append('_');
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
