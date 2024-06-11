import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {
    @Test
    public void testMetaphoneWithSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String expectedResult = "A";
        String actualResult = metaphone.metaphone("A");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMetaphoneWithTwoCharactersSameVowel() {
        Metaphone metaphone = new Metaphone();
        String expectedResult = "AE";
        String actualResult = metaphone.metaphone("APPLE");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMetaphoneWithInitialK() {
        Metaphone metaphone = new Metaphone();
        String expectedResult = "K";
        String actualResult = metaphone.metaphone("KNOW");
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullString() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input);
    }
}