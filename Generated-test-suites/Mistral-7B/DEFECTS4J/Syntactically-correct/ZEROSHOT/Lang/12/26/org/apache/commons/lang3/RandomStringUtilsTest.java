package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertTrue(randomAsciiString.length() == 10);
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertTrue(randomAlphabeticString.length() == 10);
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertTrue(randomAlphanumericString.length() == 10);
        for (char c : randomAlphanumericString.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                fail("Character '" + c + "' is not alphabetic or numeric.");
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertTrue(randomNumericString.length() == 10);
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }

        String randomString2 = RandomStringUtils.random(10, false, true);
        assertNotNull(randomString2);
        assertTrue(randomString2.length() == 10);
        for (char c : randomString2.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=<>?";
        String randomString = RandomStringUtils.random(10, 0, customCharacterSet.length(), false, false, customCharacterSet.toCharArray(), new Random());
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(customCharacterSet.indexOf(c) != -1);
        }
    }
}