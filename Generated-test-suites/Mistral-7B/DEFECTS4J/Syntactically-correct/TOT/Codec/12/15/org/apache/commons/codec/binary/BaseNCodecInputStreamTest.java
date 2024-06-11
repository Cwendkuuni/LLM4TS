import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for BaseNCodecInputStream class.
 */
public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodec codec;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream("BaseNTestData".getBytes());
        codec = new BaseNCodec();
    }

    @Test
    public void testReadByte() throws IOException {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] readBytes = new byte[10];
        for (int i = 0; i < 10; i++) {
            int b = baseNStream.read();
            readBytes[i] = (byte) b;
            assertEquals(i < 10 ? (byte) (i + 48) : BaseNCodec.EOF, b);
        }

        assertEquals(BaseNCodec.EOF, baseNStream.read());
        assertFalse(baseNStream.hasNextByte());
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteNullInputStream() {
        new BaseNCodecInputStream(null, codec, false);
    }

    @Test(expected = NullPointerException.class)
    public void testBaseNCodecNull() {
        new BaseNCodecInputStream(inputStream, null, false);
    }

    @Test
    public void testReadByteDoEncode() throws IOException {
        BaseNCodec codecEncoded = new BaseNCodec(true);
        BaseNCodecInputStream baseNStreamEncoded = new BaseNCodecInputStream(inputStream, codecEncoded, true);

        byte[] readBytesEncoded = new byte[10];
        for (int i = 0; i < 10; i++) {
            int b = baseNStreamEncoded.read();
            readBytesEncoded[i] = (byte) b;
            assertEquals(i < 10 ? (byte) (i + 128) : BaseNCodec.EOF, b);
        }

        assertEquals(BaseNCodec.EOF, baseNStreamEncoded.read());
        assertFalse(baseNStreamEncoded.hasNextByte());
    }

    @Test(expected = IOException.class)
    public void testReadByteIOException() throws IOException {
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        Mockito.when(mockInputStream.read()).thenThrow(new IOException());
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(mockInputStream, codec, false);

        baseNStream.read();
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] testData = "BaseNTestData".getBytes();
        byte[] expectedResults = new byte[10];
        for (int i = 0; i < 10; i++) {
            expectedResults[i] = (byte) (i + 48);
        }

        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] readBytes = new byte[10];

        int bytesRead = baseNStream.read(readBytes, 0, 10);
        assertNotNull(readBytes);
        assertEquals(10, bytesRead);

        for (int i = 0; i < 10; i++) {
            assertEquals(expectedResults[i], readBytes[i]);
        }

        assertFalse(baseNStream.hasNextByte());
    }

    @Test(expected = NullPointerException.class)
    public void testReadBytesNullByteArray() {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        baseNStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesNegativeIndex() {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        baseNStream.read(new byte[1][], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesNegativeLength() {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        baseNStream.read(new byte[1][], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesInvalidBufferSize() {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        baseNStream.read(new byte[1][], 0, 11);
    }

    @Test
    public void testReadBytesMarkSupportedFalse() {
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(inputStream, codec, false);
        assertFalse(baseNStream.markSupported());
    }

    @Test
    public void testReadBytesMarkSupportedTrue() {
        BaseNCodecInputStream baseNStream = Mockito.spy(new BaseNCodecInputStream(inputStream, codec, false));
        Mockito.doReturn(true).when(baseNStream).markSupported();

        byte[] testData = "BaseNTestData".getBytes();
        byte[] expectedResults = new byte[10];
        for (int i = 0; i < 10; i++) {
            expectedResults[i] = (byte) (i + 48);
        }

        baseNStream.mark(10);
        byte[] readBytes = new byte[10];

        int bytesRead = baseNStream.read(readBytes, 0, 10);
        assertNotNull(readBytes);
        assertEquals(10, bytesRead);

        for (int i = 0; i < 10; i++) {
            assertEquals(expectedResults[i], readBytes[i]);
        }

        assertFalse(baseNStream.hasNextByte());
    }

    @Test
    public void testReadBytesSkipBytes() throws IOException {
        byte[] testData = "BaseNTestData".getBytes();
        byte[] expectedResults = new byte[5];
        for (int i = 0; i < 5; i++) {
            expectedResults[i] = (byte) (i + 48);
        }

        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), codec, false);

        baseNStream.skip(5);

        byte[] readBytes = new byte[5];
        int bytesRead = baseNStream.read(readBytes, 0, 5);
        assertNotNull(readBytes);
        assertEquals(5, bytesRead);

        for (int i = 0; i < 5; i++) {
            assertEquals(expectedResults[i], readBytes[i]);
        }

        assertFalse(baseNStream.hasNextByte());
    }

    @Test
    public void testReadBytesSkipBytesNegativeLength() throws IOException {
        byte[] testData = "BaseNTestData".getBytes();
        byte[] expectedResults = new byte[5];
        for (int i = 0; i < 5; i++) {
            expectedResults[i] = (byte) (i + 48);
        }

        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), codec, false);

        baseNStream.skip(-10);

        byte[] readBytes = new byte[5];
        int bytesRead = baseNStream.read(readBytes, 0, 5);
        assertNotNull(readBytes);
        assertEquals(5, bytesRead);

        for (int i = 0; i < 5; i++) {
            assertEquals(expectedResults[i], readBytes[i]);
        }

        assertFalse(baseNStream.hasNextByte());
    }

    @Test
    public void testReadBytesCloseStream() throws IOException {
        byte[] testData = "BaseNTestData".getBytes();
        byte[] expectedResults = new byte[5];
        for (int i = 0; i < 5; i++) {
            expectedResults[i] = (byte) (i + 48);
        }

        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), codec, false);
        byte[] readBytes = new byte[5];

        int bytesRead = baseNStream.read(readBytes, 0, 5);
        assertNotNull(readBytes);
        assertEquals(5, bytesRead);

        for (int i = 0; i < 5; i++) {
            assertEquals(expectedResults[i], readBytes[i]);
        }

        baseNStream.close();
        assertNull(baseNStream.in);
    }
}