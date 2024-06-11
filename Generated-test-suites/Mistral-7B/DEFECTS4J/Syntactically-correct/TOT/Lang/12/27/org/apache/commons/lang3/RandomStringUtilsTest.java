package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int length = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomAsciiString);
        assertEquals(length, randomAsciiString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAsciiString.charAt(i);
            assertTrue(randomChar >= 32 && randomChar <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int length = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomAlphabeticString);
        assertEquals(length, randomAlphabeticString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int length = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomAlphanumericString);
        assertEquals(length, randomAlphanumericString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            if (Character.isAlphabetic(randomChar)) {
                assertTrue(Character.isLetter(randomChar) || Character.isDigit(randomChar));
            } else {
                assertTrue(Character.isDigit(randomChar));
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        int length = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomNumericString);
        assertEquals(length, randomNumericString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomStringWithRangeAndLetters() {
        int length = 10;
        char start = 'a';
        char end = 'z';
        String randomString = RandomStringUtils.random(length, start, end, true, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) && randomChar >= start && randomChar <= end);
        }
    }

    @Test
    public void testRandomStringWithRangeAndLettersAndNumbers() {
        int length = 10;
        char start = 'a';
        char end = 'z';
        String randomString = RandomStringUtils.random(length, start, end, true, true, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            if (Character.isAlphabetic(randomChar)) {
                assertTrue(Character.isLetter(randomChar) && randomChar >= start && randomChar <= end);
            } else {
                assertTrue(Character.isDigit(randomChar) || randomChar >= 48 && randomChar <= 57);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharArray() {
        char[] emptyCharArray = new char[0];
        RandomStringUtils.random(0, emptyCharArray);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringOutOfBounds() {
        char[] charArray = {'a', 'b'};
        RandomStringUtils.random(2, charArray, 3, true, false);
    }
}