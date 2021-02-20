package com.nowcoder.community.a_entity;

//独立瞎凑工具类，不经edsc过程，只e
public class Page{

    /*2后补成员变量*/
    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 就是"/index.html"死地址
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /*1 先写自己要啥*/

    //分页按钮 from-12[3]45-to [背lj]
    public int getFrom() {
        int fir = current-2;
        return fir<1?1:fir;//?√:x
    }
    public int getTo(){
        int last = current+2;
        //需要总页数，写一个。。
        return last>getTotal()?getTotal():last;
    }
    //总页数
    public int getTotal() {
        return rows%limit==0?rows/limit:rows/limit+1;//注意前%后/
    }

    //每页起始行-核[b]
    public int getOffset() {
        return (current-1)*limit;
    }
}
