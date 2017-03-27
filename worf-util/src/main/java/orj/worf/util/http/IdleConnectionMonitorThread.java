package orj.worf.util.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 空间连接处理器.注意:在关闭系统时,需要调用其shutdown()方法
 *
 */
class IdleConnectionMonitorThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    /**
     * 连接空闲多少秒后被关闭,单位秒
     */
    private int idletime = 30;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(final HttpClientConnectionManager connMgr, final int idletime) {
        super();
        this.connMgr = connMgr;
        this.idletime = idletime;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (!shutdown) {
            synchronized (this) {
                try {
                    wait(10000);
                } catch (InterruptedException e) {
                }
                connMgr.closeExpiredConnections();
                connMgr.closeIdleConnections(idletime, TimeUnit.SECONDS);
            }
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
