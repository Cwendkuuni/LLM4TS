package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test file for the RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for the random() method with default character set and length 10.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for the randomAscii() method with length 5.
     */
    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
    }

    /**
     * Test for the randomAlphabetic() method with length 8.
     */
    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(8);
        assertNotNull(randomAlphabeticString);
        assertEquals(8, randomAlphabeticString.length());
    }

    /**
     * Test for the randomAlphanumeric() method with length 12.
     */
    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(12);
        assertNotNull(randomAlphanumericString);
        assertEquals(12, randomAlphanumericString.length());
    }

    /**
     * Test for the randomNumeric() method with length 6.
     */
    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(6);
        assertNotNull(randomNumericString);
        assertEquals(6, randomNumericString.length());
    }

    /**
     * Test for the random() method with length 7, letters set to true, and numbers set to false.
     */
    @Test
    public void testRandomStringWithLettersOnly() {
        String randomString = RandomStringUtils.random(7, true, false);
        assertNotNull(randomString);
        assertEquals(7, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isLetter(c));
        }
    }

    /**
     * Test for the random() method with length 15, letters set to false, and numbers set to true.
     */
    @Test
    public void testRandomStringWithNumbersOnly() {
        String randomString = RandomStringUtils.random(15, false, true);
        assertNotNull(randomString);
        assertEquals(15, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }
    }

    /**
     * Test for the random() method with length 12, letters set to true, numbers set to true, and custom character set.
     */
    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String randomString = RandomStringUtils.random(12, 'A', 'Z', '0', '9', null);
        assertNotNull(randomString);
        assertEquals(12, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isLetter(c)) {
                assertEquals(true, Character.isUpperCase(c));
            } else {
                assertEquals(true, Character.isDigit(c));
            }
        }
    }
}