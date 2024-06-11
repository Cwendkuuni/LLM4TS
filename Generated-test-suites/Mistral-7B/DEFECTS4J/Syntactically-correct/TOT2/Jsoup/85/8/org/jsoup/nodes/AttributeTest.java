import org.junit.Test;
import org.junit.Assert.*;
import java.io.IOException;
import org.jsoup.SerializationException;
import static org.jsoup.nodes.Attributes.Document;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        // Test for constructor that takes key and value
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        // Test edge cases
        Attribute attributeNullKey = new Attribute(null, "value");
        assertNull(attributeNullKey.getKey());

        Attribute attributeEmptyKey = new Attribute("", "value");
        assertEquals("", attributeEmptyKey.getKey());

        Attribute attributeEmptyValue = new Attribute("key", "");
        assertEquals("key", attributeEmptyValue.getKey());
        assertEquals("", attributeEmptyValue.getValue());

        // Test exception handling
        try {
            new Attribute(null, null);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = new StringBuilder("key=\"value\"");
        StringBuilder sb = new StringBuilder();

        try {
            attribute.html(sb, new Document.OutputSettings());
        } catch (IOException e) {
            throw new SerializationException(e);
        }

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testIsBooleanAttribute() {
        // Basic cases
        assertTrue(new Attribute("allowfullscreen", null).isBooleanAttribute());
        assertTrue(new Attribute("checked", null).isBooleanAttribute());

        // Edge cases
        assertFalse(new Attribute("key", "value").isBooleanAttribute());

        // Exception handling
        try {
            new Attribute(null, null).isBooleanAttribute();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testShouldCollapseAttribute() {
        // Basic cases
        assertTrue(new Attribute("checked", null).shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(new Attribute("checked", "checked").shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(new Attribute("allowfullscreen", null).shouldCollapseAttribute(new Document.OutputSettings()));

        // Edge cases
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(new Document.OutputSettings()));

        // Exception handling
        try {
            new Attribute(null, null).shouldCollapseAttribute(new Document.OutputSettings());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}