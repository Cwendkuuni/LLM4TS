package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string with default parameters
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test random string with specific character set
        String alphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(alphanumericString);
        assertEquals(10, alphanumericString.length());
        for (char c : alphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }

        // Test random string with specified length and character range
        String customString = RandomStringUtils.random(10, 48, 57);
        assertNotNull(customString);
        assertEquals(10, customString.length());
        for (char c : customString.toCharArray()) {
            assertTrue(Character.isDigit(c) && (c >= 48 && c <= 57));
        }

        // Test random string with specified length, character range and random seed
        Random random = new Random(1);
        String seededString = RandomStringUtils.random(10, 48, 57, random);
        assertNotNull(seededString);
        assertEquals(10, seededString.length());
        for (char c : seededString.toCharArray()) {
            assertTrue(Character.isDigit(c) && (c >= 48 && c <= 57));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterArray() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundCharacterArray() {
        char[] chars = new char[5];
        RandomStringUtils.random(10, 5, 1, true, true, chars);
    }
}