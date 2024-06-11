package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String result = attributes.get("key1");
        assertEquals("value1", result);

        attributes.put("key2", null);
        result = attributes.get("key2");
        assertNull(result);

        attributes.put("key3", "");
        result = attributes.get("key3");
        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String result = attributes.getIgnoreCase("key1");
        assertEquals("value1", result);

        attributes.put("Key2", "value2");
        result = attributes.getIgnoreCase("Key2");
        assertEquals("value2", result);

        attributes.put("", "");
        result = attributes.getIgnoreCase("");
        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.get(null);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        String result = attributes.get("key1");
        assertEquals("value2", result);

        attributes.put("key2", "value3");
        result = attributes.get("key2");
        assertEquals("value3", result);
    }

    @Test
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", null);
        String result = attributes.get("key1");
        assertNull(result);
    }

    @Test
    public void testPutMethodWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "");
        String result = attributes.get("key1");
        assertEquals("", result);
    }

    @Test
    public void testPutMethodWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        String result = attributes.get("key1");
        assertEquals("value2", result);
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        String result = attributes.get("key1");
        assertNull(result);

        boolean value = attributes.hasKey("key1");
        assertTrue(value);
    }

    @Test
    public void testPutMethodWithAttributeObject() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        String result = attributes.get("key1");
        assertEquals("value1", result);
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullKeyOrValue() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value1");
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullAttributeObject() {
        Attributes attributes = new Attributes();
        attributes.put(null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        String result1 = attributes.get("key1");
        assertNull(result1);

        attributes.remove("key2");
        String result2 = attributes.get("key2");
        assertNull(result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithInvalidKey() {
        Attributes attributes = new Attributes();
        attributes.remove("invalidKey");
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        boolean result = attributes.hasKey("key1");
        assertTrue(result);

        result = attributes.hasKey("key2");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY1", "value2");

        boolean result1 = attributes.hasKeyIgnoreCase("key1");
        assertTrue(result1);

        boolean result2 = attributes.hasKeyIgnoreCase("KEY1");
        assertTrue(result2);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();
        assertEquals(2, result);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        String resultKey1 = result.get(key1);
        String resultValue1 = result.get(key1);

        assertEquals(key1, resultKey1);
        assertEquals(value1, resultValue1);

        String resultKey2 = result.get(key2);
        String resultValue2 = result.get(key2);

        assertEquals(key2, resultKey2);
        assertEquals(value2, resultValue2);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllMethodWithNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());

        attribute = iterator.next();
        assertEquals("key2", attribute.getKey());
        assertEquals("value2", attribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> result = attributes.asList();
        Attribute attribute1 = result.get(0);
        Attribute attribute2 = result.get(1);

        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());

        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());

        assertEquals(2, result.size());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> result = attributes.dataset();
        String key1 = "data-key1";
        String value1 = "value1";

        assertEquals(key1, result.keySet().iterator().next());
        assertEquals(value1, result.get(key1));

        String key2 = "data-key2";
        String value2 = "value2";

        assertEquals(key2, result.keySet().iterator().next());
        assertEquals(value2, result.get(key2));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\"");

        String result = attributes.html();
        String actual = result.trim();

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();
        int attributes3HashCode = attributes3.hashCode();

        assertEquals(attributes1HashCode, attributes1.hashCode());
        assertEquals(attributes1HashCode, attributes2.hashCode());
        assertNotEquals(attributes1HashCode, attributes3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());

        String key1 = "key1";
        String value1 = "value1";

        String result1 = attributes.get(key1);
        String clonedResult1 = clonedAttributes.get(key1);

        assertEquals(key1, result1);
        assertEquals(key1, clonedResult1);
        assertEquals(value1, result1);
        assertEquals(value1, clonedResult1);
    }
}