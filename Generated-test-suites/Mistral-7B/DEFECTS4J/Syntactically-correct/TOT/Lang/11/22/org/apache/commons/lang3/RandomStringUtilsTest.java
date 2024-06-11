package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals((int) c, (int) Math.max(32, Math.min(126, (int) (Math.random() * 95))));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, 1, 5, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        int countAlphabetic = 0;
        int countNumeric = 0;
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                countAlphabetic++;
            } else if (Character.isDigit(c)) {
                countNumeric++;
            }
        }
        assertEquals(5, countAlphabetic);
        assertEquals(5, countNumeric);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharsAndSeed() {
        char[] customChars = {'a', 'b', 'c', 'd', 'e'};
        Random customRandom = new Random(123456789);
        String randomString = RandomStringUtils.random(10, 0, customChars.length, true, true, customChars, customRandom);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(ArrayUtils.contains(customChars, c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithCustomCharSequence() {
        String customChars = "abcdef";
        String randomString = RandomStringUtils.random(10, customChars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidInput() {
        String randomString = RandomStringUtils.random(-1, 0, 0, false, false, null, RANDOM);
    }
}