package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
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
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(Character.isISOControl(c) || Character.isWhitespace(c) || Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified length and character set.
     */
    @Test
    public void testRandomWithSet() {
        String set = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomString = RandomStringUtils.random(10, set.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(set.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with specified length, start and end positions in character set.
     */
    @Test
    public void testRandomWithSetRange() {
        String set = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomString = RandomStringUtils.random(10, set.charAt(0), set.charAt(set.length() - 1), false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(set.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with specified length, start and end positions in character set and using a provided random instance.
     */
    @Test
    public void testRandomWithSetRangeAndRandomInstance() {
        String set = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, set.charAt(0), set.charAt(set.length() - 1), false, false, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(set.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(1, (char[]) null);
    }

    /**
     * Test for random method with provided random instance.
     */
    @Test
    public void testRandomWithRandomInstance() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, false, false, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
}