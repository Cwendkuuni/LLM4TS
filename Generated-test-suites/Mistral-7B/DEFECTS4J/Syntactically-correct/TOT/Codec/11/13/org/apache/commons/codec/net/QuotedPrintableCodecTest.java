package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding of bytes using default charset
     */
    @Test
    public void testEncodeBytes() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x61, 0x20, 0x73, 0x65, 0x74, 0x5F, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x6E, 0x74, 0x61, 0x72, 0x74, 0x2E, 0x63, 0x61, 0x6D, 0x65, 0x74};
        byte[] expected = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x2D, 0x36, 0x32, 0x36, 0x36, 0x31, 0x36, 0x34, 0x34, 0x34, 0x34, 0x2E, 0x63, 0x61, 0x6D, 0x65, 0x74};
        byte[] output = codec.encode(input);
        Assert.assertArrayEquals(expected, output);
    }

    /**
     * Test decoding of bytes using default charset
     */
    @Test
    public void testDecodeBytes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x31, 0x30, 0x2D, 0x36, 0x32, 0x36, 0x36, 0x31, 0x36, 0x34, 0x34, 0x34, 0x34, 0x2E, 0x63, 0x61, 0x6D, 0x65, 0x74};
        byte[] expected = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
        byte[] output = codec.decode(input);
        Assert.assertArrayEquals(expected, output);
    }

    /**
     * Test encoding of String using default charset
     */
    @Test
    public void testEncodeString() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, World!";
        String expected = "68656C6C6F2C20576F726C6421";
        String output = codec.encode(input);
        Assert.assertEquals(expected, output);
    }

    /**
     * Test decoding of String using default charset
     */
    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "68656C6C6F2C20576F726C6421";
        String expected = "Hello, World!";
        String output = new String(codec.decode(Hex.decodeHex(input.toCharArray())), codec.getDefaultCharset());
        Assert.assertEquals(expected, output);
    }

    /**
     * Test encoding of String with invalid charset
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeStringInvalidCharset() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, World!";
        String charset = "InvalidCharset";
        codec.encode(input, charset);
    }

    /**
     * Test decoding of String with invalid charset
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringInvalidCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "68656C6C6F2C20576F726C6421";
        String charset = "InvalidCharset";
        codec.decode(input, charset);
    }
}