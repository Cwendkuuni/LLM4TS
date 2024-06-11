package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneLowercaseString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("122222111122", caverphone.caverphone("hello"));
    }

    @Test
    public void testCaverphoneStringWithDigits() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111222221111", caverphone.caverphone("test123"));
    }

    @Test
    public void testCaverphoneStringWithSpecialChars() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111333331111", caverphone.caverphone("test@123"));
    }

    @Test
    public void testCaverphoneUppercaseString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1A1A1A1A1A1A1A1A", caverphone.caverphone("HELLO"));
    }

    @Test
    public void testCaverphoneStringWithLongLength() {
        String longString = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        Caverphone caverphone = new Caverphone();
        assertEquals("12222222111111112222222211111111", caverphone.caverphone(longString));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonStringObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object());
    }

    @Test
    public void testCaverphoneStartsWithSpecialCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cou"));
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rou"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tou"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("enough "));
        assertEquals("trou2f", caverphone.caverphone("trou"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("Y3", caverphone.caverphone("^y3"));
        assertEquals("A", caverphone.caverphone("^y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        assertEquals("W3", caverphone.caverphone("w3"));
        //assertEquals("Wy", caverphone.caverphone("wy"));    // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));       // 2.0 only
        //assertEquals("Ry", caverphone.caverphone("ry"));    // 1.0 only
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));       // 2.0 only
        //assertEquals("Ly", caverphone.caverphone("ly"));    // 1.0 only
        assertEquals("2", caverphone.caverphone("l"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));       // 2.0 only
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneHandlesCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "testtest";
        String str2 = "testtest";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testCaverphoneHandlesDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "testtest";
        String str2 = "testtest1";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

}