package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default options.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c)
                    || Character.isISOControl(c) || Character.isSpaceChar(c)
                    || Character.isAlphabetic((char) 55296 + new Random().nextInt(128))
                    || Character.isDigit((char) 56320 + new Random().nextInt(128)));
        }
    }

    /**
     * Test randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c)
                    || Character.isISOControl(c) || Character.isSpaceChar(c)
                    || Character.isAscii(c) && Character.isAlphabetic((char) c)
                    || Character.isAscii(c) && Character.isDigit((char) c));
        }
    }

    /**
     * Test randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random method with custom options.
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(10, true, true, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test random method with empty set of characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptySet() {
        RandomStringUtils.random(1, (char[]) null);
    }
}