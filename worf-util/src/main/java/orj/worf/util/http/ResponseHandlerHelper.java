package orj.worf.util.http;

import java.io.IOException;
import java.nio.charset.Charset;

import orj.worf.util.Charsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

/**
 * 处理HTTP响应的工具类
 */
class ResponseHandlerHelper {

    public static HttpEntity handleNon2xxStatus(final HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            Charset responseCharset = ResponseHandlerHelper.getResponseCharset(entity);
            String result = EntityUtils.toString(entity, responseCharset);
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase() + ':' + result);
        }
        return entity;
    }

    /**
     * 从HTTP响应中尝试获取charset
     */
    public static Charset getResponseCharset(final HttpEntity entity) {
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = Charsets.UTF_8;
        }
        return charset;
    }

}
