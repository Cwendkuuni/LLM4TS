import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeTest {
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("testKey", "testValue");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    // Add test cases for edge cases and exceptions
}