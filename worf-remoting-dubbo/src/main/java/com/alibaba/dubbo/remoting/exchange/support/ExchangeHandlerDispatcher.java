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

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeChannel;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.telnet.TelnetHandler;
import com.alibaba.dubbo.remoting.telnet.support.TelnetHandlerAdapter;
import com.alibaba.dubbo.remoting.transport.ChannelHandlerDispatcher;

/**
 * ExchangeHandlerDispatcher
 *
 * @author william.liangf
 */
public class ExchangeHandlerDispatcher implements ExchangeHandler {

    private final ReplierDispatcher replierDispatcher;

    private final ChannelHandlerDispatcher handlerDispatcher;

    private final TelnetHandler telnetHandler;

    public ExchangeHandlerDispatcher() {
        replierDispatcher = new ReplierDispatcher();
        handlerDispatcher = new ChannelHandlerDispatcher();
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(final Replier<?> replier) {
        replierDispatcher = new ReplierDispatcher(replier);
        handlerDispatcher = new ChannelHandlerDispatcher();
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(final ChannelHandler... handlers) {
        replierDispatcher = new ReplierDispatcher();
        handlerDispatcher = new ChannelHandlerDispatcher(handlers);
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(final Replier<?> replier, final ChannelHandler... handlers) {
        replierDispatcher = new ReplierDispatcher(replier);
        handlerDispatcher = new ChannelHandlerDispatcher(handlers);
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher addChannelHandler(final ChannelHandler handler) {
        handlerDispatcher.addChannelHandler(handler);
        return this;
    }

    public ExchangeHandlerDispatcher removeChannelHandler(final ChannelHandler handler) {
        handlerDispatcher.removeChannelHandler(handler);
        return this;
    }

    public <T> ExchangeHandlerDispatcher addReplier(final Class<T> type, final Replier<T> replier) {
        replierDispatcher.addReplier(type, replier);
        return this;
    }

    public <T> ExchangeHandlerDispatcher removeReplier(final Class<T> type) {
        replierDispatcher.removeReplier(type);
        return this;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object reply(final ExchangeChannel channel, final Object request) throws RemotingException {
        return ((Replier) replierDispatcher).reply(channel, request);
    }

    @Override
    public void connected(final Channel channel) {
        handlerDispatcher.connected(channel);
    }

    @Override
    public void disconnected(final Channel channel) {
        handlerDispatcher.disconnected(channel);
    }

    @Override
    public void sent(final Channel channel, final Object message) {
        handlerDispatcher.sent(channel, message);
    }

    @Override
    public void received(final Channel channel, final Object message) {
        handlerDispatcher.received(channel, message);
    }

    @Override
    public void caught(final Channel channel, final Throwable exception) {
        handlerDispatcher.caught(channel, exception);
    }

    @Override
    public String telnet(final Channel channel, final String message) throws RemotingException {
        return telnetHandler.telnet(channel, message);
    }

}