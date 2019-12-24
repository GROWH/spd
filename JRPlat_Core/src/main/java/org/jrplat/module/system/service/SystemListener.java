/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service;

import org.jrplat.module.dictionary.generator.DictionaryGenerator;
import org.jrplat.module.monitor.model.RuningTime;
import org.jrplat.module.monitor.service.MemoryMonitorThread;
import org.jrplat.module.security.service.UserLoginListener;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FileUtils;
import org.jrplat.platform.util.ZipUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

/**
 * 系统启动和关闭的监听器,由Spring来调用
 * @author 西安捷然
 *
 */
public class SystemListener {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(SystemListener.class);
    private static final boolean memoryMonitor;
    private static final boolean runingMonitor;
    private static boolean running = false;
    private static String basePath;
    private static String contextPath;
    private static RuningTime runingTime = null;
    private static MemoryMonitorThread memoryMonitorThread;

    static {
        memoryMonitor = PropertyHolder.getBooleanProperty("monitor.memory");
        if (memoryMonitor) {
            LOG.info("启用内存监视日志");
            LOG.info("Enable memory monitor log", Locale.ENGLISH);
        } else {
            LOG.info("禁用内存监视日志");
            LOG.info("Disable memory monitor log", Locale.ENGLISH);
        }
        runingMonitor = PropertyHolder.getBooleanProperty("monitor.runing");
        if (runingMonitor) {
            LOG.info("启用系统运行日志");
            LOG.info("Enable system log", Locale.ENGLISH);
        } else {
            LOG.info("禁用系统运行日志");
            LOG.info("Disable system log", Locale.ENGLISH);
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void prepareForSpring() {
        //供spring扫描组件用
        String basePackage = PropertyHolder.getProperty("basePackages");
        String localBasePackage = PropertyHolder.getProperty("basePackages.local");
        if (localBasePackage != null && !"".equals(localBasePackage.trim())) {
            basePackage = basePackage + "," + localBasePackage;
        }
        System.setProperty("basePackage", basePackage);
    }

    public static void contextInitialized(ServletContextEvent sce) {
        contextPath = sce.getServletContext().getContextPath();
        LOG.info("启动【" + PropertyHolder.getProperty("app.name") + "】");
        LOG.info("Launch【" + PropertyHolder.getProperty("app.name") + "】", Locale.ENGLISH);
        LOG.info("应用上下文:" + contextPath);
        LOG.info("App context:" + contextPath, Locale.ENGLISH);
        ServletContext sc = sce.getServletContext();
        basePath = sc.getRealPath("/");
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separator;
        }
        //整个系统中的文件操作都以basePath为基础
        FileUtils.setBasePath(basePath);
        LOG.info("basePath:" + basePath);
        String userDir = System.getProperty("user.dir");
        LOG.info("user.dir:" + userDir);
        userDir = FileUtils.getAbsolutePath("/WEB-INF/classes/data/");
        System.setProperty("user.dir", userDir);
        LOG.info("将user.dir重新设置为:" + userDir);
        LOG.info("Reset user directory:" + userDir, Locale.ENGLISH);

        String encoding = System.getProperty("file.encoding");
        LOG.info("你的操作系统所用的编码file.encoding：" + encoding);
        LOG.info("Encoding of your OS is file.encoding：" + encoding, Locale.ENGLISH);

        LOG.info("启动目录监控线程");
        WatchDirectory.startWatch(basePath);

        //lib目录中去掉多余的JDBC驱动
        DatabaseDriverChecker.check();

        //为spring的配置做预处理
        prepareForSpring();
        //注册模块
        registerModules();
        //解析所有的dic.xml文件，并生成供客户端EXT JS调用的文件
        DictionaryGenerator.generateDic(basePath);

        if (runingMonitor) {
            LOG.info("记录服务器启动日志");
            LOG.info("Recording the server boot logging", Locale.ENGLISH);
            runingTime = new RuningTime();
            try {
                runingTime.setServerIP(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                LOG.error("记录服务器启动日志出错", e);
                LOG.error("Failed to record the server boot logging", e, Locale.ENGLISH);
            }
            runingTime.setAppName(contextPath);
            runingTime.setOsName(System.getProperty("os.name"));
            runingTime.setOsVersion(System.getProperty("os.version"));
            runingTime.setOsArch(System.getProperty("os.arch"));
            runingTime.setJvmName(System.getProperty("java.vm.name"));
            runingTime.setJvmVersion(System.getProperty("java.vm.version"));
            runingTime.setJvmVendor(System.getProperty("java.vm.vendor"));
            runingTime.setStartupTime(new Date());
            //保存服务器启动日志
            BufferLogCollector.collect(runingTime);
        }
        if (memoryMonitor) {
            LOG.info("启动内存监视线程");
            LOG.info("Enable memory monitor thread", Locale.ENGLISH);
            int circle = PropertyHolder.getIntProperty("monitor.memory.circle");
            memoryMonitorThread = new MemoryMonitorThread(circle);
            memoryMonitorThread.start();
        }
        running = true;
    }

    public static void contextDestroyed(ServletContextEvent sce) {
        UserLoginListener.forceAllUserOffline();

        if (runingMonitor) {
            LOG.info("记录服务器关闭日志");
            LOG.info("Recording the server shutdown logging", Locale.ENGLISH);
            runingTime.setShutdownTime(new Date());
            runingTime.setRuningTime(runingTime.getShutdownTime().getTime() - runingTime.getStartupTime().getTime());
            //保存服务器关闭日志
            BufferLogCollector.collect(runingTime);
        }
        if (memoryMonitor) {
            LOG.info("停止内存监视线程");
            LOG.info("Stop memory monitor thread", Locale.ENGLISH);
            memoryMonitorThread.running = false;
            memoryMonitorThread.interrupt();
        }

        //在关闭系统之前，处理缓冲区中的日志
        BufferLogCollector.close();

        deregisterDrivers();
        LOG.info("卸载JDBC驱动");
        LOG.info("Uninstalled JDBC driver", Locale.ENGLISH);

        LOG.info("停止目录监控线程");
        WatchDirectory.stopWatch();
    }

    public static String getContextPath() {
        return contextPath;
    }

    private static void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOG.warn("卸载JDBC驱动失败：" + driver, e);
                LOG.warn("Fail to uninstall JDBC driver：" + driver, e, Locale.ENGLISH);
            }
        }
    }

    private static void registerModules() {
        StringBuilder modules = new StringBuilder();
        try {
            Enumeration<URL> ps = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/module.xml");
            while (ps.hasMoreElements()) {
                URL url = ps.nextElement();
                String file = url.getFile();
                if (file.contains(".jar!")) {
                    int start = file.indexOf("WEB-INF/lib/");
                    int end = file.indexOf("!/META-INF/services/module.xml");
                    if (start == -1 || end == -1) {
                        continue;
                    }
                    String jar = file.substring(start, end);
                    modules.append(jar).append(",");
                    LOG.info("注册模块：" + jar);
                    LOG.info("Register module：" + jar, Locale.ENGLISH);
                    extractWebFromModule(jar);
                    extractDataFromModule(jar);
                } else {
                    LOG.warn("在非jar包中找到META-INF/services/module.xml");
                    LOG.warn("Find META-INF/services/module.xml in non-jar", Locale.ENGLISH);
                }
            }
        } catch (IOException e) {
            LOG.error("注册模块出错", e);
            LOG.error("Failed to register module", e, Locale.ENGLISH);
        }
        if (modules.length() > 0) {
            modules = modules.deleteCharAt(modules.length() - 1);
        }
        //从配置文件中获取属性
        String scanJars = PropertyHolder.getProperty("scan.jars");
        LOG.info("注册模块前，scanJars: " + scanJars);
        LOG.info("Before register，scanJars: " + scanJars, Locale.ENGLISH);
        if (scanJars != null && !"".equals(scanJars.trim())) {
            scanJars = scanJars + "," + modules.toString();
        } else {
            scanJars = modules.toString();
        }
        //设置到系统属性中
        //spring会把系统属性中的配置覆盖掉配置文件中的配置
        //<property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE" />
        System.setProperty("scan.jars", scanJars);
        //设置回配置属性
        PropertyHolder.setProperty("scan.jars", scanJars);
        LOG.info("注册模块后，scanJars: " + scanJars);
        LOG.info("After register，scanJars: " + scanJars, Locale.ENGLISH);
    }

    private static void extractWebFromModule(String jar) {
        LOG.info("从模块：" + jar + " 中提取web资源");
        LOG.info("Extract web resource from：" + jar, Locale.ENGLISH);
        String loc = FileUtils.getAbsolutePath("/");
        jar = FileUtils.getAbsolutePath(jar);
        ZipUtils.unZip(jar, "web", loc, true);
        LOG.info("jar：" + jar);
        LOG.info("loc：" + loc);
    }

    private static void extractDataFromModule(String jar) {
        LOG.info("从模块：" + jar + " 中提取数据");
        LOG.info("Extract data from：" + jar, Locale.ENGLISH);
        String loc = FileUtils.getAbsolutePath("/WEB-INF/classes/data/");
        jar = FileUtils.getAbsolutePath(jar);
        ZipUtils.unZip(jar, "data/init", loc, true);
        LOG.info("jar：" + jar);
        LOG.info("loc：" + loc);
    }
}