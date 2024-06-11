package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default options.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertFalse(Character.isDigit(randomString.charAt(i)) || Character.isLetter(randomString.charAt(i)) || randomString.charAt(i) == ' ');
        }
    }

    /**
     * Test randomAscii method with default options.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || randomString.charAt(i) == ' ');
            assertTrue(Character.isISOControl(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test randomNumeric method with default options.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random method with custom options.
     */
    @Test
    public void testRandomWithCustomOptions() {
        String randomString = RandomStringUtils.random(10, 0, 26, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test random method with null chars array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullCharsArray() {
        RandomStringUtils.random(10, null);
    }

    /**
     * Test random method with empty chars array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        String[] emptyCharsArray = {};
        RandomStringUtils.random(10, emptyCharsArray);
    }

    /**
     * Test random method with count < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }
}