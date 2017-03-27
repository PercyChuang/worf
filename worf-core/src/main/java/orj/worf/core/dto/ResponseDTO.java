package orj.worf.core.dto;

import orj.worf.core.model.BaseObject;

public class ResponseDTO<E> extends BaseObject {
    private static final long serialVersionUID = 9122633747274068561L;
    /**
     * 响应数据
     */
    private E data;
    /**
     * 操作结果
     */
    private int status;
    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误详情
     */
    private String errorDetails;

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(final String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public E getData() {
        return data;
    }

    public void setData(final E data) {
        this.data = data;
    }
}
