package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.lang3.RandomStringUtils.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLengthZero() {
        String randomString = random(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomStringLengthNegative() {
        try {
            random(1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomString() {
        String randomString = random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringAscii() {
        String randomString = randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c > 31 && c < 127);
        }
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomString = randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertIsAlphabetic(c);
        }
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            isAlphabetic(c) || isDigit(c);
        }
    }

    @Test
    public void testRandomStringNumeric() {
        String randomString = randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertIsDigit(c);
        }
    }

    @Test
    public void testRandomStringCustomCharacters() {
        String randomString = random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ');
        }
    }

    @Test
    public void testRandomStringWithStartAndEnd() {
        String randomString = random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 0, 5, true, false, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || c == ' ');
        }
    }

    @Test
    public void testRandomStringWithStartAndEndAscii() {
        String randomString = randomAscii("!\"#$%&/()=?{|}~^ `[]@", 0, 5, false, false, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c > 31 && c < 127);
        }
    }

    @Test
    public void testRandomStringWithStartAndEndAlphabetic() {
        String randomString = randomAlphabetic("abcdefghijklmnopqrstuvwxyz", 0, 5, true, false, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertIsAlphabetic(c);
        }
    }

    @Test
    public void testRandomStringWithStartAndEndAlphanumeric() {
        String randomString = randomAlphanumeric("abcdefghijklmnopqrstuvwxyz0123456789", 0, 5, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            isAlphabetic(c) || isDigit(c);
        }
    }

    @Test
    public void testRandomStringWithStartAndEndNumeric() {
        String randomString = randomNumeric("0123456789", 0, 5, false, true, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertIsDigit(c);
        }
    }

    @Test
    public void testRandomStringWithCustomCharset() {
        String randomString = random("!\"#$%&/()=?{|}~^ `[]@abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 0, 10, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) ||
                    c == '!' || c == '\"' || c == '#' || c == '$' ||
                    c == '%' || c == '&' || c == '/' || c == '(' || c == ')' ||
                    c == '=' || c == '{' || c == '}' || c == '|' || c == '}' ||
                    c == '^' || c == '`' || c == '[' || c == ']' || c == '@');
        }
    }

    @Test
    public void testRandomStringWithLimitationsOnSurrogates() {
        String randomString = random(10, 0, 0, false, false, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isSurrogate(c)) {
                if (Character.isHighSurrogate(c)) {
                    int index = randomString.indexOf(c);
                    assertEquals(index + 1, randomString.indexOf(getLowSurrogate(c)));
                }
            }
        }

        try {
            random(1, 0, 57343, false, false, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}