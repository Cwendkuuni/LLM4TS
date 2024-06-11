package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAXID;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAXSIZE;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0175547554755L, result);

        byte[] wrongTrailerBuffer = new byte[]{'0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5', '1'}; // Trailing byte is not NUL or space
        try {
            TarUtils.parseOctal(wrongTrailerBuffer, 0, wrongTrailerBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] wrongLengthBuffer = new byte[]{'0', '7', '5'}; // Buffer length is too short
        try {
            TarUtils.parseOctal(wrongLengthBuffer, 0, wrongLengthBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] invalidByteBuffer = new byte[]{'0', '7', '5', 'A', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5'}; // Invalid byte is detected
        try {
            TarUtils.parseOctal(invalidByteBuffer, 0, invalidByteBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] missingTrailingBuffer = new byte[]{'0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5'}; // Trailing NUL or space is missing
        try {
            TarUtils.parseOctal(missingTrailingBuffer, 0, missingTrailingBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        Assert.assertEquals(0x810102030405060708L, result);

        byte[] validOctalBuffer = new byte[]{'0', '7', '5', '5', '0', '7', '5', '5', '0', '7', '5', '5'};
        long resultOctal = TarUtils.parseOctalOrBinary(validOctalBuffer, 0, validOctalBuffer.length);
        Assert.assertEquals(0175547554755L, resultOctal);

        byte[] invalidByteBuffer = new byte[]{'0', '7', '5', 'A', '5', '5', '0', '7', '5', '5'}; // Invalid byte is detected
        try {
            TarUtils.parseOctalOrBinary(invalidByteBuffer, 0, invalidByteBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] tooLargeBinaryBuffer = new byte[]{(byte) 0x80, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}; // Value is too large for a signed long
        try {
            TarUtils.parseOctalOrBinary(tooLargeBinaryBuffer, 0, tooLargeBinaryBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] trueBuffer = new byte[]{0x01};
        boolean result = TarUtils.parseBoolean(trueBuffer, 0);
        Assert.assertTrue(result);

        byte[] falseBuffer = new byte[]{0x00};
        boolean resultFalse = TarUtils.parseBoolean(falseBuffer, 0);
        Assert.assertFalse(resultFalse);

        byte[] invalidByteBuffer = new byte[]{0x02}; // Invalid byte is detected
        try {
            TarUtils.parseBoolean(invalidByteBuffer, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{'t', 'a', 'r', ' ', 't', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r'};
        String result = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        Assert.assertEquals("tar test file.tar", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testFile.tar";
        byte[] buffer = new byte[MAXID+1];
        TarUtils.formatNameBytes(name, buffer, 0, MAXID);
        String result = new String(buffer, 0, MAXID);
        Assert.assertEquals("testFile.tar", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[MAXSIZE+1];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, MAXSIZE);
        String expected = String.format("%o", value);
        String result = new String(buffer, 0, MAXSIZE);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[MAXSIZE+3];
        TarUtils.formatOctalBytes(value, buffer, 0, MAXSIZE+3);
        String expected = String.format("%o ", value);
        String result = new String(buffer, 0, MAXSIZE+3);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[MAXSIZE+2];
        TarUtils.formatLongOctalBytes(value, buffer, 0, MAXSIZE+2);
        String expected = String.format("%o", value);
        String result = new String(buffer, 0, MAXSIZE+2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long valueID = 12345L;
        long valueSize = 9876543210L;
        byte[] bufferID = new byte[TarConstants.UIDLEN+1];
        byte[] bufferSize = new byte[TarConstants.UIDLEN+1];

        TarUtils.formatLongOctalOrBinaryBytes(valueID, bufferID, 0, TarConstants.UIDLEN);
        TarUtils.formatLongOctalOrBinaryBytes(valueSize, bufferSize, 0, TarConstants.UIDLEN);

        String expectedID = Long.toOctalString(valueID);
        String expectedSize = Long.toOctalString(valueSize);
        String resultID = new String(bufferID, 0, TarConstants.UIDLEN);
        String resultSize = new String(bufferSize, 0, TarConstants.UIDLEN);

        Assert.assertEquals(expectedID, resultID);
        Assert.assertEquals(expectedSize, resultSize);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = 0;
        for (byte b : buffer) {
            expected += TarUtils.BYTE_MASK & b;
        }
        Assert.assertEquals(expected, checksum);
    }
}