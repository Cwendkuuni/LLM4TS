package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test adding attributes
        attributes.put("class", "test");
        attributes.put("id", "testId");
        attributes.put("title", "Test Title");
        attributes.put("data-custom", "customValue");

        // Test getting attributes
        Assert.assertEquals("test", attributes.get("class"));
        Assert.assertEquals("testId", attributes.get("id"));
        Assert.assertEquals("Test Title", attributes.get("title"));
        Assert.assertEquals("customValue", attributes.get("data-custom"));
        Assert.assertEquals(EmptyString, attributes.get("notExistentKey"));

        // Test getting case-insensitive attributes
        Assert.assertEquals("test", attributes.getIgnoreCase("Class"));
        Assert.assertEquals("testId", attributes.getIgnoreCase("id"));
        Assert.assertEquals("Test Title", attributes.getIgnoreCase("title"));
        Assert.assertEquals("customValue", attributes.getIgnoreCase("DATA-CUSTOM"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("notExistentKey"));

        // Test putting the same attribute twice
        attributes.put("class", "test2");
        Assert.assertEquals("test2", attributes.get("class"));

        // Test removing an attribute
        attributes.remove("class");
        Assert.assertEquals(EmptyString, attributes.get("class"));

        // Test removing a non-existent attribute
        attributes.remove("notExistentKey");

        // Test adding and removing boolean attributes
        attributes.put("checked", true);
        Assert.assertEquals(EmptyString, attributes.get("checked"));
        attributes.put("checked", false);
        Assert.assertEquals(EmptyString, attributes.get("checked"));
        attributes.remove("checked");

        // Test adding all attributes from another Attributes object
        Attributes attributes2 = new Attributes();
        attributes2.put("data-custom2", "value2");
        attributes.addAll(attributes2);
        Assert.assertEquals("customValue", attributes.get("data-custom"));
        Assert.assertEquals("value2", attributes.get("data-custom2"));

        // Test size and empty check
        Assert.assertEquals(4, attributes.size());
        Assert.assertTrue(attributes.isEmpty());
        attributes.clear();
        Assert.assertEquals(0, attributes.size());
        Assert.assertTrue(attributes.isEmpty());

        // Test iteration
        List<Map.Entry<String, String>> attributesList = new LinkedHashMap<>(attributes);
        for (Map.Entry<String, String> entry : attributesList) {
            Assert.assertEquals(entry.getValue(), attributes.get(entry.getKey()));
        }

        // Test clone
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            Assert.assertEquals(attributes.get(i), clonedAttributes.get(i));
        }

        // Test normalize
        attributes.put("My-attribute");
        attributes.normalize();
        Assert.assertEquals("my-attribute", attributes.get("my-attribute"));

        // Test removing duplicate attributes (case sensitive)
        attributes.put("duplicateKey", "value1");
        attributes.put("duplicateKey", "value2");
        Assert.assertEquals("value2", attributes.get("duplicateKey"));

        // Test removing duplicate attributes (case insensitive)
        attributes.put("DuplicateKey", "value1");
        attributes.put("DuplicateKey", "value2");
        Assert.assertEquals("value2", attributes.get("duplicateKey"));

        // Test dataset
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("customValue", dataset.get("data-custom"));
        Assert.assertEquals("value2", dataset.get("data-custom2"));
    }

    @Test
    public void testExceptionHandling() {
        Attributes attributes = new Attributes();

        try {
            attributes.put(null, "test");
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            attributes.put("", "test");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}