import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        String html = attr.html();
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", null);
        assertTrue(attr.isDataAttribute());

        Attribute attr2 = new Attribute("key", null);
        assertFalse(attr2.isDataAttribute());

        Attribute attr3 = new Attribute("key", "value");
        assertFalse(attr3.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("allowfullscreen", null);
        assertTrue(attr.shouldCollapseAttribute(null));

        Attribute attr2 = new Attribute("key", "value");
        assertFalse(attr2.shouldCollapseAttribute(null));

        Attribute attr3 = new Attribute("checked", null);
        assertTrue(attr3.shouldCollapseAttribute(null));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyConstruction() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueConstruction() {
        new Attribute("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeySet() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSet() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKeyConstruction() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValueConstruction() {
        new Attribute("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKeySet() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test(expected = SerializationException.class)
    public void testIOExceptionDuringHtmlGeneration() {
        Attribute attr = new Attribute("key", "value");
        try {
            attr.html();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test
    public void testInvalidKey() {
        Attribute attr = new Attribute("key<html>", "value");
        String html = attr.html();
        String expectedHtml = "key<html>\">=\"value\"";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testInvalidValue() {
        Attribute attr = new Attribute("key", "<script>alert('Hello World')</script>");
        String html = attr.html();
        String expectedHtml = "key=\"<script>alert('Hello World')</script>\"".replaceAll("\\<[^>]*>", "");
        assertEquals(expectedHtml, html);
    }
}