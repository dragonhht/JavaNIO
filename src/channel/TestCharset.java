package channel;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 字符集.
 *
 * @author: huang
 * Date: 18-1-28
 */
public class TestCharset {

    /**
     * 字符缓冲区与字节缓冲区间使用字符集，避免乱码.
     */
    @Test
    public void test() throws CharacterCodingException {

        // 自定字符集
        Charset charset = Charset.forName("GBK");
        // 获取编码器与解码器
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();

        // 创建字符缓冲区
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("字符集");

        // 编码
        charBuffer.flip();
        ByteBuffer byteBuffer = encoder.encode(charBuffer);

        // 解码
        CharBuffer charBuffer1 = decoder.decode(byteBuffer);

        System.out.println("解码后：" + charBuffer1.toString());


    }

}
