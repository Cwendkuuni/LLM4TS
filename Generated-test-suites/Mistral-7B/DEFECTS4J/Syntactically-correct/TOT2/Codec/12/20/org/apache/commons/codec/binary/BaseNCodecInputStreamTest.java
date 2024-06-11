package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodecInputStream baseNCodecInputStream;
    private ByteArrayOutputStream outputStream;
    private BaseNCodec baseNCodec;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(BaseNCodec.ENCODED_DATA_ARRAY);
    }

    

    @Test
    public void testConstructor() {
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        Assert.assertNotNull(baseNCodecInputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = baseNCodecInputStream.read();
        Assert.assertNotEquals(-1, readByte);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] bytes = new byte[5];
        int readBytes = baseNCodecInputStream.read(bytes, 0, bytes.length);
        Assert.assertEquals(bytes.length, readBytes);
    }

    @Test
    public void testMarkSupported() {
        Assert.assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testClose() throws IOException {
        baseNCodecInputStream.close();
        Assert.assertTrue(inputStream.markSupported());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.close();
        inputStream.close();
        baseNCodecInputStream.read();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        baseNCodecInputStream = new BaseNCodecInputStream(null, baseNCodec, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        byte[] bytes = new byte[5];
        baseNCodecInputStream.read(bytes, 5, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test
    public void testReadPastEOF() throws IOException {
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        int readByte = baseNCodecInputStream.read();
        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadEmptyInputStream() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        int readByte = baseNCodecInputStream.read();
        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadNegativeLength() throws IOException {
        byte[] bytes = new byte[5];
        int readBytes = baseNCodecInputStream.read(bytes, -1, 1);
        Assert.assertEquals(-1, readBytes);
    }

    @Test
    public void testDecode() throws IOException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(BaseNCodec.ENCODED_DATA_ARRAY);
        BaseNCodec baseNCodec = new BaseNCodec();
        BinaryDecoder decoder = baseNCodec.getDecoder();
        byte[] decodedData = new byte[BaseNCodec.ENCODED_DATA_LENGTH];
        int length = 0;
        byte[] buffer = new byte[BaseNCodec.BUFFER_SIZE];
        byte[] inputBuffer = new byte[BaseNCodec.BUFFER_SIZE];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        while (baseNCodecInputStream.read(buffer, 0, BaseNCodec.BUFFER_SIZE) != -1) {
            length += decoder.decode(buffer, 0, buffer.length, decodedData, 0, decodedData.length - length);
        }
        Assert.assertArrayEquals(BaseNCodec.DECODED_DATA_ARRAY, decodedData);
    }

    @Test
    public void testEncode() throws IOException, EncoderException {
        Base32 base32 = new Base32();
        BaseNCodec baseNCodec = new BaseNCodec();
        BinaryEncoder encoder = baseNCodec.getEncoder();
        byte[] encodedData = new byte[BaseNCodec.ENCODED_DATA_LENGTH];
        byte[] decodedData = new byte[BaseNCodec.DECODED_DATA_LENGTH];
        byte[] inputData = new byte[BaseNCodec.INPUT_DATA_LENGTH];
        byte[] buffer = new byte[BaseNCodec.BUFFER_SIZE];
        byte[] outputBuffer = new byte[BaseNCodec.BUFFER_SIZE];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseNCodec.INPUT_DATA_ARRAY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        while (baseNCodecInputStream.read(buffer, 0, BaseNCodec.BUFFER_SIZE) != -1) {
            encoder.encode(buffer, 0, buffer.length, outputBuffer, 0, outputBuffer.length);
        }
        IOUtils.write(outputBuffer, 0, outputStream);
        Assert.assertArrayEquals(BaseNCodec.ENCODED_DATA_ARRAY, Base32.decode(outputStream.toString().getBytes()));
    }

    
}