package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Tests random method with default character set.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Tests randomAscii method with default character set.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Tests randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Tests randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
        }
    }

    /**
     * Tests randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Tests random method with custom character set.
     */
    @Test
    public void testRandomCustom() {
        String customChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        String randomString = RandomStringUtils.random(5, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Tests randomAlphanumeric method with custom character set.
     */
    @Test
    public void testRandomAlphanumericCustom() {
        String customChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.randomAlphanumeric(5, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
            assertTrue(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Tests randomAscii method with custom character set.
     */
    @Test
    public void testRandomAsciiCustom() {
        String customChars = "!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        String randomString = RandomStringUtils.randomAscii(5, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
            assertTrue(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Tests randomAlphabetic method with custom character set.
     */
    @Test
    public void testRandomAlphabeticCustom() {
        String customChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.randomAlphabetic(5, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            assertTrue(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Tests negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Tests empty chars array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(1, (char[]) null);
    }
}