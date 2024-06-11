package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;

/**
 * Tests for the Caverphone class.
 */
public class CaverphoneTest {

    private final Caverphone caverphone = new Caverphone();

    @Test
    public void testNullInput() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    @Test
    public void testEmptyInput() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testSimpleWords() {
        assertEquals("2nouf", caverphone.caverphone("enough"));
        assertEquals("2qouf", caverphone.caverphone("cat"));
        assertEquals("kou2f", caverphone.caverphone("tough"));
        assertEquals("kou2f", caverphone.caverphone("rough"));
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("siouf", caverphone.caverphone("city"));
        assertEquals("sio2f", caverphone.caverphone("tia"));
        assertEquals("sio2f", caverphone.caverphone("tio"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("tou2f", caverphone.caverphone("trough")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough")); // 2.0 only
    }

    @Test
    public void testComplexWords() {
        assertEquals("2noufA22m3T2Pw2Fw2", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
        assertEquals("A22m3T2Pw2Fw2", caverphone.caverphone("the")); // truncated to 6 in 1.0
        assertEquals("2noufA22m3T2Pw2Fw2", caverphone.caverphone("The quick brown fox jumps over the lazy dog."));
    }

    @Test
    public void testHandleReplacements() {
        assertEquals("siouf", caverphone.caverphone("ci"));
        assertEquals("seouf", caverphone.caverphone("ce"));
        assertEquals("syouf", caverphone.caverphone("cy"));
        assertEquals("siouf", caverphone.caverphone("cyi"));
        assertEquals("2chouf", caverphone.caverphone("tch"));
        assertEquals("kou2f", caverphone.caverphone("c"));
        assertEquals("kou2f", caverphone.caverphone("q"));
        assertEquals("kou2f", caverphone.caverphone("x"));
        assertEquals("fouf", caverphone.caverphone("v"));
        assertEquals("2gouf", caverphone.caverphone("dg"));
        assertEquals("sio2f", caverphone.caverphone("tio"));
        assertEquals("sia2f", caverphone.caverphone("tia"));
        assertEquals("tou2f", caverphone.caverphone("d"));
        assertEquals("fhouf", caverphone.caverphone("ph"));
        assertEquals("pouf", caverphone.caverphone("b"));
        assertEquals("s2ouf", caverphone.caverphone("sh"));
        assertEquals("souf", caverphone.caverphone("z"));
        assertEquals("3A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3ouf", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("L3ouf", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l"));
        //assertEquals("Wy2ouf", caverphone.caverphone("wy")); // 1.0 only
        assertEquals("Wh3ouf", caverphone.caverphone("wh3"));
        //assertEquals("Why2ouf", caverphone.caverphone("why")); // 1.0 only
        assertEquals("W2ouf", caverphone.caverphone("w"));
        assertEquals("3", caverphone.caverphone("^h"));
        assertEquals("3", caverphone.caverphone("h"));
    }

    @Test
    public void testHandleRemovals() {
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$")); // 2.0 only
        assertEquals("", caverphone.caverphone("3"));
        assertEquals("11111111111111", caverphone.caverphone("11111111111111")); // 1.0 has 12 1s
    }

    @Test
    public void testHandleStartOptions() {
        assertEquals("cou2f", caverphone.caverphone("cougph"));
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough")); // 2.0 only
        assertEquals("gn2n", caverphone.caverphone("gn"));
    }

    @Test
    public void testHandleFinalE() {
        assertEquals("siouf", caverphone.caverphone("ci"));
        assertEquals("seouf", caverphone.caverphone("ce"));
        assertEquals("syouf", caverphone.caverphone("cy"));
        assertEquals("siouf", caverphone.caverphone("cyi"));
        assertEquals("2chouf", caverphone.caverphone("tch"));
        assertEquals("kou2f", caverphone.caverphone("c"));
        assertEquals("kou2f", caverphone.caverphone("q"));
        assertEquals("kou2f", caverphone.caverphone("x"));
        assertEquals("fouf", caverphone.caverphone("v"));
        assertEquals("2gouf", caverphone.caverphone("dg"));
        assertEquals("sio2f", caverphone.caverphone("tio"));
        assertEquals("sia2f", caverphone.caverphone("tia"));
        assertEquals("tou2f", caverphone.caverphone("d"));
        assertEquals("fhouf", caverphone.caverphone("ph"));
        assertEquals("pouf", caverphone.caverphone("b"));
        assertEquals("s2ouf", caverphone.caverphone("sh"));
        assertEquals("souf", caverphone.caverphone("z"));
        assertEquals("3A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3ouf", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("L3ouf", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l"));
        //assertEquals("Wy2ouf", caverphone.caverphone("wy")); // 1.0 only
        assertEquals("Wh3ouf", caverphone.caverphone("wh3"));
        //assertEquals("Why2ouf", caverphone.caverphone("why")); // 1.0 only
        assertEquals("W2ouf", caverphone.caverphone("w"));
        assertEquals("3", caverphone.caverphone("^h"));
        assertEquals("3", caverphone.caverphone("h"));
        assertEquals("enou2f", caverphone.caverphone("enough")); // 2.0 only
        assertEquals("A", caverphone.caverphone("enoughE")); // 2.0 only
        assertEquals("enou2f", caverphone.caverphone("enough ")); // 2.0 only
        assertEquals("enou2f", caverphone.caverphone("enoughE ")); // 2.0 only
        assertEquals("enou2f", caverphone.caverphone(" enough")); // 2.0 only
        assertEquals("enou2f", caverphone.caverphone(" enough ")); // 2.0 only
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        caverphone.encode(new Object() {});
    }
}