package org.junit.junit4;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("test"));
        assertEquals("hello", attributes.get("test"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        attributes.put("test", "world");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("test"));
        assertEquals("world", attributes.get("test"));
    }

    @Test
    public void testAddIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Test", "hello");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("Test"));
        assertEquals("hello", attributes.get("Test"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        attributes.remove("test");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("test", "hello");
        attributes.removeIgnoreCase("test");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("test"));
        assertNull(attributes.get("test"));

        attributes.put("test", true);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("test"));
        assertNull(attributes.get("test"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("test", "hello");
        attributes.put(attribute);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("test"));
        assertEquals("hello", attributes.get("test"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("TEST", "hello");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("test", "hello");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "hello1");
        attributes1.put("test2", "hello2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "world2");
        attributes2.put("test3", "hello3");

        attributes1.addAll(attributes2);
        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("test1"));
        assertEquals("hello1", attributes1.get("test1"));
        assertTrue(attributes1.hasKey("test2"));
        assertEquals("world2", attributes1.get("test2"));
        assertTrue(attributes1.hasKey("test3"));
        assertEquals("hello3", attributes1.get("test3"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "hello1");
        attributes.put("data-test2", "hello2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-test1"));
        assertEquals("hello1", dataset.get("data-test1"));
        assertTrue(dataset.containsKey("data-test2"));
        assertEquals("hello2", dataset.get("data-test2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        assertEquals("test=\"hello\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "hello1");
        attributes1.put("test2", "hello2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "hello1");
        attributes2.put("test2", "hello2");

        Attributes attributes3 = new Attributes();
        attributes3.put("test1", "hello1");
        attributes3.put("test3", "hello3");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "hello1");
        attributes1.put("test2", "hello2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "hello1");
        attributes2.put("test2", "hello2");

        Attributes attributes3 = new Attributes();
        attributes3.put("test1", "hello1");
        attributes3.put("test3", "hello3");

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes1.hashCode();
        int hashCode3 = attributes1.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("test"), clonedAttributes.get("test"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("test", "HELLO");

        attributes.normalize();

        assertEquals("test", attributes.getKey(0));
        assertEquals("hello", attributes.getValue(0));
    }
}