import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import static org.jsoup.helper.Validate.*;
import java.io.IOException;

public class AttributeTest {
    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeConstructionWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeConstructionWithNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testAttributeConstructionWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get(attribute.getKey()));
    }

    @Test
    public void testParentUpdate() {
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute("key1", "value1", parent);
        Attribute attribute2 = new Attribute("key2", "value2", parent);
        parent.remove(attribute1.getKey());
        assertEquals(attribute2, parent.get(attribute1.getKey()));
    }

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey(null);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedWithNullUnencodedKey() {
        new Attribute(null, "encodedValue");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedWithNullEncodedValue() {
        new Attribute("unencodedKey", null);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "dataKey", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("booleanAttribute", "booleanAttribute");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xhtml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("booleanAttribute", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("booleanAttribute", "true");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }
}