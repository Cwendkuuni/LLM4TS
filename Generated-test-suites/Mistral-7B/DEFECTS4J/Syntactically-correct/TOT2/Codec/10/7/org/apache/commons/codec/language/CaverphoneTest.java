import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testNullInput() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testEmptyInput() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testBasicLowerCaseLetters() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("hello");
        assertEquals("h2e2l2o", result);
    }

    @Test
    public void testBasicUpperCaseLetters() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("HELLO");
        assertEquals("h2e2l2o", result);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInputWithSpecialCharacters() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("Hello, World!");
    }

    @Test
    public void testStartOptions() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("cough");
        assertEquals("cou2f", result);
        String result2 = encoder.caverphone("rough");
        assertEquals("rou2f", result2);
        String result3 = encoder.caverphone("tough");
        assertEquals("tou2f", result3);
        String result4 = encoder.caverphone("enough");
        assertEquals("enou2f", result4);
        String result5 = encoder.caverphone("trough");
        assertEquals("trou2f", result5); // Note the spec says ^enough here again, c+p error I assume
    }

    @Test
    public void testHandlingReplacements() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("cq");
        assertEquals("2q", result);
        String result2 = encoder.caverphone("ci");
        assertEquals("si", result2);
        String result3 = encoder.caverphone("ce");
        assertEquals("se", result3);
        String result4 = encoder.caverphone("cy");
        assertEquals("sy", result4);
        String result5 = encoder.caverphone("tch");
        assertEquals("2ch", result5);
        String result6 = encoder.caverphone("c");
        assertEquals("k", result6);
        String result7 = encoder.caverphone("q");
        assertEquals("k", result7);
        String result8 = encoder.caverphone("x");
        assertEquals("k", result8);
        String result9 = encoder.caverphone("v");
        assertEquals("f", result9);
        String result10 = encoder.caverphone("dg");
        assertEquals("2g", result10);
        String result11 = encoder.caverphone("tio");
        assertEquals("sio", result11);
        String result12 = encoder.caverphone("tia");
        assertEquals("sia", result12);
        String result13 = encoder.caverphone("d");
        assertEquals("t", result13);
        String result14 = encoder.caverphone("ph");
        assertEquals("fh", result14);
        String result15 = encoder.caverphone("b");
        assertEquals("p", result15);
        String result16 = encoder.caverphone("sh");
        assertEquals("s2", result16);
        String result17 = encoder.caverphone("z");
        assertEquals("s", result17);
        String result18 = encoder.caverphone("^[aeiou]");
        assertEquals("A", result18);
        String result19 = encoder.caverphone("[aeiou]");
        assertEquals("3", result19);
        String result20 = encoder.caverphone("j");
        assertEquals("y", result20); // 2.0 only
        String result21 = encoder.caverphone("^y3");
        assertEquals("Y3", result21); // 2.0 only
        String result22 = encoder.caverphone("^y");
        assertEquals("A", result22); // 2.0 only
        String result23 = encoder.caverphone("y");
        assertEquals("3", result23); // 2.0 only
        String result24 = encoder.caverphone("3gh3");
        assertEquals("3kh3", result24);
        String result25 = encoder.caverphone("gh");
        assertEquals("22", result25);
        String result26 = encoder.caverphone("g");
        assertEquals("k", result26);
        String result27 = encoder.caverphone("s+");
        assertEquals("S", result27);
        String result28 = encoder.caverphone("t+");
        assertEquals("T", result28);
        String result29 = encoder.caverphone("p+");
        assertEquals("P", result29);
        String result30 = encoder.caverphone("k+");
        assertEquals("K", result30);
        String result31 = encoder.caverphone("f+");
        assertEquals("F", result31);
        String result32 = encoder.caverphone("m+");
        assertEquals("M", result32);
        String result33 = encoder.caverphone("n+");
        assertEquals("N", result33);
        String result34 = encoder.caverphone("w3");
        assertEquals("W3", result34);
        //String result35 = encoder.caverphone("wy"); // 1.0 only
        String result36 = encoder.caverphone("wh3");
        assertEquals("Wh3", result36);
        String result37 = encoder.caverphone("w$"); // 2.0 only
        assertEquals("3", result37);
        //String result38 = encoder.caverphone("why"); // 1.0 only
        String result39 = encoder.caverphone("w");
        assertEquals("2", result39);
        String result40 = encoder.caverphone("^h");
        assertEquals("A", result40);
        String result41 = encoder.caverphone("h");
        assertEquals("2", result41);
        String result42 = encoder.caverphone("r3");
        assertEquals("R3", result42);
        String result43 = encoder.caverphone("r$"); // 2.0 only
        assertEquals("3", result43); // 2.0 only
        //String result44 = encoder.caverphone("ry"); // 1.0 only
        String result45 = encoder.caverphone("r");
        assertEquals("2", result45);
        String result46 = encoder.caverphone("l3");
        assertEquals("L3", result46);
        String result47 = encoder.caverphone("l$"); // 2.0 only
        assertEquals("3", result47); // 2.0 only
        //String result48 = encoder.caverphone("ly"); // 1.0 only
        String result49 = encoder.caverphone("l");
        assertEquals("2", result49);
        //String result50 = encoder.caverphone("j"); // 1.0 only
        //String result51 = encoder.caverphone("y3"); // 1.0 only
        //String result52 = encoder.caverphone("y"); // 1.0 only
    }

    @Test
    public void testHandlingRemovals() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("2");
        assertEquals("", result);
        String result2 = encoder.caverphone("3$"); // 2.0 only
        assertEquals("A", result2);
        String result3 = encoder.caverphone("3");
        assertEquals("", result3);
    }

    @Test
    public void testHandlingEndOption() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("123456");
        assertEquals("1234561111", result);
    }

    @Test
    public void testEncodeObject() {
        Caverphone encoder = new Caverphone();
        String input = "input string";
        String result = encoder.encode(input);
        String expected = encoder.caverphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() {
        Caverphone encoder = new Caverphone();
        Integer number = 123;
        encoder.encode(number);
    }
}