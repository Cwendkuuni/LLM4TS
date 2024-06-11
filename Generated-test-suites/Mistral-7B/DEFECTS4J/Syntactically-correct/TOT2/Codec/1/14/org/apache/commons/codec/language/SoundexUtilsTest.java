import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBc DeF gHi JkL mNo PqR sTu vWx Yz";
        String expected = "ABC DEF GHI JKL MNO PQR STU VWX YZ";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceMethodWithSoundex() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Apricot";
        int expected = 2;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullStrings() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = null;
        String s2 = "Banana";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceMethodWithNullEncoder() throws EncoderException {
        String s1 = "Pineapple";
        String s2 = "Papaya";
        SoundexUtils differenceMethod = new SoundexUtils();
        assertEquals(3, differenceMethod.differenceEncoded(null, s1, s2));
    }

    @Test
    public void testDifferenceMethodWithEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceMethodWithLongStrings() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "The quick brown fox jumps over the lazy dog";
        String s2 = "The quick red fox jumps over the lazy cat";
        int expected = 3;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceMethodWithLongEncodedStrings() {
        String es1 = "310100011111010110111111110101101111111111111111111111111111111111111111";
        String es2 = "310100011111010110111111110101101111111111111111111111111111111111111111111111111";
        int expected = 22;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}