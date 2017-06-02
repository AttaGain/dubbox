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
package com.alibaba.dubbo.common.logger.log4j;

import com.alibaba.dubbo.common.logger.Logger;
import org.apache.logging.log4j.Level;

public class Log4jLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public Log4jLogger(org.apache.logging.log4j.Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg) {
        logger.log(Level.TRACE, msg);
    }

    public void trace(Throwable e) {
        logger.log(Level.TRACE, e == null ? null : e.getMessage(), e);
    }

    public void trace(String msg, Throwable e) {
        logger.log(Level.TRACE, msg, e);
    }

    public void debug(String msg) {
        logger.log(Level.DEBUG, msg);
    }

    public void debug(Throwable e) {
        logger.log(Level.DEBUG, e == null ? null : e.getMessage(), e);
    }

    public void debug(String msg, Throwable e) {
        logger.log(Level.DEBUG, msg, e);
    }

    public void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public void info(Throwable e) {
        logger.log(Level.INFO, e == null ? null : e.getMessage(), e);
    }

    public void info(String msg, Throwable e) {
        logger.log(Level.INFO, msg, e);
    }

    public void warn(String msg) {
        logger.log(Level.WARN, msg);
    }

    public void warn(Throwable e) {
        logger.log(Level.WARN, e == null ? null : e.getMessage(), e);
    }

    public void warn(String msg, Throwable e) {
        logger.log(Level.WARN, msg, e);
    }

    public void error(String msg) {
        logger.log(Level.ERROR, msg);
    }

    public void error(Throwable e) {
        logger.log(Level.ERROR, e == null ? null : e.getMessage(), e);
    }

    public void error(String msg, Throwable e) {
        logger.log(Level.ERROR, msg, e);
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isEnabled(Level.WARN);
    }

    public boolean isErrorEnabled() {
        return logger.isEnabled(Level.ERROR);
    }

}