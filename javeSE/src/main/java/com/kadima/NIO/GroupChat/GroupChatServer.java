package com.kadima.NIO.GroupChat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private String ip;
    private final int port=6666;

    public GroupChatServer() throws Exception{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        ip = "127.0.0.1";
        //绑定监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(ip,port));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     *服务器启动
     */
    public void start() throws Exception{

        //绑定监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(ip,port));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //监听
        listen();


    }

    /**
     * 监听事件
     */
    public void listen() throws Exception{

        while (true) {
            if (selector.select(1000) ==0){
                //System.out.println("等待了一秒，无连接");
                continue;
            }
            //监听到事件需要处理
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    //创建一个新连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println(socketChannel.getRemoteAddress() + "上线了");
                }
                if (selectionKey.isReadable()) {
                    //处理读事件
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = channel.read(byteBuffer);
                    if (read > 0) {
                        System.out.println("服务端收到来自 " + channel.getRemoteAddress().toString().substring(1) + " 发来的消息：" + new String(byteBuffer.array()));
                        //转发消息
                        System.out.println("开始转发消息");
                        sendMeg(new String(byteBuffer.array()), channel);
                    }
                }
                //移除事件
                iterator.remove();
            }
        }
    }

    /**
     * 转发消息,排除自己
     */
    public void sendMeg(String message,SocketChannel sel) throws Exception{

        Set<SelectionKey> selectionKeys = selector.keys();
        System.out.println("数量为"+selectionKeys.size());
        for (SelectionKey key : selectionKeys) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel != sel){
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(byteBuffer);
                System.out.println("消息转发成功");
            }
        }
       /* Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel != sel){
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(byteBuffer);
                System.out.println("消息转发成功");
                //ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                //byteBuffer.put(message.getBytes());
                //((SocketChannel)channel).write(byteBuffer);
            }*/
        }


    public static void main(String[] args) {
        try {

            GroupChatServer groupChatServer = new GroupChatServer();
            groupChatServer.listen();
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
