package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}
     */
    @Test
    public void testRandom() {
        int length = 5;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        int length = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomAsciiString);
        assertEquals(length, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((c >= 32 && c <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        int length = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomAlphabeticString);
        assertEquals(length, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        int length = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomAlphanumericString);
        assertEquals(length, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            } else if (Character.isAlphabetic(c)) {
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        int length = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomNumericString);
        assertEquals(length, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}
     */
    @Test
    public void testRandomBoolean() {
        int length = 5;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(length, letters, numbers);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (letters) {
                assertTrue(Character.isLetter(c) || Character.isDigit(c));
            } else {
                assertTrue(!Character.isLetter(c) && !Character.isDigit(c));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomCharArray() {
        int length = 5;
        int start = 32;
        int end = 126;
        boolean letters = true;
        boolean numbers = true;
        char[] chars = new char[256];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }
        String randomString = RandomStringUtils.random(length, start, end, letters, numbers, chars, new Random());
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (letters) {
                if (Character.isLetter(c)) {
                    assertTrue((start <= c && c <= end) || (Character.isUpperCase(c) && start == 65 && c <= 90) || (Character.isLowerCase(c) && start == 97 && c <= 122));
                } else {
                    assertTrue(!Character.isLetter(c) && !Character.isDigit(c) && (start == c || (start < 32 && c == 32) || (start > 126 && c == 126)));
                }
            } else {
                assertTrue((start <= c && c <= end) || (start == 48 && c == 48) || (start > 57 && c == 58) || (start == 96 && c == 96) || (start > 58 && c <= 126));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}
     */
    @Test
    public void testRandomString() {
        int length = 5;
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(length, chars);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(chars.indexOf(c) != -1);
        }
    }
}