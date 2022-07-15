package com.ddsmile.entity;

/**
 * 分页功能
 */
public class Page {
    //当前页码
    private int current = 1; //默认为第一页
    //显示上限
    private int limit = 10;

    //数据总数(用于计算总页数)
    private int rows;
    //查询路径(用于复用分页链接)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        //进行一些判断,免得输入一些不合理的数
        if(current >= 1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //额外的方法

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getoffset(){
        //计算公式为: current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0){  //可以整除
            return rows/limit;
        }else {
            return rows/limit + 1;
        }
    }

    /**
     * 获取展示的起始页码
     * @return
     */
    public int getFrom(){
        int from = current - 2;  //展示前后两页
        //如果前面没有两页,则展示的起始页码为1,有两页及以上,则展示的起始页码为1
        return from < 1 ? 1 :from;
    }

    /**
     * 获取展示的终止页码
     * @return
     */
    public int getTo() {
        int to = current + 2;
        //最后一页
        int total = getTotal();
        //展示的终止页大于总页数,则展示到总页数,否则展示到to
        return to > total ? total : to;
    }
}
