package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default char set.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isISOControl(randomAsciiString.charAt(i)) || Character.isWhitespace(randomAsciiString.charAt(i)) || Character.isLetterOrDigit(randomAsciiString.charAt(i)));
        }
    }

    /**
     * Test randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    /**
     * Test randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)));
        }
    }

    /**
     * Test randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    /**
     * Test random method with specified length and char set.
     */
    @Test
    public void testRandomCharSet() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
            assertTrue(Character.isUpperCase(randomString.charAt(i)));
        }
    }

    /**
     * Test random method with empty char set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharSet() {
        RandomStringUtils.random(new char[0]);
    }

    /**
     * Test random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }
}