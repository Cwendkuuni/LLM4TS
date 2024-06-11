import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    // 
    @Test
    public void testDefaultConstructor() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        assertEquals(0, hashTable.size());
    }
    // 
}