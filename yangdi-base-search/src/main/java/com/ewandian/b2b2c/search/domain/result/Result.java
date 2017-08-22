package com.ewandian.b2b2c.search.domain.result;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;

/**
 * Created by suhd on 2016-11-30.
 */
public class Result {
    private Status status;
    private String message;
    private Object data;
    private long page;
    private long pageCount;
    private long totalPage;
    private long total;

    public Status getStatus() {
        return status;
    }

    public Result() {
    }

    public Result(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(Status status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    public Result(Status status, String message, Object data, long total) {
        this(status, message, data);
        this.total = total;
    }

    public Result(Status status, String message, Object data, long total, long totalPage, long page, long pageCount) {
        this(status, message, data, total);
        this.totalPage = totalPage;
        this.page = page;
        this.pageCount = pageCount;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
