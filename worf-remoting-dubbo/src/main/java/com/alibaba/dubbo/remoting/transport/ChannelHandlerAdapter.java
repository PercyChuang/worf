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

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;

/**
 * ChannelHandlerAdapter.
 * 
 * @author qian.lei
 */
public class ChannelHandlerAdapter implements ChannelHandler {

    @Override
    public void connected(final Channel channel) throws RemotingException {
    }

    @Override
    public void disconnected(final Channel channel) throws RemotingException {
    }

    @Override
    public void sent(final Channel channel, final Object message) throws RemotingException {
    }

    @Override
    public void received(final Channel channel, final Object message) throws RemotingException {
    }

    @Override
    public void caught(final Channel channel, final Throwable exception) throws RemotingException {
    }

}