package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarUtils.FORMAT_NAME_BYTES;
import static org.apache.commons.compress.archivers.tar.TarUtils.PARSE_NAME;
import static org.apache.commons.compress.archivers.tar.TarUtils.PARSE_OCTAL;

/**
 * TarUtils Test Cases.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctalValid() {
        byte[] testData = new byte[]{'0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        long expected = 017550L;
        assertEquals(expected, result);

        testData = new byte[]{'0', '1', '0', '0', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 0L;
        assertEquals(expected, result);

        testData = new byte[]{'0', '7', '7', '7', '7'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 0177777L;
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] testData = new byte[]{'0', '8', '5', '5', '0'};
        TarUtils.parseOctal(testData, 0, testData.length);
    }

    /**
     * Test parseName method with valid names.
     */
    @Test
    public void testParseNameValid() {
        byte[] testData = new byte[]{'a', 'b', 'c', 'd', '0'};
        String result = TarUtils.parseName(testData, 0, testData.length);
        String expected = "abcd";
        assertEquals(expected, result);

        testData = new byte[]{'a', 'b', 'c', 'd', '0', '0'};
        result = TarUtils.parseName(testData, 0, testData.length);
        expected = "abcd";
        assertEquals(expected, result);

        testData = new byte[]{'a', 'b', 'c', 'd', '0', '0', '0'};
        result = TarUtils.parseName(testData, 0, testData.length);
        expected = "abcd";
        assertEquals(expected, result);

        testData = new byte[]{'a', 'b', 'c', 'd', '0', '0', '0', '0'};
        result = TarUtils.parseName(testData, 0, testData.length);
        expected = "abcd";
        assertEquals(expected, result);
    }

    /**
     * Test parseName method with invalid names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalid() {
        byte[] testData = new byte[]{'a', 'b', 'c', 'd', '0', '0', '0', '\\'};
        TarUtils.parseName(testData, 0, testData.length);
    }

    /**
     * Test formatNameBytes method with valid names.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "abcdef";
        byte[] result = new byte[21];
        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);

        // Check that proper name has been written
        byte[] expected = new byte[]{
                (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
                (byte) ' ', (byte) '.', (byte) '0', (byte) '.', (byte) '0', (byte) '.',
                (byte) '0', (byte) ' ', (byte) '0', (byte) '.', (byte) '0', (byte) '.',
                (byte) '0', (byte) ' ', (byte) '0', (byte) '.', (byte) '0', (byte) '.',
                (byte) '0'};

        Assert.assertArrayEquals(expected, result, 0, result.length);

        // Test with a shorter name
        name = "abc";
        result = new byte[11];
        offset = TarUtils.formatNameBytes(name, result, 0, result.length);

        // Check that proper name has been written
        expected = new byte[]{
                (byte) 'a', (byte) 'b', (byte) 'c', (byte) ' ', (byte) '.', (byte) '0',
                (byte) '.', (byte) '0', (byte) '.', (byte) '0', (byte) ' ', (byte) '0'};

        Assert.assertArrayEquals(expected, result, 0, result.length);
    }

    /**
     * Test formatNameBytes method with invalid names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalid() {
        String name = "abcdefghijklmnopqrstuvwxyz";
        byte[] result = new byte[21];
        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);

        // The name "abcdefghijklmnopqrstuvwxyz" has 27 characters,
        // which is beyond the 25 characters allowed by the
        // formatNameBytes method.
    }

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctalWithArray() {
        byte[] testData = new byte[]{'0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        long expected = 017550L;
        assertEquals(expected, result);

        testData = new byte[]{'0', '1', '0', '0', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 0L;
        assertEquals(expected, result);

        testData = new byte[]{'0', '7', '7', '7', '7'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 0177777L;
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithArrayInvalid() {
        byte[] testData = new byte[]{'0', '8', '5', '5', '0'};
        TarUtils.parseOctal(testData, 0, testData.length);
    }
}