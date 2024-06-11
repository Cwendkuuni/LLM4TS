package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert((c >= 32) && (c <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomSetOfChars() {
        String randomString = RandomStringUtils.random(10, 33, 126, true, false, null, RandomStringUtils.RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert((c >= 33) && (c <= 126));
            assert(!Character.isDigit(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, char[], int, int, int, boolean, Random)}.
     */
    @Test
    public void testRandomWithCustomSetOfCharsAndCustomRandom() {
        final int charSetSize = 26;
        char[] charSet = new char[charSetSize];
        for (int i = 0; i < charSetSize; i++) {
            charSet[i] = (char) ('a' + i);
        }

        String randomString = RandomStringUtils.random(10, charSet, 0, charSet.length, 0, true, new Random(12345));
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            for (int i = 0; i < charSetSize; i++) {
                if (c == charSet[i]) {
                    assert(RandomStringUtils.RANDOM.nextInt(100) % 2 == 0);
                    break;
                }
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithCustomSetOfCharsFromString() {
        String randomString = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

}