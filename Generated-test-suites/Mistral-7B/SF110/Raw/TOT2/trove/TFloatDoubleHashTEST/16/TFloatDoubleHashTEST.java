import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TFloatDoubleHashTest {
    TFloatDoubleHash floatDoubleHash;

    @Before
    public void setup() {
        floatDoubleHash = new TFloatDoubleHash(5);
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
    }

    @Test
    public void testContains() {
        assertTrue(floatDoubleHash.contains(1.0f));
        assertTrue(floatDoubleHash.contains(2.0f));
        assertFalse(floatDoubleHash.contains(3.0f));
    }
}