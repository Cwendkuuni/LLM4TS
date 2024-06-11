package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for Metaphone class
 */
public class MetaphoneTest {

    /**
     * Test method for {@link Metaphone#metaphone(String)}
     */
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test single character
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test simple words
        assertEquals("BAH", metaphone.metaphone("Bath"));
        assertEquals("BHH", metaphone.metaphone("Bough"));
        assertEquals("BAT", metaphone.metaphone("Bat"));
        assertEquals("BAA", metaphone.metaphone("Bake"));
        assertEquals("BMH", metaphone.metaphone("Bomb"));

        // Test exceptions
        try {
            metaphone.metaphone(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            metaphone.metaphone("");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            metaphone.metaphone("1234");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        // Test special cases
        assertEquals("KN", metaphone.metaphone("Knights"));
        assertEquals("AE", metaphone.metaphone("Apple"));
        assertEquals("WR", metaphone.metaphone("Water"));
        assertEquals("XS", metaphone.metaphone("Thx"));
        assertEquals("JI", metaphone.metaphone("Jingle"));
        assertEquals("DJ", metaphone.metaphone("Judge"));
        assertEquals("GC", metaphone.metaphone("Gauge"));
        assertEquals("QK", metaphone.metaphone("Quagmire"));
        assertEquals("SX", metaphone.metaphone("Sex"));

        // Test complex words
        assertEquals("BT", metaphone.metaphone("Athabasca"));
        assertEquals("BS", metaphone.metaphone("Aberdeen"));
        assertEquals("BX", metaphone.metaphone("Basquiat"));
        assertEquals("BX", metaphone.metaphone("Basque"));
        assertEquals("BX", metaphone.metaphone("Basilisk"));
        assertEquals("BX", metaphone.metaphone("Basilica"));
        assertEquals("BX", metaphone.metaphone("Basilicum"));
        assertEquals("BX", metaphone.metaphone("Basilicidae"));
        assertEquals("BX", metaphone.metaphone("Basilicata"));
    }

}