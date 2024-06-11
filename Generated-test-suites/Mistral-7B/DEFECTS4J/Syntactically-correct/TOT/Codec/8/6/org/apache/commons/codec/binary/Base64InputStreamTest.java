package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };

    /**
     * Test for Base64InputStream constructor with default parameters.
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] result = new byte[TEST_DATA.length];
        int bytesRead = base64InputStream.read(result, 0, TEST_DATA.length);
        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, result);
        base64InputStream.close();
    }

    /**
     * Test for Base64InputStream constructor with encoding set to true.
     * @throws IOException
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, Base64.CRLF);
        byte[] result = new byte[TEST_DATA.length * 4 / 3 + 2]; // approximate length
        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, result.length);
        assertArrayEquals(base64OutputStream.toByteArray(), result);
        base64InputStream.close();
        base64OutputStream.close();
    }

    /**
     * Test for Base64InputStream constructor with encoding set to false.
     * @throws IOException
     */
    @Test
    public void testDecodeConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(TEST_DATA);
        base64OutputStream.close();
        ByteArrayInputStream encodedInput = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(encodedInput, false);
        byte[] result = new byte[TEST_DATA.length];
        int bytesRead = base64InputStream.read(result, 0, TEST_DATA.length);
        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, result);
        base64InputStream.close();
    }

    /**
     * Test for Base64InputStream with markSupport method.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.mark(0);
    }

    /**
     * Test for Base64InputStream with reset method.
     * @throws IOException
     */
    @Test
    public void testReset() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.mark(0);
        base64InputStream.reset();
        byte[] result = new byte[TEST_DATA.length];
        int bytesRead = base64InputStream.read(result, 0, TEST_DATA.length);
        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, result);
    }

    /**
     * Test for Base64InputStream with incorrect line length.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, -1, Base64.CRLF);
    }

    /**
     * Test for Base64InputStream with null line separator.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testNullLineSeparator() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, null);
    }

    /**
     * Test for Base64InputStream with no data.
     * @throws IOException
     */
    @Test
    public void testNoData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] result = new byte[0];
        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertFalse(bytesRead > 0);
        base64InputStream.close();
    }

    /**
     * Test for Base64InputStream with EOF.
     * @throws IOException
     */
    @Test
    public void testEndOfFile() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] result = new byte[TEST_DATA.length * 4 / 3 + 1]; // approximate length
        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(-1, bytesRead);
        assertNull(base64InputStream.read(result, 0, TEST_DATA.length));
        base64InputStream.close();
    }
}