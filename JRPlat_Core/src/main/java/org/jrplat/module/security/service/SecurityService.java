/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

/**
 *
 * @author 西安捷然
 */

import org.apache.commons.lang.StringUtils;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FileUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.util.Collection;

public class SecurityService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(SecurityService.class);

    //    public void checkSeq(String seq){
//        if(StringUtils.isNotBlank(seq)){
//            LOG.debug("机器码为："+seq);
//            if(valide(seq)){
//                authSuccess();
//                LOG.debug("产品已经取得合法授权");
//            }else{
//                LOG.debug("产品没有取得授权");
//                authFail(seq);
//            }
//        }else{
//            LOG.debug("机器码获取失败");
//            LOG.debug("产品没有取得授权");
//            authFail(seq);
//        }
//    }
    public void checkSeq(String seq) {
        authSuccess();
    }

    private void authSuccess() {
        FileUtils.removeFile("/WEB-INF/lib/server");
        FileUtils.removeFile("/WEB-INF/licence");
    }

    private void authFail(String seq) {
        FileUtils.createAndWriteFile("/WEB-INF/lib/server", seq);
        FileUtils.createAndWriteFile("/WEB-INF/licence", seq);
    }

    private String auth(String machineCode) {
        String newCode = "(jierankeji@163.com)[" + machineCode.toUpperCase() + "](JRPlat捷然开发平台)";
        String code = new Md5PasswordEncoder().encodePassword(newCode, "西安捷然").toUpperCase() + machineCode.length();
        return getSplitString(code);
    }

    private String getSplitString(String str) {
        return getSplitString(str, "-", 4);
    }

    private String getSplitString(String str, String split, int length) {
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

    private boolean valide(String seq) {
        try {
            String authCode = auth(seq);
            if (StringUtils.isBlank(authCode)) {
                return false;
            }
            Collection<String> licences = FileUtils.getTextFileContent("/WEB-INF/classes/licences/jrplat.licence");
            for (String no : licences) {
                if (authCode.equals(no)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOG.debug("安全检查出错", e);
        }
        return false;
    }
}