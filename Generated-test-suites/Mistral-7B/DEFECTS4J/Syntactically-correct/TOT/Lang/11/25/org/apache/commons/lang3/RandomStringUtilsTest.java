package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLengthZero() {
        String randomString = RandomStringUtils.random(0);
        Assert.assertEquals("", randomString);
    }

    @Test
    public void testRandomStringLengthOne() {
        String randomString = RandomStringUtils.random(1);
        Assert.assertNotNull(randomString);
        Assert.assertFalse(randomString.isEmpty());
    }

    @Test
    public void testRandomStringAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isISOControl(c) || Character.isWhitespace(c) || Character.isDigit(c) || Character.isLetter(c) || Character.isUpperCase(c));
        }
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isWhitespace(c));
        }
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
        }
    }

    @Test
    public void testRandomStringNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringCustomSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCustomSet() {
        RandomStringUtils.random("", 1);
    }
}