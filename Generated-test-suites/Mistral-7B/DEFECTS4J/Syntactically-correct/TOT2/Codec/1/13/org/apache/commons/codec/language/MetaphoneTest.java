import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneWithSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String input = "A";
        String expectedOutput = "A";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithTwoCharacters() {
        Metaphone metaphone = new Metaphone();
        String input = "KN";
        String expectedOutput = "KN";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithThreeCharacters() {
        Metaphone metaphone = new Metaphone();
        String input = "APPLE";
        String expectedOutput = "A1P";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithEmptyString() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String output = metaphone.metaphone(input);
        fail("Expected EncoderException when given empty string");
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullString() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        try {
            metaphone.metaphone(input);
        } catch (DecoderException e) {
            // expected DecoderException as it's inherited from EncoderException
        }
        fail("Expected EncoderException when given null string");
    }

    @Test
    public void testMetaphoneWithWh() {
        Metaphone metaphone = new Metaphone();
        String input = "WHITE";
        String expectedOutput = "W1T";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithInitialX() {
        Metaphone metaphone = new Metaphone();
        String input = "XTREME";
        String expectedOutput = "XK4M";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithSilentG() {
        Metaphone metaphone = new Metaphone();
        String input = "BAG";
        String expectedOutput = "B";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithSilentCH() {
        Metaphone metaphone = new Metaphone();
        String input = "BACH";
        String expectedOutput = "";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMetaphoneWithNumeral0() {
        Metaphone metaphone = new Metaphone();
        String input = "THIS";
        String expectedOutput = "T0S";
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }
}