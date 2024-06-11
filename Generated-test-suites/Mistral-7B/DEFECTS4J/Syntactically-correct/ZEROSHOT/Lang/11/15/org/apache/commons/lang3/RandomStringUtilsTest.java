package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 *
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c)) {
                // pass
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithSpecifiedChars() {
        String randomString = RandomStringUtils.random(10, 33, 126, true, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c) || c == '!') {
                // pass
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithSpecifiedCharsAndLetters() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                // pass
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithSpecifiedCharsAndNumbers() {
        String randomString = RandomStringUtils.random(10, 48, 57, false, true, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isDigit(c)) {
                // pass
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithSpecifiedCharsAndLettersNumbers() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, true, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                // pass
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }
}