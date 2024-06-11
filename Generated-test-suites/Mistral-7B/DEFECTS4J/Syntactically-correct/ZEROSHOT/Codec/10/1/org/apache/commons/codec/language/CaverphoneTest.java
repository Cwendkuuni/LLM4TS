package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Unit tests for the Caverphone class
 */
public class CaverphoneTest {

    /**
     * Test instance creation
     */
    @Test
    public void testInstanceCreation() {
        Caverphone caverphone = new Caverphone();
        assertNotNull(caverphone);
    }

    /**
     * Test encoding a null String
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }

    /**
     * Test encoding an empty String
     */
    @Test
    public void testEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
    }

    /**
     * Test encoding a single character String
     */
    @Test
    public void testEncodeSingleCharacterString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.encode("a"));
    }

    /**
     * Test encoding a single character String with initial 'e'
     */
    @Test
    public void testEncodeSingleCharacterStringWithInitialE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.encode("e")); // 2.0 only
    }

    /**
     * Test encoding a multi-character String
     */
    @Test
    public void testEncodeMultiCharacterString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("F1T2S2P2K2K2M2N2F2F2", caverphone.encode("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding a String with various start options
     */
    @Test
    public void testEncodeStringWithStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.encode("cough"));
        assertEquals("rou2f", caverphone.encode("rough"));
        assertEquals("tou2f", caverphone.encode("tough"));
        assertEquals("enou2f", caverphone.encode("enough"));  // 2.0 only
        assertEquals("trou2f", caverphone.encode("trough"));  // 2.0 only
        assertEquals("2n", caverphone.encode("gn"));

        // Handle replacements
        assertEquals("2q", caverphone.encode("cq"));
        assertEquals("si", caverphone.encode("ci"));
        assertEquals("se", caverphone.encode("ce"));
        assertEquals("sy", caverphone.encode("cy"));
        assertEquals("2ch", caverphone.encode("tch"));
        assertEquals("k", caverphone.encode("c"));
        assertEquals("k", caverphone.encode("q"));
        assertEquals("k", caverphone.encode("x"));
        assertEquals("f", caverphone.encode("v"));
        assertEquals("2g", caverphone.encode("dg"));
        assertEquals("sio", caverphone.encode("tio"));
        assertEquals("sia", caverphone.encode("tia"));
        assertEquals("t", caverphone.encode("d"));
        assertEquals("fh", caverphone.encode("ph"));
        assertEquals("p", caverphone.encode("b"));
        assertEquals("s2", caverphone.encode("sh"));
        assertEquals("s", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("^[aeiou]"));
        assertEquals("3", caverphone.encode("[aeiou]"));
        assertEquals("y", caverphone.encode("j"));       // 2.0 only
        assertEquals("Y3", caverphone.encode("^y3"));   // 2.0 only
        assertEquals("A", caverphone.encode("^y"));       // 2.0 only
        assertEquals("3", caverphone.encode("y"));        // 2.0 only
        assertEquals("3kh3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("k", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s+"));
        assertEquals("T", caverphone.encode("t+"));
        assertEquals("P", caverphone.encode("p+"));
        assertEquals("K", caverphone.encode("k+"));
        assertEquals("F", caverphone.encode("f+"));
        assertEquals("M", caverphone.encode("m+"));
        assertEquals("N", caverphone.encode("n+"));
        assertEquals("W3", caverphone.encode("w3"));
        //assertEquals("Wy", caverphone.encode("wy"));    // 1.0 only
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("2", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$"));       // 2.0 only
        //assertEquals("Ry", caverphone.encode("ry"));    // 1.0 only
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$"));       // 2.0 only
        //assertEquals("Ly", caverphone.encode("ly"));    // 1.0 only
        assertEquals("2", caverphone.encode("l"));
        //assertEquals("y", caverphone.encode("j"));      // 1.0 only
        //assertEquals("Y3", caverphone.encode("y3"));    // 1.0 only
        //assertEquals("2", caverphone.encode("y"));      // 1.0 only

        // Handle removals
        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$"));       // 2.0 only
        assertEquals("", caverphone.encode("3"));

        // Handle encoding a String with ten 1s on the end
        assertEquals("111111111111111111", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
    }

    /**
     * Test encoding a String with equal caverphones
     */
    @Test
    public void testEncodeEqualStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "the quick brown fox jumps over the lazy dog";
        assertEquals(caverphone.encode(str1), caverphone.encode(str2));
    }

    /**
     * Test encoding a String with non-equal caverphones
     */
    @Test
    public void testEncodeNonEqualStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the happy dog";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
}