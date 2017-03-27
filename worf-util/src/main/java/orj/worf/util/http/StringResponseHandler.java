package orj.worf.util.http;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * 响应String类型的数据结果
 */
class StringResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
        HttpEntity entity = null;
        try {
            entity = ResponseHandlerHelper.handleNon2xxStatus(response);
            Charset responseCharset = ResponseHandlerHelper.getResponseCharset(entity);
            return EntityUtils.toString(entity, responseCharset);
        } finally {
            EntityUtils.consume(entity);
        }
    }

}
