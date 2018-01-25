package buffer;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Buffer.
 *
 * @author: huang
 * Date: 18-1-25
 */
public class TestBuffer {

    @Test
    public void test1() {

        // 使用allocate()方法获取一定大小的缓冲区.
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 使用put()方法存入数据
        buffer.put("hello".getBytes());
        // 切换成读数据模式
        buffer.flip();
        // 使用get()方法获取数据
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));
        // 使用rewind()重读数据
        buffer.rewind();
        buffer.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));

        // 清空缓冲区, 缓冲区的数据其实依然存在，
        buffer.clear();

        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
    }

    @Test
    public void test2() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put("hello".getBytes());

        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes, 0, 3);
        System.out.println(new String(bytes, 0, 3));

        System.out.println(buffer.position());

        // 使用mark()进行标记
        buffer.mark();

        buffer.get(bytes, 3, 2);
        System.out.println(new String(bytes, 3, 2));

        System.out.println(buffer.position());

        // 重置position, 将position重置到mark处
        buffer.reset();

        System.out.println(buffer.position());

        // 判断缓冲区内是否还有剩余的数据, 若有则显示还有多少
        if (buffer.hasRemaining()) {
            System.out.println(buffer.remaining());
        }
    }

}
