import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TShortHashTest {
    @Test
    public void testDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
}