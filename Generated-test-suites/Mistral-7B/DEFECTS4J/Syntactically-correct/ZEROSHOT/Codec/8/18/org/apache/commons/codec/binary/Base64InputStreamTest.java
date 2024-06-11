package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream() {
        // Test Base64InputStream with default constructor
        try (Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("VG8gZXhwIHlvdSBuZWVkIHdvcmxkIHRoZSBwYWdlIGZvbnQtdG8gZXhwIHNlY3VyZXM=".getBytes()))) {
            int readByte = base64InputStream.read();
            Assert.assertNotEquals(-1, readByte);
            byte[] buffer = new byte[10];
            int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(bytesRead, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test Base64InputStream with decode mode and custom line length and line separator
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream("--\r\nVG8gZXhwIHlvdSBuZWVkIHdvcmxkIHRoZSBwYWdlIGZvbnQtdG8gZXhwIHNlY3VyZXMgWzFd".getBytes());
             Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 60, "--\r\n".getBytes())) {

            int readByte = base64InputStream.read();
            Assert.assertNotEquals(-1, readByte);
            byte[] buffer = new byte[10];
            int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(bytesRead, buffer.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test Base64InputStream with encode mode and custom line length and line separator
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Base64 base64 = new Base64(60, "--\r\n".getBytes());
             Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, 60, "--\r\n".getBytes())) {

            outputStream.write(base64InputStream.readAllBytes());
            String base64Encoded = new String(outputStream.toByteArray());
            Assert.assertEquals("--\r\nVG8gZXhwIHlvdSBuZWVkIHdvcmxkIHRoZSBwYWdlIGZvbnQtdG8gZXhwIHNlY3VyZXM=\r\n", base64Encoded);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}