package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * QuotedPrintableCodecTest.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test encode and decode for ASCII characters.
     */
    @Test
    public void testEncodeDecodeAscii() throws Exception {
        byte[] input = "Hello World!".getBytes("UTF-8");
        byte[] encoded = codec.encode(input);
        String encodedString = StringUtils.newStringUsAscii(encoded);
        assertEquals("14HellohelloWorld!0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, "UTF-8");
        assertEquals("Hello World!", decodedString);
    }

    /**
     * Test encode and decode for non-ASCII characters.
     */
    @Test
    public void testEncodeDecodeNonAscii() throws Exception {
        String input = "你好，世界！";
        byte[] encodedBytes = codec.encode(input.getBytes("UTF-8"));
        String encodedString = StringUtils.newStringUsAscii(encodedBytes);

        // This is a simplified test case as the actual encoded bytes would be quite long.
        // We assume the encoded string starts with "2E 31 82 ..." and ends with "0D 0D"
        String expectedEncodedString = "2E 31 82 E5 AD 97 D5 BC 8A 82 ...";
        assertEquals(expectedEncodedString, encodedString);

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, "UTF-8");
        assertEquals(input, decodedString);
    }

    /**
     * Test encode and decode for whitespace characters.
     */
    @Test
    public void testEncodeDecodeWhitespace() throws Exception {
        String input = "\t Hello\nWorld!";
        byte[] inputBytes = input.getBytes("UTF-8");
        byte[] encodedBytes = codec.encode(inputBytes);

        String encodedString = StringUtils.newStringUsAscii(encodedBytes);
        String expectedEncodedString = "7D 0909 0D 48 65 6C 6C 6F 20 48 65 6C 6C 6F 77 21 0D";
        assertEquals(expectedEncodedString, encodedString);

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, "UTF-8");
        assertEquals(input, decodedString);
    }

    /**
     * Test encode for invalid data.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidData() {
        // Invalid data
        codec.encode(new byte[]{0x00});
    }

    /**
     * Test decode for invalid data.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() {
        byte[] invalidData = new byte[]{ESCAPE_CHAR};
        codec.decode(invalidData);
    }
}