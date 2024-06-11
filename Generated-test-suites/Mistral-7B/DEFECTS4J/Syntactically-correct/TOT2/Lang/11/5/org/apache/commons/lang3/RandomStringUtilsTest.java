package org.junit.tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
    }

    @Test
    public void testRandomStringWithLimitations() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringLengthZero() {
        String randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomStringLengthNegative() {
        try {
            RandomStringUtils.random(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }
    }

    @Test
    public void testRandomAsciiStringWithLimitations() {
        String randomAsciiString = RandomStringUtils.randomAscii(10, 33, 64);
        assertNotNull(randomAsciiString);
    }

    @Test
    public void testRandomAlphabeticStringWithLimitations() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10, true, true);
        assertNotNull(randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumericStringWithLimitations() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10, true, true);
        assertNotNull(randomAlphanumericString);
    }

    @Test
    public void testRandomNumericStringWithLimitations() {
        String randomNumericString = RandomStringUtils.randomNumeric(10, false, true);
        assertNotNull(randomNumericString);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringWithHighSurrogates() {
        String randomString = RandomStringUtils.random(2, true, true);
        assertTrue(randomString.matches(".*[\\uD800-\\uDBFF][\\uDC00-\\uDFFF].*"));
    }

    @Test
    public void testRandomStringWithPrivateHighSurrogates() {
        String randomString = RandomStringUtils.random(2, true, true);
        assertFalse(randomString.matches(".*[\\uD800-\\uDBFF][\\uDC00-\\uDFFF].*"));
    }

    @Test
    public void testRandomStringWithInvalidInput() {
        try {
            RandomStringUtils.random(-1, true, true);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }

        try {
            RandomStringUtils.random(0, (char) 0, (char) 0);
        } catch (IllegalArgumentException e) {
            assertEquals("The start position must be greater than or equal to 0.", e.getMessage());
        }

        try {
            RandomStringUtils.random(0, (char) 1, (char) 0);
        } catch (IllegalArgumentException e) {
            assertEquals("The end position must be greater than or equal to the start position.", e.getMessage());
        }

        try {
            RandomStringUtils.random(1, (char) 1, (char) 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("There are not 0 characters in the set array.", e.getMessage());
        }
    }
}