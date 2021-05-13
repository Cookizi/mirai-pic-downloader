package top.cookizi.bot.common.utils.modle;

import lombok.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author heq
 * @date 2021/2/9
 * @description
 */
@Data
public class BufferReader {

    @ToString.Exclude
    private FileInputStream fileIn;
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ByteBuffer byteBuf;
    private long fileLength;
    private int arraySize = 30;
    private byte[] array;
    private byte[] cache = null;
    private int index;

    public BufferReader(String path) throws IOException {
        this.fileIn = new FileInputStream(path);
        this.fileLength = fileIn.getChannel().size();
        this.byteBuf = ByteBuffer.allocate(arraySize);
    }

    public BufferReader(String path, int arraySize) throws IOException {
        this.fileIn = new FileInputStream(path);
        this.fileLength = fileIn.getChannel().size();
        this.arraySize = arraySize;
        this.byteBuf = ByteBuffer.allocate(arraySize);
    }

    public String readWord() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        while (flag) {
            byte[] bytes = cache;
            int start = index;
            if (cache == null || index >= cache.length) {
                bytes = getByte();
                start = 0;
                index = 0;
                if (bytes == null) {
                    return sb.toString();
                }
            }
            for (int i = start; i < bytes.length; i++) {
                byte b = bytes[i];
                if ((64 < b && b < 91) || (96 < b && b < 123)) {
                    sb.append((char) b);
                } else if (sb.length() != 0) {
                    flag = false;
                    cache = bytes;
                    index = ++i;
                    break;
                }
            }
            if (flag) {
                cache = null;
                index = 0;
            }
        }
        return sb.toString();
    }

    /**
     * 从文件中获取一段bytes
     *
     * @return
     * @throws IOException
     */
    public byte[] getByte() throws IOException {
        FileChannel fileChannel = fileIn.getChannel();
        int bytes = fileChannel.read(byteBuf);
        if (bytes == -1) {
            return null;
        }
        array = new byte[bytes];
        byteBuf.flip();
        byteBuf.get(array);
        byteBuf.clear();
        return array;
    }
}
