package com.xnfh.cec.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
public class UserRegCheck {
    /**

     * 验证用户名，支持中英文(包括全角字符)、数字、下划线和减号 (全角及汉字算两位),长度为4-20位,中文按二位计数

     * @author www.zuidaima.com

     * @param userName

     * @return

     */

    public boolean validateUserName(String userName) {
        String validateStr = "^[\\w\\-－＿[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";

        boolean rs = false;

        rs = matcher(validateStr, userName);

        if (rs) {
            int strLenth = getStrLength(userName);

            if (strLenth < 4 || strLenth > 20) {
                rs = false;

            }

        }

        return rs;

    }

    /**

     * 获取字符串的长度，对双字符(包括汉字)按两位计数

     *

     * @param value

     * @return

     */

    public   int getStrLength(String value) {
        int valueLength = 0;

        String chinese = "[\u0391-\uFFE5]";

        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);

            if (temp.matches(chinese)) {
                valueLength += 2;

            } else {
                valueLength += 1;

            }

        }

        return valueLength;

    }

    private   boolean matcher(String reg, String string) {
        boolean tem = false;

        Pattern pattern = Pattern.compile(reg);

        Matcher matcher = pattern.matcher(string);

        tem = matcher.matches();

        return tem;

    }

}
