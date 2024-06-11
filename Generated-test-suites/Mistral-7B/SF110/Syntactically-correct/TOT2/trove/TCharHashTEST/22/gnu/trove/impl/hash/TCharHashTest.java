import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('B');

        // Basic test case
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));

        // Edge case: empty hash map
        TCharHash emptyCharHash = new TCharHash(0);
        assertFalse(emptyCharHash.contains('A'));

        // Edge case: null character
        assertFalse(charHash.contains(null));

        // Exception handling: negative capacity
        try {
            new TCharHash(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}