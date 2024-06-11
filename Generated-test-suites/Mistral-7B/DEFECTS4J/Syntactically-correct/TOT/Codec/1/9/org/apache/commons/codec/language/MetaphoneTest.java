package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for Metaphone class
 */
public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test empty string
        assertEquals("", metaphone.metaphone(""));

        // Test single character
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test double character
        assertEquals("BA", metaphone.metaphone("AB"));
        assertEquals("KN", metaphone.metaphone("KN"));
        assertEquals("AE", metaphone.metaphone("AE"));

        // Test initial exceptions
        assertEquals("AW", metaphone.metaphone("AW")); // looks for AW, not WAW
        assertEquals("WR", metaphone.metaphone("WR")); // WR -> R

        // Test special cases
        assertEquals("XS", metaphone.metaphone("X")); // initial X becomes S
        assertEquals("SIA", metaphone.metaphone("CIA")); // "CIA" -> X
        assertEquals("SCH", metaphone.metaphone("SCH")); // Silent if in "TCH"
        assertEquals("0", metaphone.metaphone("TH")); // Silent TH

        // Test vowels
        assertEquals("AEIO", metaphone.metaphone("AEIOU"));
        assertEquals("I", metaphone.metaphone("I")); // single vowel
        assertEquals("AI", metaphone.metaphone("AI")); // AI
        assertEquals("EI", metaphone.metaphone("EI")); // EI

        // Test consonants
        assertEquals("B", metaphone.metaphone("B")); // B
        assertEquals("C", metaphone.metaphone("C")); // C
        assertEquals("CH", metaphone.metaphone("CH")); // Silent if in "TCH"
        assertEquals("D", metaphone.metaphone("D")); // D
        assertEquals("F", metaphone.metaphone("F")); // F
        assertEquals("GJ", metaphone.metaphone("GH")); // GH -> J
        assertEquals("H", metaphone.metaphone("H")); // H
        assertEquals("J", metaphone.metaphone("J")); // J
        assertEquals("K", metaphone.metaphone("K")); // K
        assertEquals("L", metaphone.metaphone("L")); // L
        assertEquals("M", metaphone.metaphone("M")); // M
        assertEquals("N", metaphone.metaphone("N")); // N
        assertEquals("P", metaphone.metaphone("P")); // P
        assertEquals("Q", metaphone.metaphone("Q")); // Q
        assertEquals("R", metaphone.metaphone("R")); // R
        assertEquals("SX", metaphone.metaphone("S")); // S
        assertEquals("T", metaphone.metaphone("T")); // T
        assertEquals("V", metaphone.metaphone("V")); // V
        assertEquals("W", metaphone.metaphone("W")); // W
        assertEquals("XKS", metaphone.metaphone("XKS")); // XKS
        assertEquals("ZS", metaphone.metaphone("Z")); // Z

        // Test special cases with multiple letters
        assertEquals("AJ", metaphone.metaphone("AG")); // A + G -> J
        assertEquals("DJ", metaphone.metaphone("DGE")); // DGE -> J
        assertEquals("DJ", metaphone.metaphone("DGI")); // DGI -> J
        assertEquals("DJ", metaphone.metaphone("DGY")); // DGY -> J
        assertEquals("F", metaphone.metaphone("PH")); // PH -> F

        // Test error scenarios
        assertEquals("", metaphone(null)); // Null string
        assertEquals("", metaphone("")); // Empty string
        assertEquals("", metaphone("")); // Empty string
        assertEquals("", metaphone(" ")); // String with only spaces
        assertEquals("", metaphone("123")); // String with only digits
        assertEquals("", metaphone("ABC123")); // String with letters and digits
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() {
        new Metaphone().encode(new Object()); // Object not String
    }
}