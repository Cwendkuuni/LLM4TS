package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        byte[] inputData = "Hello World!".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        this.outputStream = outputStream;
        this.inputStream = new Base64InputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
        if (this.outputStream != null) {
            this.outputStream.close();
        }
    }

    @Test
    public void testDecodeDefaultConstructor() throws IOException {
        byte[] expectedOutput = "SGVsbG8gV29ybGQh".getBytes();
        byte[] buffer = new byte[expectedOutput.length];
        int length = this.inputStream.read(buffer);
        assertEquals(expectedOutput.length, length);
        assertEquals(expectedOutput, buffer);
    }

    @Test
    public void testDecodeCustomLineLengthAndLineSeparator() throws IOException {
        byte[] expectedOutput = "SGVsbG8gV29ybGQh\r\n".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(this.inputStream, false, 72, "\r\n".getBytes());
        byte[] buffer = new byte[expectedOutput.length];
        int length = -1;
        while (length < 0) {
            length = base64InputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, length);
        }
        byte[] actualOutput = outputStream.toByteArray();
        assertEquals(expectedOutput.length, actualOutput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testDecodeNullByteArray() {
        new Base64InputStream(this.inputStream, false).read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeNegativeIndex() {
        new Base64InputStream(this.inputStream, false).read(new byte[10], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeInvalidOffset() {
        new Base64InputStream(this.inputStream, false).read(new byte[10], 12, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeInvalidLength() {
        new Base64InputStream(this.inputStream, false).read(new byte[10], 0, 12);
    }

    @Test
    public void testEncodeDefaultConstructor() throws IOException {
        byte[] inputData = "Hello World!".getBytes();
        byte[] expectedOutput = new byte[inputData.length * 4 / 3 + 4];
        byte[] buffer = new byte[expectedOutput.length];
        int length = -1;
        Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream(expectedOutput.length));
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true);
        while (length < 0) {
            length = base64InputStream.read(buffer, 0, buffer.length);
            base64OutputStream.write(buffer, 0, length);
        }
        byte[] actualOutput = base64OutputStream.toByteArray();
        byte[] decodedOutput = new Base64InputStream(new ByteArrayInputStream(actualOutput)).readAllBytes();
        assertEquals(inputData.length, decodedOutput.length);
        assertEquals(inputData, decodedOutput);
    }

    @Test
    public void testEncodeCustomLineLengthAndLineSeparator() throws IOException {
        byte[] inputData = "Hello World!".getBytes();
        byte[] expectedOutput = new byte[]{"SGVsbG8gV29ybGQh\r\n".getBytes()[0], "SGVsbG8gV29ybGQh\r\n".getBytes()[1], "SGVsbG8gV29ybGQh\r\n".getBytes()[2], "SGVsbG8gV29ybGQh\r\n".getBytes()[3], "SGVsbG8gV29ybGQh\r\n".getBytes()[4], "SGVsbG8gV29ybGQh\r\n".getBytes()[5], "SGVsbG8gV29ybGQh\r\n".getBytes()[6], "SGVsbG8gV29ybGQh\r\n".getBytes()[7], "SGVsbG8gV29ybGQh\r\n".getBytes()[8], "SGVsbG8gV29ybGQh\r\n".getBytes()[9]};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true, 72, "\r\n".getBytes());
        byte[] buffer = new byte[expectedOutput.length];
        int length = -1;
        while (length < 0) {
            length = base64InputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, length);
        }
        byte[] actualOutput = outputStream.toByteArray();
        byte[] decodedOutput = new Base64InputStream(new ByteArrayInputStream(actualOutput)).readAllBytes();
        assertEquals(inputData.length, decodedOutput.length);
        assertEquals(inputData, decodedOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeNullByteArray() {
        new Base64InputStream(this.inputStream, true).read(null, 0, 1);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] inputData = "Hello World!".getBytes();
        byte[] expectedOutput = "SGVsbG8gV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQhV29ybGQh".getBytes();
        byte[] buffer = new byte[expectedOutput.length];
        byte[] output = new Base64InputStream(new ByteArrayInputStream(inputData), false).readAllBytes();
        assertEquals(expectedOutput.length, output.length);
        assertEquals(expectedOutput, output);
    }
}