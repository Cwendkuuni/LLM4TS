package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    // Test for random method
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomStringAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Test for random method with range and flags
    @Test
    public void testRandomStringWithRangeAndFlags() {
        String randomString = RandomStringUtils.random(5, 1, 10, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c) && c >= '1' && c <= '9');
            } else {
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    // Test for random method with custom character array and flags
    @Test
    public void testRandomStringWithCustomCharacterArrayAndFlags() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String randomString = RandomStringUtils.random(5, chars.toCharArray(), true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(chars.indexOf(c) != -1);
            if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c) && c >= '1' && c <= '9');
            } else {
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    // Test for random method with custom character array and no flags
    @Test
    public void testRandomStringWithCustomCharacterArrayAndNoFlags() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String randomString = RandomStringUtils.random(5, chars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(chars.indexOf(c) != -1);
        }
    }

    // Test for random method with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    // Test for random method with empty character array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterArray() {
        RandomStringUtils.random(5, new char[0]);
    }

    // Test for random method with range equal to start and end
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithRangeEqualsStartAndEnd() {
        RandomStringUtils.random(5, 'A', 'A');
    }

    // Test for random method with start > end
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithStartGreaterThanEnd() {
        RandomStringUtils.random(5, 'A', 'Z');
        RandomStringUtils.random(5, 'C', 'B');
    }

    // Test for random method with invalid characters in character array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithInvalidCharactersInCharacterArray() {
        String chars = "ABC!DEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        RandomStringUtils.random(5, chars.toCharArray());
    }

    // Test for random method with null character array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullCharacterArray() {
        RandomStringUtils.random(5, null);
    }

    // Test for randomString method with empty argument
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyArgument() {
        RandomStringUtils.random("");
    }

    // Test for randomString method with negative argument
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeArgument() {
        RandomStringUtils.random(-1);
    }

    // Test for randomString method with null argument
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullArgument() {
        RandomStringUtils.random(null);
    }

    // Test for randomString method with empty character array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterArray() {
        RandomStringUtils.random(5, new char[0]);
    }
}