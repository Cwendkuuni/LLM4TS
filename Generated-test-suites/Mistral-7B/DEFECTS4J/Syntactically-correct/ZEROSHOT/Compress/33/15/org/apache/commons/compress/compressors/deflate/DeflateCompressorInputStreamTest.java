import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() throws IOException {
        // Test empty constructor
        DeflateCompressorInputStream deflateCompressorInputStream1 = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));

        // Test constructor with default parameters
        DeflateCompressorInputStream deflateCompressorInputStream2 = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDE, (byte) 0xDB, (byte) 0x16, (byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x1C, (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x01, (byte) 0x08, (byte) 0x00}));
        Assert.assertEquals(32, deflateCompressorInputStream2.available());

        // Test constructor with custom parameters
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream3 = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDE, (byte) 0xDB, (byte) 0x16, (byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x1C, (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x01, (byte) 0x08, (byte) 0x00}), deflateParameters);
        Assert.assertEquals(32, deflateCompressorInputStream3.available());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        int readByte;

        for (int i = 0; i < 10; i++) {
            readByte = deflateCompressorInputStream.read(buffer, 0, 1);
            Assert.assertNotEquals(-1, readByte);
            Assert.assertEquals((byte) (i + 1), buffer[0]);
        }

        readByte = deflateCompressorInputStream.read(buffer, 0, 1);
        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];

        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, length);

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals((byte) (i + 1), buffer[i]);
        }
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes;

        skippedBytes = deflateCompressorInputStream.skip(1);
        Assert.assertEquals(1, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(8);
        Assert.assertEquals(9, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(100);
        Assert.assertEquals(91, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        Assert.assertEquals(9, deflateCompressorInputStream.available());

        byte[] buffer = new byte[1];
        deflateCompressorInputStream.read(buffer, 0, 1);
        Assert.assertEquals(8, deflateCompressorInputStream.available());

        deflateCompressorInputStream.read(buffer, 0, 1);
        Assert.assertEquals(7, deflateCompressorInputStream.available());

        deflateCompressorInputStream.read(buffer, 0, 1);
        Assert.assertEquals(6, deflateCompressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();
        Assert.assertThrows(IOException.class, () -> deflateCompressorInputStream.read());
    }
}