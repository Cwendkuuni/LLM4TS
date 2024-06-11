package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * QuotedPrintableCodecTest class for testing QuotedPrintableCodec class methods.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec(StandardCharsets.UTF_8);

    /**
     * Test encode method with plain ASCII string.
     */
    @Test
    public void testEncodePlainASCII() {
        String input = "Hello World!";
        byte[] expected = new byte[]{0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x21, 0x0d, 0x0a};
        byte[] result = CODEC.encode(input);
        assertNotNull(result);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    /**
     * Test encode method with binary data.
     */
    @Test
    public void testEncodeBinaryData() {
        byte[] input = {0x01, 0x02, 0x03, 0x04};
        BitSet printable = new BitSet(256);
        byte[] expected = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);
        byte[] result = CODEC.encode(input);
        assertNotNull(result);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    /**
     * Test decode method with plain ASCII string.
     */
    @Test
    public void testDecodePlainASCII() {
        String input = "=48=65=6c=6c=6f=21=0d=0a";
        String expected = "Hello World!";
        String result = CODEC.decode(input);
        assertEquals(expected, result);
    }

    /**
     * Test decode method with quoted-printable encoded binary data.
     */
    @Test
    public void testDecodeQuotedPrintableBinaryData() {
        byte[] input = new byte[]{0x41, 0x42, 0x43, 0x44, 0x61, 0x72, 0x61, 0x63, 0x61, 0x61, 0x72, 0x61, 0x73, 0x73};
        byte[] expected = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] result = CODEC.decode(StringUtils.newStringUsAscii(input));
        assertNotNull(result);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    /**
     * Test encode method with unsupported charset.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeUnsupportedCharset() {
        String input = "你好"; // Chinese characters
        CODEC.encode(input);
    }

    /**
     * Test decode method with unsupported encoding.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeUnsupportedEncoding() {
        byte[] input = new byte[]{0x41, 0x42, 0x43, 0x44};
        CODEC.decode(new String(input));
    }
}