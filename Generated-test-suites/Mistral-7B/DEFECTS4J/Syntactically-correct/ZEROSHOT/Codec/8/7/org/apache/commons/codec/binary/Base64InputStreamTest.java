import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testData;

    @Before
    public void setUp() {
        String testDataString = "TestString12345";
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamTemp = new ByteArrayInputStream(testDataString.getBytes());
        Base64 base64 = new Base64();
        inputStream = new Base64InputStream(inputStreamTemp, false);
        base64.encodeStream(inputStream, outputStream);
        this.inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        this.testData = new ByteArrayInputStream(testDataString.getBytes());
    }

    @After
    public void tearDown() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadByte() throws IOException {
        int readByte = inputStream.read();
        assertEquals(-1, readByte);

        inputStream = new Base64InputStream(testData, false);
        readByte = inputStream.read();
        assertEquals(testData.read(), readByte);

        inputStream = new Base64InputStream(testData, true);
        byte[] decodedByte = new byte[1];
        int readDecodedByte = inputStream.read(decodedByte, 0, 1);
        assertEquals(1, readDecodedByte);
        assertEquals(testData.read(), decodedByte[0] & 0xFF);
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] expected = new byte[10];
        byte[] result = new byte[10];

        inputStream = new Base64InputStream(testData, false);
        int readLength = inputStream.read(result, 0, 10);
        assertEquals(testData.available(), readLength);
        assertArrayEquals(expected, result);

        inputStream = new Base64InputStream(testData, true);
        base64.decode(result, 0, readLength);
        assertArrayEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        new Base64InputStream(inputStream, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() {
        new Base64InputStream(inputStream, 1, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidArraySize() {
        new Base64InputStream(inputStream, 1, 15);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream testIOExceptionInputStream = new IOException("Test IOException").wrapInput();
        new Base64InputStream(testIOExceptionInputStream, false);
    }
}