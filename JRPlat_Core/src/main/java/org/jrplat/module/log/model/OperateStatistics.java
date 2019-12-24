/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.log.model;

/**
 *
 * @author 西安捷然
 */
public class OperateStatistics {
    private String username;
    private int addCount;
    private int deleteCount;
    private int updateCount;

    public void increaseAddCount() {
        addCount++;
    }

    public void increaseDeleteCount() {
        deleteCount++;
    }

    public void increaseUpdateCount() {
        updateCount++;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int addCount) {
        this.addCount = addCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}