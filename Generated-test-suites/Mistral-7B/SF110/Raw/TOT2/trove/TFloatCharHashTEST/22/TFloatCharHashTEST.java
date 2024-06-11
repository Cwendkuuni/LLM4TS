import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TFloatCharHashTest {

    @Test
    public void testEmptyConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
    }
}