/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.model;

import org.jrplat.module.security.model.User;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.annotation.ModelAttrRef;

import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

/**
 *
 * 继承这个类的模型必须和User模型存放在同一个数据库中
 *
 * @author 西安捷然
 */
@MappedSuperclass
@Table(indexes = {@Index(columnList = "ownerUser")})
public abstract class SimpleModel extends Model {

    @ManyToOne
    //数据所有者可以按照用户名，姓名，所属org名称来查询
//	@SupportLikeQuery("ownerUser.org.orgName;ownerUser.username;ownerUser.realName")
    @ModelAttr("数据所有者名称")
    @ModelAttrRef("username")
    protected User ownerUser;

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        if (this.ownerUser == null) {
            this.ownerUser = ownerUser;
        } else {
            LOG.info("忽略设置OwnerUser");
        }
    }
}