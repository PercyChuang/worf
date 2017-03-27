package com.alibaba.dubbo.remoting.zookeeper.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.StateListener;
import com.alibaba.dubbo.remoting.zookeeper.support.AbstractZookeeperClient;

public class ZkclientZookeeperClient extends AbstractZookeeperClient<IZkChildListener> {

    private final ZkClient client;

    private volatile KeeperState state = KeeperState.SyncConnected;

    public ZkclientZookeeperClient(final URL url) {
        super(url);
        client = new ZkClient(url.getBackupAddress());
        client.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(final KeeperState state) throws Exception {
                ZkclientZookeeperClient.this.state = state;
                if (state == KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if (state == KeeperState.SyncConnected) {
                    stateChanged(StateListener.CONNECTED);
                }
            }

            @Override
            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
            }
        });
    }

    @Override
    public void createPersistent(final String path) {
        try {
            client.createPersistent(path, true);
        } catch (ZkNodeExistsException e) {
        }
    }

    @Override
    public void createEphemeral(final String path) {
        try {
            client.createEphemeral(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    @Override
    public void delete(final String path) {
        try {
            client.delete(path);
        } catch (ZkNoNodeException e) {
        }
    }

    @Override
    public List<String> getChildren(final String path) {
        try {
            return client.getChildren(path);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    @Override
    public boolean isConnected() {
        return state == KeeperState.SyncConnected;
    }

    @Override
    public void doClose() {
        client.close();
    }

    @Override
    public IZkChildListener createTargetChildListener(final String path, final ChildListener listener) {
        return new IZkChildListener() {
            @Override
            public void handleChildChange(final String parentPath, final List<String> currentChilds) throws Exception {
                listener.childChanged(parentPath, currentChilds);
            }
        };
    }

    @Override
    public List<String> addTargetChildListener(final String path, final IZkChildListener listener) {
        return client.subscribeChildChanges(path, listener);
    }

    @Override
    public void removeTargetChildListener(final String path, final IZkChildListener listener) {
        client.unsubscribeChildChanges(path, listener);
    }

}
