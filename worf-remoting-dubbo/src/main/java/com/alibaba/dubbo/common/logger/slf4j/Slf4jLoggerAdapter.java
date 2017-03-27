package com.alibaba.dubbo.common.logger.slf4j;

import java.io.File;

import com.alibaba.dubbo.common.logger.Level;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerAdapter;

public class Slf4jLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(final String key) {
        return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(key));
    }

    @Override
    public Logger getLogger(final Class<?> key) {
        return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(key));
    }

    private Level level;

    private File file;

    @Override
    public void setLevel(final Level level) {
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(final File file) {
        this.file = file;
    }

}
