package com.choice.netty.unsafe;

/**
 * Unsafe接口实际上是Channel接口的辅助接口，它不应该被用户代码直接调用。实际的I/O读写操作都是由Unsafe接口负责完成的。
 * (1) 常用方法
 * invoker() 返回默认使用的ChannelHandlerInvoker
 * localAddress() 返回本地绑定的Socket地址
 * remoteAddress() 返回通信对端的Socket地址
 * register() 注册Channel 到多路复用器上，一旦注册操作完成，通知 ChannelFuture
 * bind() 绑定指定的本地地址 localAddress 到当前的 Channel 上，一旦完成，通知ChannelFuture
 * connect() 绑定本地的localAddress 之后，连接服务器，一旦连接完成，通知 ChannelFuture
 * disconnect() 断开Channel连接，一旦完成，通知 ChannelFuture
 * close() 关闭Channel连接，一旦完成，通知ChannelFuture
 * closeForcibly() 强制立即关闭连接
 * beginRead() 设置网络操作位为读取消息
 * write() 发送消息，一旦完成，通知ChannelFuture
 * flush() 将发送缓冲区的消息写入到 Channel 之中
 * voidPromise() 返回一个特殊的可重用和传递的ChannelFuture，不用于操作成功或失败的通知器，仅仅作为一个容器被使用
 * outboundBuffer() 返回消息发送缓冲区
 */
public class ExploreUnsafe {
}
