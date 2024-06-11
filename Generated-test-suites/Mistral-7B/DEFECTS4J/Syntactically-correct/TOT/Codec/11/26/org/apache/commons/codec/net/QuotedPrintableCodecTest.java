package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World! こんにちは世界！";
        byte[] inputBytes = StringUtils.getBytesUsAscii(input);
        byte[] encodedBytes = codec.encode(inputBytes);
        String encodedString = StringUtils.newStringUsAscii(encodedBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, codec.getDefaultCharset());

        Assert.assertArrayEquals("Encoded and decoded bytes should match the original input bytes", inputBytes, decodedBytes);
        Assert.assertEquals("Encoded and decoded strings should match the original input string", input, decodedString);
        Assert.assertEquals("Encoded string should match the encoded bytes as a string", new String(encodedBytes), encodedString);
    }

    @Test
    public void testEncodeEscapeWhitespace() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World!\t  Testing whitespace\t escaping.";
        byte[] inputBytes = StringUtils.getBytesUsAscii(input);
        byte[] encodedBytes = codec.encode(inputBytes);
        String encodedString = StringUtils.newStringUsAscii(encodedBytes);

        Assert.assertNotEquals("Encoded string should have more length than the input string due to whitespace escaping", input.length(), encodedString.length());
        for (int i = 0; i < encodedString.length(); i++) {
            if (encodedString.charAt(i) == '=') {
                Assert.assertTrue("Encoded string should alternate between '=' and hex digits for each whitespace character", (i % 2) == 0);
            }
        }

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, codec.getDefaultCharset());

        Assert.assertEquals("Decoded string should match the original input string", input, decodedString);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeIllegalCharacter() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello 世"; // contains an illegal character for quoted-printable encoding
        byte[] inputBytes = StringUtils.getBytesUsAscii(input);
        codec.encode(inputBytes); // should throw an EncoderException
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String invalidInput = "=?Hello=?Q\n=?World=?Q"; // missing whitespace between '=' and hex digits
        byte[] invalidInputBytes = StringUtils.getBytesUsAscii(invalidInput);
        codec.decode(invalidInputBytes); // should throw a DecoderException
    }

    @Test
    public void testDecodeInvalidLineEnding() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String invalidInput = "Hello World!\r\nTesting line ending\r escaping.";
        byte[] invalidInputBytes = StringUtils.getBytesUsAscii(invalidInput);
        byte[] decodedBytes = codec.decode(invalidInputBytes);
        String decodedString = new String(decodedBytes, codec.getDefaultCharset());

        Assert.assertEquals("Decoded string should match the original input string", input, decodedString);
    }
}