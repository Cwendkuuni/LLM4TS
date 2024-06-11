package org.apache.commons.codec.net;

import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Utils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Test
    public void testEncodeAndDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");

        byte[] input = "Héllo Wórld!".getBytes(StandardCharsets.ISO_8859_1);
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeWithBitSet() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet printable = new BitSet(256);
        printable.set(BitSet.BIT0, BitSet.BIT32, BitSet.BIT33, BitSet.BIT34, BitSet.BIT35, BitSet.BIT36, BitSet.BIT37,
                BitSet.BIT59, BitSet.BIT60, BitSet.BIT61, BitSet.BIT62, BitSet.BIT63, BitSet.BIT126, BitSet.BIT127,
                BitSet.BIT128, BitSet.BIT129, BitSet.BIT130, BitSet.BIT131, BitSet.BIT132, BitSet.BIT133, BitSet.BIT134,
                BitSet.BIT135, BitSet.BIT136, BitSet.BIT137, BitSet.BIT138, BitSet.BIT139, BitSet.BIT140, BitSet.BIT141,
                BitSet.BIT142, BitSet.BIT143, BitSet.BIT144, BitSet.BIT145, BitSet.BIT146, BitSet.BIT147, BitSet.BIT148,
                BitSet.BIT149, BitSet.BIT150, BitSet.BIT151, BitSet.BIT152, BitSet.BIT153, BitSet.BIT154, BitSet.BIT155,
                BitSet.BIT156, BitSet.BIT157, BitSet.BIT158, BitSet.BIT159, BitSet.BIT160, BitSet.BIT161, BitSet.BIT162,
                BitSet.BIT163, BitSet.BIT164, BitSet.BIT165, BitSet.BIT166, BitSet.BIT167, BitSet.BIT168, BitSet.BIT169,
                BitSet.BIT170, BitSet.BIT171, BitSet.BIT172, BitSet.BIT173, BitSet.BIT174, BitSet.BIT175, BitSet.BIT176,
                BitSet.BIT177);

        byte[] input = new byte[]{'H', 'é', 'l', 'l', 'o', ' ', 'W', 'ór', 'l', 'd', '!'};
        byte[] encoded = codec.encode(input, printable);
        byte[] decoded = codec.decode(encoded, printable);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeWithWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello\nWorld!".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(input);

        byte[] decoded = codec.decode(encoded);
        Assert.assertEquals("Hello\nWorld!", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeAndDecodeWithLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\n".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(input);

        byte[] decoded = codec.decode(encoded);
        Assert.assertEquals("\n", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeAndDecodeWithCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\r".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(input);

        byte[] decoded = codec.decode(encoded);
        Assert.assertEquals("\r", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeAndDecodeWithCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\r\n".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(input);

        byte[] decoded = codec.decode(encoded);
        Assert.assertEquals("\r\n", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidByte() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{(byte) 0x80};

        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'=', 'A', 'B', '='};

        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingEquals() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', '=', '='};

        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeUnterminatedLine() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', '=', '='};

        codec.decode(encoded, 0, 6);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{(byte) 0x81};

        codec.decode(encoded, "ISO-8859-1");
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        codec.encode(null);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidByte() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{(byte) 0xFF};

        codec.encode(input);
    }

    @Test
    public void testEncodeLineLength() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[4096];
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte) (i % 128);
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        codec.encode(input, output);

        byte[] encoded = output.toByteArray();

        int lineCount = 0;
        int index = 0;
        while (index < encoded.length) {
            if (encoded[index++] == '\r') {
                lineCount++;
                if (lineCount > 32) {
                    throw new AssertionError("Exceeded maximum line length");
                }
                index++;
                if (encoded[index++] != '\n') {
                    throw new AssertionError("Missing LF after CR");
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        codec.decode(null);
    }
}