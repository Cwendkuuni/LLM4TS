package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isISOControl(randomString.charAt(i)) || Character.isHighSurrogate(randomString.charAt(i)) || Character.isLowSurrogate(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isISOControl(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
            assertTrue(randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z');
        }
    }

    @Test
    public void testRandomWithCharArray() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertNotNull(randomString.charAt(i));
            assertTrue(new String(new char[]{randomString.charAt(i)}).indexOf(String.valueOf(new char[]{'a', 'b', 'c'})) >= 0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharArray() {
        RandomStringUtils.random(1, new char[0]);
    }

}