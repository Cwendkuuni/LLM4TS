
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {
    @Test
    void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    // Test cases for constructors with null key or value
    @Test
    void testAttributeNullKeyOrValueConstruction() {
        assertThrows(NullPointerException.class, () -> new Attribute(null, "value"));
        assertThrows(NullPointerException.class, () -> new Attribute("key", null));
    }

    // Test case for empty string key
    @Test
    void testAttributeEmptyStringKeyConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", "value"));
    }

    // Test case for empty string value
    @Test
    void testAttributeEmptyStringValueConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("key", ""));
    }

    // Test case for parent is null
    @Test
    void testAttributeParentIsNullConstruction() {
        assertThrows(NullPointerException.class, () -> new Attribute("key", "value", null));
    }

    // Test case for key already exists in parent Attributes
    @Test
    void testAttributeKeyAlreadyExistsInParent() {
        Attributes attributes = new Attributes();
        Attribute existingAttribute = new Attribute("existingKey", "existingValue", attributes);
        Attribute newAttribute = new Attribute("existingKey", "newValue", attributes);

        assertThrows(IllegalStateException.class, () -> newAttribute.setParent(attributes));
    }

    // Test case for key or value contains invalid characters
    @Test
    void testAttributeKeyOrValueContainsInvalidCharacters() {
        String invalidKey = "key< invalid";
        String invalidValue = "value\\";

        assertThrows(IllegalArgumentException.class, () -> new Attribute(invalidKey, invalidValue));
    }

    // Test case for getters and setters
    @Test
    void testGettersAndSetters() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());

        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test case for shouldCollapseAttribute()
    @Test
    void testShouldCollapseAttribute() {
        Document.OutputSettings out = Document.OutputSettings.html();
        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.shouldCollapseAttribute(out));

        Attribute attribute1 = new Attribute("key", "value");
        assertFalse(attribute1.shouldCollapseAttribute(out));
    }

    // Test case for isDataAttribute()
    @Test
    void testIsDataAttribute() {
        assertTrue(new Attribute("data-key", "value").isDataAttribute());
        assertFalse(new Attribute("key", "value").isDataAttribute());
    }

    // Test case for equals() and hashCode()
    @Test
    void testEqualsAndHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", "value2");

        assertEquals(attribute1, attribute3);
        assertEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1, attribute2);
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1, attribute4);
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
    }

    // Test case for clone()
    @Test
    void testAttributeClone() {
        Attribute originalAttribute = new Attribute("key", "value");
        Attribute clonedAttribute = originalAttribute.clone();

        originalAttribute.setValue("newValue");

        assertEquals("value", clonedAttribute.getValue());
        assertEquals("key", clonedAttribute.getKey());
    }
}
