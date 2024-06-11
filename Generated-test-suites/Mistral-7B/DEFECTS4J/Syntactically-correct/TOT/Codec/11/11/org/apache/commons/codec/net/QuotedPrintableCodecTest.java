package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding bytes using default charset.
     */
    @Test
    public void testEncodeDecodeDefaultCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x3F, 0x78, 0x41, 0x4D, 0x65, 0x64, 0x69, 0x64, 0x65, 0x78};
        byte[] expectedOutput = {ESCAPE_CHAR, '7', '8', '=', ESCAPE_CHAR, '3', 'F', '=', ESCAPE_CHAR, '4', '1',
                ESCAPE_CHAR, '4', 'D', ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6', '4', ESCAPE_CHAR, '6', '9',
                '=', ESCAPE_CHAR, '6', '4', '=', ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6', '9', ESCAPE_CHAR, '6'};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    /**
     * Test encoding and decoding bytes using specified charset.
     */
    @Test
    public void testEncodeDecodeCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        byte[] input = {0xE4, 0xBC, 0xA0, 0x3F, 0x78, 0x41, 0x4D, 0x65, 0x64, 0x69, 0x64, 0x65, 0x78};
        byte[] expectedOutput = {ESCAPE_CHAR, 'C3', 'BC', '=', ESCAPE_CHAR, '7', '8', '=', ESCAPE_CHAR, '3', 'F',
                '=', ESCAPE_CHAR, '4', '1', ESCAPE_CHAR, '4', 'D', ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6', '4',
                ESCAPE_CHAR, '6', '9', '=', ESCAPE_CHAR, '6', '4', '=', ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6',
                '9', ESCAPE_CHAR, '6'};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    /**
     * Test decoding invalid input.
     */
    @Test
    public void testDecodeInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertThrows(DecoderException.class, () -> codec.decode(new byte[]{ESCAPE_CHAR, ESCAPE_CHAR}));
        assertThrows(DecoderException.class,
                () -> codec.decode(new byte[]{ESCAPE_CHAR, '3', 'F', ESCAPE_CHAR, '4', '1', ESCAPE_CHAR, '4', 'D',
                        ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6', '9', ESCAPE_CHAR, '=', ESCAPE_CHAR, '6'}));
    }

    /**
     * Test decoding unescaped input.
     */
    @Test
    public void testDecodeUnescapedInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {ESCAPE_CHAR, '7', '8', ESCAPE_CHAR, '3', 'F', ESCAPE_CHAR, '4', '1', ESCAPE_CHAR, '4', 'D',
                ESCAPE_CHAR, '6', '5', ESCAPE_CHAR, '6', '4', ESCAPE_CHAR, '6', '9', ESCAPE_CHAR, '6'};

        assertArrayEquals(input, codec.decode(input));
    }

    /**
     * Test encoding empty input.
     */
    @Test
    public void testEncodeEmptyInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] expectedOutput = new byte[0];

        byte[] input = new byte[0];
        byte[] output = codec.encode(input);

        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test encoding string with default charset.
     */
    @Test
    public void testEncodeStringDefaultCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "\u003F\u0078Hello\u0041\u004DWorld\u0065\u0064\u0069\u0064\u0065\u0078";
        String expectedOutput = "=\u003F=\u0078=\u003F=\u0041=\u004D=\u0065=\u0064=\u0069=\u0064=\u0065=\u0078";

        String output = codec.encode(input);

        assertEquals(expectedOutput, output);
    }

    /**
     * Test encoding string with custom charset.
     */
    @Test
    public void testEncodeStringCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        String input = "\u00E4\u00BC\u00A0\u003F\u0078Hello\u0041\u004DWorld\u0065\u0064\u0069\u0064\u0065\u0078";
        String expectedOutput = "=\u00C3=\u00BC=\u00A0=\u003F=\u0078=\u003F=\u0041=\u004D=\u0065=\u0064=\u0069=\u0064=\u0065=\u0078";

        String output = codec.encode(input);

        assertEquals(expectedOutput, output);
    }

    /**
     * Test decoding string with default charset.
     */
    @Test
    public void testDecodeStringDefaultCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "=\u003F=\u0078=\u003F=\u0041=\u004D=\u0065=\u0064=\u0069=\u0064=\u0065=\u0078";
        String expectedOutput = "\u003F\u0078Hello\u0041\u004DWorld\u0065\u0064\u0069\u0064\u0065\u0078";

        String output = codec.decode(input.getBytes());

        assertEquals(expectedOutput, output);
    }

    /**
     * Test decoding string with custom charset.
     */
    @Test
    public void testDecodeStringCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        String input = "=\u00C3=\u00BC=\u00A0=\u003F=\u0078=\u003F=\u0041=\u004D=\u0065=\u0064=\u0069=\u0064=\u0065=\u0078";
        String expectedOutput = "\u00E4\u00BC\u00A0\u003F\u0078Hello\u0041\u004DWorld\u0065\u0064\u0069\u0064\u0065\u0078";

        String output = new String(codec.decode(input.getBytes()), "UTF-8");

        assertEquals(expectedOutput, output);
    }

    /**
     * Test encoding a null string.
     */
    @Test
    public void testEncodeNullString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertNull(codec.encode((String) null));
    }

    /**
     * Test decoding a null byte array.
     */
    @Test
    public void testDecodeNullByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertNull(codec.decode((byte[]) null));
    }

    /**
     * Test decoding an empty byte array.
     */
    @Test
    public void testDecodeEmptyByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertArrayEquals(new byte[0], codec.decode(new byte[0]));
    }
}