import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Basic test case
        assertEquals("BAIT", metaphone.metaphone("baith"));

        // Test with empty and null strings
        assertEquals("", metaphone.metaphone(""));
        assertNull(metaphone.metaphone(null));

        // Test with single character strings
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("K", metaphone.metaphone("K"));

        // Test with strings containing only vowels
        assertEquals("AEIO", metaphone.metaphone("aeiou"));

        // Test edge cases
        assertEquals("AY", metaphone.metaphone("a")); // single letter becomes itself
        assertEquals("S0", metaphone.metaphone("thetah")); // TH becomes 0
        assertEquals("KN", metaphone.metaphone("knights")); // KN exception
        assertEquals("AEIOU", metaphone.metaphone("aieou")); // all vowels
    }
}