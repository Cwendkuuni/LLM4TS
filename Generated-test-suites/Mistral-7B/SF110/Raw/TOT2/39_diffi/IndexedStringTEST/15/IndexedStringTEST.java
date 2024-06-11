import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {
    @Test
    public void testIndexedStringConstructor() {
        String testString = "TestString";
        IndexedString indexedString = new IndexedString(testString);
        assertEquals(testString.length(), indexedString.values.length);
    }
}