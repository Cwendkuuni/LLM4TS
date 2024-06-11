package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Tests for the Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Tests the default constructor and the default behaviour of decoding.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDefaultConstructorAndDefaultDecoding() throws IOException {
        String base64Data = "VG8gZXhwIHRoYXQgdGhpbmdzIHVwIHlvdSBmaWxl";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }

        assertEquals("Decoded data should be equal to the original data.", new String(inputStream.readAllBytes()),
                new String(outputStream.toByteArray()));

        base64InputStream.close();
        outputStream.close();
        inputStream.close();
    }

    /**
     * Tests the constructor with the given boolean doEncode parameter and the default behaviour of decoding.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testConstructorWithDoEncodeFalseAndDefaultDecoding() throws IOException {
        String base64Data = "VG8gZXhwIHRoYXQgdGhpbmdzIHVwIHlvdSBmaWxl";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        InputStream expectedInputStream = new ByteArrayInputStream(base64Data.getBytes());

        byte[] buffer = new byte[1024];
        int read = 0;

        while ((read = base64InputStream.read(buffer)) > 0) {
            // Asserting that read data is the same as the expected data.
            assertArrayEquals("Read data should be equal to the expected data.", expectedInputStream.read(buffer), buffer, 0, read);
        }

        base64InputStream.close();
        expectedInputStream.close();
    }

    /**
     * Tests the constructor with the given boolean doEncode parameter and the given lineLength and lineSeparator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testConstructorWithDoEncodeTrueAndLineLengthAndLineSeparator() throws IOException {
        String base64Data = "VG8gZXhwIHRoYXQgdGhpbmdzIHVwIHlvdSBmaWxl";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64 base64 = new Base64(76, "\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, base64.getLineLength(), base64.getLineSeparator());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];

        while ((read = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }

        String decodedData = new String(outputStream.toByteArray());
        assertEquals("Decoded data should be equal to the original data.", new String(inputStream.readAllBytes()), decodedData);

        base64InputStream.close();
        outputStream.close();
        inputStream.close();
    }
}