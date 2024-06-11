package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        final int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        final int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        final int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        final int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomLettersAndNumbers() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithCustomRange() {
        final int count = 10;
        final char start = 'A';
        final char end = 'F';
        String randomString = RandomStringUtils.random(count, (int) start, (int) end, true, false);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= start && c <= end);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        final int count = 10;
        final String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final char[] chars = characterSet.toCharArray();
        String randomString = RandomStringUtils.random(count, chars, new Random());
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.toString(c).equals(Character.toString(findChar(chars, c))));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithCustomCharacterSetString() {
        final int count = 10;
        final String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, characterSet);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(String.valueOf(c)) != -1);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSetAndRange() {
        final int count = 10;
        final char start = 'A';
        final char end = 'F';
        final String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final char[] chars = characterSet.toCharArray();
        String randomString = RandomStringUtils.random(count, (int) start, (int) end, true, false, chars, new Random());
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= start && c <= end);
            assert(Character.toString(c).equals(Character.toString(findChar(chars, c))));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)}.
     * Tests the edge case where the character set is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        final int count = 10;
        String characterSet = "";
        RandomStringUtils.random(count, characterSet);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     * Tests the edge case where the character set length is less than the range length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSet() {
        final int count = 10;
        final int start = 0;
        final int end = 5;
        final char[] chars = {'A', 'B'};
        RandomStringUtils.random(count, start, end, true, false, chars, new Random());
    }

    /**
     * Find the character in the given character array that matches the given character.
     *
     * @param chars the character array
     * @param c the character to find
     * @return the character in the character array that matches the given character,
     * or null if not found
     */
    private char findChar(char[] chars, char c) {
        for (char ch : chars) {
            if (ch == c) {
                return ch;
            }
        }
        return null;
    }
}