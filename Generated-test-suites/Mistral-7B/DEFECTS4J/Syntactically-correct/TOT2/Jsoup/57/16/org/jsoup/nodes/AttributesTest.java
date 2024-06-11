import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Attributes;

public class AttributesTest {
    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }
}