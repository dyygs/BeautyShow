package com.dy.www.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {
    /**
     * 字符串拼接
     * @param subStrings 可变字符串数组
     * @return 拼接后的字符串
     */
    public static String connectStrings(String... subStrings) {
        StringBuilder sb = new StringBuilder();
        for (String str : subStrings) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 返回指定长度的带后缀字符串的子字符串，如果字符串的长度比指定的值小，则返回原字符串
     * 是getStringSub（String str,int length)方法的扩展
     * */
    public static String getStringSubSuffix(String str, int length, String suffix){
        if(str==null) return null;
        String substr = str;
        int len = str.length();
        if(len > length)
            substr = str.substring(0,length) + suffix;
        return substr;
    }

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->hello_world
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String camel2Underscore(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成小写
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成小写
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->helloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String underscore2Camel(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 根据传递的参数将String转换成Json格式的数据
     * @param params 键值对
     * @return
     */
    public static String transformStringToJson(String... params) {
        int count = params.length;
        String ret;
        if (count == 0) {
            ret = "{}";
        } else {
            StringBuffer sb = new StringBuffer("\"{");
            if (count%2 == 0) {
                for (int i=0;i<count;i++) {
                    sb.append(params[i]);
                    sb.append(":");
                    sb.append(params[++i]);
                    if (i == count-1) {
                        sb.append("}\"");
                    } else {
                        sb.append(",");
                    }
                }
            } else {
                for (int i=0;i<count-1;i++) {
                    sb.append(params[i]);
                    sb.append(":");
                    sb.append(params[++i]);
                    sb.append(",");
                }
                sb.append("");
                sb.append(params[count-1]);
                sb.append(": }\"");
            }
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * 将数字转换成汉语文字
     *
     * @param num
     * @return
     */
    public static String num2China(int num) {
        String str = "";
        int size = (num + "").length();
        if (size == 1) {
            str = singleNum2China(num);
        }else if (size == 2) {
            str = singleNum2China(num / 10) + "十";
            if (num % 10 != 0) {
                str += singleNum2China(num % 10);
            }
        } else if (size == 3) {
            str = singleNum2China(num / 100) + "百";
            if (num % 100 != 0) {
                int num1 = num % 100;
                str += singleNum2China(num1 / 10) + "十";
                if (num % 10 != 0) {
                    str += singleNum2China(num1 % 10);
                }
            }
        }
        return str;
    }

    private static String singleNum2China(int a) {
        StringBuffer sb = new StringBuffer();
        String[] str = new String[] { "零", "一", "二", "三", "四", "五", "六", "七",
                "八", "九" };
        return sb.append(str[a]).toString();
    }

    /**
     * tzhk 2012-5-8 19:21
     * 传参到html页面时替换敏感字符
     * */
    public static String getReplacedStringForTransfer(String original)
    {
        String dealed = original;
        dealed = dealed.replace("\\", "\\\\");
        dealed = dealed.replace("\"", "\\\"");
        dealed = dealed.replace("\n", "");
        dealed = dealed.replace("\r", "");
        return dealed;
    }

    /**
     * 判断String是否是数
     *
     * @param str
     * @return
     */
    public static Boolean isNumeric(String str)
    {
        if(str==null || str.equals(""))
            return false;

        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断String是否是JSON格式 简单的判断一下是以‘{’开头以‘}’结尾 或者以‘[’开头以‘]’结尾
     *
     * @param str
     * @return
     */
    public static Boolean isJSON(String str)
    {
        if(str==null || str.equals(""))
            return false;
        return ((str.startsWith("{") && str.endsWith("}"))
                || (str.startsWith("[") && str.endsWith("]")));
    }

    /**
     * unicode转UTF8
     *
     * @param unicodeString
     * @return
     */
    public static String unicode2UTF8(String unicodeString) {
        char aChar;
        int len = unicodeString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = unicodeString.charAt(x++);
            if (aChar == '\\') {
                aChar = unicodeString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = unicodeString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * trim方法，去除首尾空格
     * @param input
     * @return String
     * */
    public static String trim(String input) {
        return input != null ? input.trim() : input;
    }

    /**
     * filter，替换半角单引号、双引号为全角
     * @param input
     * @return String
     */
    private static String filter(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }

        return input.replaceAll("'", "＇").replaceAll("\"", "＂");
    }

    /**
     * changeCode全角转半角
     * */
    private static String changeCode(String input) {
        if(input == null || input.length() == 0) {
            return input;
        }

        StringBuffer outputBuffer = new StringBuffer("");
        String str = "";
        byte[] b = null;

        for(int i = 0; i < input.length(); i++) {
            try {
                str = input.substring(i, i+1);
                if (str.equals("　")) {
                    str = " ";
                }

                b = str.getBytes("unicode");
            } catch(UnsupportedEncodingException e) {
            }

            if(b[3] == -1) {
                b[2] = (byte)(b[2] + 32);
                b[3] = 0;

                try {
                    outputBuffer.append(new String(b,"unicode"));
                } catch(UnsupportedEncodingException e) {
                }

            } else {
                outputBuffer.append(str);
            }
        }

        return outputBuffer.toString();
    }

    /**
     * 常见的对字符串处理集合方法
     * @param input
     * @param changeCode 是否转换全角为半角
     * @param trim 是否进行trim操作
     * @param filter 是否进行filter替换
     * @return String
     * */
    public static String commonDeal(String input, boolean changeCode, boolean trim, boolean filter) {
        if (input == null || input.length() == 0) {
            return input;
        }
        if (changeCode) {
            input = changeCode(input);
        }

        if (trim) {
            input = trim(input);
        }

        if (filter) {
            input = filter(input);
        }

        return input;
    }

    /**
     * List转换String
     *
     * @param list
     *            :需要转换的List
     * @return String转换后的字符串
     */
    public static String ListToString(List<String> list, String devider) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                sb.append(list.get(i));
                if (i != list.size() - 1) {
                    sb.append(devider);
                }
            }
        }
        return sb.toString();
    }


    /**
     * 过滤所有以<开头以>结尾的标签
     *
     * @param str
     * @return
     */
    public static String filterHtml(String str) {
        String regxpForHtml = "<([^>]*)>";
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }



    public static boolean isEmpty(String str) {
        return (str==null || str.isEmpty());
    }


    /**
     * 是否是手机号码
     * @param mobiles   手机号码字符串
     * @return          true：是，false：不是
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 返回URL编码的字符串
     * @param orginalStr
     * @param encoder
     * @return
     */
    public static String getURLEncodeString(String orginalStr, String encoder) {
        if (orginalStr == null || encoder == null || encoder.equals("")) {
            return "";
        }
        try {
            return URLEncoder.encode(orginalStr, encoder);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
