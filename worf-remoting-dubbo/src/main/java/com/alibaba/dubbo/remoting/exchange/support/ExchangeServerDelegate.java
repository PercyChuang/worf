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
package com.alibaba.dubbo.remoting.exchange.support;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeChannel;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;

/**
 * ExchangeServerDelegate
 *
 * @author william.liangf
 */
public class ExchangeServerDelegate implements ExchangeServer {

    private transient ExchangeServer server;

    public ExchangeServerDelegate() {
    }

    public ExchangeServerDelegate(final ExchangeServer server) {
        setServer(server);
    }

    public ExchangeServer getServer() {
        return server;
    }

    public void setServer(final ExchangeServer server) {
        this.server = server;
    }

    @Override
    public boolean isBound() {
        return server.isBound();
    }

    @Override
    public void reset(final URL url) {
        server.reset(url);
    }

    @Override
    @Deprecated
    public void reset(final com.alibaba.dubbo.common.Parameters parameters) {
        reset(getUrl().addParameters(parameters.getParameters()));
    }

    @Override
    public Collection<Channel> getChannels() {
        return server.getChannels();
    }

    @Override
    public Channel getChannel(final InetSocketAddress remoteAddress) {
        return server.getChannel(remoteAddress);
    }

    @Override
    public URL getUrl() {
        return server.getUrl();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return server.getChannelHandler();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return server.getLocalAddress();
    }

    @Override
    public void send(final Object message) throws RemotingException {
        server.send(message);
    }

    @Override
    public void send(final Object message, final boolean sent) throws RemotingException {
        server.send(message, sent);
    }

    @Override
    public void close() {
        server.close();
    }

    @Override
    public boolean isClosed() {
        return server.isClosed();
    }

    @Override
    public Collection<ExchangeChannel> getExchangeChannels() {
        return server.getExchangeChannels();
    }

    @Override
    public ExchangeChannel getExchangeChannel(final InetSocketAddress remoteAddress) {
        return server.getExchangeChannel(remoteAddress);
    }

    @Override
    public void close(final int timeout) {
        server.close(timeout);
    }

}