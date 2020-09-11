package com.iec.service;

import com.iec.Handler.TcpHandler;
import com.iec.work.RuleBuild;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceTCPService extends RuleBuild {

    /**
     * 104规约默认端口2404
     */
    private final static int PORT = 2404;
    private final static String ADDRESS = "127.0.0.1";
    private boolean mIsInit = false;

    public static DeviceTCPService INSTANCE = new DeviceTCPService();

    public void initTCPService() {
        if (mIsInit) {
            return;
        }
        mIsInit = true;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                EventLoopGroup bossGroup = new NioEventLoopGroup(); // 主线程组
//                EventLoopGroup workerGroup = new NioEventLoopGroup(); // 从线程组
//                try {
//                    // netty服务器的创建, 辅助工具类，用于服务器通道的一系列配置
//                    ServerBootstrap serverBootstrap = new ServerBootstrap();
//                    serverBootstrap.group(bossGroup, workerGroup)           //绑定两个线程组
//                            .channel(NioServerSocketChannel.class)   //指定NIO的模式
//                            .option(ChannelOption.SO_BACKLOG, 1024)
//                            .childHandler(new BraceletServerInitializer()); // 子处理器，用于处理workerGroup
//
//                    // 启动server，并且设置启动的端口号，同时启动方式为同步
//                    ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
//
//                    System.out.println("----" + Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");
//
//                    // 监听关闭的channel，设置位同步方式
//                    channelFuture.channel().closeFuture().sync();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    //退出线程组
//                    bossGroup.shutdownGracefully();
//                    workerGroup.shutdownGracefully();
//                }
//            }
//        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(); // 主线程组
                EventLoopGroup workerGroup = new NioEventLoopGroup(); // 从线程组

                try {

                    EventLoopGroup group = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap()
                            .group(group)
                            //该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
                            .option(ChannelOption.TCP_NODELAY, true)
                            .channel(NioSocketChannel.class)
                            .handler(new BraceletServerInitializer());
                    ChannelFuture future = bootstrap.connect(ADDRESS, PORT).sync();
                    log.info("客户端成功....");
                    //发送消息
                    String msg = UBuild104(false, false, true);
                    byte[] decode = HexBin.decode(msg);
                    ByteBuf respByteBuf = Unpooled.copiedBuffer(decode);
                    future.channel().writeAndFlush(respByteBuf);
                    // 等待连接被关闭
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //退出线程组
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        }).start();
    }

    /**
     * @Description: 初始化器，channel注册后，会执行里面的相应的初始化方法
     */
    public class BraceletServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            // 通过SocketChannel去获得对应的管道
            ChannelPipeline pipeline = channel.pipeline();

            // 添加自定义的助手类
            pipeline.addLast(new TcpHandler());
        }

    }

}
