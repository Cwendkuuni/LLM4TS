package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)} .
     */
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Random string of length " + count, count, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)} .
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Random string of length " + count + " composed of ASCII characters only", count, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue("Character is an ASCII character", (c >= 0x20 && c <= 0x7E));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)} .
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Random string of length " + count + " composed of alphabetic characters only", count, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue("Character is an alphabetic character", Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)} .
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Random string of length " + count + " composed of alphanumeric characters only", count, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue("Character is an alphanumeric character", (Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)} .
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Random string of length " + count + " composed of numeric characters only", count, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue("Character is a numeric character", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} .
     */
    @Test
    public void testRandomWithRangeAndFlags() {
        int count = 10;
        String randomStringWithRangeAndFlags = RandomStringUtils.random(count, 48, 57, true, false);
        assertEquals("Random string of length " + count + " composed of digits only in the given range", count, randomStringWithRangeAndFlags.length());
        for (char c : randomStringWithRangeAndFlags.toCharArray()) {
            assertTrue("Character is a digit", Character.isDigit(c));
            assertEquals("Character in given range", c, Character.forDigit(RandomStringUtils.nextInt(10) + 48));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} .
     */
    @Test
    public void testRandomWithCustomCharsetAndSeed() {
        int count = 10;
        char[] charset = {'A', 'B', 'C'};
        String randomStringWithCustomCharsetAndSeed = RandomStringUtils.random(count, 0, charset.length, true, true, charset, new Random(123));
        assertEquals("Random string of length " + count + " composed of characters from the given charset", count, randomStringWithCustomCharsetAndSeed.length());
        for (char c : randomStringWithCustomCharsetAndSeed.toCharArray()) {
            assertTrue("Character is in the given charset", Arrays.stream(charset).anyMatch(c::equals));
        }
    }

}