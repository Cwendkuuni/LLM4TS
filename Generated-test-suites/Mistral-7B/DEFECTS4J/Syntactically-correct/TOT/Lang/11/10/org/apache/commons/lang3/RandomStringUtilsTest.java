package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default character set.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertNotNull(randomString.charAt(i));
        }
    }

    /**
     * Test randomAscii method with default character set.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int asciiValue = (int) randomAsciiString.charAt(i);
            assertTrue(asciiValue >= 32 && asciiValue <= 126);
        }
    }

    /**
     * Test randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertNotNull(randomAlphabeticString.charAt(i));
            assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    /**
     * Test randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char randomCharacter = randomAlphanumericString.charAt(i);
            assertTrue(Character.isLetterOrDigit(randomCharacter));
        }
    }

    /**
     * Test randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertNotNull(randomNumericString.charAt(i));
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    /**
     * Test random method with specified character set.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, customCharacterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(customCharacterSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    /**
     * Test random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        String emptyCharacterSet = "";
        RandomStringUtils.random(1, emptyCharacterSet.toCharArray());
    }

    /**
     * Test random method with invalid character position in character set.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharacterPositionInCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        RandomStringUtils.random(5, customCharacterSet.toCharArray(), 12);
    }

    /**
     * Test random method with invalid character count in character set.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharacterCountInCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        RandomStringUtils.random(1, customCharacterSet.toCharArray(), customCharacterSet.length() + 1);
    }
}