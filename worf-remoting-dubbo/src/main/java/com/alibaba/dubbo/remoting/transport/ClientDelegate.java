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
package com.alibaba.dubbo.remoting.transport;

import java.net.InetSocketAddress;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;

/**
 * ClientDelegate
 *
 * @author william.liangf
 */
public class ClientDelegate implements Client {

    private transient Client client;

    public ClientDelegate() {
    }

    public ClientDelegate(final Client client) {
        setClient(client);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client == null");
        }
        this.client = client;
    }

    @Override
    public void reset(final URL url) {
        client.reset(url);
    }

    @Override
    @Deprecated
    public void reset(final com.alibaba.dubbo.common.Parameters parameters) {
        reset(getUrl().addParameters(parameters.getParameters()));
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
    public void reconnect() throws RemotingException {
        client.reconnect();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return client.getChannelHandler();
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
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
    public void send(final Object message) throws RemotingException {
        client.send(message);
    }

    @Override
    public Object getAttribute(final String key) {
        return client.getAttribute(key);
    }

    @Override
    public void setAttribute(final String key, final Object value) {
        client.setAttribute(key, value);
    }

    @Override
    public void send(final Object message, final boolean sent) throws RemotingException {
        client.send(message, sent);
    }

    @Override
    public void removeAttribute(final String key) {
        client.removeAttribute(key);
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public void close(final int timeout) {
        client.close(timeout);
    }

    @Override
    public boolean isClosed() {
        return client.isClosed();
    }

}