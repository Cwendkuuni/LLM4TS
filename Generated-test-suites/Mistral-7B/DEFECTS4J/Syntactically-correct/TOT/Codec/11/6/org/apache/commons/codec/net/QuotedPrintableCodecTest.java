package org.apache.commons.codec.net;

import static org.apache.commons.codec.CharEncoding.US_ASCII;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of raw bytes
     */
    @Test
    public void testEncodeDecodeBytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test encoding
        byte[] bytes = new byte[] { 0x01, 0x2A, 0x3F, 0x42, 0x5C, 0x60, 0x7D, 0x8B };
        byte[] encodedBytes = codec.encode(bytes);
        byte[] decodedBytes = codec.decode(encodedBytes);

        // test decoded bytes are identical to original bytes
        assertArrayEquals(bytes, decodedBytes);

        // test encoding and decoding of specific bytes
        byte bTab = 0x09;
        byte bSpace = 0x20;
        byte bCRLF = 0x0D;
        byte bLF = 0x0A;

        byte[] testBytes = new byte[] { bTab, bSpace, bCRLF, bLF, bTab, bSpace };
        byte[] testEncodedBytes = new byte[testBytes.length + 2];
        System.arraycopy(testBytes, 0, testEncodedBytes, 0, testBytes.length);
        encodeQuotedPrintable(testBytes, testEncodedBytes, 0);

        byte[] testDecodedBytes = codec.decode(testEncodedBytes);
        assertArrayEquals(testBytes, testDecodedBytes);
    }

    /**
     * Test encoding and decoding of strings
     */
    @Test
    public void testEncodeDecodeStrings() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test encoding and decoding of ASCII strings
        String originalString = "Test\r\nTab Space\tTest";
        String encodedString = codec.encode(originalString, US_ASCII).toString(US_ASCII);
        String decodedString = new String(codec.decode(encodedString.getBytes(US_ASCII)), US_ASCII);
        assertEquals(originalString, decodedString);

        // test encoding and decoding of non-ASCII strings (UTF-8)
        String originalStringUtf8 = "Test\r\nTab Space\tTest";
        String encodedStringUtf8 = codec.encode(originalStringUtf8, "UTF-8").toString("UTF-8");
        String decodedStringUtf8 = new String(codec.decode(encodedStringUtf8.getBytes("UTF-8")), "UTF-8");
        assertEquals(originalStringUtf8, decodedStringUtf8);
    }

    /**
     * Test encoding of specific characters
     */
    @Test
    public void testEncodeSpecificCharacters() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test encoding of control characters
        byte bDel = 0x7F;
        byte[] controlChars = new byte[] { 0x00, 0x01, bDel };
        byte[] encodedControlChars = codec.encode(controlChars);
        String encodedControlCharsString = new String(encodedControlChars, US_ASCII);
        assertEquals("=\r=\r=\r=\r=\r=\r=\r=\r=\r=DE=\n", encodedControlCharsString);

        // test encoding of space and tab characters
        byte bSpace = 0x20;
        byte bTab = 0x09;
        byte[] spaceTabChars = new byte[] { bSpace, bTab };
        byte[] encodedSpaceTabChars = codec.encode(spaceTabChars);
        String encodedSpaceTabCharsString = new String(encodedSpaceTabChars, US_ASCII);
        assertEquals("=\r=20=\r=\t", encodedSpaceTabCharsString);

        // test encoding of printable characters
        byte[] printableChars = new byte[] { 0x21, 0x23, 0x25, 0x27, 0x2A, 0x2C, 0x2E, 0x30 };
        byte[] encodedPrintableChars = codec.encode(printableChars);
        String encodedPrintableCharsString = new String(encodedPrintableChars, US_ASCII);
        assertEquals("=!=\r=#=\r=$=\r=%=\r&==\r*=\r(=\r)=\r;=\r<=\r=\r0=\r", encodedPrintableCharsString);
    }

    /**
     * Test decoding of specific characters
     */
    @Test
    public void testDecodeSpecificCharacters() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test decoding of control characters
        byte bDel = 0x7F;
        byte[] encodedControlChars = new byte[] { (byte) '=', 0x64, (byte) '=', (byte) '=', (byte) 'D', (byte) 'E', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) '=', (byte) 'N' };
        byte[] decodedControlChars = codec.decode(encodedControlChars);
        byte[] controlChars = new byte[] { 0x00, 0x01, bDel };
        assertArrayEquals(controlChars, decodedControlChars);

        // test decoding of space and tab characters
        byte bSpace = 0x20;
        byte bTab = 0x09;
        byte[] encodedSpaceTabChars = new byte[] { (byte) '=', 0x62, (byte) '=', 0x20, 0x0D, 0x0A, (byte) '=', (byte) 't' };
        byte[] decodedSpaceTabChars = codec.decode(encodedSpaceTabChars);
        byte[] spaceTabChars = new byte[] { bSpace, bTab };
        assertArrayEquals(spaceTabChars, decodedSpaceTabChars);

        // test decoding of printable characters
        byte[] encodedPrintableChars = new byte[] { (byte) '=', 0x63, (byte) '=', 0x31, (byte) '=', 0x61, (byte) '=', 0x33, (byte) '=', 0x61, (byte) '=', 0x35, (byte) '=', 0x61, (byte) '=', 0x37, (byte) '=', 0x61, (byte) '=', 0x2A, (byte) '=', 0x61, (byte) '=', 0x3C, (byte) '=', 0x61, (byte) '=', 0x2E, (byte) '=', 0x61, (byte) '=', 0x30, (byte) '=', 0x61, (byte) '=' };
        byte[] decodedPrintableChars = codec.decode(encodedPrintableChars);
        byte[] printableChars = new byte[] { 0x21, 0x23, 0x25, 0x27, 0x2A, 0x2C, 0x2E, 0x30 };
        assertArrayEquals(printableChars, decodedPrintableChars);
    }

    /**
     * Helper method for encoding bytes to Quoted-Printable format
     */
    private static void encodeQuotedPrintable(byte[] bytes, byte[] encodedBytes, int offset) throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = codec.encode(bytes);
        System.arraycopy(encoded, 0, encodedBytes, offset, encoded.length);
    }
}