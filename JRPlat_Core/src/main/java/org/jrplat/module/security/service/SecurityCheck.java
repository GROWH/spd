/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

/**
 *
 * @author 西安捷然
 */

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Service
public class SecurityCheck {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(SecurityCheck.class);
    private static final String securityKeyName;
    private static final String securityClspath;
    private static final String sequenceKeyName;
    private static final String sequenceClspath;
    private static String osName = "Windows";

    static {
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
            osName = "Linux";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1) {
            osName = "Mac";
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("solaris") != -1) {
            osName = "Solaris";
        }
        LOG.debug("osName: " + osName);
        sequenceKeyName = "/org/jrplat/module/security/service/sequence/SequenceKey";
        sequenceClspath = "/org/jrplat/module/security/service/sequence/" + osName + "SequenceService";

        securityKeyName = "/org/jrplat/module/security/service/SecurityKey";
        securityClspath = "/org/jrplat/module/security/service/SecurityService";
    }

    /**
     *
     * @param <T>
     * @param keyFile 类路径
     * @param classFile 类路径
     * @param className
     * @return
     */
    private static <T> T loadClass(String keyFile, String classFile, final String className) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            byte[] rawKeyData;
            try (InputStream fi = SecurityCheck.class.getResourceAsStream(keyFile)) {
                rawKeyData = FileUtils.readAll(fi);
            }
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            byte[] encryptedData;
            try (InputStream fi2 = SecurityCheck.class.getResourceAsStream(classFile)) {
                encryptedData = FileUtils.readAll(fi2);
            }
            // 正式执行解密操作
            final byte decryptedData[] = cipher.doFinal(encryptedData);
            Object obj = new ClassLoader() {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    if (className.equals(name)) {
                        return super.defineClass(name, decryptedData, 0, decryptedData.length);
                    }

                    return SecurityCheck.class.getClassLoader().loadClass(name);
                }
            }.loadClass(className);
            return (T) obj;
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | ClassNotFoundException e) {
            LOG.debug("加载类失败", e);
        }
        return null;
    }

    private static String getSequence() {
        try {
            Class clazz = loadClass(sequenceKeyName, sequenceClspath, "org.jrplat.module.security.service.sequence." + osName + "SequenceService");
            Object obj = clazz.newInstance();
            Method method = ReflectionUtils.findMethod(clazz, "getSequence");
            String seq = method.invoke(obj).toString();
            return seq;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOG.debug("获取机器码钥失败", e);
        }
        return "";
    }

    @PostConstruct
    public static void check() {
//        LOG.debug("开始进行安全检查");        
//        String seq="";
//        try{
//            seq=getSequence();
//            LOG.debug("机器指纹："+seq);
//        }catch(Exception e){
//            LOG.debug("安全检查失败",e);
//        }
//        try {
//            Class clazz=loadClass(securityKeyName,securityClspath,"org.jrplat.module.security.service.SecurityService");
//            Object obj=clazz.newInstance();
//            Method method=ReflectionUtils.findMethod(clazz, "checkSeq",String.class);
//            method.invoke(obj,seq);
//            LOG.debug("安全检查完成");
//        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            LOG.debug("安全检查出错",e);
//        }
    }
}