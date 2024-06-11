package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            int randomChar = RandomStringUtils.RANDOM.nextInt(128);
            assertEquals(true, randomString.indexOf((char) randomChar) >= 0);
        }
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < 10; i++) {
            int randomChar = RandomStringUtils.RANDOM.nextInt(95) + 32;
            assertEquals(true, randomAsciiString.indexOf((char) randomChar) >= 0);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < 10; i++) {
            char randomChar = (char) (RandomStringUtils.RANDOM.nextInt(26) + 'a');
            assertEquals(true, randomAlphabeticString.indexOf(randomChar) >= 0);
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < 10; i++) {
            int randomChar = RandomStringUtils.RANDOM.nextInt(52);
            if (randomChar < 10) {
                assertEquals(true, Character.isDigit((char) randomChar));
            } else {
                assertEquals(true, Character.isAlphabetic((char) randomChar));
            }
            assertEquals(true, randomAlphanumericString.indexOf((char) randomChar) >= 0);
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < 10; i++) {
            int randomChar = RandomStringUtils.RANDOM.nextInt(10) + '0';
            assertEquals(true, randomNumericString.indexOf((char) randomChar) >= 0);
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            char randomChar = (char) (RandomStringUtils.RANDOM.nextInt(26) + 'a');
            assertEquals(true, randomString.indexOf(randomChar) >= 0);
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            int randomChar = RandomStringUtils.RANDOM.nextInt(128);
            assertEquals(true, randomString.indexOf((char) randomChar) >= 0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullChars() {
        RandomStringUtils.random(1, (char[]) null);
    }
}