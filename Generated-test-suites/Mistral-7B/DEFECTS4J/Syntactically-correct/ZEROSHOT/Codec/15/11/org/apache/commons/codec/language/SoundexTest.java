package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test class for the Soundex class.
 */
public class SoundexTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    /**
     * Test US_ENGLISH mapping.
     */
    @Test
    public void testUS_ENGLISH_Mapping() {
        Soundex soundex = Soundex.US_ENGLISH;
        char[] mapping = soundex.getSoundexMapping();
        assertEquals('0', mapping[0]);
        assertEquals('1', mapping[1]);
        assertEquals('0', mapping[2]);
        assertEquals('2', mapping[3]);
        assertEquals('1', mapping[4]);
        assertEquals('2', mapping[5]);
        assertEquals('0', mapping[6]);
        assertEquals('2', mapping[7]);
        assertEquals('1', mapping[8]);
        assertEquals('2', mapping[9]);
        assertEquals('0', mapping[10]);
        assertEquals('2', mapping[11]);
        assertEquals('3', mapping[12]);
        assertEquals('0', mapping[13]);
        assertEquals('3', mapping[14]);
        assertEquals('0', mapping[15]);
        assertEquals('3', mapping[16]);
        assertEquals('0', mapping[17]);
        assertEquals('3', mapping[18]);
        assertEquals('5', mapping[19]);
        assertEquals('0', mapping[20]);
        assertEquals('5', mapping[21]);
        assertEquals('5', mapping[22]);
        assertEquals('5', mapping[23]);
        assertEquals('5', mapping[24]);
        assertEquals('5', mapping[25]);
        assertEquals('0', mapping[26]);
    }

    /**
     * Test encode method with string arguments.
     */
    @Test
    public void testEncodeStringString() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        String str3 = "Banana";
        String expected1 = "A231";
        String expected2 = "O150";
        String expected3 = "B161";

        assertEquals(expected1, soundex.soundex(str1));
        assertEquals(expected2, soundex.soundex(str2));
        assertEquals(expected3, soundex.soundex(str3));
    }

    /**
     * Test encode method with Object arguments.
     */
    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        Object obj1 = str1;
        String expected1 = "A231";

        assertEquals(expected1, soundex.encode(obj1));
    }

    /**
     * Test difference method.
     */
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        String str3 = "Banana";
        int expected1 = 2;
        int expected2 = 1;
        int expected3 = 3;

        assertEquals(expected1, soundex.difference(str1, str2));
        assertEquals(expected2, soundex.difference(str2, str1));
        assertEquals(expected3, soundex.difference(str1, str3));
    }
}