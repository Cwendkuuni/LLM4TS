package org.apache.commons.codec.language;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone encoder = new Metaphone();

        String input = "apple";
        String expected = "A0P0L0E0";
        String output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "banana";
        expected = "B2N1A0N1A0";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "cucumber";
        expected = "K2U1K1U1B1M1N1R1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "example";
        expected = "X3M1P1L1E1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "metaphone";
        expected = "M3T1P1H1O1N1E1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "opportunity";
        expected = "O1P1P1O1P1N1T1Y1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "rhubarb";
        expected = "R1H1U1B1B1A1R1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "sherlock";
        expected = "S2H1R1L1O1C1K1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "tomorrow";
        expected = "T2M1O1R1M1O1R1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "two";
        expected = "T1W1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "xylophonist";
        expected = "X1L1O1P1H1O1N1I1S1T1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "zebra";
        expected = "Z2E1B1R1A1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "applebanana";
        expected = "A0P0L0E0B2N1A0N1A0";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "rhubarbapple";
        expected = "R1H1U1B1B1A1A0P0L0E0";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "sherlockholmes";
        expected = "S2H1R1L1O1C1K1H1M1E1S1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "tomorrowisquiterainy";
        expected = "T2M1O1R1M1O1R1I1S1Q1T1E1R1A1N1Y1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "thequickbrownfoxjumpsoverthelazydog";
        expected = "T1H1E1Q1U1I1C1K1B1R1O1N1F1O1X1P1O1V1E1R1T1H1E1L1A1Z1Y1D1O1G1";
        output = encoder.metaphone(input);
        assertEquals(expected, output);

        input = "";
        output = encoder.metaphone(input);
        assertEquals("", output);

        input = null;
        try {
            output = encoder.metaphone(input);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        input = new Object();
        try {
            output = encoder.metaphone(input);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        input = new StringBuffer();
        try {
            output = encoder.metaphone(input.toString());
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }

        input = "αβγδεζηθικλμνξοπρστυφχψωαβγδεζηθικλμνξοπρστυφχψω";
        expected = "A1B1G1D1Z1E1Z1H1I1K1L1M1N1X1O1P1R1S1T1F1K1P1S1O1F1C1P1S1";
        output = encoder.metaphone(input, Locale.US);
        assertEquals(expected, output);
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone encoder = new Metaphone();

        String input = "apple";
        String expected = "A0P0L0E0";
        String output = encoder.encode(input);
        assertEquals(expected, output);

        input = "banana";
        expected = "B2N1A0N1A0";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "cucumber";
        expected = "K2U1K1U1B1M1N1R1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "example";
        expected = "X3M1P1L1E1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "metaphone";
        expected = "M3T1P1H1O1N1E1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "opportunity";
        expected = "O1P1P1O1P1N1T1Y1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "rhubarb";
        expected = "R1H1U1B1B1A1R1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "sherlock";
        expected = "S2H1R1L1O1C1K1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "tomorrow";
        expected = "T2M1O1R1M1O1R1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "two";
        expected = "T1W1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "xylophonist";
        expected = "X1L1O1P1H1O1N1I1S1T1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "zebra";
        expected = "Z2E1B1R1A1";
        output = encoder.encode(input);
        assertEquals(expected, output);

        input = "applebanana";
        expected = "A0P0L0E0B2N1A0N1A0";
        output = encoder.encode(new String[]{"apple", "banana"});
        assertEquals(expected, output[0]);
        assertEquals(expected, output[1]);

        input = "rhubarbapple";
        expected = "R1H1U1B1B1A1A0P0L0E0";
        output = encoder.encode(new String[]{"rhubarb", "apple"});
        assertEquals(expected, output[0]);
        assertEquals(expected, output[1]);

        input = "sherlockholmes";
        expected = "S2H1R1L1O1C1K1H1M1E1S1";
        output = encoder.encode(new String[]{"sherlock", "holmes"});
        assertEquals(expected, output[0]);
        assertEquals(expected, output[1]);

        input = "tomorrowisquiterainy";
        expected = "T2M1O1R1M1O1R1I1S1Q1T1E1R1A1N1Y1";
        output = encoder.encode(new String[]{"tomorrow", "is", "quite", "rainy"});
        assertEquals(expected, output[2]);

        input = new String[]{"the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"};
        expected = "T1H1E1Q1U1I1C1K1B1R1O1N1F1O1X1P1O1V1E1R1T1H1E1L1A1Z1Y1D1O1G1";
        output = encoder.encode(input);
        assertEquals(expected, output[8]);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone encoder = new Metaphone();

        String str1 = "apple";
        String str2 = "apple";
        boolean expected = true;
        boolean output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "apple";
        str2 = "banana";
        expected = false;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "banana";
        str2 = "apple";
        expected = false;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "cucumber";
        str2 = "cucumber";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "example";
        str2 = "example";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "metaphone";
        str2 = "metaphone";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "opportunity";
        str2 = "opportunity";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "rhubarb";
        str2 = "rhubarb";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "sherlock";
        str2 = "sherlock";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "tomorrow";
        str2 = "tomorrow";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "two";
        str2 = "two";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "xylophonist";
        str2 = "xylophonist";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "zebra";
        str2 = "zebra";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "applebanana";
        str2 = "bananaapple";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "rhubarbapple";
        str2 = "apple rhubarb";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "sherlockholmes";
        str2 = "holmes sherlock";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "tomorrowisquiterainy";
        str2 = "isquiterainy tomorrow";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "thequickbrownfoxjumpsoverthelazydog";
        str2 = "thelazydogjumpsoverthequickbrownfox";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = "";
        str2 = "";
        expected = true;
        output = encoder.isMetaphoneEqual(str1, str2);
        assertEquals(expected, output);

        str1 = null;
        try {
            str2 = "test";
            output = encoder.isMetaphoneEqual(str1, str2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        str1 = "test";
        str2 = null;
        try {
            output = encoder.isMetaphoneEqual(str1, str2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        str1 = new Object();
        str2 = "test";
        try {
            output = encoder.isMetaphoneEqual(str1, str2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        str1 = "test";
        str2 = new Object();
        try {
            output = encoder.isMetaphoneEqual(str1, str2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        str1 = new StringBuffer();
        str2 = "test";
        try {
            output = encoder.isMetaphoneEqual(str1.toString(), str2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        str1 = "test";
        str2 = new StringBuffer();
        try {
            output = encoder.isMetaphoneEqual(str1, str2.toString());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone encoder = new Metaphone();
        int expected = 4;
        int output = encoder.getMaxCodeLen();
        assertEquals(expected, output);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone encoder = new Metaphone();
        encoder.setMaxCodeLen(5);
        assertEquals(5, encoder.getMaxCodeLen());
    }
}