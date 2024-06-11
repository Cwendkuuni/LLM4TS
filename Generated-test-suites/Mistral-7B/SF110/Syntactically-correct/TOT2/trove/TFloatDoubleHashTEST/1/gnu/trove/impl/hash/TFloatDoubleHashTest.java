import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash(5);
        floatDoubleHash.insertKey(1.0f);
        assertTrue(floatDoubleHash.contains(1.0f));
    }
}