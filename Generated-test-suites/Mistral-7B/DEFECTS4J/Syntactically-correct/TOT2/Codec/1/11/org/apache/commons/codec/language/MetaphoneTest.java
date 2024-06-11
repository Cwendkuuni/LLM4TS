import org.junit.Test;
import org.apache.commons.codec.language.Metaphone;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Basic functionality test
        assertEquals("BAKE", metaphone.metaphone("Bake"));
        assertEquals("BAK", metaphone.metaphone("Bak"));
        assertEquals("BAK", metaphone.metaphone("back"));

        // Edge cases and exception handling tests
        assertEquals("KNIGHT", metaphone.metaphone("KNIGHT"));
        assertEquals("AEGIS", metaphone.metaphone("AEGIS"));
        assertEquals("WRONG", metaphone.metaphone("WRONG"));
        assertEquals("X", metaphone.metaphone("CHI"));
        assertEquals("J", metaphone.metaphone("DGE"));
        assertEquals("J", metaphone.metaphone("DGI"));
        assertEquals("J", metaphone.metaphone("DGY"));

        // Test with empty String
        assertEquals("", metaphone.metaphone(""));

        // Test with null String
        try {
            metaphone.metaphone(null);
            fail("Expected EncoderException when passing null");
        } catch (EncoderException e) {
            // Expected
        }
    }
}