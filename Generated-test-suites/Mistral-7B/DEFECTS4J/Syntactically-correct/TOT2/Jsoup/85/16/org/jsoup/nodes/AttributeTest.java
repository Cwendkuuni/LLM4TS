import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
}