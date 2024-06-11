package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Random character should be a printable ASCII character", Character.isISOControl(randomChar) == false);
        }
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAsciiString.charAt(i);
            assertTrue("Random character should be an ASCII character", (randomChar >= 32 && randomChar <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAlphabeticString.charAt(i);
            assertTrue("Random character should be an alphabetic character", Character.isAlphabetic(randomChar));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            assertTrue("Random character should be an alphanumeric character", Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomNumericString.charAt(i);
            assertTrue("Random character should be a numeric character", Character.isDigit(randomChar));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        char[] chars = {'a', 'b', 'c'};
        int count = 10;
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Random character should be one of the specified chars", contains(chars, randomChar));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringOutOfBounds() {
        int start = 50;
        int end = 5;
        RandomStringUtils.random(10, start, end, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeStart() {
        int start = -1;
        int end = 5;
        RandomStringUtils.random(10, start, end, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeEnd() {
        int start = 5;
        int end = -1;
        RandomStringUtils.random(10, start, end, false, false, null, RANDOM);
    }

    @Test
    public void testRandomStringEmptySet() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, "");
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Random character should be a printable ASCII character", Character.isISOControl(randomChar) == false);
        }
    }

    private static boolean contains(char[] array, char character) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == character) {
                return true;
            }
        }
        return false;
    }
}