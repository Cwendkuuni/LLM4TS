package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * CaverphoneTest class to test all methods in the Caverphone class
 */
public class CaverphoneTest {

    /**
     * Test instance of Caverphone class
     */
    private Caverphone caverphone = new Caverphone();

    /**
     * Test empty string
     */
    @Test
    public void testEmptyString() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    /**
     * Test various strings
     */
    @Test
    public void testVariousStrings() {
        assertEquals("2nouf2f2f2nouf2f", caverphone("CavershamProject"));
        assertEquals("A2nouf2f2n23f2f2nouf2f", caverphone("TheCaverphoneAlgorithm"));
        assertEquals("A2mw3M3mw3M3mw3M3mw3M3", caverphone("Mississippi"));
        assertEquals("A2m2mM2m2mM2m2mM2m2m", caverphone("Massachusetts"));
        assertEquals("A2mw3M3mw3M3mw3M3mw3M3", caverphone("MississippiRiver"));
    }

    /**
     * Test start options
     */
    @Test
    public void testStartOptions() {
        assertEquals("cou2f", caverphone("Cough"));
        assertEquals("rou2f", caverphone("Rough"));
        assertEquals("tou2f", caverphone("Tough"));
        assertEquals("enou2f", caverphone("Enough"));
        assertEquals("trou2f", caverphone("Trough"));
        assertEquals("2n", caverphone("Gn"));
        assertEquals("m2", caverphone("Mb"));

        // Test 2.0 only start options
        assertEquals("Y3", caverphone("Why")); // Old behaviour
        assertEquals("A", caverphone("Y")); // New behaviour
        assertEquals("A", caverphone("y")); // New behaviour
        assertEquals("3kh3", caverphone("3gh3"));
        assertEquals("22", caverphone("gh"));
        assertEquals("K", caverphone("g"));
        assertEquals("S", caverphone("s+"));
        assertEquals("T", caverphone("t+"));
        assertEquals("P", caverphone("p+"));
        assertEquals("K", caverphone("k+"));
        assertEquals("F", caverphone("f+"));
        assertEquals("M", caverphone("m+"));
        assertEquals("N", caverphone("n+"));
        assertEquals("W3", caverphone("w3"));
        //assertEquals("Wy", caverphone("wy")); // 1.0 only
        assertEquals("Wh3", caverphone("wh3"));
        assertEquals("3", caverphone("w$")); // New behaviour
        //assertEquals("Why", caverphone("why")); // 1.0 only
        assertEquals("2", caverphone("w"));
        assertEquals("A", caverphone("h"));
        assertEquals("A", caverphone("h"));
        assertEquals("R3", caverphone("r3"));
        assertEquals("3", caverphone("r$")); // New behaviour
        //assertEquals("Ry", caverphone("ry")); // 1.0 only
        assertEquals("2", caverphone("r"));
        assertEquals("L3", caverphone("l3"));
        //assertEquals("Ly", caverphone("ly")); // 1.0 only
        assertEquals("L3", caverphone("l$")); // New behaviour
    }

    /**
     * Test replacements
     */
    @Test
    public void testReplacements() {
        assertEquals("2q", caverphone("cq"));
        assertEquals("si", caverphone("ci"));
        assertEquals("se", caverphone("ce"));
        assertEquals("sy", caverphone("cy"));
        assertEquals("2ch", caverphone("tch"));
        assertEquals("k", caverphone("c"));
        assertEquals("k", caverphone("q"));
        assertEquals("k", caverphone("x"));
        assertEquals("f", caverphone("v"));
        assertEquals("2g", caverphone("dg"));
        assertEquals("sio", caverphone("tio"));
        assertEquals("sia", caverphone("tia"));
        assertEquals("t", caverphone("d"));
        assertEquals("fh", caverphone("ph"));
        assertEquals("p", caverphone("b"));
        assertEquals("s2", caverphone("sh"));
        assertEquals("s", caverphone("z"));
        assertEquals("A", caverphone("^[aeiou]"));
        assertEquals("3", caverphone("[aeiou]"));
        assertEquals("y", caverphone("j")); // 2.0 only
        assertEquals("Y3", caverphone("^y3")); // 2.0 only
        assertEquals("A", caverphone("^y")); // 2.0 only
        assertEquals("3", caverphone("y")); // 2.0 only
        assertEquals("3kh3", caverphone("3gh3"));
        assertEquals("22", caverphone("gh"));
        assertEquals("k", caverphone("g"));
        assertEquals("S", caverphone("s+"));
        assertEquals("T", caverphone("t+"));
        assertEquals("P", caverphone("p+"));
        assertEquals("K", caverphone("k+"));
        assertEquals("F", caverphone("f+"));
        assertEquals("M", caverphone("m+"));
        assertEquals("N", caverphone("n+"));
        assertEquals("W3", caverphone("w3"));
        //assertEquals("Wy", caverphone("wy")); // 1.0 only
        assertEquals("Wh3", caverphone("wh3"));
        assertEquals("3", caverphone("w"));
        assertEquals("A", caverphone("h"));
        assertEquals("A", caverphone("h"));
        assertEquals("R3", caverphone("r3"));
        assertEquals("3", caverphone("r$")); // New behaviour
        //assertEquals("Ry", caverphone("ry")); // 1.0 only
        assertEquals("2", caverphone("r"));
        assertEquals("L3", caverphone("l3"));
        //assertEquals("Ly", caverphone("ly")); // 1.0 only
        assertEquals("2", caverphone("l"));
        //assertEquals("j", caverphone("y")); // 1.0 only
        //assertEquals("Y3", caverphone("Y3")); // 1.0 only
        //assertEquals("y", caverphone("2")); // 1.0 only

    }

    /**
     * Test removals
     */
    @Test
    public void testRemovals() {
        assertEquals("", caverphone("2"));
        assertEquals("A", caverphone("3$")); // 2.0 only
        assertEquals("", caverphone("3"));

        // Test 2.0 only removals
        assertEquals("", caverphone("A")); // 2.0 only
        //assertEquals("", caverphone("Why")); // 1.0 only

    }

    /**
     * Test encoding an Object
     */
    @Test(expected = EncoderException.class)
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    /**
     * Test encoding a String
     */
    @Test
    public void testEncodeString() {
        assertEquals("2nouf2f2nouf2f", caverphone.encode("CavershamProject"));
    }

    /**
     * Test isCaverphoneEqual method
     */
    @Test
    public void testIsCaverphoneEqual() {
        assertEquals(true, caverphone.isCaverphoneEqual("CavershamProject", "CavershamProject"));
        assertEquals(false, caverphone.isCaverphoneEqual("CavershamProject", "TheCaverphoneAlgorithm"));
    }

}