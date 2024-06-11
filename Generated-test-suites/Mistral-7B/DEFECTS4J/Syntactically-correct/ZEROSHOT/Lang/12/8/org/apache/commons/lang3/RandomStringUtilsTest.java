package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Tests for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default options.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test randomAscii method with default options.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
    }

    /**
     * Test randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
    }

    /**
     * Test randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
    }

    /**
     * Test randomNumeric method with default options.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
    }

    /**
     * Test random method with custom options.
     */
    @Test
    public void testRandomCustomOptions() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                fail("Generated string contains non-alphanumeric character: " + c);
            }
        }
    }

    /**
     * Test random method with custom start and end options.
     */
    @Test
    public void testRandomCustomStartAndEnd() {
        String randomString = RandomStringUtils.random(5, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (!Character.isAlphabetic(c) || Character.toLowerCase(c) < 'a' || Character.toLowerCase(c) > 'z') {
                fail("Generated string contains character out of range: " + c);
            }
        }
    }

    /**
     * Test random method with custom start, end, and options.
     */
    @Test
    public void testRandomCustomStartEndOptions() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (!Character.isAlphabetic(c) || Character.toLowerCase(c) < 'a' || Character.toLowerCase(c) > 'z') {
                fail("Generated string contains character out of range: " + c);
            }
        }
    }

    /**
     * Test random method with custom set of characters.
     */
    @Test
    public void testRandomCustomSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, customSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (!customSet.contains(Character.toString(c))) {
                fail("Generated string contains character not in set: " + c);
            }
        }
    }

}