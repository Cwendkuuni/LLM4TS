package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test Base64InputStream constructor with default settings (decode).
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = { 1, 2, 3, 4, 5 };
        byte[] actual = new byte[5];
        int len = base64InputStream.read(actual, 0, actual.length);
        assertEquals(len, 5);
        assertArrayEquals(expected, actual);

        base64InputStream.close();
    }

    /**
     * Test Base64InputStream constructor with encode flag.
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        String input = "HelloWorld";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64 base64 = new Base64();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64.encodeStream(inputStream, outputStream);

        byte[] expected = outputStream.toByteArray();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(expected), true);

        byte[] actual = new byte[6];
        int len = base64InputStream.read(actual, 0, actual.length);
        assertEquals(len, 6);
        assertArrayEquals(new byte[] { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' }, actual);

        base64InputStream.close();
    }

    /**
     * Test Base64InputStream constructor with different line length and line separator.
     */
    @Test
    public void testCustomLineLengthAndLineSeparatorConstructor() throws IOException {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64 base64 = new Base64(72, "\r\n\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, "\r\n\r\n".getBytes());

        byte[] expected = { 1, 2, 3, 4, 5 };
        byte[] actual = new byte[5];
        int len = base64InputStream.read(actual, 0, actual.length);
        assertEquals(len, 5);
        assertArrayEquals(expected, actual);

        byte[] lineSeparator = new byte[4];
        len = base64InputStream.read(lineSeparator, 0, lineSeparator.length);
        assertArrayEquals("\r\n\r\n".getBytes(), lineSeparator);

        base64InputStream.close();
    }

    /**
     * Test Base64InputStream when there is no data.
     */
    @Test
    public void testNoData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] actual = new byte[1];
        int len = base64InputStream.read(actual, 0, actual.length);
        assertFalse(len > 0);
        assertNull(actual);

        base64InputStream.close();
    }

    /**
     * Test Base64InputStream when there is invalid data.
     */
    @Test(expected = IOException.class)
    public void testInvalidData() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] actual = new byte[1];

        int len = base64InputStream.read(actual, 0, actual.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { -1 }, actual);

        len = base64InputStream.read(actual, 0, actual.length);
        assertEquals(len, -1);

        base64InputStream.close();
    }
}