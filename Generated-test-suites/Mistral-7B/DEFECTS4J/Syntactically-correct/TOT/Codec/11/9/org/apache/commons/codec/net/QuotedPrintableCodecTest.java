package org.apache.commons.codec.net.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import org.junit.Test;

/**
 * Unit tests for {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeAndDecodeByteArray() throws UnsupportedEncodingException, DecoderException {
        byte[] input = { 0x3F, 0x3F, 0x3F, 0x2D, 0x31, 0x30, 0x2D, 0x31, 0x30, 0x31, 0x2D, 0x31, 0x30, 0x31 };
        byte[] expectedOutput = Hex.decodeHex("3F3F3F-10-10-11-10-11-10-11-11-10-11");

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeString() throws UnsupportedEncodingException, DecoderException {
        String input = "~ського~-10-10-11-10-11-10-11-11-10-11";
        String expectedOutput = "~\\x3F\\x3F\\-10-10-11-10-11-10-11-11-10-11";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output, codec.getDefaultCharset());
        assertEquals(input, decodedOutput);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testInvalidCharsetForDecodeString() throws UnsupportedEncodingException {
        String input = "~gorithms\\xFF-10-10-11-10-11-10-11-11-10-11";
        String output = codec.encode(input);

        codec.decode(output, "invalid-charset");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        byte[] input = null;
        byte[] expectedOutput = null;

        byte[] output = codec.encode(input);

        codec.decode(output, codec.getDefaultCharset());

        assertNull(output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutput() {
        byte[] input = new byte[] { 0x3F, 0x3F, 0x3F };
        byte[] output = null;

        codec.decode(output, codec.getDefaultCharset());

        assertNull(output);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidByte() throws UnsupportedEncodingException {
        byte[] input = { 0x7F, 0xFF, 0xFF };
        byte[] expectedOutput = new byte[] { 0x3D, (byte) 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, (byte) 0x7F };

        byte[] output = codec.encode(input);

        assertArrayEquals(expectedOutput, output);

        output = codec.encode(new byte[] { 0xFF });

        throw new EncoderException("Encoding failed");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidByte() throws UnsupportedEncodingException {
        byte[] input = Hex.decodeHex("3F3F3F-10-10-11-10-11-10-11-11-10-11-FF");

        codec.decode(input, codec.getDefaultCharset());

        throw new DecoderException("Decoding failed");
    }
}