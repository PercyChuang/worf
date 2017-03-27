package orj.worf.core.dto;

import orj.worf.core.model.BaseObject;

public class PageRequestDTO<T> extends BaseObject {

    private static final long serialVersionUID = 4480303445151998440L;
    /**
     * 参数对象
     */
    private T argument;
    /**
     * 当前页号
     */
    private int pageNum;
    /**
     * 每页记录条数
     */
    private int pageSize;

    public T getArgument() {
        return argument;
    }

    public void setArgument(final T argument) {
        this.argument = argument;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(final int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (this.pageNum < 0 ? 0 : this.pageNum) * this.pageSize;
    }

}