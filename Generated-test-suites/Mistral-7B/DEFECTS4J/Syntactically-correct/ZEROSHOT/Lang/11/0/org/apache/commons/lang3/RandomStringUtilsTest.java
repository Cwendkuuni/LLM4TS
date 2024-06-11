package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(10);
        assertNotNull(random);
        assertEquals(10, random.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAscii);
        assertEquals(10, randomAscii.length());
        for (int i = 0; i < randomAscii.length(); i++) {
            char ch = randomAscii.charAt(i);
            assertTrue((ch >= 32 && ch <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabetic);
        assertEquals(10, randomAlphabetic.length());
        for (int i = 0; i < randomAlphabetic.length(); i++) {
            char ch = randomAlphabetic.charAt(i);
            assertTrue(Character.isAlphabetic(ch));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumeric);
        assertEquals(10, randomAlphanumeric.length());
        for (int i = 0; i < randomAlphanumeric.length(); i++) {
            char ch = randomAlphanumeric.charAt(i);
            assertTrue((Character.isAlphabetic(ch) || Character.isDigit(ch)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumeric);
        assertEquals(10, randomNumeric.length());
        for (int i = 0; i < randomNumeric.length(); i++) {
            char ch = randomNumeric.charAt(i);
            assertTrue(Character.isDigit(ch));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomBoolean() {
        String random = RandomStringUtils.random(10, true, false);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (int i = 0; i < random.length(); i++) {
            char ch = random.charAt(i);
            assertTrue(Character.isAlphabetic(ch));
        }

        String randomNumeric = RandomStringUtils.random(10, false, true);
        assertNotNull(randomNumeric);
        assertEquals(10, randomNumeric.length());
        for (int i = 0; i < randomNumeric.length(); i++) {
            char ch = randomNumeric.charAt(i);
            assertTrue(Character.isDigit(ch));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomCustom() {
        String random = RandomStringUtils.random(10, 'A', 'Z', true, true, null, RANDOM);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (int i = 0; i < random.length(); i++) {
            char ch = random.charAt(i);
            assertTrue((Character.isAlphabetic(ch) || Character.isDigit(ch)) && (ch >= 'A' && ch <= 'Z'));
        }

        String randomLower = RandomStringUtils.random(10, 'a', 'z', true, true, null, RANDOM);
        assertNotNull(randomLower);
        assertEquals(10, randomLower.length());
        for (int i = 0; i < randomLower.length(); i++) {
            char ch = randomLower.charAt(i);
            assertTrue((Character.isAlphabetic(ch) || Character.isDigit(ch)) && (ch >= 'a' && ch <= 'z'));
        }

        String randomCustom = RandomStringUtils.random(10, '1', '9', true, false, new char[]{'0', '2', '3', '4', '5', '6', '7', '8'}, RANDOM);
        assertNotNull(randomCustom);
        assertEquals(10, randomCustom.length());
        for (int i = 0; i < randomCustom.length(); i++) {
            char ch = randomCustom.charAt(i);
            assertTrue(Character.isDigit(ch) && (ch >= '1' && ch <= '9'));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomCustomWithSurrogate() {
        String random = RandomStringUtils.random(10, 0, 10000, true, true, null, RANDOM);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (int i = 0; i < random.length(); i++) {
            char ch = random.charAt(i);
            if (Character.isHighSurrogate(ch)) {
                assertEquals(Character.HIGH_SURROGATE_VALUE + i, ch);
                if (i < random.length() - 1) {
                    char next = random.charAt(i + 1);
                    assertTrue(Character.isLowSurrogate(next));
                    assertEquals(Character.LOW_SURROGATE_VALUE + (i + 1), next);
                }
            } else {
                assertTrue((ch >= 0x20 && ch <= 0x7E) || (ch >= 0xA0 && ch <= 0xFF));
            }
        }
    }
}