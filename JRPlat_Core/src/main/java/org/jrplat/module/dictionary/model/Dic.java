/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jrplat.platform.annotation.Database;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.annotation.ModelAttrRef;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *数据字典
 * @author 西安捷然
 */
@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "Dic")

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Database
public class Dic extends SimpleModel {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentDic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected List<Dic> subDics = new ArrayList<>();
    //不对字典项集合进行缓存，因为当字典项改变导致缓存失效后，字典的缓存没有失效
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dic")
    @OrderBy("orderNum ASC")
    protected List<DicItem> dicItems = new ArrayList<>();
    @ManyToOne
    @ModelAttr("父字典")
    @ModelAttrRef("chinese")
    protected Dic parentDic;
    @ModelAttr("英文名称")
    protected String english;
    @ModelAttr("中文名称")
    protected String chinese;
    //当此属性为真时，此词典和模型没有建立关联关系，而只是把此词典的编码值作为字符串赋值给模型
    @ModelAttr("伪词典")
    protected boolean justCode;

    public static void main(String[] args) {
        Dic obj = new Dic();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    @XmlAttribute
    public boolean isJustCode() {
        return justCode;
    }

    public void setJustCode(boolean justCode) {
        this.justCode = justCode;
    }

    @XmlAttribute
    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @XmlAttribute
    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    @XmlTransient
    public Dic getParentDic() {
        return parentDic;
    }

    public void setParentDic(Dic parentDic) {
        this.parentDic = parentDic;
    }

    @XmlElementWrapper(name = "dicItems")
    @XmlElement(name = "dicItem")
    public List<DicItem> getDicItems() {
        return dicItems;
    }

    public void addDicItem(DicItem dicItem) {
        this.dicItems.add(dicItem);
    }

    public void removeDicItem(DicItem dicItem) {
        this.dicItems.remove(dicItem);
    }

    @XmlElementWrapper(name = "subDics")
    @XmlElement(name = "dic")
    public List<Dic> getSubDics() {
        return subDics;
    }

    public void addSubDics(Dic subDic) {
        this.subDics.add(subDic);
    }

    public void removeSubDics(Dic subDic) {
        this.subDics.remove(subDic);
    }

    @Override
    public String getMetaData() {
        return "数据字典";
    }
}