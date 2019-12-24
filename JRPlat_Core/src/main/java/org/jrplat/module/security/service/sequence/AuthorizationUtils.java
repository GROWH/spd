/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.sequence;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 *此工具负责根据用户的机器码来生成注册码
 * @author 西安捷然
 */
public class AuthorizationUtils {
    private static final int SPLITLENGTH = 4;

    public static void main(String args[]) throws Exception {
        String code = "71F5-DA7F-495E-7F70-6D47-F3E6-3DC6-349A";
        String authCode = auth(code);
        System.out.println("机器码：" + code);
        System.out.println("注册码：" + authCode);
    }

    public static String auth(String machineCode) {
        String newCode = "(jierankeji@163.com)[" + machineCode.toUpperCase() + "](JRPlat捷然开发平台)";
        String code = new Md5PasswordEncoder().encodePassword(newCode, "西安捷然").toUpperCase() + machineCode.length();
        return getSplitString(code);
    }

    private static String getSplitString(String str) {
        return getSplitString(str, "-", SPLITLENGTH);
    }

    private static String getSplitString(String str, String split, int length) {
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i % length == 0 && i > 0) {
                temp.append(split);
            }
            temp.append(str.charAt(i));
        }
        String[] attrs = temp.toString().split(split);
        StringBuilder finalMachineCode = new StringBuilder();
        for (String attr : attrs) {
            if (attr.length() == length) {
                finalMachineCode.append(attr).append(split);
            }
        }
        String result = finalMachineCode.toString().substring(0, finalMachineCode.toString().length() - 1);
        return result;
    }
}