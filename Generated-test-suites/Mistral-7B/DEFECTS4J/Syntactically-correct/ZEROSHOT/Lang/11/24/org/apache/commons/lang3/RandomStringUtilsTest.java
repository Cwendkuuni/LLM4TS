package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test for {@link RandomStringUtils} class
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default settings
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    /**
     * Test randomAscii method with default settings
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    /**
     * Test randomAlphabetic method with default settings
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    /**
     * Test randomAlphanumeric method with default settings
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    /**
     * Test randomNumeric method with default settings
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    /**
     * Test random method with custom settings
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z');
        }
    }

    /**
     * Test randomAscii method with custom settings
     */
    @Test
    public void testRandomAsciiCustom() {
        String randomString = RandomStringUtils.randomAscii(10, 33, 126);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 33 && randomString.charAt(i) <= 126);
        }
    }

    /**
     * Test randomAlphabetic method with custom settings
     */
    @Test
    public void testRandomAlphabeticCustom() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test randomAlphanumeric method with custom settings
     */
    @Test
    public void testRandomAlphanumericCustom() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test randomNumeric method with custom settings
     */
    @Test
    public void testRandomNumericCustom() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random method with custom character set
     */
    @Test
    public void testRandomCustomCharacterSet() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 'A' && randomString.charAt(i) <= 'Z');
        }
    }

    /**
     * Test random method with empty character set and default settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(10, new char[0]);
    }

    /**
     * Test random method with empty character set and custom settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSetCustom() {
        RandomStringUtils.random(10, 1, 1, false, false, new char[0], new Random());
    }

    /**
     * Test random method with negative length and default settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test random method with negative length and custom settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLengthCustom() {
        RandomStringUtils.random(-5, 'a', 'z', false, false, null, new Random());
    }

    /**
     * Test random method with null character set and default settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullCharacterSet() {
        RandomStringUtils.random(10, null);
    }

    /**
     * Test random method with null character set and custom settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullCharacterSetCustom() {
        RandomStringUtils.random(10, null, 1, false, false, null, new Random());
    }
}