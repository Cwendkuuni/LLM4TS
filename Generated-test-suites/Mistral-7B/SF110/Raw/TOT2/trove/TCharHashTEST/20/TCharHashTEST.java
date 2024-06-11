import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TCharHashTest {
    @Test
    public void testTCharHashDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        char defaultValue = charHash.getNoEntryValue();
        assertEquals("Default char value should be set to '\\u0000'", defaultValue, '\u0000');
    }
}