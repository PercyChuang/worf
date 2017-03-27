package com.alibaba.dubbo.remoting.transport;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public abstract class AbstractChannelHandlerDelegate implements ChannelHandlerDelegate {

    protected ChannelHandler handler;

    protected AbstractChannelHandlerDelegate(final ChannelHandler handler) {
        Assert.notNull(handler, "handler == null");
        this.handler = handler;
    }

    @Override
    public ChannelHandler getHandler() {
        if (handler instanceof ChannelHandlerDelegate) {
            return ((ChannelHandlerDelegate) handler).getHandler();
        }
        return handler;
    }

    @Override
    public void connected(final Channel channel) throws RemotingException {
        handler.connected(channel);
    }

    @Override
    public void disconnected(final Channel channel) throws RemotingException {
        handler.disconnected(channel);
    }

    @Override
    public void sent(final Channel channel, final Object message) throws RemotingException {
        handler.sent(channel, message);
    }

    @Override
    public void received(final Channel channel, final Object message) throws RemotingException {
        handler.received(channel, message);
    }

    @Override
    public void caught(final Channel channel, final Throwable exception) throws RemotingException {
        handler.caught(channel, exception);
    }
}
