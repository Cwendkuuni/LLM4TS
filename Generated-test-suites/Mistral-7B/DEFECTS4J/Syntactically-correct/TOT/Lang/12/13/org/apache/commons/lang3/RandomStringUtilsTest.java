package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test of random method, of class RandomStringUtils.
     */
    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[\u0000-\u007F\u0080-\uFFFD]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.random(0);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.random(-5);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of randomAscii method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[\u0020-\u007E\u00A0-\uFFFF]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.randomAscii(0);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.randomAscii(-5);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of randomAlphabetic method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[\u0041-\u005A\u0061-\u007A]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.randomAlphabetic(-5);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of randomAlphanumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[\u0020-\u007E\u0030-\u0039\u0041-\u005A\u0061-\u007A]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.randomAlphanumeric(-5);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of randomNumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[0-9]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.randomNumeric(0);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.randomNumeric(-5);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of random method, of class RandomStringUtils, with various options.
     */
    @Test
    public void testRandomWithOptions() {
        String result = RandomStringUtils.random(10, true, true);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[0-9A-Za-z]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.random(0, true, true);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.random(-5, true, true);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of random method, of class RandomStringUtils, with various options and custom set of chars.
     */
    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, '!', '~', false, false, null, RANDOM);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[!-~]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.random(0, '!', '~', false, false, null, RANDOM);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.random(-5, '!', '~', false, false, null, RANDOM);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test of random method, of class RandomStringUtils, with various options and custom set of chars.
     */
    @Test
    public void testRandomWithCustomCharsAndLimit() {
        String result = RandomStringUtils.random(10, '!', '~', true, false, "!@#$%^&*()_+-=[]{}|;:,.<>/?", RANDOM);
        assertEquals(10, result.length());
        // typical use case
        assertTrue(result.matches("[!@#$%^&*()_+-=[]{}|;:,.<>/?]+"));

        // edge case: empty string
        String emptyResult = RandomStringUtils.random(0, '!', '~', true, false, "!@#$%^&*()_+-=[]{}|;:,.<>/?", RANDOM);
        assertEquals("", emptyResult);

        // error scenario: negative length
        try {
            String negativeResult = RandomStringUtils.random(-5, '!', '~', true, false, "!@#$%^&*()_+-=[]{}|;:,.<>/?", RANDOM);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }
}