package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandom() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Random length string of size: " + count, count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAscii() {
        final int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Random Ascii length string of size: " + count, count, randomAsciiString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabetic() {
        final int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Random alphabetic length string of size: " + count, count, randomAlphabeticString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumeric() {
        final int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Random alphanumeric length string of size: " + count, count, randomAlphanumericString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumeric() {
        final int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Random numeric length string of size: " + count, count, randomNumericString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, boolean, boolean)} methods.
     */
    @Test
    public void testRandomWithLettersAndNumbers() {
        final int count = 10;
        String randomAlphanumericString = RandomStringUtils.random(count, true, true);
        assertEquals("Random alphanumeric length string of size: " + count, count, randomAlphanumericString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} methods.
     */
    @Test
    public void testRandomWithCustomCharset() {
        final int count = 10;
        final String charset = "abcdefghijklmnopqrstuvwxyz0123456789";
        String randomCustomString = RandomStringUtils.random(count, charset.length(), charset.length(), true, true, charset.toCharArray(), new Random());
        assertEquals("Random string of size: " + count + " with custom charset of length: " + charset.length(), count, randomCustomString.length());
    }
}