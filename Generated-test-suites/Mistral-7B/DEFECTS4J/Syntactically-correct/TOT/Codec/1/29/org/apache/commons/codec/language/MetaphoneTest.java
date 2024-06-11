package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Metaphone encoder.
 */
public class MetaphoneTest {

    /**
     * Test for method metaphone with basic English words.
     */
    @Test
    public void testBasicEnglishWords() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"apple", "banana", "cat", "dog", "elephant", "giraffe", "hippopotamus", "iguana", "jaguar", "kangaroo", "lion", "mango", "monkey", "orange", "penguin", "quokka", "rhinoceros", "tiger", "unicorn", "zebra"};
        String[] codes = {"AA0", "A20", "K3T", "D2G", "E1P1N2T3H1S1", "G2R1F1F1E1", "H1P1P1O1T1A1M2S1", "I2G1R1F1A1N1", "J1G1U1R1", "K1N1G1R1", "L1O1N1", "M1N1G1", "O1N1G1", "P1N1N1N1N1N1N1O1", "Q1W1O1K1", "R1H1N1C1O1S1", "T1G3R1"};
        for (int i = 0; i < words.length; i++) {
            assertEquals(codes[i], metaphone.metaphone(words[i]));
        }
    }

    /**
     * Test for method metaphone with special cases.
     */
    @Test
    public void testSpecialCases() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"abalone", "beauty", "caramel", "cinematography", "doughnut", "eighth", "faces", "gentleman", "horseshoe", "indigo", "kilogram", "metaphor", "noodles", "octopus", "pancake", "queen", "quicksand", "tycoon", "velvet", "wagon", "xylophone", "yak", "zero"};
        String[] codes = {"A0B1A1L1O1N1E1", "B2U1T1Y1", "K1R1A1M1E1L1", "S1I1N1A1T1O1G1R1A1F1Y1", "D1O1G1H1N1T1", "E1T1H1", "F1A1C1E1S1", "G1E1N1T1L1E1M1N1", "H1O1R1S1O1S1E1", "I1N1D1G1", "K1I1L1G1R1A1M1", "M1E1T1A1P1H1O1R1", "O1C1T1O1P1S1", "P1A1N1N1L1E1", "Q1E1N1", "Q1I1K1S1S1", "T1Y1K1O1N1", "V1E1L1V1E1T1", "W1A1G1O1N1", "X1Y1L1O1P1H1O1N1", "Y1A1K1", "Z1E1R1O1"};
        for (int i = 0; i < words.length; i++) {
            assertEquals(codes[i], metaphone.metaphone(words[i]));
        }
    }

    /**
     * Test for method metaphone with empty and null strings.
     */
    @Test
    public void testEmptyAndNullStrings() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"", null};
        String[] codes = {"", null};
        for (int i = 0; i < words.length; i++) {
            assertEquals(codes[i], metaphone.metaphone(words[i]));
        }
    }

    /**
     * Test for method metaphone with invalid inputs.
     */
    @Test(expected = EncoderException.class)
    public void testInvalidInputs() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"12345", "abcdEfgHIjK@#$%", "!@#$%^&*()_+-=[]{}|;:<>?"};
        for (String word : words) {
            metaphone.metaphone(word);
        }
    }

    /**
     * Test for method setMaxCodeLen.
     */
    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

    /**
     * Test for method getMaxCodeLen.
     */
    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }
}