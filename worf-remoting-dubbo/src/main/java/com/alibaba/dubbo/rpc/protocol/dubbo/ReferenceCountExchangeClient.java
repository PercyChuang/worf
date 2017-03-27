/*
 * Copyright 1999-2011 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.protocol.dubbo;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.Parameters;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;

/**
 * dubbo protocol support class.
 * 
 * @author chao.liuc
 */
@SuppressWarnings("deprecation")
final class ReferenceCountExchangeClient implements ExchangeClient {

    private ExchangeClient client;

    private final URL url;

    // private final ExchangeHandler handler;

    private final AtomicInteger refenceCount = new AtomicInteger(0);

    private final ConcurrentMap<String, LazyConnectExchangeClient> ghostClientMap;

    public ReferenceCountExchangeClient(final ExchangeClient client,
            final ConcurrentMap<String, LazyConnectExchangeClient> ghostClientMap) {
        this.client = client;
        refenceCount.incrementAndGet();
        this.url = client.getUrl();
        if (ghostClientMap == null) {
            throw new IllegalStateException("ghostClientMap can not be null, url: " + url);
        }
        this.ghostClientMap = ghostClientMap;
    }

    @Override
    public void reset(final URL url) {
        client.reset(url);
    }

    @Override
    public ResponseFuture request(final Object request) throws RemotingException {
        return client.request(request);
    }

    @Override
    public URL getUrl() {
        return client.getUrl();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return client.getRemoteAddress();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return client.getChannelHandler();
    }

    @Override
    public ResponseFuture request(final Object request, final int timeout) throws RemotingException {
        return client.request(request, timeout);
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public void reconnect() throws RemotingException {
        client.reconnect();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return client.getLocalAddress();
    }

    @Override
    public boolean hasAttribute(final String key) {
        return client.hasAttribute(key);
    }

    @Override
    public void reset(final Parameters parameters) {
        client.reset(parameters);
    }

    @Override
    public void send(final Object message) throws RemotingException {
        client.send(message);
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return client.getExchangeHandler();
    }

    @Override
    public Object getAttribute(final String key) {
        return client.getAttribute(key);
    }

    @Override
    public void send(final Object message, final boolean sent) throws RemotingException {
        client.send(message, sent);
    }

    @Override
    public void setAttribute(final String key, final Object value) {
        client.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(final String key) {
        client.removeAttribute(key);
    }

    /*
     * close方法将不再幂等,调用需要注意.
     */
    @Override
    public void close() {
        close(0);
    }

    @Override
    public void close(final int timeout) {
        if (refenceCount.decrementAndGet() <= 0) {
            if (timeout == 0) {
                client.close();
            } else {
                client.close(timeout);
            }
            client = replaceWithLazyClient();
        }
    }

    // 幽灵client,
    private LazyConnectExchangeClient replaceWithLazyClient() {
        // 这个操作只为了防止程序bug错误关闭client做的防御措施，初始client必须为false状态
        URL lazyUrl = url.addParameter(Constants.LAZY_CONNECT_INITIAL_STATE_KEY, Boolean.FALSE)
                .addParameter(Constants.RECONNECT_KEY, Boolean.FALSE)
                .addParameter(Constants.SEND_RECONNECT_KEY, Boolean.TRUE.toString())
                .addParameter("warning", Boolean.TRUE.toString())
                .addParameter(LazyConnectExchangeClient.REQUEST_WITH_WARNING_KEY, true)
                .addParameter("_client_memo", "referencecounthandler.replacewithlazyclient");

        String key = url.getAddress();
        // 最差情况下只有一个幽灵连接
        LazyConnectExchangeClient gclient = ghostClientMap.get(key);
        if (gclient == null || gclient.isClosed()) {
            gclient = new LazyConnectExchangeClient(lazyUrl, client.getExchangeHandler());
            ghostClientMap.put(key, gclient);
        }
        return gclient;
    }

    @Override
    public boolean isClosed() {
        return client.isClosed();
    }

    public void incrementAndGetCount() {
        refenceCount.incrementAndGet();
    }
}