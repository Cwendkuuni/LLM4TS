package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating random strings with specified length.
     */
    @Test
    public void testRandomStringCreation() {
        // Test creating random strings of different lengths
        for (int i = 0; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i);
            Assert.assertEquals("Random string of length " + i + " created.", i, randomString.length());
        }
    }

    /**
     * Test creating random strings with specified character set.
     */
    @Test
    public void testRandomStringCreationWithSet() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i, chars);
            Assert.assertEquals("Random string of length " + i + " created with given set.", i, randomString.length());
            for (int j = 0; j < i; j++) {
                Assert.assertTrue("Random string contains character from given set.", chars.indexOf(randomString.charAt(j)) != -1);
            }
        }
    }

    /**
     * Test creating random strings with ascii characters.
     */
    @Test
    public void testRandomAsciiStringCreation() {
        String randomString = RandomStringUtils.randomAscii(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Random string contains ASCII character.", Character.isASCII(randomString.charAt(i)));
        }
    }

    /**
     * Test creating random strings with alphabetic characters.
     */
    @Test
    public void testRandomAlphabeticStringCreation() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Random string contains alphabetic character.", Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test creating random strings with alphanumeric characters.
     */
    @Test
    public void testRandomAlphanumericStringCreation() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Random string contains alphanumeric character.", Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test creating random strings with numeric characters.
     */
    @Test
    public void testRandomNumericStringCreation() {
        String randomString = RandomStringUtils.randomNumeric(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Random string contains numeric character.", Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test creating random strings with custom character range.
     */
    @Test
    public void testRandomStringCreationWithCustomRange() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Random string contains character from given set.",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".indexOf(randomString.charAt(i)) != -1);
        }
    }

    /**
     * Test creating random strings with empty set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithEmptySet() {
        RandomStringUtils.random(10, new char[0]);
    }

    /**
     * Test creating random strings with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithNegativeLength() {
        RandomStringUtils.random(-1);
    }
}