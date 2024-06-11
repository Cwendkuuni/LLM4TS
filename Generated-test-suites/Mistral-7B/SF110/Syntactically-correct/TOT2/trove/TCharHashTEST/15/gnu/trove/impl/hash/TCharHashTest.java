import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TCharHashTest {
    @Test
    public void testTCharHash_DefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        char hashValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hashValue);
    }
}