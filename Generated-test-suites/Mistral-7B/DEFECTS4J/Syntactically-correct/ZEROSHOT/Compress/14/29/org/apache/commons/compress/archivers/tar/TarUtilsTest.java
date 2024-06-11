package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalOrBinaryBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseBoolean;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctalOrBinary;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

import java.nio.ByteBuffer;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{
            '0', '7', '5', '5', ' ', '\u0000'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal test failed.", 07550L, result);

        buffer = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '\u0000'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal test failed.", 01234567L, result);

        buffer = new byte[]{
            '5', '\u0000'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("parseOctal expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{
            '2', '5', '\u0000'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("parseOctal expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{
            '\u0000', '\u0000'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal test failed.", 0L, result);

        buffer = new byte[]{
            '\u0001'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("parseOctal expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test parseOctalOrBinary method.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{
            '\u007f', '7', '5', '5'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal or binary test failed.", 0x7f755L, result);

        buffer = new byte[]{
            '\u0080', '7', '5', '5'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal or binary test failed.", 0x80755L, result);

        buffer = new byte[]{
            '\u0000'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Parse octal or binary test failed.", 0L, result);

        buffer = new byte[]{
            '1'};
        try {
            result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            Assert.fail("parseOctalOrBinary expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test parseBoolean method.
     */
    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{
            '\u0001'};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertTrue("Parse boolean test failed.", result);

        buffer = new byte[]{
            '\u0000'};
        result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertFalse("Parse boolean test failed.", result);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{
            'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Parse name test failed.", "Tar.file.tar", result);

        buffer = new byte[]{
            'T', 'a', 'r', '\u0000'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Parse name test failed.", "Tar", result);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "Tar.file.tar";
        byte[] buffer = new byte[TarConstants.TAR_NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        Assert.assertArrayEquals("Format name bytes test failed.",
                new byte[]{'T', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.','t', 'a', 'r'}, buffer, 0, offset);
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0755;
        byte[] buffer = new byte[TarConstants.TAR_UID_LENGTH];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        Assert.assertArrayEquals("Format unsigned octal string test failed.",
                new byte[]{'0', '1', '2', '5'}, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 0755;

        byte[] buffer = new byte[TarConstants.TAR_UID_LENGTH + 2];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        Assert.assertArrayEquals("Format octal bytes test failed.",
                new byte[]{'0', '1', '2', '5', ' ', '\u0000'}, buffer, 0, offset);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x7f_ff_ff_ff_ff_ff;

        byte[] buffer = new byte[TarConstants.TAR_UID_LENGTH + 1];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        Assert.assertArrayEquals("Format long octal or binary bytes test failed.",
                new byte[]{(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, buffer, 0, offset);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{
            0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        long checksum = TarUtils.computeCheckSum(buffer);

        long expectedChecksum = 0;
        for (byte b : buffer) {
            expectedChecksum += BYTE_MASK & b;
        }

        Assert.assertEquals("Compute checksum test failed.", expectedChecksum, checksum);
    }

}