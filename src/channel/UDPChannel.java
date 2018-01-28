package channel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * UDP连接的非阻塞式IO.
 *
 * @author: huang
 * Date: 18-1-28
 */
public class UDPChannel {

    /**
     * 客户端.
     */
    @Test
    public void client() throws IOException {
        // 获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();

        // 切换为非阻塞
        datagramChannel.configureBlocking(false);

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 发送信息
        buffer.put("你好!".getBytes());
        buffer.flip();
        datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 8080));

        // 关闭通道
        datagramChannel.close();
    }

    /**
     * 服务端.
     */
    @Test
    public void server() throws IOException {
        // 获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(8080));

        // 切换为非阻塞模式
        datagramChannel.configureBlocking(false);

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 获取选择器
        Selector selector = Selector.open();
        // 注册
        datagramChannel.register(selector, SelectionKey.OP_READ);

        // 轮询
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey sk = iterator.next();

                if (sk.isReadable()) { // 可读状态
                    datagramChannel.receive(buffer);
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            // 移除
            iterator.remove();
        }
    }

}
