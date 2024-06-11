import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Caverphone.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() throws DecoderException {
        String encoded = new Caverphone().encode("");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("", decoded);
    }

    @Test
    public void testSingleCharacter() throws DecoderException {
        String encoded = new Caverphone().encode("a");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("a", decoded);
    }

    @Test
    public void testSingleCharacterUpperCase() throws DecoderException {
        String encoded = new Caverphone().encode("A");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("A", decoded);
    }

    @Test
    public void testMultipleLetters() throws DecoderException {
        String encoded = new Caverphone().encode("apple");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("apple", decoded);
    }

    @Test
    public void testMultipleLettersDifferentCases() throws DecoderException {
        String encoded = new Caverphone().encode("Apple");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("apple", decoded);
    }

    @Test(expected = EncoderException.class)
    public void testNull() {
        new Caverphone().encode(null);
    }

    @Test
    public void testCaverphoneEncoding() throws DecoderException {
        String str1 = "Caversham";
        String encoded1 = new Caverphone().encode(str1);
        String decoded1 = new StringDecoder().decode(encoded1.getBytes(), getClass());
        assertEquals(str1, decoded1);

        String str2 = "project";
        String encoded2 = new Caverphone().encode(str2);
        String decoded2 = new StringDecoder().decode(encoded2.getBytes(), getClass());
        assertEquals(str2, decoded2);

        String encoded3 = new Caverphone().encode("University of Otago");
        String decoded3 = new StringDecoder().decode(encoded3.getBytes(), getClass());
        assertEquals("University of Otago", decoded3);

        String encoded4 = new Caverphone().encode("the Apache Software Foundation");
        String decoded4 = new StringDecoder().decode(encoded4.getBytes(), getClass());
        assertEquals("the Apache Software Foundation", decoded4);

        String encoded5 = new Caverphone().encode("Wikipedia - Caverphone");
        String decoded5 = new StringDecoder().decode(encoded5.getBytes(), getClass());
        assertEquals("Wikipedia - Caverphone", decoded5);

        String encoded6 = new Caverphone().encode("http://en.wikipedia.org/wiki/Caverphone");
        String decoded6 = new StringDecoder().decode(encoded6.getBytes(), getClass());
        assertEquals("http://en.wikipedia.org/wiki/Caverphone", decoded6);

        String encoded7 = new Caverphone().encode("Caverphone 2.0 specification");
        String decoded7 = new StringDecoder().decode(encoded7.getBytes(), getClass());
        assertEquals("Caverphone 2.0 specification", decoded7);
    }

    @Test
    public void testCaverphoneEqual() throws DecoderException {
        String str1 = "Caversham";
        String str2 = "project";
        String str3 = "University of Otago";
        String str4 = "the Apache Software Foundation";
        String str5 = "Wikipedia - Caverphone";
        String str6 = "http://en.wikipedia.org/wiki/Caverphone";
        String str7 = "Caverphone 2.0 specification";

        assertTrue(isCaverphoneEqual(str1, str1));
        assertTrue(isCaverphoneEqual(str1, encoded(str1)));
        assertTrue(isCaverphoneEqual(str1, decoded(encoded(str1))));

        assertFalse(isCaverphoneEqual(str1, str2));
        assertFalse(isCaverphoneEqual(str1, str3));
        assertFalse(isCaverphoneEqual(str1, str4));
        assertFalse(isCaverphoneEqual(str1, str5));
        assertFalse(isCaverphoneEqual(str1, str6));
        assertFalse(isCaverphoneEqual(str1, str7));
        assertFalse(isCaverphoneEqual(str2, str3));
        assertFalse(isCaverphoneEqual(str2, str4));
        assertFalse(isCaverphoneEqual(str2, str5));
        assertFalse(isCaverphoneEqual(str2, str6));
        assertFalse(isCaverphoneEqual(str2, str7));
        assertFalse(isCaverphoneEqual(str3, str4));
        assertFalse(isCaverphoneEqual(str3, str5));
        assertFalse(isCaverphoneEqual(str3, str6));
        assertFalse(isCaverphoneEqual(str3, str7));
        assertFalse(isCaverphoneEqual(str4, str5));
        assertFalse(isCaverphoneEqual(str4, str6));
        assertFalse(isCaverphoneEqual(str4, str7));
        assertFalse(isCaverphoneEqual(str5, str6));
        assertFalse(isCaverphoneEqual(str5, str7));

        assertTrue(isCaverphoneEqual(encoded1, decoded1));
        assertTrue(isCaverphoneEqual(encoded1, encoded1));
        assertTrue(isCaverphoneEqual(decoded1, decoded1));
        assertTrue(isCaverphoneEqual(encoded2, decoded2));
        assertTrue(isCaverphoneEqual(encoded2, encoded2));
        assertTrue(isCaverphoneEqual(decoded2, decoded2));
        assertTrue(isCaverphoneEqual(encoded3, decoded3));
        assertTrue(isCaverphoneEqual(encoded3, encoded3));
        assertTrue(isCaverphoneEqual(decoded3, decoded3));
        assertTrue(isCaverphoneEqual(encoded4, decoded4));
        assertTrue(isCaverphoneEqual(encoded4, encoded4));
        assertTrue(isCaverphoneEqual(decoded4, decoded4));
        assertTrue(isCaverphoneEqual(encoded5, decoded5));
        assertTrue(isCaverphoneEqual(encoded5, encoded5));
        assertTrue(isCaverphoneEqual(decoded5, decoded5));
        assertTrue(isCaverphoneEqual(encoded6, decoded6));
        assertTrue(isCaverphoneEqual(encoded6, encoded6));
        assertTrue(isCaverphoneEqual(decoded6, decoded6));
        assertTrue(isCaverphoneEqual(encoded7, decoded7));
        assertTrue(isCaverphoneEqual(encoded7, encoded7));
        assertTrue(isCaverphoneEqual(decoded7, decoded7));
    }

    @Test
    public void testCaverphoneEncodingEdgeCases() throws DecoderException {
        String encoded = new Caverphone().encode("Cough");
        String decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("cou2f", decoded);

        encoded = new Caverphone().encode("Rough");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("rou2f", decoded);

        encoded = new Caverphone().encode("Tough");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("tou2f", decoded);

        encoded = new Caverphone().encode("Enough");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("enou2f", decoded);

        encoded = new Caverphone().encode("Trough");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("trou2f", decoded);

        encoded = new Caverphone().encode("Gn");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("2n", decoded);

        encoded = new Caverphone().encode("Mb");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("m2", decoded);

        encoded = new Caverphone().encode("Cq");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("2q", decoded);

        encoded = new Caverphone().encode("Ci");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("si", decoded);

        encoded = new Caverphone().encode("Ce");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("se", decoded);

        encoded = new Caverphone().encode("Cy");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("sy", decoded);

        encoded = new Caverphone().encode("Tch");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("2ch", decoded);

        encoded = new Caverphone().encode("C");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("k", decoded);

        encoded = new Caverphone().encode("Q");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("k", decoded);

        encoded = new Caverphone().encode("X");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("k", decoded);

        encoded = new Caverphone().encode("V");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("f", decoded);

        encoded = new Caverphone().encode("Dg");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("2g", decoded);

        encoded = new Caverphone().encode("Tio");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("sio", decoded);

        encoded = new Caverphone().encode("Tia");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("sia", decoded);

        encoded = new Caverphone().encode("D");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("t", decoded);

        encoded = new Caverphone().encode("Ph");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("fh", decoded);

        encoded = new Caverphone().encode("B");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("p", decoded);

        encoded = new Caverphone().encode("Sh");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("s2", decoded);

        encoded = new Caverphone().encode("Z");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("s", decoded);

        encoded = new Caverphone().encode("Why");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        //assertEquals("Why", decoded); // 1.0 only

        encoded = new Caverphone().encode("Wh3");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("Wh3", decoded);

        encoded = new Caverphone().encode("W");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("2", decoded);

        encoded = new Caverphone().encode("H");
        decoded = new StringDecoder().decode(encoded.getBytes(), getClass());
        assertEquals("A", decoded);
    }

}