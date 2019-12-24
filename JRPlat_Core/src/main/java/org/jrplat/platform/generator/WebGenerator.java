/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.generator;


import org.jrplat.module.module.model.Module;
import org.jrplat.module.module.service.ModuleParser;
import org.jrplat.module.module.service.ModuleService;

import java.util.List;
import java.util.Set;

/**
 *
 * @author 西安捷然
 */
public class WebGenerator {
    /**
     * 生成所有模块对应的JSP和JS
     */
    public static void generate() {
        generate(null);
    }

    /**
     * 生成指定模块的JSP和JS
     * 如果没有指定，则生成所有模块对应的JSP和JS
     * @param generateModules 顶级模块英文名称
     */
    public static void generate(Set<String> generateModules) {
        //不会强行覆盖JSP和JS页面，如果待生成的文件存在则会忽略生成
        List<Module> list = ModuleParser.getRootModules();
        System.out.println("--------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------");
        System.out.println("分割模块数:" + list.size());
        System.out.println("--------------------------------------------------------------");
        int i = 1;
        for (Module module : list) {
            System.out.println("--------------------------------------------------------------");
            System.out.println("分割模块" + (i++) + "包含模块数目:" + module.getSubModules().size());
            System.out.println("--------------------------------------------------------------");
            int j = 1;
            for (Module m : module.getSubModules()) {
                if (generateModules != null && !generateModules.contains(m.getEnglish())) {
                    System.out.println("忽略生成模块【" + m.getEnglish() + "】的JSP和JS文件");
                    continue;
                }
                System.out.println("    " + (j++) + ":" + m.getChinese() + "(" + m.getEnglish() + ")");
                generateForModule(m);
            }
            System.out.println("--------------------------------------------------------------");
        }
    }

    private static void generateForModule(Module module) {
        //如果模块不为叶子节点
        if (module.getCommands().isEmpty()) {
            for (Module subModule : module.getSubModules()) {
                generateForModule(subModule);
            }
        } else {
            String path = ModuleService.getModulePath(module.getParentModule());
            System.out.println("        module: " + module.getChinese() + "(" + module.getEnglish() + ")" + ", path: " + path);
            if (module.isDisplay()) {
                JspGenerator.generate(path, module.getEnglish(), module.getChinese());
            } else {
                System.out.println("        模块不显示，不生成页面和JS");
            }
        }

    }
}