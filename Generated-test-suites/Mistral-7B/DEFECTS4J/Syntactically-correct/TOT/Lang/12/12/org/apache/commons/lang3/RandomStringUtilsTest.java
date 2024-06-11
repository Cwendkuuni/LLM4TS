package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, 1, 26, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetter(c)) {
                assertTrue(Character.isUpperCase(c) || Character.isLowerCase(c));
            } else if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with the default source of randomness.
     */
    @Test
    public void testRandomWithDefaultSourceOfRandomness() {
        String randomString = RandomStringUtils.random(10, 1, 26, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetter(c)) {
                assertTrue(Character.isUpperCase(c) || Character.isLowerCase(c));
            } else if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with a custom source of randomness.
     */
    @Test
    public void testRandomWithCustomSourceOfRandomness() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 1, 26, true, true, new char[]{'A', 'B', 'C'}, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetter(c)) {
                assertTrue(Character.isUpperCase(c) || Character.isLowerCase(c));
            } else if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)}
     */
    @Test
    public void testRandomWithString() {
        String randomString = RandomStringUtils.random(10, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(new char[]{c}).matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789]"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, char[])}.
     */
    @Test
    public void testRandomWithCharArray() {
        char[] chars = new char[]{'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(10, chars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(findIndex(chars, c) != -1);
        }
    }

    private int findIndex(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     * when length is less than zero and options are invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidOptions() {
        RandomStringUtils.random(-5, true, true);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     * when length is less than zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-5, 1, true, true);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     * when character set is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }
}