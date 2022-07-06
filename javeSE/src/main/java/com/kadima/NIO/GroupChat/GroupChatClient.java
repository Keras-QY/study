package com.kadima.NIO.GroupChat;

import sun.applet.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    private SocketChannel socketChannel;
    private final int port=6666;
    private String ip = "127.0.0.1";
    private Selector selector;

    public GroupChatClient() throws Exception{
        selector = Selector.open();
        //绑定端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        //连接服务器
        socketChannel = SocketChannel.open(inetSocketAddress);
        //socketChannel = SocketChannel.open();
        //通道设置为非阻塞
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);

    }

    /**
     * 客户端启动
     */
    public void start() throws Exception{


    }

    /**
     * 发送消息
     * @param
     */
    public void  sendMeg(String s){
        try {
            socketChannel.write(ByteBuffer.wrap(s.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void readMeg()  {
        try{
        if (selector.select(1000) != 0 ){
            //System.out.println("有可用的通道");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    channel.read(byteBuffer);
                    System.out.println("读取到消息 "+ new String(byteBuffer.array()));
                    iterator.remove();
                }
            }
        }else {
            //System.out.println("没有可用的通道");
        }
    }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            GroupChatClient groupChatClient = new GroupChatClient();
            //groupChatClient.start();
            new Thread(() -> {
                try {
                    while (true) {
                        //System.out.println("线程执行读信息");
                        groupChatClient.readMeg();
                        try {
                            Thread.sleep(3000);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                groupChatClient.sendMeg(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
