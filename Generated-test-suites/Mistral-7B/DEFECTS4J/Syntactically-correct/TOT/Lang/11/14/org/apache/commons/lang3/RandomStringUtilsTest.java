package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        int length = 5;
        String actual = RandomStringUtils.random(length);
        String expected = RandomStringUtils.random(length);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int length = 5;
        String actual = RandomStringUtils.randomAscii(length);
        String expected = RandomStringUtils.randomAscii(length);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int length = 5;
        String actual = RandomStringUtils.randomAlphabetic(length);
        String expected = RandomStringUtils.randomAlphabetic(length);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int length = 5;
        String actual = RandomStringUtils.randomAlphanumeric(length);
        String expected = RandomStringUtils.randomAlphanumeric(length);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int length = 5;
        String actual = RandomStringUtils.randomNumeric(length);
        String expected = RandomStringUtils.randomNumeric(length);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithRangeAndTypes() {
        int length = 5;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        String actual = RandomStringUtils.random(length, start, end, letters, numbers);
        String expected = RandomStringUtils.random(length, start, end, letters, numbers);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            if (letters) {
                assertTrue(Character.isLetter(c) || (c == '\u0000' && length > 1));
            }
            if (numbers) {
                assertTrue(Character.isDigit(c) || (c == '\u0000' && length > 1));
            }
            if (!letters && !numbers) {
                assertTrue(start <= c && c <= end || (c == '\u0000' && length > 1));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharsAndRandom() {
        int length = 5;
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random random = new Random();
        String actual = RandomStringUtils.random(length, chars, random);
        String expected = RandomStringUtils.random(length, chars, random);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(Arrays.binarySearch(chars, c) >= 0);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithCustomCharsString() {
        int length = 5;
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String actual = RandomStringUtils.random(length, chars);
        String expected = RandomStringUtils.random(length, chars);
        assertEquals("Random strings should be equal when generated with the same parameters", actual, expected);
        assertNotNull(actual);
        assertTrue(actual.length() > 0 && actual.length() <= length);
        for (char c : actual.toCharArray()) {
            assertTrue(chars.indexOf(c) >= 0);
        }
    }
}