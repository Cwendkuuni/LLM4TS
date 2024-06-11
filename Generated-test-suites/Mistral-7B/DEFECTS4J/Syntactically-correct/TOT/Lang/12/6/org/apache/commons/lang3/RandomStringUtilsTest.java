package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Expected random string of length " + count, count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue("Expected ASCII character", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 8;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue("Expected alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 12;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue("Expected alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 7;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        for (char c : randomNumericString.toCharArray()) {
            assertTrue("Expected numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCountRandomString() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 15;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected character within range [" + start + ", " + end + "]", (c >= start && c <= end));
        }
    }

    @Test
    public void testRandomStringWithRangeLetters() {
        int count = 12;
        int start = 65;
        int end = 97;
        String randomString = RandomStringUtils.random(count, start, end, true, false, null, RANDOM);
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected alphabetic character within range [" + start + ", " + end + "]", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomStringWithRangeLettersNumbers() {
        int count = 15;
        int start = 48;
        int end = 92;
        String randomString = RandomStringUtils.random(count, start, end, true, true, null, RANDOM);
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected alphanumeric character within range [" + start + ", " + end + "]", Character.isAlphanumeric(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringAsChars() {
        RandomStringUtils.random(10, (char[]) null);
    }

    @Test
    public void testRandomStringCustomChars() {
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        int count = 7;
        String randomString = RandomStringUtils.random(count, chars);
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected custom character", contains(chars, c));
        }
    }

    private static boolean contains(char[] arr, char c) {
        for (char ac : arr) {
            if (ac == c) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testRandomStringCustomCharsWithRange() {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
        int count = 10;
        int start = 3;
        int end = 8;
        String randomString = RandomStringUtils.random(count, start, end, false, false, chars, RANDOM);
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected custom character within range [" + start + ", " + end + "]", (chars[start - 1] <= c && c <= chars[end]));
        }
    }
}