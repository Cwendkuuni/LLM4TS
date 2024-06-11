package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertEquals("Expected length: " + length, length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue("Expected random character: " + c + " to be in the range of ascii values 0 to 127", (c >= 0 && c <= 127));
        }
    }

    @Test
    public void testRandomAsciiString() {
        int length = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertEquals("Expected length: " + length, length, randomAsciiString.length());
        for (int i = 0; i < length; i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue("Expected random character: " + c + " to be an ascii character", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int length = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertEquals("Expected length: " + length, length, randomAlphabeticString.length());
        for (int i = 0; i < length; i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue("Expected random character: " + c + " to be an alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int length = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertEquals("Expected length: " + length, length, randomAlphanumericString.length());
        for (int i = 0; i < length; i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue("Expected random character: " + c + " to be an alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        int length = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertEquals("Expected length: " + length, length, randomNumericString.length());
        for (int i = 0; i < length; i++) {
            char c = randomNumericString.charAt(i);
            assertTrue("Expected random character: " + c + " to be a numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthRandomString() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringRandomString() {
        RandomStringUtils.random(0);
    }

    @Test
    public void testRandomStringWithSpecifiedRange() {
        int length = 10;
        String randomString = RandomStringUtils.random(length, 65, 90);
        assertEquals("Expected length: " + length, length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue("Expected random character: " + c + " to be in the range of ascii values 65 to 90", (c >= 65 && c <= 90));
        }
    }

    @Test
    public void testRandomStringWithSpecifiedRangeAndCustomChars() {
        int length = 10;
        String randomString = RandomStringUtils.random(length, 65, 90, true, false, null, RandomStringUtils.RANDOM);
        assertEquals("Expected length: " + length, length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue("Expected random character: " + c + " to be in the range of ascii values 65 to 90", (c >= 65 && c <= 90));
        }
    }
}