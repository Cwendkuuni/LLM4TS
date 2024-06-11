package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.jsoup.nodes.*;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        // Test for constructor with key and value
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeWithNullKey() {
        String key = null;
        String value = "testValue";
        try {
            new Attribute(key, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAttributeWithEmptyKey() {
        String key = "";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
    }

    // Testing the behavior when the key contains special characters
    // ...

    @Test
    public void testAttributeWithLongValue() {
        String key = "testKey";
        String value = "longValueThatIsLongerThanTheMaximumLength";
        try {
            new Attribute(key, value);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Testing the behavior when the key is a boolean attribute
    // ...

    @Test
    public void testSettingAttributeWithSameKeyTwice() {
        String key = "testKey";
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute(key, "value1", parent);
        Attribute attribute2 = new Attribute(key, "value2", parent);

        // The second attribute should not be added to the parent Attributes
        assertEquals(1, parent.size());
    }
}