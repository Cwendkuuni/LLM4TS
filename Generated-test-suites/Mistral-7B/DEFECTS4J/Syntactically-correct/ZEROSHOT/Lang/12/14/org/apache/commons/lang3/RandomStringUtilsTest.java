package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils
 *
 */
public class RandomStringUtilsTest {

 /**
 * Test method for {@link RandomStringUtils#random(int)}
 */
 @Test
 public void testRandom() {
 String randomString = RandomStringUtils.random(5);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#randomAscii(int)}
 */
 @Test
 public void testRandomAscii() {
 String randomString = RandomStringUtils.randomAscii(5);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
 */
 @Test
 public void testRandomAlphabetic() {
 String randomString = RandomStringUtils.randomAlphabetic(5);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
 */
 @Test
 public void testRandomAlphanumeric() {
 String randomString = RandomStringUtils.randomAlphanumeric(5);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#randomNumeric(int)}
 */
 @Test
 public void testRandomNumeric() {
 String randomString = RandomStringUtils.randomNumeric(5);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}
 */
 @Test
 public void testRandomBoolean() {
 String randomStringLetters = RandomStringUtils.randomAlphabetic(5);
 String randomStringNumbers = RandomStringUtils.randomNumeric(5);
 assertNotNull(randomStringLetters);
 assertNotNull(randomStringNumbers);
 assertEquals(5, randomStringLetters.length());
 assertEquals(5, randomStringNumbers.length());
 }

 /**
 * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
 */
 @Test
 public void testRandomWithCustomParams() {
 int min = 33, max = 126;
 String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 Random random = new Random();
 String randomString = RandomStringUtils.random(5, min, max, true, true, chars.toCharArray(), random);
 assertNotNull(randomString);
 assertEquals(5, randomString.length());
 }

 /**
 * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
 * with empty character array
 */
 @Test(expected = IllegalArgumentException.class)
 public void testRandomEmptyCharacterArray() {
 String randomString = RandomStringUtils.random(5, 0, 0, false, false, null, RANDOM);
 }

}