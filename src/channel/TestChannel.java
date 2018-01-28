package channel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Channel.
 *
 * @author: huang
 * Date: 18-1-26
 */
public class TestChannel {

    /**
     * 利用Channel完成文件的复制(非直接缓冲区).
     */
    @Test
    public void test1() throws IOException {
        FileInputStream fis = new FileInputStream("JavaNIO.iml");
        FileOutputStream fos = new FileOutputStream("JavaNIO2.iml");

        // 获取通道
        FileChannel inputChannel = fis.getChannel();
        FileChannel outputChannel = fos.getChannel();

        // 分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 将通道内的数据读入缓冲区
        while (inputChannel.read(buffer) != -1) {
            // 将缓冲区内的数据写入通道
            buffer.flip();  //  切换为读数据
            outputChannel.write(buffer);
            buffer.clear(); // 清空缓冲区
        }

        // 关闭通道
        outputChannel.close();
        inputChannel.close();
        fos.close();
        fis.close();
    }

    /**
     * 使用直接缓冲区复制文件(内存映射文件).
     */
    @Test
    public void test2() throws IOException {
        // 使用Open()获取通道
        FileChannel inputChannel = FileChannel.open(Paths.get("JavaNIO.iml"), StandardOpenOption.READ);
        FileChannel outputChannel = FileChannel.open(Paths.get("JavaNIO2.iml"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        // 内存映射文件，和使用allocateDirect()获取内存一样，内存在物理内存中
        MappedByteBuffer inputMappedBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputChannel.size());
        MappedByteBuffer outputMappedBuffer = outputChannel.map(FileChannel.MapMode.READ_WRITE, 0, inputChannel.size());

        // 直接对缓冲区进行读写操作
        byte[] bytes = new byte[inputMappedBuffer.limit()];
        inputMappedBuffer.get(bytes);
        outputMappedBuffer.put(bytes);

        // 关闭通道
        inputChannel.close();
        outputChannel.close();
    }

    /**
     * 通道间的数据传输(直接缓冲区).
     */
    @Test
    public void test3() throws IOException {
        // 使用Open()获取通道
        FileChannel inputChannel = FileChannel.open(Paths.get("JavaNIO.iml"), StandardOpenOption.READ);
        FileChannel outputChannel = FileChannel.open(Paths.get("JavaNIO2.iml"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        // 通道间的数据传输(二用一)
        // inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        inputChannel.close();
        outputChannel.close();
    }

    /**
     * 分散与聚集.
     */
    @Test
    public void test4() throws IOException {
        // 获取通道
        FileChannel inputChannel = FileChannel.open(Paths.get("JavaNIO.iml"), StandardOpenOption.READ);
        FileChannel ouputChannel = FileChannel.open(Paths.get("JavaNIO2.iml"),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 分配缓冲区数组
        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = {buffer1, buffer2};

        // 分散读取
        inputChannel.read(buffers);

        // 聚集写入
        for (ByteBuffer b : buffers) {
            b.flip();
        }
        ouputChannel.write(buffers);

        ouputChannel.close();
        inputChannel.close();
    }
}
