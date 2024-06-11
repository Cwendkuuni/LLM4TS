import org.junit.Test;
import org.junit.Assert.*;
import static org.apache.commons.codec.language.Metaphone.*;

public class MetaphoneTest {

    @Test
    public void testEmptyString() {
        String actual = metaphone("");
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testSingleCharacter() {
        String actual = metaphone("A");
        String expected = "A";
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneSimpleWord() {
        String actual = metaphone("cat");
        String expected = "KAT";
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        String actual = metaphone(null);
        fail("Expected EncoderException but got null");
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInputLengthZero() {
        String actual = metaphone("");
        fail("Expected EncoderException but got an empty string");
    }

    @Test
    public void testMetaphoneComplexWord() {
        String actual = metaphone("telephone");
        String expected = "TELxPHOn";
        assertEquals(expected, actual);
    }
}