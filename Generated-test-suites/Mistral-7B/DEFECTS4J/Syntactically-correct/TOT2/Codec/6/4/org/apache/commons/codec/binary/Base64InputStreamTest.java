import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with no arguments.
     * @throws IOException for testing purposes
     */
    @Test
    public void testConstructorWithNoArguments() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedByteArray = new byte[7];
        int decodedLength = base64InputStream.read(decodedByteArray, 0, decodedByteArray.length);
        String decodedString = new String(decodedByteArray, 0, decodedLength);
        Assert.assertEquals("VXNlCRvT", decodedString);

        base64InputStream.close();
    }

    /**
     * Test constructor with an InputStream and false as argument.
     * @throws IOException for testing purposes
     */
    @Test
    public void testConstructorWithInputStreamAndDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] encodedByteArray = new byte[7];
        int encodedLength = base64InputStream.read(encodedByteArray, 0, encodedByteArray.length);
        String encodedString = new String(encodedByteArray, 0, encodedLength);
        Assert.assertEquals("VXNlCRvT", encodedString);

        base64InputStream.close();
    }

    /**
     * Test constructor with an InputStream, true as argument, and valid line length.
     * @throws IOException for testing purposes
     */
    @Test
    public void testConstructorWithInputStreamAndEncodeWithValidLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, new byte[]{(byte) 0x0D, (byte) 0x0A});

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] encodedByteArray = new byte[7];
        int encodedLength = 0;
        int readLength = 0;

        while ((readLength = base64InputStream.read(encodedByteArray, 0, encodedByteArray.length)) != -1) {
            byteArrayOutputStream.write(encodedByteArray, 0, readLength);
            encodedByteArray = new byte[encodedByteArray.length];
        }

        byteArrayOutputStream.flush();
        String encodedString = new String(byteArrayOutputStream.toByteArray());
        Assert.assertEquals("VXNlCRvT\r\n", encodedString);

        base64InputStream.close();
    }

    /**
     * Test constructor with an InputStream, true as argument, and invalid line length.
     * @throws IOException for testing purposes
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInputStreamAndEncodeWithInvalidLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, new byte[0]);
    }

    /**
     * Test read method with empty buffer.
     * @throws IOException for testing purposes
     */
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedByteArray = new byte[0];
        int decodedLength = base64InputStream.read(decodedByteArray, 0, decodedByteArray.length);

        base64InputStream.close();
    }

    /**
     * Test read method with buffer larger than data size.
     * @throws IOException for testing purposes
     */
    @Test
    public void testReadWithBufferLargerThanDataSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedByteArray = new byte[8];
        int decodedLength = base64InputStream.read(decodedByteArray, 0, decodedByteArray.length);

        byte[] expectedByteArray = new byte[]{"V".getBytes()[0], "X".getBytes()[0], "N".getBytes()[0], "l".getBytes()[0], "C".getBytes()[0], "R".getBytes()[0], "v".getBytes()[0], "T".getBytes()[0]};
        Assert.assertArrayEquals(expectedByteArray, decodedByteArray, 0, decodedLength);

        base64InputStream.close();
    }

    /**
     * Test read method with buffer smaller than data size.
     * @throws IOException for testing purposes
     */
    @Test
    public void testReadWithBufferSmallerThanDataSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedByteArray = new byte[3];
        int decodedLength = base64InputStream.read(decodedByteArray, 0, decodedByteArray.length);

        byte[] expectedByteArray = new byte[]{"V".getBytes()[0], "X".getBytes()[0], "N".getBytes()[0]};
        Assert.assertArrayEquals(expectedByteArray, decodedByteArray, 0, decodedLength);

        base64InputStream.close();
    }

    /**
     * Test read method with buffer larger than one read and remaining data.
     * @throws IOException for testing purposes
     */
    @Test
    public void testReadWithBufferLargerThanOneReadAndRemainingData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedByteArray1 = new byte[3];
        int decodedLength1 = base64InputStream.read(decodedByteArray1, 0, decodedByteArray1.length);

        byte[] decodedByteArray2 = new byte[5];
        int decodedLength2 = base64InputStream.read(decodedByteArray2, 0, decodedByteArray2.length);

        byte[] expectedByteArray1 = new byte[]{"V".getBytes()[0], "X".getBytes()[0], "N".getBytes()[0]};
        Assert.assertArrayEquals(expectedByteArray1, decodedByteArray1, 0, decodedLength1);

        byte[] expectedByteArray2 = new byte[]{"l".getBytes()[0], "C".getBytes()[0], "R".getBytes()[0], "v".getBytes()[0], "T".getBytes()[0]};
        Assert.assertArrayEquals(expectedByteArray2, decodedByteArray2, 0, decodedLength2);

        base64InputStream.close();
    }

    /**
     * Test hasData method.
     * @throws IOException for testing purposes
     */
    @Test
    public void testHasData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        boolean hasData = base64InputStream.hasData();
        Assert.assertTrue(hasData);

        base64InputStream.read(new byte[1], 0, 1);
        hasData = base64InputStream.hasData();
        Assert.assertFalse(hasData);

        base64InputStream.close();
    }
}