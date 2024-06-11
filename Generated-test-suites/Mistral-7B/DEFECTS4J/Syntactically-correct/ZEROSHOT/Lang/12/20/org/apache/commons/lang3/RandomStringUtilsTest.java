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
     * Test RandomStringUtils.random().
     */
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should be equal to specified count.", count, randomString.length());
    }

    /**
     * Test RandomStringUtils.randomAscii().
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should be equal to specified count.", count, randomAsciiString.length());

        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue("Character should be in range 32 to 126.", c >= 32 && c <= 126);
        }
    }

    /**
     * Test RandomStringUtils.randomAlphabetic().
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should be equal to specified count.", count, randomAlphabeticString.length());

        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue("Character should be alphabetic.", isAlphabetic(c));
        }
    }

    /**
     * Test RandomStringUtils.randomAlphanumeric().
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should be equal to specified count.", count, randomAlphanumericString.length());

        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue("Character should be alphanumeric.", isAlphanumeric(c));
        }
    }

    /**
     * Test RandomStringUtils.randomNumeric().
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should be equal to specified count.", count, randomNumericString.length());

        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue("Character should be numeric.", isDigit(c));
        }
    }

    /**
     * Test RandomStringUtils.random(int, boolean, boolean).
     */
    @Test
    public void testRandomBoolean() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, false);
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphabetic.", isAlphabetic(c));
        }

        randomString = RandomStringUtils.random(count, false, true);
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be numeric.", isDigit(c));
        }

        randomString = RandomStringUtils.random(count, true, true);
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphanumeric.", isAlphanumeric(c));
        }
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random).
     */
    @Test
    public void testRandomCustom() {
        int count = 10;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, true, false, null, new Random());
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphabetic.", isAlphabetic(c));
            assertTrue("Character should be in range start to end.", c >= start && c <= end);
        }

        randomString = RandomStringUtils.random(count, 48, 57, false, true, null, new Random());
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be numeric.", isDigit(c));
            assertTrue("Character should be in range start to end.", c >= 48 && c <= 57);
        }

        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        randomString = RandomStringUtils.random(count, 0, chars.length, true, true, chars, new Random());
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphanumeric.", isAlphanumeric(c));
            assertTrue("Character should be present in chars array.", indexOf(chars, c) != -1);
        }
    }

    /**
     * Test RandomStringUtils.random(int, char[]).
     */
    @Test
    public void testRandomCharArray() {
        int count = 10;
        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be present in chars array.", indexOf(chars, c) != -1);
        }
    }

    /**
     * Test RandomStringUtils.random(String).
     */
    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertEquals("Length should be equal to specified count.", count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be present in chars array.", indexOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), c) != -1);
        }
    }

    /**
     * Test RandomStringUtils.random(int, int).
     */
    @Test
    public void testRandomLength() {
        int minLength = 5;
        int maxLength = 10;
        int length = RandomStringUtils.random(minLength, maxLength);
        assertTrue("Length should be in range [minLength, maxLength]", length >= minLength && length <= maxLength);
    }

    /**
     * Helper method to check if a character is alphabetic.
     */
    private boolean isAlphabetic(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Helper method to check if a character is alphanumeric.
     */
    private boolean isAlphanumeric(char c) {
        return isAlphabetic(c) || isDigit(c);
    }

    /**
     * Helper method to check if a character is digit.
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Helper method to get the index of a character in a char array.
     */
    private int indexOf(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return i;
            }
        }
        return -1;
    }
}