import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {
    @Test
    public void testMetaphoneSimple() {
        Metaphone metaphone = new Metaphone();
        String input = "cat";
        String expected = "KAT";
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialK() {
        Metaphone metaphone = new Metaphone();
        String input = "kangaroo";
        String expected = "KANGAROO"; // initial K
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialC() {
        Metaphone metaphone = new Metaphone();
        String input = "cellar";
        String expected = "SEL"; // initial C
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input); // must throw EncoderException
    }

    @Test
    public void testMetaphoneEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String expected = "";
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneWithDigits() {
        Metaphone metaphone = new Metaphone();
        String input = "apple123";
        String expected = ""; // Metaphone does not support digits
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneWithPunctuation() {
        Metaphone metaphone = new Metaphone();
        String input = "apple.";
        String expected = "PPLE";
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneLongString() {
        Metaphone metaphone = new Metaphone();
        String input = "thisisateststringforlongstring";
        String expected = "T2S1TS3NG";
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        String input = "metaphonealgorithm";
        String expected = "M3T1P1H1N1"; // max code length of 4
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }
}