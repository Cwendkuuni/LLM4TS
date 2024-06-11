package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default parameters.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertFalse("Random string should not be empty.", randomString.isEmpty());
        assertNotNull("Random string should not be null.", randomString);
        int length = randomString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertFalse("Random string should contain only valid characters.", Character.isHighSurrogate(randomString.charAt(i)) || Character.isLowSurrogate(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isLetter(randomString.charAt(i)));
        }
    }

    /**
     * Test randomAscii method with default parameters.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertFalse("Random string should not be empty.", randomAsciiString.isEmpty());
        assertNotNull("Random string should not be null.", randomAsciiString);
        int length = randomAsciiString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertTrue("Random string should contain only valid ASCII characters.", Character.isISOControl(randomAsciiString.charAt(i)) || Character.isWhitespace(randomAsciiString.charAt(i)) || Character.isDigit(randomAsciiString.charAt(i)) || Character.isLetter(randomAsciiString.charAt(i)) || Character.isAlphabetic(randomAsciiString.charAt(i)) || Character.isUpperCase(randomAsciiString.charAt(i)) || Character.isLowerCase(randomAsciiString.charAt(i)) || Character.isDigit(randomAsciiString.charAt(i)));
        }
    }

    /**
     * Test randomAlphabetic method with default parameters.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertFalse("Random string should not be empty.", randomAlphabeticString.isEmpty());
        assertNotNull("Random string should not be null.", randomAlphabeticString);
        int length = randomAlphabeticString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertTrue("Random string should contain only valid alphabetic characters.", Character.isLetter(randomAlphabeticString.charAt(i)) || Character.isUpperCase(randomAlphabeticString.charAt(i)) || Character.isLowerCase(randomAlphabeticString.charAt(i)));
        }
    }

    /**
     * Test randomAlphanumeric method with default parameters.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertFalse("Random string should not be empty.", randomAlphanumericString.isEmpty());
        assertNotNull("Random string should not be null.", randomAlphanumericString);
        int length = randomAlphanumericString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertTrue("Random string should contain only valid alphanumeric characters.", Character.isLetter(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)) || Character.isWhitespace(randomAlphanumericString.charAt(i)) || Character.isUpperCase(randomAlphanumericString.charAt(i)) || Character.isLowerCase(randomAlphanumericString.charAt(i)));
        }
    }

    /**
     * Test randomNumeric method with default parameters.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertFalse("Random string should not be empty.", randomNumericString.isEmpty());
        assertNotNull("Random string should not be null.", randomNumericString);
        int length = randomNumericString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertTrue("Random string should contain only valid numeric characters.", Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    /**
     * Test random method with specified char range.
     */
    @Test
    public void testRandomWithSpecifiedCharRange() {
        String randomString = RandomStringUtils.random('a', 'z', 10);
        assertFalse("Random string should not be empty.", randomString.isEmpty());
        assertNotNull("Random string should not be null.", randomString);
        int length = randomString.length();
        assertTrue("Random string should have the specified length.", length == 10);
        for (int i = 0; i < length; i++) {
            assertTrue("Random string should contain only specified characters.", Character.isAlphabetic(randomString.charAt(i)) && (randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z'));
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
     * Test random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test random method with empty chars array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(1, new char[0]);
    }

    /**
     * Test randomAscii method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithNegativeCount() {
        RandomStringUtils.randomAscii(-1);
    }

    /**
     * Test randomAlphabetic method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithNegativeCount() {
        RandomStringUtils.randomAlphabetic(-1);
    }

    /**
     * Test randomAlphanumeric method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithNegativeCount() {
        RandomStringUtils.randomAlphanumeric(-1);
    }

    /**
     * Test randomNumeric method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericWithNegativeCount() {
        RandomStringUtils.randomNumeric(-1);
    }

    /**
     * Test random method with start > end.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithStartGreaterThanEnd() {
        RandomStringUtils.random(1, 'a', 'z', true, false, new char[]{'a'}, RANDOM);
        RandomStringUtils.random(1, 'z', 'a', true, false, new char[]{'a'}, RANDOM);
    }
}