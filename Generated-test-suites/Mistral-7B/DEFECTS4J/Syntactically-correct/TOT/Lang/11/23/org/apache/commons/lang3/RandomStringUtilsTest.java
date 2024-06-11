package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomASCIIString() {
        String randomASCIIString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomASCIIString);
        assertEquals(10, randomASCIIString.length());
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random("A", "Z", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c) && (c >= 'A' && c <= 'Z'));
        }
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String customChars = "!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        String randomStringWithCustomChars = RandomStringUtils.random(10, customChars);
        assertNotNull(randomStringWithCustomChars);
        assertEquals(10, randomStringWithCustomChars.length());
        for (char c : randomStringWithCustomChars.toCharArray()) {
            assertEquals(true, customChars.indexOf(c) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharArray() {
        RandomStringUtils.random(5, (char[]) null);
    }
}