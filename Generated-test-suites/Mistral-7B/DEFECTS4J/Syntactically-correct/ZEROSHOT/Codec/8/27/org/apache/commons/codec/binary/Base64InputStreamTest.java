package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final byte[] ONE_BYTE_ARRAY = {1};
    private static final byte[] TWO_BYTES_ARRAY = {1, 2};
    private static final byte[] THREE_BYTES_ARRAY = {1, 2, 3};
    private static final byte[] FIVE_BYTES_ARRAY = {1, 2, 3, 4, 5};
    private static final byte[] FOURTEEN_BYTES_ARRAY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    private static final byte[] TWENTY_ONE_BYTES_ARRAY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};

    private InputStream inputStream;
    private Base64InputStream base64InputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(TWENTY_ONE_BYTES_ARRAY));
        this.inputStream = new Base64InputStream(inputStream);
        this.base64InputStream = new Base64InputStream(inputStream, false);
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodesData() throws IOException {
        byte[] expected = Base64.decodeBase64(Base64.encodeBase64(TWENTY_ONE_BYTES_ARRAY));
        int actual = base64InputStream.read(EMPTY_BYTE_ARRAY, 0, EMPTY_BYTE_ARRAY.length);
        assertTrue(actual == -1);
        actual = base64InputStream.read(ONE_BYTE_ARRAY, 0, ONE_BYTE_ARRAY.length);
        assertFalse(actual == -1);
        assertArrayEquals(expected, ONE_BYTE_ARRAY);
        int bytesRead = 0;
        while (actual > 0) {
            System.arraycopy(ONE_BYTE_ARRAY, 0, EMPTY_BYTE_ARRAY, bytesRead, ONE_BYTE_ARRAY.length);
            actual = base64InputStream.read(EMPTY_BYTE_ARRAY, bytesRead, EMPTY_BYTE_ARRAY.length);
            bytesRead += ONE_BYTE_ARRAY.length;
        }
        assertArrayEquals(expected, EMPTY_BYTE_ARRAY);
    }

    @Test
    public void testBase64InputStreamDecodesData() throws IOException {
        byte[] expected = TWENTY_ONE_BYTES_ARRAY;
        byte[] decoded = new byte[201];
        int actual = base64InputStream.read(decoded, 0, decoded.length);
        assertFalse(actual == -1);
        assertArrayEquals(expected, decoded, 0, expected.length);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamThrowsIOExceptionWhenEncounteringUnexpectedEndOfStream() throws IOException {
        byte[] inputBytes = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int bytesRead = 0;
        while (bytesRead < TWO_BYTES_ARRAY.length) {
            int numBytesRead = base64InputStream.read(EMPTY_BYTE_ARRAY, 0, EMPTY_BYTE_ARRAY.length);
            bytesRead += numBytesRead;
        }
        base64InputStream.read(EMPTY_BYTE_ARRAY, 0, EMPTY_BYTE_ARRAY.length);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamThrowsIOExceptionWhenMarkSupportedIsCalled() throws IOException {
        inputStream.mark(0);
        base64InputStream.markSupported();
    }

    @Test
    public void testBase64InputStreamDoesNotChangeOriginalInputStream() throws IOException {
        byte[] inputBytes = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream1 = new Base64InputStream(inputStream, false);
        Base64InputStream base64InputStream2 = new Base64InputStream(inputStream, false);
        assertArrayEquals(TWO_BYTES_ARRAY, new byte[2][]);
        assertArrayEquals(TWO_BYTES_ARRAY, Base64.decodeBase64(base64InputStream1.readAllBytes()));
        assertArrayEquals(TWO_BYTES_ARRAY, Base64.decodeBase64(base64InputStream2.readAllBytes()));
    }

    @Test
    public void testBase64InputStreamHandlesEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamHandlesNullInput() {
        InputStream inputStream = null;
        Base64InputStream base64InputStream = null;
        try {
            base64InputStream = new Base64InputStream(inputStream, false);
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // expected
        } finally {
            inputStream = null;
        }
    }

    @Test
    public void testBase64InputStreamHandlesNullOutput() {
        ByteArrayOutputStream outputStream = null;
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(outputStream, 0, 1);
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // expected
        } finally {
            outputStream = null;
        }
    }

    @Test
    public void testBase64InputStreamHandlesNegativeOffset() {
        byte[] expected = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(EMPTY_BYTE_ARRAY, -1, 1);
            assertFalse("Expected IndexOutOfBoundsException", true);
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            inputStream.close();
            base64InputStream.close();
        }
    }

    @Test
    public void testBase64InputStreamHandlesNegativeLength() {
        byte[] expected = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(EMPTY_BYTE_ARRAY, 0, -1);
            assertFalse("Expected IndexOutOfBoundsException", true);
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            inputStream.close();
            base64InputStream.close();
        }
    }

    @Test
    public void testBase64InputStreamHandlesNegativeBufferSize() {
        byte[] expected = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(EMPTY_BYTE_ARRAY, 0, Integer.MIN_VALUE);
            assertFalse("Expected IndexOutOfBoundsException", true);
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            inputStream.close();
            base64InputStream.close();
        }
    }

    @Test
    public void testBase64InputStreamHandlesInvalidLength() {
        byte[] expected = Base64.encodeBase64(TWO_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(EMPTY_BYTE_ARRAY, 0, EMPTY_BYTE_ARRAY.length + 1);
            assertFalse("Expected IndexOutOfBoundsException", true);
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            inputStream.close();
            base64InputStream.close();
        }
    }

    @Test
    public void testBase64InputStreamHandlesLongRead() throws IOException {
        byte[] expected = Base64.encodeBase64(FOURTEEN_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(FOURTEEN_BYTES_ARRAY));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[10];
        int bytesRead = 0;
        while (bytesRead < expected.length) {
            int numBytesRead = base64InputStream.read(buffer, bytesRead, buffer.length);
            bytesRead += numBytesRead;
        }
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testBase64InputStreamHandlesReadAllBytes() throws IOException {
        byte[] expected = Base64.encodeBase64(TWENTY_ONE_BYTES_ARRAY);
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(TWENTY_ONE_BYTES_ARRAY));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] actual = base64InputStream.readAllBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64InputStreamHandlesReadAllBytesWhenEncodedDataIsEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] actual = base64InputStream.readAllBytes();
        assertNull(actual);
    }
}