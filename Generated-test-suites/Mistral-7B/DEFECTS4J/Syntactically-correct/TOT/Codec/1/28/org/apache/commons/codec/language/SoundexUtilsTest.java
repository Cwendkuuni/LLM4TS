package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link SoundexUtils} utility methods.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method with null input.
     */
    @Test
    public void testCleanNull() {
        assertEquals("", SoundexUtils.clean(null));
    }

    /**
     * Test for {@link SoundexUtils#clean(String)} method with empty input.
     */
    @Test
    public void testCleanEmpty() {
        assertEquals("", SoundexUtils.clean(""));
    }

    /**
     * Test for {@link SoundexUtils#clean(String)} method with an input containing
     * only letters.
     */
    @Test
    public void testCleanLetters() {
        String input = "TestString";
        String expected = "TESTSTRING";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#clean(String)} method with an input containing
     * non-letters.
     */
    @Test
    public void testCleanNonLetters() {
        String input = "Test1234String!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~ `";
        String expected = "TEST";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with null input, encoder, s1, and s2.
     */
    @Test
    public void testDifferenceNull() {
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(null, null, null));
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new StringEncoder() {}, null, null));
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new StringEncoder() {}, "", null));
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new StringEncoder() {}, null, ""));
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new StringEncoder() {}, null, null));
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with an input encoder that throws an exception.
     */
    @Test
    public void testDifferenceThrowsException() {
        StringEncoder encoder = new StringEncoder() {
            @Override
            public String encode(String s) throws EncoderException {
                throw new EncoderException("Encoding error");
            }
        };
        String s1 = "TestString1";
        String s2 = "TestString2";
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, s2));
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with an input encoder that returns an empty string.
     */
    @Test
    public void testDifferenceEmptyEncoded() {
        StringEncoder encoder = new StringEncoder() {
            @Override
            public String encode(String s) {
                return "";
            }
        };
        String s1 = "TestString1";
        String s2 = "TestString2";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with two different soundex encoded strings.
     */
    @Test
    public void testDifferenceDifferentStrings() {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = "TestString1";
        String s2 = "TestString2";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with two identical soundex encoded strings.
     */
    @Test
    public void testDifferenceIdenticalStrings() {
        StringEncoder encoder = new SoundexEncoder();
        String s1 = "TestString1";
        String s2 = "TestString1";
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method with
     * null input.
     */
    @Test
    public void testDifferenceEncodedNull() {
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(null, null));
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(null, ""));
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded("", null));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method
     * with two empty encoded strings.
     */
    @Test
    public void testDifferenceEncodedEmpty() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method
     * with two different encoded strings.
     */
    @Test
    public void testDifferenceEncodedDifferentStrings() {
        String es1 = "12345";
        String es2 = "54321";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method
     * with two identical encoded strings.
     */
    @Test
    public void testDifferenceEncodedIdenticalStrings() {
        String es1 = "12345";
        String es2 = "12345";
        assertEquals(5, SoundexUtils.differenceEncoded(es1, es2));
    }
}

class SoundexEncoder implements StringEncoder {
    @Override
    public String encode(String s) {
        String cleaned = SoundexUtils.clean(s);
        String soundex = null;
        switch (cleaned.charAt(0)) {
            case 'B':
                soundex = "B";
                break;
            case 'C':
                soundex = "K";
                break;
            case 'D':
                soundex = "D";
                break;
            case 'E':
                soundex = "S";
                break;
            case 'F':
                soundex = "F";
                break;
            case 'G':
                soundex = "G";
                break;
            case 'H':
                soundex = "H";
                break;
            case 'I':
                soundex = "J";
                break;
            case 'J':
                soundex = "J";
                break;
            case 'K':
                soundex = "K";
                break;
            case 'L':
                soundex = "L";
                break;
            case 'M':
                soundex = "N";
                break;
            case 'N':
                soundex = "N";
                break;
            case 'O':
                soundex = "P";
                break;
            case 'P':
                soundex = "P";
                break;
            case 'Q':
                soundex = "Q";
                break;
            case 'R':
                soundex = "R";
                break;
            case 'S':
                soundex = "S";
                break;
            case 'T':
                soundex = "T";
                break;
            case 'U':
                soundex = "T";
                break;
            case 'V':
                soundex = "V";
                break;
            case 'W':
                soundex = "W";
                break;
            case 'X':
                soundex = "X";
                break;
            case 'Y':
                soundex = "Y";
                break;
            case 'Z':
                soundex = "Z";
                break;
            default:
                soundex = "";
                break;
        }
        if (soundex != null) {
            StringBuilder encoded = new StringBuilder();
            encoded.append(soundex.charAt(0));
            for (int i = 1; i < cleaned.length(); i++) {
                char c = cleaned.charAt(i);
                char prev = cleaned.charAt(i - 1);
                char soundexChar = soundex.charAt(i);
                if (soundexChar != soundex.charAt(i - 1)) {
                    encoded.append(soundexChar);
                }
                if (prev != c && Character.isLetter(c)) {
                    encoded.append(soundexChar);
                }
            }
            return encoded.toString();
        } else {
            return "";
        }
    }
}