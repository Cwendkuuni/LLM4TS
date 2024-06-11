package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    private static final String CHARSET = "UTF-8";

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CHARSET);

        byte[] input = "Hello, World!".getBytes(CHARSET);
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, CHARSET);
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, CHARSET);

        Assert.assertArrayEquals(input, decoded);
        Assert.assertEquals(encodedString, codec.encode(decodedString));
    }

    @Test
    public void testEncodeDecodeMixed() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CHARSET);

        String input = "Hello, World!\n\t12345\n67890";
        byte[] encoded = codec.encode(input.getBytes(CHARSET));
        String encodedString = new String(encoded, CHARSET);
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, CHARSET);

        Assert.assertEquals(input, decodedString);
        Assert.assertEquals(encodedString, codec.encode(decodedString));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CHARSET);

        String input = "Hello, World!\n\u0000";
        byte[] encoded = codec.encode(input.getBytes(CHARSET)); // CTRL-Char should not be encoded
    }

    @Test(expected = DecoderException.class)
    public void testDecodeException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CHARSET);

        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', ','}; // Missing '=' sign
        byte[] decoded = codec.decode(encoded);
    }

    @Test
    public void testEncodeLineBreaks() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CHARSET);

        byte[] input = "Line 1\nLine 2\nLine 3".getBytes(CHARSET);
        byte[] encoded = codec.encode(input);

        String encodedString = new String(encoded, CHARSET);
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, CHARSET);

        Assert.assertEquals(decodedString, "Line 1\nLine 2\nLine 3");
        Assert.assertEquals(encodedString, codec.encode(decodedString));
    }
}