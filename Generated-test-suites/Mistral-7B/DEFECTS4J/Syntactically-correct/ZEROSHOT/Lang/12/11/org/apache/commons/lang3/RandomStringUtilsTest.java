package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default settings.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain printable ASCII characters.", Character.isISOControl(c) == false);
        }
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain ASCII characters.", Character.isISOControl(c) == false
                      && Character.isAlphabetic(c) == false && Character.isDigit(c) == true);
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain alphabetic characters.", Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) == true);
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain alphanumeric characters.", Character.isLetterOrDigit(c) || Character.isWhitespace(c) || Character.isISOControl(c) == true);
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain numeric characters.", Character.isDigit(c) || Character.isWhitespace(c) || Character.isISOControl(c) == true);
        }
    }

    /**
     * Test for random method with custom range and inclusion flags.
     */
    @Test
    public void testRandomWithCustomRange() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain specified characters.", ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".indexOf(c) != -1));
        }
    }

    /**
     * Test for random method with custom range, inclusion flags, and custom random number generator.
     */
    @Test
    public void testRandomWithCustomRangeAndRandomGenerator() {
        Random random = new Random();
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, random);
        int length = randomString.length();
        assertTrue("Generated string should have expected length.", length == 10);
        for (int i = 0; i < length; i++) {
            Character c = randomString.charAt(i);
            assertTrue("Generated string should only contain specified characters.", ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".indexOf(c) != -1));
        }
    }
}