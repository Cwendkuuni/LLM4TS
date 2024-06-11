import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test constructor with default parameters.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        Assert.assertNotNull(inputStream);
    }

    /**
     * Test constructor with custom parameters.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCustomConstructor() throws IOException {
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}), parameters);
        Assert.assertNotNull(inputStream);
        Assert.assertEquals(parameters, inputStream.getParameters());
    }

    /**
     * Test reading a single byte.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x01);
        outputStream.writeTo(new DeflateCompressorOutputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        int ret = inputStream.read();
        Assert.assertEquals(0x01, ret);
    }

    /**
     * Test reading multiple bytes.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x01);
        outputStream.write(0x02);
        outputStream.writeTo(new DeflateCompressorOutputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        byte[] buffer = new byte[2];
        int ret = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(ret, buffer.length);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
    }

    /**
     * Test skipping bytes.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x01);
        outputStream.writeTo(new DeflateCompressorOutputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        long skipped = inputStream.skip(1);
        Assert.assertEquals(skipped, 1);
        int ret = inputStream.read();
        Assert.assertEquals(0x01, ret);
    }

    /**
     * Test available bytes.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x01);
        outputStream.writeTo(new DeflateCompressorOutputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        int available = inputStream.available();
        Assert.assertEquals(available, 0);
        int ret = inputStream.read();
        Assert.assertEquals(ret, 0x01);
        available = inputStream.available();
        Assert.assertEquals(available, 0);
    }

    /**
     * Test closing the input stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x01);
        outputStream.writeTo(new DeflateCompressorOutputStream(new ByteArrayInputStream(outputStream.toByteArray())));
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        inputStream.close();
        try {
            inputStream.read();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}