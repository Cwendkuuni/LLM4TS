import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "This is a test data for Base64InputStreamTest.";

    /**
     * Test constructor with default parameters
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        byte[] result = new byte[TEST_DATA.length()];
        int len = inputStream.read(result);
        Assert.assertEquals(TEST_DATA.length(), len);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), result);
    }

    /**
     * Test constructor with encoding parameter
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(TEST_DATA.getBytes());
        base64OutputStream.close();

        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true);
        byte[] result = new byte[TEST_DATA.length()];
        int len = inputStream.read(result);
        Assert.assertEquals(TEST_DATA.length(), len);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), result);
    }

    /**
     * Test constructor with encoding and line length parameters
     */
    @Test
    public void testEncodeConstructorWithLineLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n");
        base64OutputStream.write(TEST_DATA.getBytes());
        base64OutputStream.close();

        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true, 76, "\r\n".getBytes());
        byte[] result = new byte[TEST_DATA.length()];
        int len = inputStream.read(result);
        Assert.assertEquals(TEST_DATA.length(), len);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), result);
    }

    /**
     * Test read method with empty input
     */
    @Test
    public void testReadEmptyInput() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        int len = inputStream.read();
        Assert.assertEquals(-1, len);
    }

    /**
     * Test read method with one byte input
     */
    @Test
    public void testReadOneByteInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"A"});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[1];
        int len = base64InputStream.read(result);
        Assert.assertEquals(1, len);
        Assert.assertArrayEquals(new byte[]{'A'}, result);
    }

    /**
     * Test read method with multiple bytes input
     */
    @Test
    public void testReadMultipleBytesInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D'});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[4];
        int len = base64InputStream.read(result);
        Assert.assertEquals(4, len);
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C', 'D'}, result);
    }

    /**
     * Test read method with EOF input
     */
    @Test
    public void testReadEndOfInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[1];
        int len = base64InputStream.read(result);
        Assert.assertEquals(1, len);
        Assert.assertArrayEquals(new byte[]{'A'}, result);

        len = base64InputStream.read(result);
        Assert.assertEquals(-1, len);
    }

    /**
     * Test markSupported method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        base64InputStream.markSupported();
    }

    /**
     * Test read method with null byte array
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        base64InputStream.read(null);
    }

    /**
     * Test read method with invalid offset and length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidArguments() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        byte[] result = new byte[TEST_DATA.length()];
        int offset = TEST_DATA.length();
        int len = TEST_DATA.length() + 1;
        base64InputStream.read(result, offset, len);
    }

    /**
     * Test read method with input stream without data
     */
    @Test
    public void testReadEmptyInputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));
        byte[] result = new byte[1];
        int len = base64InputStream.read(result);
        Assert.assertEquals(-1, len);
    }
}