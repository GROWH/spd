package org.jrplat.module.config.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.config.model.Gridconfig;
import org.jrplat.module.config.service.GridconfigService;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 赵腾飞 on 2016/10/12.
 * <p>
 * 表格设置 action
 */
@Scope("prototype")
@Controller
@Namespace("/extgridconfig")
public class GridconfigAction extends ExtJSSimpleAction<Gridconfig> {

    private String gridSetting;//表格设置
    private String whichGrid;//哪个表格
    private Integer size;//分页大小
    @Autowired
    private GridconfigService gridconfigService;

    /**
     * 保存表格设置
     *
     * @return
     */
    public String save() {
        User user = UserHolder.getCurrentLoginUser();
        String jpql = "select o from Gridconfig o where o.ownerUser.id = :id and o.whichGrid = :whichGrid";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("id", user.getId());
        query.setParameter("whichGrid", whichGrid);
        List<Gridconfig> gridConfigList = query.getResultList();
        if (gridConfigList.size() == 1) {
            model = gridConfigList.get(0);
            model.setGridSetting(gridSetting);
            model.setSize(size);
            getService().update(model);
            return null;
        } else {
            model.setGridSetting(gridSetting);
            model.setWhichGrid(whichGrid);
            model.setSize(size);
            getService().create(model);
        }
        return null;
    }

    /**
     * 表格配置清除
     *
     * @return
     */
    public String clean() {
        try {
            gridconfigService.clean(whichGrid);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 查询表格设置
     *
     * @return
     */
    @Override
    public String query() {
        User user = UserHolder.getCurrentLoginUser();
        String jpql = "select o from Gridconfig o where o.ownerUser.id = :id and o.whichGrid = :whichGrid";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("id", user.getId());
        query.setParameter("whichGrid", whichGrid);
        List<Gridconfig> gridConfigList = query.getResultList();
        if (!gridConfigList.isEmpty()) {
            model = gridConfigList.get(0);
            Map<String, Object> temp = new HashMap();
            renderJsonForRetrieve(temp);
            Struts2Utils.renderJson(temp);
        } else {
            Struts2Utils.renderText("");
        }
        return null;
    }

    public String getGridSetting() {
        return gridSetting;
    }

    public void setGridSetting(String gridSetting) {
        this.gridSetting = gridSetting;
    }

    public String getWhichGrid() {
        return whichGrid;
    }

    public void setWhichGrid(String whichGrid) {
        this.whichGrid = whichGrid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
