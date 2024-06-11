package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    /**
     * Test random String creation with default settings
     */
    @Test
    public void testRandomStringCreation() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test random String creation with specified length and char set
     */
    @Test
    public void testRandomStringCreationWithCharSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.indexOf(c) > -1);
            assertTrue((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
        }
    }

    /**
     * Test random String creation with specified length, char set and seed
     */
    @Test
    public void testRandomStringCreationWithCharSetAndSeed() {
        Long seed = System.currentTimeMillis();
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, seed);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.indexOf(c) > -1);
            assertTrue((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
        }

        // Test with the same seed
        String randomString1 = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, seed);
        String randomString2 = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, seed);
        assertEquals(randomString1, randomString2);
    }

    /**
     * Test random String creation with negative length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test random String creation with empty char set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithEmptyCharSet() {
        RandomStringUtils.random("", 10);
    }
}