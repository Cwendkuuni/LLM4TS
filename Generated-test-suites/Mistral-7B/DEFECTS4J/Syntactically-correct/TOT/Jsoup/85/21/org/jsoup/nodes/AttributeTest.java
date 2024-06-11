import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "class";
        String value = "test";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(parent, attribute.getParent());
        assertEquals(1, parent.size());
    }

    @Test
    public void testAttributeKeySetter() {
        String oldKey = "class";
        String newKey = "id";
        Attribute attribute = new Attribute(oldKey, "test");

        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
        assertNotEquals(oldKey, attribute.getKey());
        assertNull(attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeKeySetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        String oldValue = "test";
        String newValue = "newTest";
        Attribute attribute = new Attribute("key", oldValue);

        String newValueReturned = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(oldValue, newValueReturned);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeValueSetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        String actual = attribute.html();

        assertEquals("key=\"value\"", actual);
        assertEquals("key=\"value\"", expected.toString());
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        String unencodedKey = "class";
        String encodedValue = "class=\"test\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("test", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attribute = new Attribute("class", "test");
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("data-test", null);
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("class", "test");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("hidden", null);

        assertTrue(attribute.shouldCollapseAttribute(out));
        assertTrue(attribute.shouldCollapseAttribute(out, ""));
        assertTrue(attribute.shouldCollapseAttribute(out, "hidden"));
        assertFalse(attribute.shouldCollapseAttribute(out, "value"));
    }

    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute2.equals(attribute3));
    }

    @Test
    public void testAttributeClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
    }
}