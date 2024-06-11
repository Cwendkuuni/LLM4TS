package org.test.jsoup.nodes; // Assuming you have a test package

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {
    // Test for constructor with key and value
    @Test
    public void testAttributeWithKeyAndValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    // Test for constructor with key, value, and parent
    @Test
    public void testAttributeWithKeyValueAndParent() {
        Attribute attribute = new Attribute("testKey", "testValue", null);
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
        assertNull(attribute.getParent());
    }

    // Test for getKey() method
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
    }

    // Test for setKey() method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey1", "testValue");
        attribute.setKey("testKey2");
        assertEquals("testKey2", attribute.getKey());
    }

    // Test for getValue() method
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testValue", attribute.getValue());
    }

    // Test for setValue() method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue1");
        attribute.setValue("testValue2");
        assertEquals("testValue2", attribute.getValue());
    }

    // Test for html() method
    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "testKey=\"testValue\"";
        String actualHtml = StringUtil.releaseBuilder(sb);
        assertEquals(expectedHtml, actualHtml);
    }

    // Test for isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-testKey", "testValue");
        assertTrue(attribute.isDataAttribute());
    }

    // Test for isBooleanAttribute() method (deprecated)
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("testKey", null);
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    // Test for shouldCollapseAttribute() method
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);

        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        assertTrue(attribute1.shouldCollapseAttribute(outputSettings));

        Attribute attribute2 = new Attribute("testKey2", "testValue2");
        assertFalse(attribute2.shouldCollapseAttribute(outputSettings));
    }

    // Test for equals() method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        Attribute attribute2 = new Attribute("testKey1", "testValue1");
        Attribute attribute3 = new Attribute("testKey2", "testValue2");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    // Test for hashCode() method
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        Attribute attribute2 = new Attribute("testKey1", "testValue1");
        Attribute attribute3 = new Attribute("testKey2", "testValue2");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    // Test for clone() method
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    // Test for constructor with empty key and value
    @Test
    public void testAttributeWithEmptyKeyAndValue() {
        Attribute attribute = new Attribute("", "");
        assertEquals("", attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    // Test for constructor with null key and non-null value
    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullKeyAndNonNullValue() {
        new Attribute(null, "testValue");
    }

    // Test for constructor with null key and null value
    @Test
    public void testAttributeWithNullKeyAndNullValue() {
        Attribute attribute = new Attribute(null, null);
        assertNull(attribute.getKey());
        assertNull(attribute.getValue());
    }

    // Test for constructor with key containing special characters
    @Test
    public void testAttributeWithSpecialCharacterKey() {
        Attribute attribute = new Attribute("testKey%", "testValue");
        assertEquals("testKey%", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    // Test for constructor with value containing special characters
    @Test
    public void testAttributeWithSpecialCharacterValue() {
        Attribute attribute = new Attribute("testKey", "testValue%");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue%", attribute.getValue());
    }

    // Test for constructor with empty key and empty value
    @Test
    public void testAttributeWithEmptyKeyAndEmptyValue() {
        Attribute attribute = new Attribute("", "");
        assertEquals("", attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    // Test for constructor with long key length
    @Test
    public void testAttributeWithLongKeyLength() {
        String longKey = "testKeyWithLongLengthWithLengthOf256";
        Attribute attribute = new Attribute(longKey, "testValue");
        assertEquals(longKey, attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    // Test for constructor with long value length
    @Test
    public void testAttributeWithLongValueLength() {
        Attribute attribute = new Attribute("testKey", "testValueWithLongLengthWithLengthOf256");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValueWithLongLengthWithLengthOf256", attribute.getValue());
    }

    // Test for key collision with same key in parent Attributes
    @Test
    public void testAttributeWithParentAndKeyCollision() {
        Attributes parent = new Attributes();
        parent.put("testKey", new Attribute("testKey", "value1"));

        Attribute attribute = new Attribute("testKey", "value2");
        attribute.setParent(parent);

        assertEquals("value2", parent.get("testKey").getValue());
    }

    // Test for setKey() method with parent and same key
    @Test
    public void testSetKeyWithParentAndSameKey() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue");
        parent.put("testKey", attribute);

        attribute.setKey("newTestKey");

        Attribute newAttribute = parent.get("newTestKey");
        assertEquals("newTestKey", newAttribute.getKey());
        assertEquals("testValue", newAttribute.getValue());
    }

    // Test for setValue() method with parent and same key
    @Test
    public void testSetValueWithParentAndSameKey() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue1");
        parent.put("testKey", attribute);

        attribute.setValue("testValue2");

        Attribute newAttribute = parent.get("testKey");
        assertEquals("testKey", newAttribute.getKey());
        assertEquals("testValue2", newAttribute.getValue());
    }

    // Test for illegal arguments in constructors with null parent
    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullParent() {
        new Attribute("testKey", "testValue", null);
    }

    // Test for illegal arguments in constructors with null key and null value
    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullKeyAndNullValueInConstructor() {
        new Attribute(null, null);
    }

    // Test for SerializationException when calling html() method with null document
    @Test(expected = SerializationException.class)
    public void testSerializationExceptionWhenCallingHtmlWithNullDocument() {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
    }

    // Test for IOException when calling html() method with invalid output settings
    @Test(expected = IOException.class)
    public void testIOExceptionWhenCallingHtmlWithInvalidOutputSettings() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.invalidSyntax));
    }
}