package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default character set.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((c >= 32 && c <= 126));
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom character set.
     */
    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-={}[]|\\:;\"<>,.?/~`";
        String randomString = RandomStringUtils.random(10, customCharacterSet);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customCharacterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with custom character set and length <= 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        String randomString = RandomStringUtils.random(-1);
    }

    /**
     * Test for random method with null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullCharacterSet() {
        String randomString = RandomStringUtils.random(10, null);
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterSet() {
        String randomString = RandomStringUtils.random(10, new String(""));
    }
}