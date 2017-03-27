/**
 * File Created at 2012-02-02
 * $Id$
 *
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.dubbo.rpc.protocol.thrift;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class DubboClassNameGenerator implements ClassNameGenerator {

    public static final String NAME = "dubbo";

    @Override
    public String generateArgsClassName(final String serviceName, final String methodName) {
        return ThriftUtils.generateMethodArgsClassName(serviceName, methodName);
    }

    @Override
    public String generateResultClassName(final String serviceName, final String methodName) {
        return ThriftUtils.generateMethodResultClassName(serviceName, methodName);
    }

}
