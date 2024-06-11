package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for RandomStringUtils.random().
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for RandomStringUtils.randomAscii().
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert((c >= 32 && c <= 126));
        }
    }

    /**
     * Test for RandomStringUtils.randomAlphabetic().
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
     * Test for RandomStringUtils.randomAlphanumeric().
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for RandomStringUtils.randomNumeric().
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
     * Test for RandomStringUtils.random(int, boolean, boolean).
     */
    @Test
    public void testRandomWithFlags() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }

        String randomStringWithNumbers = RandomStringUtils.random(10, true, true);
        assertNotNull(randomStringWithNumbers);
        assertEquals(10, randomStringWithNumbers.length());
        for (char c : randomStringWithNumbers.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random).
     */
    @Test
    public void testRandomWithCustomSet() {
        String randomStringWithCustomSet = RandomStringUtils.random(10, 'a', 'z', true, false, null, RANDOM);
        assertNotNull(randomStringWithCustomSet);
        assertEquals(10, randomStringWithCustomSet.length());
        for (char c : randomStringWithCustomSet.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }

        String randomStringWithCustomSetAscii = RandomStringUtils.random(10, 32, 126, false, false, null, RANDOM);
        assertNotNull(randomStringWithCustomSetAscii);
        assertEquals(10, randomStringWithCustomSetAscii.length());
        for (char c : randomStringWithCustomSetAscii.toCharArray()) {
            assert((c >= 32 && c <= 126));
        }
    }

    /**
     * Test for RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random) with
     * empty custom set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCustomSet() {
        RandomStringUtils.random(10, 0, 0, false, false, null, RANDOM);
    }
}