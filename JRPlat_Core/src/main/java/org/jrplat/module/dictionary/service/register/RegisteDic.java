/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.service.register;

import org.jrplat.module.dictionary.model.Dic;
import org.jrplat.module.dictionary.service.DicParser;
import org.jrplat.module.dictionary.service.DicService;
import org.jrplat.module.system.service.RegisterService;
import org.jrplat.platform.result.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *数据字典注册服务
 * @author 西安捷然
 */
@Service
public class RegisteDic extends RegisterService<Dic> {
    private List<Dic> data;
    @Resource(name = "dicService")
    private DicService dicService;

    private Dic rootDic;

    /**
     * 每一次启动的时候都要检查所有的数据字典是否已经注册
     * @return
     */
    @Override
    protected boolean shouldRegister() {
        return true;
    }

    @Override
    public void registe() {
        data = new ArrayList<>();
        List<Dic> dics = DicParser.getDics();
        for (Dic dic : dics) {
            registeDic(dic);
        }
    }

    private void registeDic(Dic dic) {
        Page<Dic> page = serviceFacade.query(Dic.class);
        if (page.getTotalRecords() == 0) {
            LOG.info("第一次注册第一个数据字典(First time to register the first data dictionary): " + dic.getChinese());
            serviceFacade.create(dic);
            //保存根数据字典
            rootDic = dic;
            data.add(dic);
        } else {
            LOG.info("以前已经注册过数据字典(Already registered the data dictionary)");
            LOG.info("查找出根数据字典(Find out the root data dictionary)");
            Dic root = null;
            Dic existRoot = dicService.getRootDic();
            if (existRoot != null) {
                root = existRoot;
                LOG.info("找到以前导入的根数据字典(Find out previous imported root data dictionary): " + root.getChinese());
            } else {
                LOG.info("没有找到以前导入的根数据字典(Failed to find out previous imported root data dictionary)");
                if (rootDic != null) {
                    LOG.info("使用本次导入的根数据字典(Use the import root data dictionary)");
                    root = rootDic;
                }
            }
            if (root != null) {
                LOG.info("将第一次以后的数据字典的根数据字典设置为第一次注册的根数据字典(Set the later data dictionary to the first registered root data dictionary)");
                for (Dic subDic : dic.getSubDics()) {
                    if (hasRegisteDic(subDic)) {
                        LOG.info("数据字典(This data dictionary) " + subDic.getChinese() + " (already registered)在此前已经被注册过，此次忽略，检查其子数据字典");
                        registeSubDic(subDic);
                        continue;
                    }
                    subDic.setParentDic(root);
                    LOG.info("注册后续数据字典(Register the follow-up data dictionary): " + subDic.getChinese());
                    serviceFacade.create(subDic);
                    data.add(subDic);
                }
            } else {
                LOG.info("没有找到根数据字典，注册失败！(Not find out root data dictionary, failed to register)");
            }
        }
    }

    private void registeSubDic(Dic dic) {
        for (Dic sub : dic.getSubDics()) {
            if (hasRegisteDic(sub)) {
                LOG.info("数据字典(Data dictionary) " + sub.getChinese() + " (already registered, checking other sub-data dictionary)在此前已经被注册过，此次忽略，检查其子数据字典");
                registeSubDic(sub);
            } else {
                LOG.info("注册后续数据字典(Register follow-up data dictionary): " + sub.getChinese());
                //重新从数据库中查询出数据字典，参数中的数据字典是从XML中解析出来的
                sub.setParentDic(dicService.getDic(dic.getEnglish()));
                serviceFacade.create(sub);
                data.add(sub);
            }
        }
    }

    private boolean hasRegisteDic(Dic dic) {
        Dic existsDic = dicService.getDic(dic.getEnglish());
        if (existsDic == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<Dic> getRegisteData() {
        return data;
    }
}