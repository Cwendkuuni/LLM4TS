package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCountNegative() {
        int count = -5;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            assertEquals("Expected an IllegalArgumentException with message: Requested random string length " + count + " is less than 0.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCountZero() {
        int count = 0;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            assertEquals("Expected an IllegalArgumentException with message: Requested random string length " + count + " is less than 0.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRandomStringFunctionality() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Expected a random string of length " + count, count, randomString.length());
    }

    @Test
    public void testRandomStringLengthLimit() {
        int count = Integer.MAX_VALUE + 1;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Expected a random string of the maximum length " + Integer.MAX_VALUE, Integer.MAX_VALUE, randomString.length());
    }

    @Test
    public void testRandomStringNullInput() {
        String chars = null;
        int count = 5;
        try {
            RandomStringUtils.random(count, chars);
        } catch (IllegalArgumentException e) {
            assertEquals("Expected an IllegalArgumentException with message: The chars array must not be empty", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRandomStringEmptyInput() {
        String chars = "";
        int count = 5;
        String randomString = RandomStringUtils.random(count, chars.toCharArray());
        assertEquals("Expected a random string of the specified length containing characters from the given char array", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals("Expected all characters in the random string to be in the given char array", chars.charAt(i % chars.length()), randomString.charAt(i));
        }
    }
}
//