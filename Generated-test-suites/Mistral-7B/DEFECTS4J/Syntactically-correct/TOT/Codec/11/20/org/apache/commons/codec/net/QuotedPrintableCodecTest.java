package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeAndDecodeByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x61, 0x62, 0x63, 0x64};
        byte[] expectedOutput = {0x61, 0x62, 0x3D, 0x31, 0x30, 0x33, 0x3D, 0x31, 0x36, 0x32, 0x3D, 0x31, 0x36, 0x33, 0x3D, 0x31, 0x36, 0x34};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abcd";
        String expectedOutput = "abcd=\r\n=\r\n313033=\r\n313632=\r\n313633=\r\n313634";
        String output = codec.encode(input, StandardCharsets.UTF_8);
        String decodedOutput = new String(codec.decode(output.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertEquals(input, decodedOutput);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidByte() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x00};
        codec.encode(input); // This should throw an exception
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidByteSequence() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x61, 0x62, 0x3D, 0x31, 0x30, 0x33, 0x3D, 0x31, 0x36, 0x33, 0x3D, 0x31, 0x36, 0x34, 0x3D, 0x31, 0x36, 0x35};
        codec.decode(input); // This should throw an exception
    }
}