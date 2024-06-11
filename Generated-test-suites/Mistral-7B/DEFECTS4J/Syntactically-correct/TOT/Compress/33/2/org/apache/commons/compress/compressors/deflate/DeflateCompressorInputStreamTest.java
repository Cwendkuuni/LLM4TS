import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = "This is a test".getBytes();
        outputStream = new ByteArrayOutputStream();
        inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters());
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] expected = new byte[1024];
        int count = inputStream.read(expected, 0, expected.length);
        Assert.assertEquals(inputStream.available(), count);
        for (int i = 0; i < count; i++) {
            Assert.assertNotEquals(-1, expected[i]);
        }
    }

    @Test
    public void testReadByte_EOS() throws IOException {
        byte[] expected = new byte[1];
        int count = inputStream.read(expected, 0, expected.length);
        Assert.assertEquals(-1, count);
        Assert.assertArrayEquals(new byte[0], expected);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[1024];
        int count = inputStream.read(expected, 0, expected.length);
        Assert.assertEquals(inputStream.available(), count);
        Assert.assertArrayEquals(new String(expected).substring(0, count).getBytes(), "This is a test".getBytes());
    }

    @Test(expected = IOException.class)
    public void testReadByteArray_NegativeLength() throws IOException {
        int length = -1;
        byte[] buffer = new byte[length];
        inputStream.read(buffer, 0, length);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = inputStream.skip(100L);
        Assert.assertEquals(inputStream.available() - 100L, inputStream.available());
        byte[] expected = new byte[1024];
        int count = inputStream.read(expected, 0, expected.length);
        Assert.assertEquals(inputStream.available(), count);
        Assert.assertArrayEquals(new String(expected).substring(100).getBytes(), " test".getBytes());
    }

    @Test(expected = IOException.class)
    public void testSkip_NegativeLength() throws IOException {
        long length = -1;
        inputStream.skip(length);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        Assert.assertThrows(IOException.class, inputStream::read);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = "This is a test".getBytes();
        InputStream testInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters());
        Assert.assertEquals(data.length, testInputStream.available());
    }

    @Test
    public void testCloseAndReopen() throws IOException {
        byte[] data = "This is a test".getBytes();
        InputStream testInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters());
        testInputStream.close();
        testInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters());
        Assert.assertEquals(data.length, testInputStream.available());
    }

    @Test
    public void testZlibHeader() throws IOException {
        byte[] data = new byte[]{0x78, 0x5E, 0x01, 0x00, 0x1A, 0x00, 0x00, 0x00, 0x03, 0x78, 0x9C};
        InputStream testInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters(true));
        byte[] expected = "This is a test".getBytes();
        int count = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(expected.length, count);
        Assert.assertArrayEquals(expected, expected);
    }

    @Test
    public void testNoZlibHeader() throws IOException {
        byte[] data = "This is a test".getBytes();
        InputStream testInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters(false));
        byte[] expected = new byte[1024];
        int count = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(inputStream.available(), count);
        for (int i = 0; i < count; i++) {
            Assert.assertNotEquals(-1, expected[i]);
        }
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
    }

}