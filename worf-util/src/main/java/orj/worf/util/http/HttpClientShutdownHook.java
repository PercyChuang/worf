package orj.worf.util.http;

/**
 * 确保在容器进程在非正常关闭时能执行正常的容器销毁
 */
class HttpClientShutdownHook extends Thread {
    private final HttpClient httpClient;

    public HttpClientShutdownHook(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void run() {
        if (httpClient != null) {
            httpClient.close();
        }
    }

}
