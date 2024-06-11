import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for DeflateCompressorInputStream
 */
public class DeflateCompressorInputStreamTest {

    private InputStream testInputStream;
    private ByteArrayOutputStream testOutputStream;
    private ByteArrayInputStream testInput;

    @Before
    public void setUp() throws IOException {
        testOutputStream = new ByteArrayOutputStream();
        testInput = new ByteArrayInputStream(testOutputStream.toByteArray());
        testInputStream = new DeflateCompressorInputStream(testInput);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(testInputStream);
        testOutputStream.close();
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(null);
        assertNull(deflateCompressorInputStream);
    }

    @Test
    public void testConstructorWithInputStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInputStream);
        assertTrue(deflateCompressorInputStream instanceof DeflateCompressorInputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndParameters() throws IOException {
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInputStream, deflateParameters);
        assertTrue(deflateCompressorInputStream instanceof DeflateCompressorInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int readByte;
        byte[] testData = {0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1};
        testOutputStream.write(testData);
        testOutputStream.flush();

        readByte = testInputStream.read();
        assertEquals(testData[0], readByte);

        readByte = testInputStream.read();
        assertEquals(testData[1], readByte);

        readByte = testInputStream.read();
        assertEquals(testData[2], readByte);

        readByte = testInputStream.read();
        assertEquals(testData[3], readByte);

        readByte = testInputStream.read();
        assertEquals(-1, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1};
        byte[] readData = new byte[8];

        testOutputStream.write(testData);
        testOutputStream.flush();

        int readBytes = testInputStream.read(readData, 0, readData.length);
        assertEquals(testData.length, readBytes);

        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], readData[i]);
        }
    }

    @Test
    public void testSkip() throws IOException {
        testOutputStream.write(new byte[]{0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1});
        testOutputStream.flush();

        long skippedBytes = testInputStream.skip(4);
        assertEquals(4, skippedBytes);

        int readByte = testInputStream.read();
        assertEquals(testData[5], readByte);

        readByte = testInputStream.read();
        assertEquals(testData[6], readByte);

        readByte = testInputStream.read();
        assertEquals(testData[7], readByte);

        readByte = testInputStream.read();
        assertEquals(-1, readByte);
    }

    @Test
    public void testAvailable() throws IOException {
        testOutputStream.write(new byte[]{0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1});
        testOutputStream.flush();

        int availableBytes = testInputStream.available();
        assertEquals(7, availableBytes);

        testInputStream.read();
        testInputStream.read();
        testInputStream.read();
        testInputStream.read();

        availableBytes = testInputStream.available();
        assertEquals(3, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        testOutputStream.write(new byte[]{0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1});
        testOutputStream.flush();

        testInputStream.close();
        assertFalse(testInputStream.available() > 0);
    }

    @Test
    public void testZlibHeader() throws IOException {
        Inflater inflater = new Inflater();
        byte[] zlibTestData = {0x78, 0x9C, 0x61, 0x78, 0x0, 0x14, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0xF8};
        testOutputStream.write(new byte[]{0x78, 0x9C, 0x61, 0x78});
        testOutputStream.write(zlibTestData, 4, zlibTestData.length - 4);
        testOutputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInputStream);
        byte[] readData = new byte[zlibTestData.length];
        int readBytes = deflateCompressorInputStream.read(readData, 0, readData.length);

        InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(zlibTestData));
        byte[] inflaterReadData = new byte[zlibTestData.length];
        int inflaterReadBytes = inflaterInputStream.read(inflaterReadData, 0, inflaterReadData.length);

        for (int i = 0; i < inflaterReadData.length; i++) {
            assertEquals(inflaterReadData[i], readData[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testReadWhenInputStreamClosed() throws IOException {
        testOutputStream.write(new byte[]{0x0, 0x78, 0xF4, 0xBC, 0x0, 0x0, 0x0, 0x1});
        testOutputStream.flush();
        testInputStream.close();
        testInputStream.read();
    }
}