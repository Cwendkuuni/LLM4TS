package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for the Soundex class.
 */
public class SoundexTest {

    /**
     * Test encoding of single characters.
     *
     * @throws EncoderException if an error occurs encoding a character
     */
    @Test
    public void testEncodeSingleCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        CharSequence[] testCases = new CharSequence[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for (CharSequence cs : testCases) {
            String encoded = soundex.encode(cs.toString());
            assertEquals("Incorrect Soundex encoding for character: " + cs, String.valueOf(soundex.getMappingCode(cs.toString(), 0)), encoded);
        }
    }

    /**
     * Test encoding of strings with multiple characters.
     *
     * @throws EncoderException if an error occurs encoding a string
     */
    @Test
    public void testEncodeStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String[] testCases = new String[]{"Apple", "Banana", "Cat", "Dog", "Elephant", "Fish", "Grapes", "Hippopotamus", "IceCream", "Jaguar", "Kangaroo", "Lemon", "Mango", "Nightingale", "Orange", "Penguin", "Queen", "Raspberry", "Strawberry", "Tomato", "Umbrella", "Violet", "Watermelon", "Xylophone", "Yak", "Zebra"};
        for (String str : testCases) {
            String encoded = soundex.encode(str);
            String expectedEncoding = soundex.soundex(str);
            assertEquals("Incorrect Soundex encoding for string: " + str, expectedEncoding, encoded);
        }
    }

    /**
     * Test encoding with special characters.
     *
     * @throws EncoderException if an error occurs encoding a string
     */
    @Test
    public void testEncodeSpecialCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        String[] testCases = new String[]{"Apple!", "Banana$", "Cat#", "Dog%", "Elephant@", "Fish&", "Grapes^", "Hippopotamus_", "IceCream~", "Jaguar`", "Kangaroo|", "Lemon(", "Mango)", "Nightingale<", "Orange>", "Penguin{", "Queen[", "Raspberry]", "Strawberry|", "Tomato\"", "Umbrella\"", "Violet\"", "Watermelon\"", "Xylophone|", "Yak$", "Zebra%"};
        for (String str : testCases) {
            String encoded = soundex.encode(str);
            String expectedEncoding = soundex.soundex(str);
            assertEquals("Incorrect Soundex encoding for string with special characters: " + str, expectedEncoding, encoded);
        }
    }

    /**
     * Test encoding with empty and null strings.
     *
     * @throws EncoderException if an error occurs encoding a string
     */
    @Test
    public void testEncodeEmptyAndNullStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String[] testCases = new String[]{"", null};
        for (String str : testCases) {
            String encoded = soundex.encode(str);
            assertEquals("Incorrect Soundex encoding for empty or null string", "", encoded);
        }
    }

    /**
     * Test encoding with long strings.
     *
     * @throws EncoderException if an error occurs encoding a string
     */
    @Test
    public void testEncodeLongStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String longString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        String encoded = soundex.encode(longString);
        String expectedEncoding = soundex.soundex(longString);
        assertEquals("Incorrect Soundex encoding for long string", expectedEncoding, encoded);
    }

    /**
     * Test encoding with non-ASCII characters.
     *
     * @throws EncoderException if an error occurs encoding a string
     */
    @Test
    public void testEncodeNonAsciiCharacters() throws EncoderException {
        Soundex soundex = new Soundex("US_ENGLISH_MAPPING_FOR_NON_ASCII_CHARACTERS");
        String[] testCases = new String[]{"Appel", "Banane", "Catégorie", "Dogue", "Éléphant", "Fish", "Grapes", "Hippopotamus", "IceCrème", "Jaguar", "Kängurou", "Lemondes", "Mangue", "Nuitingale", "Orange", "Penguin", "Queen", "Raspberry", "Strawberry", "Tomate", "Umbrelle", "Violette", "Watermélon", "Xylophone", "Yak", "Zèbre"};
        for (String str : testCases) {
            String encoded = soundex.encode(str);
            String expectedEncoding = soundex.soundex(str);
            assertEquals("Incorrect Soundex encoding for string with non-ASCII characters: " + str, expectedEncoding, encoded);
        }
    }
}