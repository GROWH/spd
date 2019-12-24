/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.sequence;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 *此工具负责把
 * LinuxSequenceService.class
 * WindowsSequenceService.class
 * MacSequenceService.class
 * SolarisSequenceService.class
 * SecurityService.class
 * 进行加密并放到对应的包中
 * 在maven打包的时候，class文件不会打进包中
 * @author 西安捷然
 */
public class EncryptClassUtils {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(EncryptClassUtils.class);
    private static final String sequenceKeyName;
    private static final String securityKeyName;
    private static final String winClspath;
    private static final String linuxClspath;
    private static final String macClspath;
    private static final String solarisClspath;
    private static final String securityClspath;

    private static final String workDir = "D:/Workspaces/NetBeansProjects/JRPlat/JRPlat_Core/";

    static {
        String dir = workDir + "src/main/resources/org/jrplat/module/security/service/";
        sequenceKeyName = dir + "sequence/SequenceKey";
        securityKeyName = dir + "SecurityKey";
        winClspath = dir + "sequence/WindowsSequenceService";
        linuxClspath = dir + "sequence/LinuxSequenceService";
        macClspath = dir + "sequence/MacSequenceService";
        solarisClspath = dir + "sequence/SolarisSequenceService";
        securityClspath = dir + "SecurityService";
    }

    private static void createKey(String keyName) {
        File file = new File(keyName);
        if (file.exists()) {
            return;
        }
        try {
            // 创建一个可信任的随机数源，DES算法需要
            SecureRandom sr = new SecureRandom();
            // 用DES算法创建一个KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            // 初始化此密钥生成器,使其具有确定的密钥长度
            kg.init(sr);
            // 生成密匙
            SecretKey key = kg.generateKey();
            // 获取密钥数据
            byte rawKeyData[] = key.getEncoded();
            try (FileOutputStream fo = new FileOutputStream(file)) {
                fo.write(rawKeyData);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            LOG.error("创建私钥失败", e);
        }
    }

    private static void encrypt(String keyFile, String classFile, String newClassFile) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            byte[] rawKeyData;
            try (FileInputStream fi = new FileInputStream(new File(keyFile))) {
                rawKeyData = readAll(fi);
            }
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            byte[] data;
            try (FileInputStream fi2 = new FileInputStream(new File(classFile))) {
                data = readAll(fi2);
            }
            // 正式执行加密操作
            byte encryptedData[] = cipher.doFinal(data);
            // 用加密后的数据覆盖原文件
            File file = new File(newClassFile);
            file.createNewFile();
            try (FileOutputStream fo = new FileOutputStream(file)) {
                fo.write(encryptedData);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            LOG.error("加密失败", e);
        }
    }

    public static void main(String args[]) throws Exception {
        createKey(sequenceKeyName);
        createKey(securityKeyName);
        encrypt(sequenceKeyName, workDir + "target/classes/org/jrplat/module/security/service/sequence/WindowsSequenceService.class", winClspath);
        encrypt(sequenceKeyName, workDir + "target/classes/org/jrplat/module/security/service/sequence/LinuxSequenceService.class", linuxClspath);
        encrypt(sequenceKeyName, workDir + "target/classes/org/jrplat/module/security/service/sequence/MacSequenceService.class", macClspath);
        encrypt(sequenceKeyName, workDir + "target/classes/org/jrplat/module/security/service/sequence/SolarisSequenceService.class", solarisClspath);
        encrypt(securityKeyName, workDir + "target/classes/org/jrplat/module/security/service/SecurityService.class", securityClspath);
    }

    private static byte[] readAll(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            for (int n; (n = in.read(buffer)) > 0; ) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            LOG.error("读取数据失败", e);
        }
        return out.toByteArray();
    }
}