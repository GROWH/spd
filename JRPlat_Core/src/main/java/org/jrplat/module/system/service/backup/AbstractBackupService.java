/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FileUtils;

import java.io.File;
import java.util.*;

/**
 *备份恢复数据库抽象类，抽象出了针对各个数据库来说通用的功能
 * @author 西安捷然
 */
public abstract class AbstractBackupService implements BackupService {
    protected static final StandardPBEStringEncryptor encryptor;
    protected static final String username;
    protected static final String password;

    //从配置文件中获取数据库用户名和密码，如果用户名和密码被加密，则解密
    static {
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("config");

        encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        String uname = PropertyHolder.getProperty("db.username");
        String pwd = PropertyHolder.getProperty("db.password");
        if (uname != null && uname.contains("ENC(") && uname.contains(")")) {
            uname = uname.substring(4, uname.length() - 1);
            username = decrypt(uname);
        } else {
            username = uname;
        }
        if (pwd != null && pwd.contains("ENC(") && pwd.contains(")")) {
            pwd = pwd.substring(4, pwd.length() - 1);
            password = decrypt(pwd);
        } else {
            password = pwd;
        }
    }

    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    /**
     * 解密用户名和密码
     *
     * @param encryptedMessage 加密后的用户名或密码
     * @return 解密后的用户名或密码
     */
    protected static String decrypt(String encryptedMessage) {
        String plain = encryptor.decrypt(encryptedMessage);
        return plain;
    }

    @Override
    public String getBackupFilePath() {
        String path = "/WEB-INF/backup/" + PropertyHolder.getProperty("jpa.database") + "/";
        path = FileUtils.getAbsolutePath(path);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    @Override
    public File getNewestBackupFile() {
        Map<String, File> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        String path = getBackupFilePath();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            String name = file.getName();
            if (!name.contains("bak")) {
                continue;
            }
            map.put(name, file);
            list.add(name);
        }
        if (list.isEmpty()) {
            return null;
        }
        //按备份时间排序
        Collections.sort(list);
        //最新备份的在最前面
        Collections.reverse(list);

        String name = list.get(0);
        File file = map.get(name);
        //加速垃圾回收
        list.clear();
        map.clear();

        return file;
    }

    @Override
    public List<String> getExistBackupFileNames() {
        List<String> result = new ArrayList<>();
        String path = getBackupFilePath();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            String name = file.getName();
            if (!name.contains("bak")) {
                continue;
            }
            name = name.substring(0, name.length() - 4);
            String[] temp = name.split("-");
            String y = temp[0];
            String m = temp[1];
            String d = temp[2];
            String h = temp[3];
            String mm = temp[4];
            String s = temp[5];
            name = y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + s;
            result.add(name);
        }
        //按备份时间排序
        Collections.sort(result);
        //最新备份的在最前面
        Collections.reverse(result);

        return result;
    }
}