import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationWithNullArguments() {
        String key = null;
        String value = null;
        new Attribute(key, value);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newKey = "newTestKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullArguments() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newKey = null;
        attribute.setKey(newKey);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newValue = "newTestValue";
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullArguments() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newValue = null;
        attribute.setValue(newValue);
    }

    @Test
    public void testIsBooleanAttribute() {
        String booleanAttributeKey = "checked";
        Attribute booleanAttribute = new Attribute(booleanAttributeKey, null);
        assertTrue(booleanAttribute.isBooleanAttribute());

        String nonBooleanAttributeKey = "testKey";
        Attribute nonBooleanAttribute = new Attribute(nonBooleanAttributeKey, "testValue");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    // Add test cases for edge cases and exception handling scenarios here

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, (new Document("")).outputSettings());
        String expectedHtml = "testKey=\"testValue\"";
        assertEquals(expectedHtml, sb.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue%3D%3D";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals(encodedValue, attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedWithInvalidKey() {
        String unencodedKey = "";
        String encodedValue = "testValue%3D%3D";
        Attribute.createFromEncoded(unencodedKey, encodedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedWithInvalidValue() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue%";
        Attribute.createFromEncoded(unencodedKey, encodedValue);
    }
}