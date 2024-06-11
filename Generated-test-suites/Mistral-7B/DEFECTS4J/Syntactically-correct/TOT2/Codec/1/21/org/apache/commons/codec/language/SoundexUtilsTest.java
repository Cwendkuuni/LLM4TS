import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws Exception {
        StringEncoder encoder = new MetaphoneEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "12345";
        String es2 = "23456";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanEmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    @Test
    public void testCleanNullString() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    @Test
    public void testCleanOnlyNumbers() {
        String input = "12345";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanOnlySpecialCharacters() {
        String input = "!@#$%^&*()_+={}[]|\\:;\"<>,.?/~`";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullEncoder() throws Exception {
        String s1 = "Apple";
        String s2 = "Orange";
        SoundexUtils.difference(null, s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullS1() throws Exception {
        StringEncoder encoder = new MetaphoneEncoder();
        String s2 = "Orange";
        SoundexUtils.difference(encoder, null, s2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceNullS2() throws Exception {
        StringEncoder encoder = new MetaphoneEncoder();
        String s1 = "Apple";
        SoundexUtils.difference(encoder, s1, null);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodingError() throws Exception {
        StringEncoder encoder = new EncoderWithError(); // Custom encoder with encoding error
        String s1 = "Apple";
        String s2 = "Orange";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncodedStringsEmptyOrNull() {
        String es1 = "";
        String es2 = "";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        es2 = "";
        expected = 0;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "";
        es2 = null;
        expected = 0;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedStringsDifferentLengths() {
        String es1 = "12345";
        String es2 = "23456";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "12345";
        es2 = "56789";
        expected = 0;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedStringsNonMatchingCharacters() {
        String es1 = "123ABC";
        String es2 = "234DEF";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedStringsSameCharacters() {
        String es1 = "123ABC";
        String es2 = "123ABC";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedStringsIdentical() {
        String es1 = "123ABC";
        String es2 = "123ABC";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}