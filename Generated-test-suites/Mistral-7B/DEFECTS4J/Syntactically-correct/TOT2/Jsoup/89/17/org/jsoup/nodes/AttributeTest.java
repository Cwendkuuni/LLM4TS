import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetAttribute() {
        Attribute attribute = new Attribute("key", "value");
        String result = attribute.getKey();
        assertEquals("key", result);
    }

    @Test
    public void testGetAttributeWithNullKey() {
        Attribute attribute = new Attribute(null, "value");
        assertNull(attribute.getKey());
    }

    @Test
    public void testGetAttributeWithEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        assertEquals("", attribute.getKey());
    }

    @Test
    public void testGetAttributeWithWhitespaceKey() {
        Attribute attribute = new Attribute(" key ", "value");
        assertEquals(" key ", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeWithEmptyAttribute() {
        Attribute attribute = null;
        attribute.getKey();
    }

    @Test(expected = ClassCastException.class)
    public void testGetAttributeWithObjectKey() {
        Attribute attribute = new Attribute("key", new Object());
        assertEquals("key", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithUndefinedKey() {
        Attribute attribute = new Attribute("key", "value");
        assertThrows(IllegalArgumentException.class, () -> attribute.get("unknownKey"));
    }

    @Test
    public void testGetAttributeWithStringKey() {
        Attribute attribute = new Attribute("key", "value");
        String result = attribute.getKey();
        assertEquals("key", result);
    }

    @Test
    public void testGetAttributeWithBooleanKey() {
        Attribute attribute = new Attribute("boolean", null);
        boolean result = attribute.isBooleanAttribute();
        assertTrue(result);
    }

    @Test
    public void testGetAttributeWithBooleanValue() {
        Attribute attribute = new Attribute("boolean", "true");
        boolean result = attribute.isBooleanAttribute();
        assertTrue(result);
    }

    @Test
    public void testGetAttributeWithBooleanValueFalse() {
        Attribute attribute = new Attribute("boolean", "false");
        boolean result = attribute.isBooleanAttribute();
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithInvalidBooleanKey() {
        Attribute attribute = new Attribute("invalid", "value");
        assertFalse(attribute.isBooleanAttribute());
        assertThrows(IllegalArgumentException.class, () -> attribute.isBooleanAttribute());
    }

    @Test
    public void testGetAttributeWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testGetAttributeWithNullValue() {
        Attribute attribute = new Attribute("key", null);
        assertNull(attribute.getValue());
    }

    @Test
    public void testGetAttributeWithNonEmptyValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
}