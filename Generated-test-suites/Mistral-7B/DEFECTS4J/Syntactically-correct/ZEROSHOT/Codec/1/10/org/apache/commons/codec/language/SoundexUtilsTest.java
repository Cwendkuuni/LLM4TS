import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "A short test string with numbers 123 and special character @";
        String expected = "A SHORT TEST STRING.";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int difference = SoundexUtils.difference(soundex, str1, str2);
        assertEquals(2, difference);

        str1 = "Apple";
        str2 = "Apple";
        difference = SoundexUtils.difference(soundex, str1, str2);
        assertEquals(4, difference);

        str1 = "Apple";
        str2 = "Appel";
        difference = SoundexUtils.difference(soundex, str1, str2);
        assertEquals(3, difference);

        str1 = "Apple";
        str2 = "Apricot";
        difference = SoundexUtils.difference(soundex, str1, str2);
        assertEquals(3, difference);

        str1 = "Apple";
        str2 = "Apricot";
        String soundex1 = soundex.encode(str1);
        String soundex2 = soundex.encode(str2);
        difference = SoundexUtils.differenceEncoded(soundex1, soundex2);
        assertEquals(3, difference);

        str1 = "Apple";
        str2 = "Apricot";
        soundex1 = soundex.encode(str1);
        soundex2 = soundex.encode(str2);
        String refinedSoundex1 = new RefinedSoundex().encode(str1);
        String refinedSoundex2 = new RefinedSoundex().encode(str2);
        difference = SoundexUtils.differenceEncoded(refinedSoundex1, refinedSoundex2);
        assertEquals(4, difference);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "AS12";
        String es2 = "AS10";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(2, difference);

        es1 = "AS12";
        es2 = "AS12";
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, difference);

        es1 = "AS12";
        es2 = "AS11";
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(3, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = null;
        String str2 = "Apple";
        SoundexUtils.difference(soundex, str1, str2);
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceWithNullES1() {
        String es1 = null;
        String es2 = "AS12";
        try {
            SoundexUtils.differenceEncoded(es1, es2);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDifferenceWithNullES2() {
        String es1 = "AS12";
        String es2 = null;
        try {
            SoundexUtils.differenceEncoded(es1, es2);
        } catch (NullPointerException e) {
            // Expected
        }
    }

}