import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash floatCharHash = new TFloatCharHash(10);
        float expectedNoEntryKey = 0.0f;
        float actualNoEntryKey = floatCharHash.getNoEntryKey();
        assertEquals(expectedNoEntryKey, actualNoEntryKey);
    }
}