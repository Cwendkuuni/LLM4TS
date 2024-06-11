package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string creation with default settings.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    /**
     * Test random string creation with specific character set.
     */
    @Test
    public void testRandomStringWithChars() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.indexOf(c) != -1);
        }
    }

    /**
     * Test random string creation with specific character set and limits.
     */
    @Test
    public void testRandomStringWithLimits() {
        String randomString = RandomStringUtils.random("abc", 3, 5);
        assertNotNull(randomString);
        assertTrue(randomString.length() >= 3 && randomString.length() <= 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.indexOf(c) != -1 && (c >= 'a' && c <= 'z'));
        }
    }

    /**
     * Test random string creation with letter only setting.
     */
    @Test
    public void testRandomStringWithLetters() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test random string creation with numeric only setting.
     */
    @Test
    public void testRandomStringWithNumbers() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random string creation with alphanumeric setting.
     */
    @Test
    public void testRandomStringWithAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test random string creation with ascii characters only.
     */
    @Test
    public void testRandomStringWithAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 126);
        }
    }

    /**
     * Test random string creation with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptySet() {
        RandomStringUtils.random("", 10);
    }

    /**
     * Test random string creation with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test random string creation with null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullSet() {
        RandomStringUtils.random(null);
    }

    /**
     * Test random string creation with random.
     */
    @Test
    public void testRandomStringWithRandom() {
        String randomString = RandomStringUtils.random(new Random(), 10, 'a', 'z', false, false);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue((int) c >= 'a' && (int) c <= 'z');
        }
    }
}