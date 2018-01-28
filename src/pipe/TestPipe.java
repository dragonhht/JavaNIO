package pipe;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 管道Pipe.
 *
 * @author: huang
 * Date: 18-1-28
 */
public class TestPipe {

    @Test
    public void test() throws IOException {
        // 获取管道
        Pipe pipe = Pipe.open();

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
        buffer.put("你好!".getBytes());

        // 将缓冲区数据写入管道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buffer.flip();
        sinkChannel.write(buffer);
        buffer.clear();
        System.out.println(new String(buffer1.array()));

        // 读取管道内数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        int len = sourceChannel.read(buffer1);
        System.out.println(new String(buffer1.array(), 0, len));

        sourceChannel.close();
        sinkChannel.close();
    }

}
