package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ByteArrayInputStream;
import java.util.ByteArrayOutputStream;
import java.util.Collections;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid inputs.
     */
    @Test
    public void testParseOctal_ValidInputs() {
        byte[] buffer = new byte[]{'0', '7', '5', '0', ' ', '0'};
        long expected = 0175000L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test parseOctal method with invalid inputs: length < 2.
     */
    @Test
    public void testParseOctal_InvalidInput_LengthLessThanTwo() {
        byte[] buffer = new byte[]{'0'};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'0', '1'};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length - 1));
    }

    /**
     * Test parseOctal method with invalid inputs: non-trailing space or NUL.
     */
    @Test
    public void testParseOctal_InvalidInput_TrailingCharNotSpaceOrNull() {
        byte[] buffer = new byte[]{'0', '7', '5', '0', '1'};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with valid inputs: binary number.
     */
    @Test
    public void testParseOctalOrBinary_ValidInput_BinaryNumber() {
        byte[] buffer = new byte[]{0x80, 0x35, 0x9a, 0x73};
        long expected = 0x80359a73L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with valid inputs: octal number.
     */
    @Test
    public void testParseOctalOrBinary_ValidInput_OctalNumber() {
        byte[] buffer = new byte[]{'0', '7', '5', '0', ' ', '0'};
        long expected = 0175000L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with invalid input: length < 1.
     */
    @Test
    public void testParseOctalOrBinary_InvalidInput_LengthLessThanOne() {
        byte[] buffer = new byte[0];
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with invalid input: binary number exceeding the maximum signed long value.
     */
    @Test
    public void testParseOctalOrBinary_InvalidInput_BinaryNumberExceedingMaxSignedLongValue() {
        byte[] buffer = new byte[]{0x80, (byte) 0xff, 0xff, (byte) 0xff, 0xff, 0xff, 0xff, 0xff};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBoolean_ValidInput() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBoolean_InvalidInput() {
        byte[] buffer = new byte[]{0};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test parseName method with valid inputs.
     */
    @Test
    public void testParseName_ValidInputs() {
        byte[] buffer = new byte[]{0, 'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', 0};
        String expected = "Tar File";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test parseName method with invalid inputs:
     * invalid byte in the name.
     */
    @Test
    public void testParseName_InvalidInput_InvalidByteInName() {
        byte[] buffer = new byte[]{(byte) 0x80, 'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', 0};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    /**
     * Test formatNameBytes method with valid inputs.
     */
    @Test
    public void testFormatNameBytes_ValidInputs() {
        String name = "Tar File";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assertions.assertEquals(name.length(), offset);

        String actual = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assertions.assertEquals(name, actual);
    }

    /**
     * Test formatNameBytes method with invalid inputs: name longer than buffer.
     */
    @Test
    public void testFormatNameBytes_InvalidInput_NameLongerThanBuffer() {
        String name = "Tar File (longer than buffer length)";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assertions.assertEquals(10, offset);

        String actual = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assertions.assertEquals("Tar File (longer than buffer length)", actual.substring(0, 10));
    }

    /**
     * Test formatUnsignedOctalString method with valid inputs.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInputs() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        String expected = "012345";
        String actual = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input: value too large.
     */
    @Test
    public void testFormatUnsignedOctalString_InvalidInput_ValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];

        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatOctalBytes method with valid inputs.
     */
    @Test
    public void testFormatOctalBytes_ValidInputs() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        String expected = "012345  ";
        String actual = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test formatOctalBytes method with invalid input: value and trailer too large.
     */
    @Test
    public void testFormatOctalBytes_InvalidInput_ValueAndTrailerTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[8];

        Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid inputs:
     * value and trailer can be stored as octal string.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidInput_ValueAndTrailerCanBeStoredAsOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        String expected = "012345";
        String actual = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid inputs:
     * value and trailer cannot be stored as octal string.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidInput_ValueAndTrailerCannotBeStoredAsOctalString() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        byte[] expectedBuffer = new byte[]{0x80, (byte) 0xff, (byte) 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
        byte[] actualBuffer = new byte[10];
        System.arraycopy(buffer, 0, actualBuffer, 0, offset);

        Assertions.assertArrayEquals(expectedBuffer, actualBuffer);
    }

    /**
     * Test parseOctal method with missing fields.
     */
    @Test
    public void testParseOctal_MissingFields() {
        byte[] buffer = new byte[0];
        Assertions.assertEquals(0L, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with missing fields.
     */
    @Test
    public void testParseOctalOrBinary_MissingFields() {
        byte[] buffer = new byte[0];
        Assertions.assertEquals(0L, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseBoolean method with missing fields.
     */
    @Test
    public void testParseBoolean_MissingFields() {
        byte[] buffer = new byte[0];
        Assertions.assertFalse(TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test parseName method with missing fields.
     */
    @Test
    public void testParseName_MissingFields() {
        byte[] buffer = new byte[0];
        String expected = "";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assertions.assertEquals(expected, actual);
    }
}