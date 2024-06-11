package org.apache.commons.lang3.test;

import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating random String of given length.
     */
    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i);
            assertEquals("Random String of length " + i, i, randomString.length());
        }
    }

    /**
     * Test creating random Ascii String of given length.
     */
    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(i);
            for (int j = 0; j < i; j++) {
                char c = randomAsciiString.charAt(j);
                assertTrue("Random Ascii character is not between 32 and 126", c >= 32 && c <= 126);
            }
        }
    }

    /**
     * Test creating random Alphabetic String of given length.
     */
    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(i);
            for (int j = 0; j < i; j++) {
                char c = randomAlphabeticString.charAt(j);
                assertTrue("Random Alphabetic character is not alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    /**
     * Test creating random Alphanumeric String of given length.
     */
    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(i);
            for (int j = 0; j < i; j++) {
                char c = randomAlphanumericString.charAt(j);
                assertTrue("Random Alphanumeric character is not alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating random Numeric String of given length.
     */
    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(i);
            for (int j = 0; j < i; j++) {
                char c = randomNumericString.charAt(j);
                assertTrue("Random Numeric character is not numeric", Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating random String of given length with specified characters.
     */
    @Test
    public void testRandomStringWithSpecifiedCharacters() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i, characters.toCharArray());
            for (int j = 0; j < i; j++) {
                char c = randomString.charAt(j);
                assertTrue("Random character is not in specified set", characters.indexOf(c) != -1);
            }
        }
    }

    /**
     * Test creating random String of given length with specified start and end characters.
     */
    @Test
    public void testRandomStringWithSpecifiedStartAndEndCharacters() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            int start = random.nextInt(characters.length());
            int end = random.nextInt(characters.length()) + 1;
            if (end > characters.length()) {
                end = characters.length();
            }
            String randomString = RandomStringUtils.random(i, characters.charAt(start), characters.charAt(end), false, false, null, RANDOM);
            for (int j = 0; j < i; j++) {
                char c = randomString.charAt(j);
                assertTrue("Random character is not in specified set", (c >= characters.charAt(start) && c <= characters.charAt(end)) || (start == 0 && end == 0));
            }
        }
    }
}