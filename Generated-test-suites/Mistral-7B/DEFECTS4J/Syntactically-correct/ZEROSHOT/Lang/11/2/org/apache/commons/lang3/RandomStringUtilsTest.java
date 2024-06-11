package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.java
 *
 * @author John Doe
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());

        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(Character.isISOControl(c) || Character.isWhitespace(c) || Character.isAlphabetic(c) || Character.isDigit(c) || Character.isGraphic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());

        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());

        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());

        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}
     */
    @Test
    public void testRandomBoolean() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        int countAlphabets = 0;
        int countDigits = 0;

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (Character.isAlphabetic(c)) {
                countAlphabets++;
            } else if (Character.isDigit(c)) {
                countDigits++;
            }
        }

        int expectedAlphabets = 3;
        int expectedDigits = 2;

        assertEquals(expectedAlphabets, countAlphabets);
        assertEquals(expectedDigits, countDigits);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomCustom() {
        char[] chars = new char[26];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + i);
        }

        String randomString = RandomStringUtils.random(5, 0, chars.length, true, false, chars, new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(5, "abcdefghijklmopqrstuvwxyz");
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= 'a' && c <= 'z');
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, char[])}
     */
    @Test
    public void testRandomCharArray() {
        char[] chars = new char[26];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + i);
        }

        String randomString = RandomStringUtils.random(5, chars);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
            assertTrue(Arrays.binarySearch(chars, c) >= 0);
        }
    }
}