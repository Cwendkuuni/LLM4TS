
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.junit.Assert.*;
import java.io.IOException;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Other test cases for other methods ...

    @Test
    public void testAttributeCreationWithNullKeyAndValue() {
        try {
            new Attribute(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key cannot be null", e.getMessage());
        }
    }

    @Test
    public void testAttributeCreationWithEmptyStringAsKey() {
        try {
            new Attribute("", "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testAttributeCreationWithEmptyStringAsValue() {
        try {
            new Attribute("key", "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("value cannot be empty", e.getMessage());
        }
    }

    // Other test cases for edge cases and exception handling scenarios ...

    @Test
    public void testIOExceptionInHtmlMethod() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attribute.html(sb, new Document("").outputSettings());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    // Other test cases for other methods and scenarios ...
}