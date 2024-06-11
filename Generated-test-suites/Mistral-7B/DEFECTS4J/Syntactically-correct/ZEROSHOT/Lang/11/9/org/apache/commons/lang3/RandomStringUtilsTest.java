package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

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
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test randomAscii method with default options.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals((int) c, (int) (Math.random() * 95 + 32));
        }
    }

    /**
     * Test randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c));
        }
    }

    /**
     * Test randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test randomNumeric method with default options.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }
    }

    /**
     * Test random method with custom options.
     */
    @Test
    public void testRandomWithCustomOptions() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isLetter(c));
        }

        randomString = RandomStringUtils.random(10, false, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }

        randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test random method with custom start and end options.
     */
    @Test
    public void testRandomWithCustomStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 'A', 'Z');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c));
            assertEquals((int) c, (int) ('A' + Math.random() * 26));
        }

        randomString = RandomStringUtils.random(10, '0', '9');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
            assertEquals((int) c, (int) ('0' + Math.random() * 10));
        }
    }

    /**
     * Test random method with custom chars array and source of randomness.
     */
    @Test
    public void testRandomWithCustomCharsAndRandomness() {
        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        String randomString = RandomStringUtils.random(10, chars, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(chars[new Random().nextInt(chars.length)], randomString.charAt(i));
        }
    }
}