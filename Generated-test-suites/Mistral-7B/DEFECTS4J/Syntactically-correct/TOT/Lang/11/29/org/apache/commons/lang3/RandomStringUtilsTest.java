package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomString = RandomStringUtils.random(length);
            assertEquals("Random string of length " + length, length, randomString.length());
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomAsciiString = RandomStringUtils.randomAscii(length);
            for (int j = 0; j < length; j++) {
                int randomChar = new Random().nextInt(127);
                assertEquals("Random ASCII character: expected = " + (char) randomChar + ", actual = "
                        + randomAsciiString.charAt(j), (char) randomChar, randomAsciiString.charAt(j));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
            for (int j = 0; j < length; j++) {
                int randomChar = new Random().nextInt(26) + 'a';
                assertEquals("Random alphabetic character: expected = " + (char) randomChar + ", actual = "
                        + randomAlphabeticString.charAt(j), (char) randomChar, randomAlphabeticString.charAt(j));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
            boolean hasLetter = false;
            boolean hasNumber = false;
            for (int j = 0; j < length; j++) {
                int randomChar = new Random().nextInt(52);
                char currentChar = (char) randomChar;
                if (Character.isAlphabetic(currentChar)) {
                    hasLetter = true;
                }
                if (Character.isDigit(currentChar)) {
                    hasNumber = true;
                }
                assertEquals("Random alphanumeric character: expected = " + currentChar + ", actual = "
                        + randomAlphanumericString.charAt(j), currentChar, randomAlphanumericString.charAt(j));
            }
            assertTrue("Random alphanumeric string must contain at least one letter: " + randomAlphanumericString, hasLetter);
            assertTrue("Random alphanumeric string must contain at least one number: " + randomAlphanumericString, hasNumber);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomNumericString = RandomStringUtils.randomNumeric(length);
            for (int j = 0; j < length; j++) {
                int randomChar = new Random().nextInt(10) + '0';
                assertEquals("Random numeric character: expected = " + randomChar + ", actual = "
                        + randomNumericString.charAt(j), randomChar, randomNumericString.charAt(j));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method
     * with the given start and end characters.
     */
    @Test
    public void testRandomStringWithSpecifiedCharacters() {
        char startChar = 'A';
        char endChar = 'Z';
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomString = RandomStringUtils.random(length, startChar, endChar, false, false);
            for (int j = 0; j < length; j++) {
                char expectedChar = (char) (startChar + new Random().nextInt(endChar - startChar + 1));
                assertEquals("Random character: expected = " + expectedChar + ", actual = "
                        + randomString.charAt(j), expectedChar, randomString.charAt(j));
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method
     * with the given start and end characters and a supplied {@link Random} instance.
     */
    @Test
    public void testRandomStringWithSpecifiedCharactersAndSeed() {
        char startChar = 'A';
        char endChar = 'Z';
        Random seededRandom = new Random(1L); // seeded with a constant value for repeatable results
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20);
            String randomString = RandomStringUtils.random(length, startChar, endChar, false, false, null, seededRandom);
            String constantRandomString = RandomStringUtils.random(length, startChar, endChar, false, false, null, new Random(1L));
            assertEquals("Random strings generated with the same seed must be equal: " + randomString + " != " + constantRandomString, constantRandomString, randomString);
        }
    }

    /**
     * Test for generating random strings using the given character set.
     */
    @Test
    public void testRandomStringWithGivenCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(20) + 1; // minimum length = 1
            String randomString = RandomStringUtils.random(length, characterSet.toCharArray());
            for (int j = 0; j < length; j++) {
                assertEquals("Random character: expected = " + characterSet.charAt(new Random().nextInt(characterSet.length())) + ", actual = "
                        + randomString.charAt(j), characterSet.charAt(new Random().nextInt(characterSet.length())), randomString.charAt(j));
            }
        }
    }
}