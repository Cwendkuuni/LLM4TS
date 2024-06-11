package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string generation.
     */
    @Test
    public void testRandomStringGeneration() {
        // Test generating a random string with no constraints
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test generating a random string with only ascii characters
        String randomAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAscii);
        assertEquals(10, randomAscii.length());

        // Test generating a random string with only alphabetic characters
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabetic);
        assertEquals(10, randomAlphabetic.length());

        // Test generating a random string with only alphanumeric characters
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumeric);
        assertEquals(10, randomAlphanumeric.length());

        // Test generating a random string with numeric characters only
        String randomNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumeric);
        assertEquals(10, randomNumeric.length());

        // Test generating a random string with a specified length and character set
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        String randomCustom = RandomStringUtils.random(10, chars);
        assertNotNull(randomCustom);
        assertEquals(10, randomCustom.length());
    }

    /**
     * Test generating random strings with specified length, but empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    /**
     * Test generating random strings with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test generating random strings with invalid character ranges.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterRanges() {
        RandomStringUtils.random(10, 126, 31);
        RandomStringUtils.random(10, 32, 125);
    }
}