import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    @Test
    public void testDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        // Testing the functionality of the class by checking if an empty TCharHash can contain a character.
        assertFalse(charHash.contains('a'));
    }
}