import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        // Test creating a new attribute with valid key and value
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        // Test creating a new attribute with valid key, value, and parent
        Attributes parent = new Attributes();
        Attribute attrParent = new Attribute("parentKey", "parentValue", parent);
        attrParent.setKey("newKey");
        assertEquals("newKey", attrParent.getKey());
        assertTrue(parent.containsKey("parentKey"));
        assertTrue(parent.containsValue("parentValue"));
        assertFalse(parent.containsKey("newKey"));
        assertFalse(parent.containsValue("value"));
        Attribute newAttr = new Attribute("newKey", "newValue", parent);
        assertEquals("newKey", newAttr.getKey());
        assertEquals("newValue", newAttr.getValue());
        assertTrue(parent.containsKey("newKey"));
        assertTrue(parent.containsValue("newValue"));

        // Test creating a new attribute from encoded key and value
        Attribute encodedAttr = Attribute.createFromEncoded("unencodedKey", "%65%6E%63%6F%64%65%64%20%77%72%69%74%65%20%61%74%74%72%65%20%61%6D%65%20%68%74%74%70%3D%22%63%61%6D%73%63%61%6E%6E%65%63%74%68%69%6E%67%22");
        assertEquals("unencodedKey", encodedAttr.getKey());
        assertEquals("source", encodedAttr.getValue());
    }

    @Test
    public void testAttributeSetting() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", attr.getOldValue());

        Attribute attrParent = new Attribute("parentKey", "parentValue", null);
        Attributes parent = new Attributes();
        parent.put(attrParent);
        attrParent.setValue("newValue");
        assertEquals("newValue", parent.get("parentKey").getValue());
        assertEquals("parentValue", attrParent.getOldValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeCreation() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeCreationWithParent() {
        new Attribute(null, "value", null);
    }

    @Test
    public void testAttributeHtmlEncoding() {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attr.html(sb, new Document("").outputSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String html = StringUtil.releaseBuilder(sb);
        assertTrue(html.contains("key=\"value\""));

        Attribute booleanAttr = new Attribute("boolean", null);
        assertTrue(booleanAttr.isBooleanAttribute());
        sb.setLength(0);
        try {
            booleanAttr.html(sb, new Document("").outputSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String booleanHtml = StringUtil.releaseBuilder(sb);
        assertTrue(booleanHtml.isEmpty());
    }

    @Test
    public void testAttributeEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testAttributeClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
        attr.setValue("newValue");
        assertNotEquals(attr, clonedAttr);
    }
}