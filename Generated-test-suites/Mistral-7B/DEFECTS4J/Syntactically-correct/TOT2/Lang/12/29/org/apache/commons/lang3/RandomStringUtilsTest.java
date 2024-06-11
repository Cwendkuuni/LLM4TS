package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default character set.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default character set.
     */
    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char character = randomAsciiString.charAt(i);
            assertTrue(character >= 32 && character <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());

        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char character = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(character));
        }
    }

    /**
     * Test for randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());

        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char character = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(character) || Character.isDigit(character));
        }
    }

    /**
     * Test for randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());

        for (int i = 0; i < randomNumericString.length(); i++) {
            char character = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(character));
        }
    }

    /**
     * Test for random method with custom character set.
     */
    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(10, customCharacterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char character = randomString.charAt(i);
            assertTrue(customCharacterSet.indexOf(character) != -1);
        }
    }

    /**
     * Test for random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterSet() {
        RandomStringUtils.random(1, new char[0]);
    }

    /**
     * Test for random method with invalid start and end values.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidCharacterSetRange() {
        String characterSet = "ABC";
        RandomStringUtils.random(1, characterSet.charAt(0), characterSet.charAt(1) + 1);
    }
}