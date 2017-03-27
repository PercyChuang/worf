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
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;

/**
 * ChannelDelegate
 *
 * @author william.liangf
 */
public class ChannelDelegate implements Channel {

    private transient Channel channel;

    public ChannelDelegate() {
    }

    public ChannelDelegate(final Channel channel) {
        setChannel(channel);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel == null");
        }
        this.channel = channel;
    }

    @Override
    public URL getUrl() {
        return channel.getUrl();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channel.getChannelHandler();
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public boolean hasAttribute(final String key) {
        return channel.hasAttribute(key);
    }

    @Override
    public void send(final Object message) throws RemotingException {
        channel.send(message);
    }

    @Override
    public Object getAttribute(final String key) {
        return channel.getAttribute(key);
    }

    @Override
    public void setAttribute(final String key, final Object value) {
        channel.setAttribute(key, value);
    }

    @Override
    public void send(final Object message, final boolean sent) throws RemotingException {
        channel.send(message, sent);
    }

    @Override
    public void removeAttribute(final String key) {
        channel.removeAttribute(key);
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void close(final int timeout) {
        channel.close(timeout);
    }

    @Override
    public boolean isClosed() {
        return channel.isClosed();
    }

}