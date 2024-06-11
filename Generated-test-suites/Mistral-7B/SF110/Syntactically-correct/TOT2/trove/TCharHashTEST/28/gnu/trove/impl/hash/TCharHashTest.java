import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    TCharHash charHash;

    @Before
    public void setUp() {
        this.charHash = new TCharHash();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        assertEquals(0, charHash.size());
    }
}