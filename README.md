# NIO

## Java IO 与 NIO的主要区别

| IO    |   NIO |
|-------|-------|
|   面向流 |   面向缓冲区   |
|   阻塞IO    |   非阻塞IO   |
|   |   选择器 |

## 通道(Channel)与缓冲区(Buffer)

> Channel表示打开IO设备（如，文件、套接字）的连接。若要使用NIO，则需获取用于连接IO设备的管道以及容纳数据的缓冲区。然后操作缓冲区，对数据进行处理

###   [缓冲区(Buffer)]()

> 在Java NIO中负责数据的存取，缓冲区就是数组用于操作不同类型的数据。不同的数据类型提供了不同的缓冲区，boolean除外。

-   ByteBuffer
-   CharBuffer
-   DoubleBuffer
-   FloatBuffer
-   IntBuffer
-   LongBuffer
-   ShortBuffer

#### 使用方法

1.  使用`allocate()`方法获取一定大小的缓冲区：`ByteBuffer buffer = ByteBuffer.allocate(1024);`

2.  使用`put()`存入数据到缓冲区：`buffer.put("hello".getBytes());`

3.  使用`flip()`切换成读取数据模式：`buffer.flip();`

4.  使用`get()`获取缓冲区内的数据：`byte[] bytes = new byte[buffer.limit()]; buffer.get(bytes);`

#### 核心属性

-   `mark`：标记， 表示记录当前position的位置，可通过reset()恢复到mark的位置
-   `position`：位置，缓冲区中正在操作数据的位置
-   `limit`：界限，缓冲区中可以操作数据的大小。 limit后的数据不可读写
-   `capacity`：容量，表示缓冲区中最大存储数据的容量，一旦声明不可改变