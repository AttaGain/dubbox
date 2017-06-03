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
package com.alibaba.dubbo.remoting.transport.mina;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ExecutorUtil;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.transport.AbstractServer;
import com.alibaba.dubbo.remoting.transport.dispatcher.ChannelHandlers;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * MinaServer
 *
 * @author qian.lei
 * @author william.liangf
 * @author ding.lid
 */
public class MinaServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(MinaServer.class);

    private NioSocketAcceptor acceptor;

    public MinaServer(URL url, ChannelHandler handler) throws RemotingException {
        super(url, ChannelHandlers.wrap(handler, ExecutorUtil.setThreadName(url, SERVER_THREAD_POOL_NAME)));
    }

    @Override
    protected void doOpen() throws Throwable {
        // set thread pool.
        acceptor = new NioSocketAcceptor(getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS));
        // config
        // SocketSessionConfig cfg = acceptor.getSessionConfig();
        // MINA 1 only
        // cfg.setThreadModel(ThreadModel.MANUAL);
        // set codec.
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaCodecAdapter(getCodec(), getUrl(), this)));

        acceptor.bind(getBindAddress());
    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (acceptor != null) {
                acceptor.unbind(getBindAddress());
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public Collection<Channel> getChannels() {
        Map<Long, IoSession> sessions = acceptor.getManagedSessions();
        Collection<Channel> channels = new HashSet<Channel>();
        for (IoSession session : sessions.values()) {
            if (session.isConnected()) {
                channels.add(MinaChannel.getOrAddChannel(session, getUrl(), this));
            }
        }
        return channels;
    }

    public Channel getChannel(InetSocketAddress remoteAddress) {
        Map<Long, IoSession> sessions = acceptor.getManagedSessions();
        for (IoSession session : sessions.values()) {
            if (session.getRemoteAddress().equals(remoteAddress)) {
                return MinaChannel.getOrAddChannel(session, getUrl(), this);
            }
        }
        return null;
    }

    public boolean isBound() {
        return acceptor.getLocalAddresses().contains(getBindAddress());
    }

}