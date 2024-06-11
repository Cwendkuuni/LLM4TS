package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Get and set attributes
        attributes.put("id", "test");
        Assert.assertEquals("test", attributes.get("id"));

        // Get ignore case attribute
        attributes.put("CLASS", "test");
        Assert.assertEquals("test", attributes.getIgnoreCase("class"));

        // Add multiple attributes
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");
        attributes.put("test3", "value3");

        // Test size
        Assert.assertEquals(3, attributes.size());

        // Test iterator
        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();
        while (iterator.hasNext()) {
            attributesList.add(iterator.next());
        }
        Assert.assertEquals(Arrays.asList(
                new Attribute("test3", "value3"),
                new Attribute("data-test1", "value1"),
                new Attribute("data-test2", "value2")),
                attributesList);

        // Test put with existing key
        attributes.put("id", "newValue");
        Assert.assertEquals("newValue", attributes.get("id"));

        // Test put with ignore case key
        attributes.putIgnoreCase("ID", "newValueIgnoreCase");
        Assert.assertEquals("newValueIgnoreCase", attributes.get("id"));

        // Test put boolean attribute
        attributes.put("testBoolean", true);
        Assert.assertFalse(attributes.hasValue("testBoolean"));
        Assert.assertTrue(attributes.hasKey("testBoolean"));
        Assert.assertFalse(attributes.get("testBoolean").isBlank());

        // Test put with Attribute object
        Attribute attribute = new Attribute("data-test4", "value4");
        attributes.put(attribute);
        Assert.assertEquals("value4", attributes.get("data-test4"));

        // Test remove attribute
        attributes.remove("id");
        Assert.assertFalse(attributes.hasKey("id"));

        // Test remove ignore case attribute
        attributes.removeIgnoreCase("ID");
        Assert.assertFalse(attributes.hasKey("id"));

        // Test hasKey
        Assert.assertTrue(attributes.hasKey("data-test1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Data-test1"));
        Assert.assertFalse(attributes.hasKey("nonExistent"));

        // Test addAll
        Attributes attributes2 = new Attributes();
        attributes2.put("data-test5", "value5");
        attributes.addAll(attributes2);
        Assert.assertEquals(5, attributes.size());
        Assert.assertEquals("value5", attributes.get("data-test5"));

        // Test dataset
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-test1"));
        Assert.assertEquals("value2", dataset.get("data-test2"));
        Assert.assertEquals("value3", dataset.get("test3"));
        Assert.assertEquals("value4", dataset.get("data-test4"));

        // Test html
        Assert.assertEquals("<id>test</id> data-test1=\"value1\" data-test2=\"value2\" class=\"test\" test3=\"value3\" data-test4=\"value4\" testBoolean=\"\">", attributes.html());

        // Test equals
        Attributes attributes3 = new Attributes();
        attributes3.put("id", "test");
        attributes3.put("data-test1", "value1");
        attributes3.put("data-test2", "value2");
        attributes3.put("test3", "value3");
        Assert.assertEquals(attributes, attributes3);

        // Test clone
        Attributes attributes4 = attributes.clone();
        Assert.assertEquals(attributes, attributes4);

        // Test normalize
        attributes.normalize();
        Assert.assertEquals(Arrays.asList(
                new Attribute("data-test1", "value1"),
                new Attribute("data-test2", "value2"),
                new Attribute("test3", "value3"),
                new Attribute("id", "test"),
                new Attribute("testboolean", "true")),
                attributes.asList());
    }
}