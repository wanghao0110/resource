package com.xnfh.cec.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
public class IdCardCheck {
    /**
     * 判断身份证号码是否合法
     *
     * @param number
     * @return true为合法 false为不合法
     */
    public boolean getShenFenZhengBollean(String number) {
        //获取身份证长度除去最后一位的长度
        int length = number.length() - 1;
        Boolean trueOrFalse = true;
        if (17 != length) {
            System.out.println("您输入的身份证号格式有误（身份证位数不正确），请检查后重新输入！");
            trueOrFalse = false;
            return trueOrFalse;
        }
        int[] array = new int[length];
        //求和
        int sum = 0;
        //余数
        int residue = -1;
        //余数对应校验数字
        char check = ' ';
        //最后一位字符
        char lastChar = number.charAt(length);
        for (int i = 0; i < length; i++) {
            //  array[i] = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (!Character.isDigit(number.charAt(i))) {
                System.out.println("您输入的身份证号格式有误(前17位中存在非数字类型字符)，请检查后重新输入！");
                trueOrFalse = false;
                return trueOrFalse;
            } else if (true == isSpecialChar(String.valueOf(lastChar))) {
                System.out.println("您输入的身份证号格式有误(第18位存在非法字符)，请检查后重新输入！");
                trueOrFalse = false;
                return trueOrFalse;
            }
            int[] array2 = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            array[i] = Integer.parseInt(String.valueOf(number.charAt(i)));
            sum += array2[i] * array[i];
            //  System.out.println(sum);
        }
        residue = sum % 11;
        switch (residue) {
            case 0:
                check = '1';
                break;
            case 1:
                check = '0';
                break;
            case 2:
                check = 'X';
                break;
            case 3:
                check = '9';
                break;
            case 4:
                check = '8';
                break;
            case 5:
                check = '7';
                break;
            case 6:
                check = '6';
                break;
            case 7:
                check = '5';
                break;
            case 8:
                check = '4';
                break;
            case 9:
                check = '3';
                break;
            case 10:
                check = '2';
                break;
        }
        if (check != lastChar) {
            System.out.println("对不起，您查询的身份证号码非法！");
            trueOrFalse = false;
        } else {
            System.out.println("恭喜！您查询的身份证号码合法！");
        }
        //System.out.println(Arrays.toString(array));
        return trueOrFalse;
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
