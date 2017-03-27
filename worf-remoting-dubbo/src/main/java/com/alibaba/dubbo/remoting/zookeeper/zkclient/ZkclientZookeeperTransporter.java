package com.alibaba.dubbo.remoting.zookeeper.zkclient;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter {

    @Override
    public ZookeeperClient connect(final URL url) {
        return new ZkclientZookeeperClient(url);
    }

}
