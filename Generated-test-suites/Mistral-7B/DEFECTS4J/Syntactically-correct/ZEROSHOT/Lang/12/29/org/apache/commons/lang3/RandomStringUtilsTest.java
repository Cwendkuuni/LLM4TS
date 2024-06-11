package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test of random method, of class RandomStringUtils.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isISOControl(randomString.charAt(i))
                    || Character.isWhitespace(randomString.charAt(i))
                    || Character.isLetterOrDigit(randomString.charAt(i))
                    || Character.isHighSurrogate(randomString.charAt(i))
                    || Character.isLowSurrogate(randomString.charAt(i))
                    || Character.isUnicodeIdentifierStart(randomString.charAt(i))
                    || Character.isUnicodeIdentifierPart(randomString.charAt(i)));
        }
    }

    /**
     * Test of randomAscii method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue('!'.asChar() <= randomString.charAt(i)
                    && randomString.charAt(i) <= '~'.asChar());
        }
    }

    /**
     * Test of randomAlphabetic method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetter(randomString.charAt(i)));
        }
    }

    /**
     * Test of randomAlphanumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetterOrDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test of randomNumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with different
     * character sets.
     */
    @Test
    public void testRandomCustomCharacterSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz", 5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) == 'a'
                    || randomString.charAt(i) == 'b'
                    || randomString.charAt(i) == 'c'
                    || randomString.charAt(i) == 'd'
                    || randomString.charAt(i) == 'e'
                    || randomString.charAt(i) == 'f'
                    || randomString.charAt(i) == 'g'
                    || randomString.charAt(i) == 'h'
                    || randomString.charAt(i) == 'i'
                    || randomString.charAt(i) == 'j'
                    || randomString.charAt(i) == 'k'
                    || randomString.charAt(i) == 'l'
                    || randomString.charAt(i) == 'm'
                    || randomString.charAt(i) == 'n'
                    || randomString.charAt(i) == 'o'
                    || randomString.charAt(i) == 'p'
                    || randomString.charAt(i) == 'q'
                    || randomString.charAt(i) == 'r'
                    || randomString.charAt(i) == 's'
                    || randomString.charAt(i) == 't'
                    || randomString.charAt(i) == 'u'
                    || randomString.charAt(i) == 'v'
                    || randomString.charAt(i) == 'w'
                    || randomString.charAt(i) == 'x'
                    || randomString.charAt(i) == 'y'
                    || randomString.charAt(i) == 'z');
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with a null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullCharacterSet() {
        RandomStringUtils.random(5, null);
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing no characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing both letters and digits.
     */
    @Test
    public void testRandomCharacterSetWithLettersAndDigits() {
        String randomString = RandomStringUtils.random("abcdef0123", 5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) == 'a'
                    || randomString.charAt(i) == 'b'
                    || randomString.charAt(i) == 'c'
                    || randomString.charAt(i) == 'd'
                    || randomString.charAt(i) == 'e'
                    || randomString.charAt(i) == 'f'
                    || randomString.charAt(i) == '0'
                    || randomString.charAt(i) == '1'
                    || randomString.charAt(i) == '2'
                    || randomString.charAt(i) == '3');
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing only letters.
     */
    @Test
    public void testRandomCharacterSetWithOnlyLetters() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz", 5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetter(randomString.charAt(i)));
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing only digits.
     */
    @Test
    public void testRandomCharacterSetWithOnlyDigits() {
        String randomString = RandomStringUtils.random("0123456789", 5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing only surrogates.
     */
    @Test
    public void testRandomCharacterSetWithOnlySurrogates() {
        String randomString = RandomStringUtils.random("", 10, new char[]{'🅱', '🅳', '🅴', '🅵', '🅶', '🅷', '🅸', '🅹', '🅺', '🅻'}, new Random());
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isHighSurrogate(randomString.charAt(i))
                    || Character.isLowSurrogate(randomString.charAt(i))
                    || Character.isUnicodeIdentifierStart(randomString.charAt(i))
                    || Character.isUnicodeIdentifierPart(randomString.charAt(i))
                    || Character.isISOControl(randomString.charAt(i))
                    || Character.isWhitespace(randomString.charAt(i)));
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with a character set
     * containing both letters and surrogates.
     */
    @Test
    public void testRandomCharacterSetWithLettersAndSurrogates() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz🅱🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿", 5, new Random());
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetter(randomString.charAt(i))
                    || Character.isHighSurrogate(randomString.charAt(i))
                    || Character.isLowSurrogate(randomString.charAt(i))
                    || Character.isUnicodeIdentifierStart(randomString.charAt(i))
                    || Character.isUnicodeIdentifierPart(randomString.charAt(i))
                    || Character.isISOControl(randomString.charAt(i))
                    || Character.isWhitespace(randomString.charAt(i)));
        }
    }
}