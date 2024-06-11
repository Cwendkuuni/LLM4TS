import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }
}