package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex encoder.
 */
public class SoundexTest {

    /**
     * Test for the default Soundex encoder.
     */
    @Test
    public void testDefaultSoundexEncoder() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("E000", soundex.encode("apple"));
        assertEquals("B261", soundex.encode("banana"));
        assertEquals("E151", soundex.encode("echo"));
        assertEquals("F622", soundex.encode("frequency"));
        assertEquals("A320", soundex.encode("america"));
        assertEquals("A220", soundex.encode("austria"));
        assertEquals("E000", soundex.encode(""));
        assertEquals("E000", soundex.encode(null));
        try {
            soundex.encode("!");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test for custom Soundex encoder.
     */
    @Test
    public void testCustomSoundexEncoder() throws EncoderException {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertEquals("E000", soundex.encode("apple"));
        assertEquals("B261", soundex.encode("banana"));
        assertEquals("E151", soundex.encode("echo"));
        assertEquals("F622", soundex.encode("frequency"));
        assertEquals("A320", soundex.encode("america"));
        assertEquals("A220", soundex.encode("austria"));
        assertEquals("E000", soundex.encode(""));
        assertEquals("E000", soundex.encode(null));
        try {
            soundex.encode("!");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        Soundex internationalSoundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'});
        assertEquals("A000", internationalSoundex.encode("apple"));
        assertEquals("A220", internationalSoundex.encode("austria"));
        // other tests for internationalSoundex encoder
    }

    /**
     * Test for difference method.
     */
    @Test
    public void testDifferenceMethod() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals(3, soundex.difference("apple", "appel"));
        assertEquals(3, soundex.difference("banana", "plantain"));
        assertEquals(4, soundex.difference("echo", "echo"));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference(null, ""));
        try {
            soundex.difference("", null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        Soundex internationalSoundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'});
        assertEquals(3, internationalSoundex.difference("apple", "appel"));
        // other tests for difference method with internationalSoundex encoder
    }
}