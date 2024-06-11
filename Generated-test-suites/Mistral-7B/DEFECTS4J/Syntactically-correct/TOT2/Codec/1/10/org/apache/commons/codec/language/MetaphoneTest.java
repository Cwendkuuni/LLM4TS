import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone_SingleCharacter() {
        Metaphone encoder = new Metaphone();
        String txt = "A";
        String expected = "A";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_TwoCharacters_InitialException() {
        Metaphone encoder = new Metaphone();
        String txt = null;
        try {
            String actual = encoder.metaphone(txt);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            //Expected
        }
    }

    @Test
    public void testMetaphone_TwoCharacters_InitialKG() {
        Metaphone encoder = new Metaphone();
        String txt = "KG";
        String expected = "KG";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_TwoCharacters_InitialAE() {
        Metaphone encoder = new Metaphone();
        String txt = "AE";
        String expected = "A";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_TwoCharacters_InitialWH() {
        Metaphone encoder = new Metaphone();
        String txt = "WH";
        String expected = "W";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_TwoCharacters_InitialWR() {
        Metaphone encoder = new Metaphone();
        String txt = "WR";
        String expected = "R";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_ThreeCharacters_InitialX() {
        Metaphone encoder = new Metaphone();
        String txt = "X";
        String expected = "S";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_EdgeCase_InitialTwoSIlentG() {
        Metaphone encoder = new Metaphone();
        String txt = "SHING";
        String expected = "S";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_EdgeCase_InitialTwoSIlentG_EndingH() {
        Metaphone encoder = new Metaphone();
        String txt = "BATHING";
        String expected = "B";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_EdgeCase_InitialTwoSIlentG_EndingConsonant() {
        Metaphone encoder = new Metaphone();
        String txt = "BARKING";
        String expected = "B";
        String actual = encoder.metaphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphone_ErrorScenario_NullInput() {
        Metaphone encoder = new Metaphone();
        String txt = null;
        try {
            String actual = encoder.metaphone(txt);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            //Expected
        }
    }

    @Test(expected = EncoderException.class)
    public void testMetaphone_ErrorScenario_EmptyInput() {
        Metaphone encoder = new Metaphone();
        String txt = "";
        String actual = encoder.metaphone(txt);
        fail("Expected EncoderException");
    }
}