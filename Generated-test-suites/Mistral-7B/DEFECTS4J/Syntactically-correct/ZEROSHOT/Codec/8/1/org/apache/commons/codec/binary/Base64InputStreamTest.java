package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream inputStream;
    private InputStream originalInputStream;
    private byte[] inputData;
    private byte[] expectedOutput;

    @Before
    public void setUp() {
        inputData = "HelloWorld!".getBytes();
        originalInputStream = new ByteArrayInputStream(inputData);
        inputStream = new Base64InputStream(originalInputStream);

        // Test decoding
        expectedOutput = new byte[]{
                (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x2E, (byte) 0x48, (byte) 0x65,
                (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x72, (byte) 0x21, (byte) 0x00
        };
    }

    @Test
    public void testDecodeOneByte() throws IOException {
        byte testByte = (byte) inputStream.read();
        Assert.assertEquals(expectedOutput[0], testByte);
    }

    @Test
    public void testDecodeMultipleBytes() throws IOException {
        byte[] testBytes = new byte[expectedOutput.length];
        int numBytesRead = inputStream.read(testBytes, 0, testBytes.length);
        Assert.assertEquals(expectedOutput.length, numBytesRead);
        Assert.assertArrayEquals(expectedOutput, testBytes);
    }

    @Test
    public void testEncodeOneByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamBase64OutputStream encodedOutputStream = new OutputStreamBase64OutputStream(outputStream);
        encodedOutputStream.write(0x48);

        byte testByte = (byte) inputStream.read();
        Assert.assertEquals((byte) 0x48, testByte);

        byte[] output = outputStream.toByteArray();
        Assert.assertEquals(1, output.length);
        Assert.assertEquals("A", new String(output, "ASCII"));
    }

    @Test
    public void testEncodeMultipleBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamBase64OutputStream encodedOutputStream = new OutputStreamBase64OutputStream(outputStream);

        String testString = "HelloWorld!";
        byte[] inputBytes = testString.getBytes();
        inputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true);

        int bytesRead = 0;
        byte[] testBytes = new byte[inputBytes.length];
        while (bytesRead < inputBytes.length) {
            int numBytesRead = inputStream.read(testBytes, bytesRead, inputBytes.length - bytesRead);
            Assert.assertNotEquals(0, numBytesRead);
            bytesRead += numBytesRead;
        }

        encodedOutputStream.write(testBytes, 0, testBytes.length);
        encodedOutputStream.flush();

        byte[] output = outputStream.toByteArray();
        String outputString = new String(output, "ASCII");
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", outputString);
    }
}

class OutputStreamBase64OutputStream extends FilterOutputStream {

    private final Base64 base64;
    private byte[] lineSeparator = new byte[] { (byte) 0x0D, (byte) 0x0A };

    public OutputStreamBase64OutputStream(OutputStream out) {
        super(out);
        this.base64 = new Base64(76, lineSeparator);
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        base64.encode(b, off, len);
        out.write(base64.getOutput());
        out.write(lineSeparator);
    }
}