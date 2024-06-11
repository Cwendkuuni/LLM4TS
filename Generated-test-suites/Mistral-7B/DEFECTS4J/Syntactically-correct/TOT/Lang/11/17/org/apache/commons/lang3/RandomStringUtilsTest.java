package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;

import junit.framework.TestCase;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest extends TestCase {

    /**
     * Test method for {@link RandomStringUtils#random(int)} .
     */
    public void testRandom() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertEquals("Length is not as expected.", length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Character is not a valid ascii character.", (randomChar >= 0 && randomChar <= 127));
        }
        // Test with empty String as argument
        String emptyString = RandomStringUtils.random(0);
        assertEquals("String length is not zero.", "", emptyString);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)} .
     */
    public void testRandomAscii() {
        int length = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertEquals("Length is not as expected.", length, randomAsciiString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAsciiString.charAt(i);
            assertTrue("Character is not a valid ascii character.", (randomChar >= 32 && randomChar <= 126));
        }
        // Test with empty String as argument
        String emptyAsciiString = RandomStringUtils.randomAscii(0);
        assertEquals("String length is not zero.", "", emptyAsciiString);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)} .
     */
    public void testRandomAlphabetic() {
        int length = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertEquals("Length is not as expected.", length, randomAlphabeticString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAlphabeticString.charAt(i);
            assertTrue("Character is not an alphabetic character.", Character.isAlphabetic(randomChar));
        }
        // Test with empty String as argument
        String emptyAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("String length is not zero.", "", emptyAlphabeticString);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)} .
     */
    public void testRandomAlphanumeric() {
        int length = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertEquals("Length is not as expected.", length, randomAlphanumericString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            assertTrue("Character is not an alphanumeric character.", Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
        // Test with empty String as argument
        String emptyAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("String length is not zero.", "", emptyAlphanumericString);
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)} .
     */
    public void testRandomNumeric() {
        int length = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertEquals("Length is not as expected.", length, randomNumericString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomNumericString.charAt(i);
            assertTrue("Character is not a numeric character.", Character.isDigit(randomChar));
        }
        // Test with empty String as argument
        String emptyNumericString = RandomStringUtils.randomNumeric(0);
        assertEquals("String length is not zero.", "", emptyNumericString);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} .
     */
    public void testRandomCustom() {
        int length = 10;
        char startChar = 'A';
        char endChar = 'Z';
        boolean letters = true;
        boolean numbers = false;
        String randomCustomString = RandomStringUtils.random(length, startChar, endChar, letters, numbers);
        assertEquals("Length is not as expected.", length, randomCustomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomCustomString.charAt(i);
            if (letters) {
                assertTrue("Character is not an alphabetic character.", Character.isAlphabetic(randomChar));
            } else {
                assertFalse("Character is an alphabetic character.", Character.isAlphabetic(randomChar));
            }
            if (numbers) {
                assertTrue("Character is not a numeric character.", Character.isDigit(randomChar));
            } else {
                assertFalse("Character is a numeric character.", Character.isDigit(randomChar));
            }
            if (startChar <= randomChar && randomChar <= endChar) {
                assertTrue("Random character is not within the given range.", randomChar >= startChar && randomChar <= endChar);
            } else {
                fail("Random character is not within the given range.");
            }
        }
        // Test with empty String as argument
        String emptyCustomString = RandomStringUtils.random(0, startChar, endChar, letters, numbers);
        assertEquals("String length is not zero.", "", emptyCustomString);
        // Test with empty set of characters
        String emptySetString = RandomStringUtils.random(length, (char) 0, (char) 0, letters, numbers);
        assertEquals("String length is not zero.", "", emptySetString);
        // Test with invalid range
        String invalidRangeString = RandomStringUtils.random(length, (char) 1, (char) 0, letters, numbers);
        assertEquals("String length is not zero.", "", invalidRangeString);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, char[], java.util.Random)} .
     */
    public void testRandomCustomWithRandom() {
        int length = 10;
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        Random random = new Random();
        String randomCustomString = RandomStringUtils.random(length, chars, random);
        assertEquals("Length is not as expected.", length, randomCustomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomCustomString.charAt(i);
            assertTrue("Character is not within the given set.", java.util.Arrays.binarySearch(chars, randomChar) >= 0);
        }
        // Test with empty String as argument
        String emptyCustomString = RandomStringUtils.random(0, chars, random);
        assertEquals("String length is not zero.", "", emptyCustomString);
        // Test with empty set of characters
        String emptySetString = RandomStringUtils.random(length, null, random);
        assertEquals("String length is not zero.", "", emptySetString);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], java.util.Random)} .
     */
    public void testRandomCustomWithRandomAndCustomSet() {
        int length = 10;
        char startChar = 'A';
        char endChar = 'Z';
        boolean letters = true;
        boolean numbers = false;
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        Random random = new Random();
        String randomCustomString = RandomStringUtils.random(length, startChar, endChar, letters, numbers, chars, random);
        assertEquals("Length is not as expected.", length, randomCustomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomCustomString.charAt(i);
            if (letters) {
                assertTrue("Character is not an alphabetic character.", Character.isAlphabetic(randomChar));
            } else {
                assertFalse("Character is an alphabetic character.", Character.isAlphabetic(randomChar));
            }
            if (numbers) {
                assertTrue("Character is not a numeric character.", Character.isDigit(randomChar));
            } else {
                assertFalse("Character is a numeric character.", Character.isDigit(randomChar));
            }
            if (startChar <= randomChar && randomChar <= endChar) {
                assertTrue("Random character is not within the given range.", randomChar >= startChar && randomChar <= endChar);
            } else {
                fail("Random character is not within the given range.");
            }
            assertTrue("Character is within the given set.", java.util.Arrays.binarySearch(chars, randomChar) >= 0);
        }
        // Test with empty String as argument
        String emptyCustomString = RandomStringUtils.random(0, startChar, endChar, letters, numbers, chars, random);
        assertEquals("String length is not zero.", "", emptyCustomString);
        // Test with empty set of characters
        String emptySetString = RandomStringUtils.random(length, startChar, endChar, letters, numbers, null, random);
        assertEquals("String length is not zero.", "", emptySetString);
        // Test with invalid range
        String invalidRangeString = RandomStringUtils.random(length, (char) 1, (char) 0, letters, numbers, chars, random);
        assertEquals("String length is not zero.", "", invalidRangeString);
    }
}