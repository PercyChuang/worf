package orj.worf.core.dto;

import java.util.ArrayList;
import java.util.List;

import orj.worf.core.model.BaseObject;

public class PageResponseDTO<T> extends BaseObject {

    private static final long serialVersionUID = 3571606415879693166L;
    /**
     * 分页数据
     */
    private List<T> data;
    /**
     * 总行数
     */
    private int totalRow;

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

}