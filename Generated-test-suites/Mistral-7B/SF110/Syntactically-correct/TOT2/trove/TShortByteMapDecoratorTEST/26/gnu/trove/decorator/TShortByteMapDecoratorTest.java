import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
    @Test
    public void testTShortByteMapDecoratorEmptyConstructor() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(Short.MIN_VALUE));
    }
}