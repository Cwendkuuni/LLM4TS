package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default settings.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom length, letters and numbers.
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(10, true, true);
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
     * Test for random method with custom length, start, end, letters and numbers.
     */
    @Test
    public void testRandomCustomSet() {
        String randomString = RandomStringUtils.random(10, 'a', 'z', true, true, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test for random method with custom length, start, end, letters, numbers and custom chars array.
     */
    @Test
    public void testRandomCustomSetWithChars() {
        String randomString = RandomStringUtils.random(10, 0, 26, true, true, new char[]{'a', 'b', 'c', 'd'}, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (c == 'a' || c == 'b' || c == 'c' || c == 'd') {
                continue;
            }

            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test for edge cases with custom length and custom set of chars.
     */
    @Test
    public void testRandomEdgeCases() {
        String randomString = RandomStringUtils.random(0, 'a', 'z', true, true, null, RANDOM);
        assertEquals("", randomString);

        String randomStringShort = RandomStringUtils.random(1, 'a', 'z', true, true, null, RANDOM);
        assertNotNull(randomStringShort);
        assertEquals(1, randomStringShort.length());

        String randomStringLong = RandomStringUtils.random(Integer.MAX_VALUE, 'a', 'z', true, true, null, RANDOM);
        assertNotNull(randomStringLong);
        int maxLength = Integer.MAX_VALUE;
        assertEquals(maxLength, randomStringLong.length());
    }

    /**
     * Test for error case with custom length and custom set of chars.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomError() {
        String randomString = RandomStringUtils.random(-1, 'a', 'z', true, true, null, RANDOM);
    }
}