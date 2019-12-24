/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 * 指定需要的数据是哪一页，页面大小是多少
 * 默认为第一页，每页大小为10
 * @author 西安捷然
 *
 */
public class PageCriteria {
    private int page = 1;
    private int size = 20;

    public PageCriteria() {
        super();
    }

    public PageCriteria(int page, int size) {
        super();
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}