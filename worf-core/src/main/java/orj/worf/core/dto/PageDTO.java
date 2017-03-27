package orj.worf.core.dto;

import java.util.ArrayList;
import java.util.List;

import orj.worf.core.model.BaseObject;

public class PageDTO<T, S> extends BaseObject {

    private static final long serialVersionUID = -3117352321619838300L;
    /**
     * 参数对象
     */
    private S argument;
    /**
     * 分页数据
     */
    private List<T> data;
    /**
     * 总行数
     */
    private int totalRow;
    /**
     * 当前页号
     */
    private int pageNum;
    /**
     * 每页记录条数
     */
    private int pageSize;

    public S getArgument() {
        return argument;
    }

    public void setArgument(final S argument) {
        this.argument = argument;
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<T>(0);
        }
        return data;
    }

    public void setData(final List<T> data) {
        this.data = data;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(final int totalRow) {
        this.totalRow = totalRow;
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