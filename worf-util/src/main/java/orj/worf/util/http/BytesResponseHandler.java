package orj.worf.util.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 *响应内容全部为json,可直接返回byte数组,但是需要注意转成string时的字符集是请求时传给服务器端的字符集
 */
class BytesResponseHandler implements ResponseHandler<byte[]> {

    @Override
    public byte[] handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
        HttpEntity entity = null;
        try {
            entity = ResponseHandlerHelper.handleNon2xxStatus(response);
            return EntityUtils.toByteArray(entity);
        } finally {
            EntityUtils.consume(entity);
        }
    }

}
