package channel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * TCP连接的非阻塞式IO.
 *
 * @author: huang
 * Date: 18-1-28
 */
public class TCPChannel {

    /**
     * 服务器.
     */
    @Test
    public void server() throws IOException {
        // 获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));

        // 切换为非阻塞
        serverChannel.configureBlocking(false);

        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器, 并指定监听事件，多个监听事件可用位或操作符("|")分开
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 轮询获取选择器上已准备就绪的事件
        while (selector.select() > 0) {
            // 获取选择器中所有已就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey sk = iterator.next();

                // 判断具体为何事件准备就绪
                if (sk.isAcceptable()) { // 接受就绪
                    SocketChannel socketChannel = serverChannel.accept();
                    // 切换为非阻塞模式
                    socketChannel.configureBlocking(false);

                    // 将该通道注册到选择器
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    // 获取读就绪状态通道
                    SocketChannel socketChannel = (SocketChannel) sk.channel();

                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = socketChannel.read(buffer)) != -1) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                }
                // 取消选择器
                iterator.remove();
            }
        }

    }

    /**
     * 客户端.
     */
    @Test
    public void client() throws IOException {
        // 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));

        // 切换为非阻塞模式
        socketChannel.configureBlocking(false);

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 发送数据
        buffer.put("Hello World".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();

        socketChannel.close();
    }

}
