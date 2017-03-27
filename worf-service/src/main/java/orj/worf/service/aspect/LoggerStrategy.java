package orj.worf.service.aspect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import orj.worf.core.builder.ToStringBuilder;
import orj.worf.core.builder.ToStringStyle;
import orj.worf.core.dto.PageResponseDTO;
import orj.worf.core.dto.ResponseDTO;

abstract class LoggerStrategy implements Runnable {
    abstract void log();

    public void run() {
        log();
    }

    protected Object[] getArgs(Object[] args) {
        if ((args == null) || (args.length == 0)) {
            return new Object[0];
        }
        List list = new ArrayList(args.length);
        for (Object obj : args) {
            if ((obj instanceof ServletRequest) || (obj instanceof ServletResponse))
                continue;
            if (obj instanceof HttpSession) {
                continue;
            }
            list.add(reflection(obj));
        }
        return list.toArray();
    }

    protected Object reflection(Object obj) {
        if (obj instanceof Collection) {
            return collectionSize((Collection) obj);
        }
        if ((obj == null) || (obj.getClass().getClassLoader() == null)) {
            return obj;
        }
        if ((obj instanceof ResponseDTO) && (((ResponseDTO) obj).getData() instanceof Collection)) {
            ResponseDTO result = (ResponseDTO) obj;
            ResponseDTO copyOf = new ResponseDTO();
            copyOf.setStatus(result.getStatus());
            copyOf.setErrorCode(result.getErrorCode());
            copyOf.setErrorDetails(result.getErrorDetails());
            copyOf.setData(collectionSize((Collection) result.getData()));
            return copyOf;
        }
        if (obj instanceof PageResponseDTO) {
            return obj.getClass().getName() + " [totalRow:" + ((PageResponseDTO) obj).getTotalRow() + "]";
        }
        return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
    }

    private String collectionSize(Collection<?> collection) {
        return collection.getClass().getName() + " [size:" + collection.size() + "]";
    }
}