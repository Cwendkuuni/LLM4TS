package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.LF;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAX_NAME_LENGTH;
import static org.apache.commons.compress.archivers.tar.TarConstants.TAR_MAGIC;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testOctal = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(testOctal, 0, testOctal.length);
        assertEquals("Test parsing of valid octal number", 0123456L, result);

        byte[] testOctalTrailingSpace = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        long resultTrailingSpace = TarUtils.parseOctal(testOctalTrailingSpace, 0, testOctalTrailingSpace.length);
        assertEquals("Test parsing of octal number with trailing space", 0123456L, resultTrailingSpace);

        byte[] testOctalTrailingNul = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', (byte) 0};
        long resultTrailingNul = TarUtils.parseOctal(testOctalTrailingNul, 0, testOctalTrailingNul.length);
        assertEquals("Test parsing of octal number with trailing null", 0123456L, resultTrailingNul);

        byte[] testOctalLength = new byte[MAX_NAME_LENGTH];
        long resultLength = TarUtils.parseOctal(testOctalLength, 0, testOctalLength.length);
        assertEquals("Test parsing of octal number longer than buffer", -1, resultLength);

        byte[] testOctalInvalid = new byte[]{'0', '1', '2', '3', 'x'};
        long resultInvalid = TarUtils.parseOctal(testOctalInvalid, 0, testOctalInvalid.length);
        assertEquals("Test parsing of invalid octal digit", -1, resultInvalid);
    }

    @Test
    public void testParseName() {
        byte[] testName = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String result = TarUtils.parseName(testName, 0, testName.length);
        assertEquals("Test parsing of valid name", "tar.gz", result);

        byte[] testNameTrailingSpace = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', ' '};
        String resultTrailingSpace = TarUtils.parseName(testNameTrailingSpace, 0, testNameTrailingSpace.length);
        assertEquals("Test parsing of name with trailing space", "tar.gz", resultTrailingSpace);

        byte[] testNameLength = new byte[MAX_NAME_LENGTH];
        String resultLength = TarUtils.parseName(testNameLength, 0, testNameLength.length);
        assertEquals("Test parsing of name longer than buffer", "", resultLength);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.gz";
        byte[] testBuffer = new byte[MAX_NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, testBuffer, 0, testBuffer.length);

        byte[] expected = new byte[]{'t', 'a', 'r', '.', 'g', 'z'};
        assertArrayEquals("Test formatting of name", expected, testBuffer, 0, expected.length);

        assertEqual("Test formatting of name offset", offset, testBuffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long testValue = 255;
        byte[] testBuffer = new byte[MAX_NAME_LENGTH];
        TarUtils.formatUnsignedOctalString(testValue, testBuffer, 0, testBuffer.length);

        byte[] expected = new byte[]{'0', '3', '7', '7'};
        assertArrayEquals("Test formatting of unsigned octal string", expected, testBuffer, 0, expected.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long testValue = 255;
        byte[] testBuffer = new byte[MAX_NAME_LENGTH * 2];
        TarUtils.formatOctalBytes(testValue, testBuffer, 0, testBuffer.length);

        byte[] expected = new byte[]{'0', '3', '7', '7', ' ', '0'};
        assertArrayEquals("Test formatting of octal bytes", expected, testBuffer, 0, expected.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testBuffer = new byte[]{TAR_MAGIC, '0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.computeCheckSum(testBuffer);
        assertEquals("Test computing checksum", 1129664816793141106L, result);
    }
}