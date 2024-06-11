import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
        assertEquals((short) 1, hash.getNoEntryValue());
    }
}