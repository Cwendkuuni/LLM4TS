import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    @Test
    public void testTFloatDoubleHash_NoArgumentConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }
}