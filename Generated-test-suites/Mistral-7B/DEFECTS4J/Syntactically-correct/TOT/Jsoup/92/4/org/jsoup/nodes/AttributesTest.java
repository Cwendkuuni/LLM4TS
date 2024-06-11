package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");
        attributes.put("data-test", "testData");
    }

    @Test
    public void testGetAttribute() {
        assertEquals("testId", attributes.get("id"));
        assertEquals("testClass", attributes.get("class"));
        assertEquals("testData", attributes.get("data-test"));
        assertEquals("", attributes.get("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        assertEquals("testId", attributes.getIgnoreCase("id"));
        assertEquals("testClass", attributes.getIgnoreCase("CLASS"));
        assertEquals("testData", attributes.getIgnoreCase("data-test"));
        assertEquals("", attributes.getIgnoreCase("nonExistentAttribute"));
    }

    @Test
    public void testPutAttribute() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));

        attributes.put("id", "newTestId");
        assertEquals("newTestId", attributes.get("id"));

        attributes.put("class", null);
        Assert.assertNull(attributes.get("class"));

        attributes.put("data-test", null);
        Assert.assertNull(attributes.get("data-test"));
    }

    @Test
    public void testPutAttributeIgnoreCase() {
        attributes.putIgnoreCase("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));

        attributes.putIgnoreCase("ID", "newTestId");
        assertEquals("newTestId", attributes.get("id"));

        attributes.putIgnoreCase("CLASS", null);
        Assert.assertNull(attributes.get("class"));

        attributes.putIgnoreCase("DATA-TEST", null);
        Assert.assertNull(attributes.get("data-test"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertNull(attributes.get("test"));
        assertTrue(attributes.size() == 1);

        attributes.put("test", false);
        assertFalse(attributes.hasKey("test"));
        assertFalse(attributes.size() > 0);
    }

    @Test
    public void testPutBooleanAttributeIgnoreCase() {
        attributes.putIgnoreCase("TEST", true);
        assertTrue(attributes.hasKey("test"));
        assertNull(attributes.get("test"));
        assertTrue(attributes.size() == 1);

        attributes.putIgnoreCase("TEST", false);
        assertFalse(attributes.hasKey("test"));
        assertFalse(attributes.size() > 0);
    }

    @Test
    public void testRemoveAttribute() {
        attributes.remove("id");
        assertFalse(attributes.hasKey("id"));
        assertEquals(2, attributes.size());

        attributes.remove("class");
        assertFalse(attributes.hasKey("class"));
        assertFalse(attributes.size() > 0);

        attributes.remove("nonExistentAttribute");
        assertTrue(attributes.size() == 1);
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        attributes.removeIgnoreCase("id");
        assertFalse(attributes.hasKey("id"));
        assertEquals(2, attributes.size());

        attributes.removeIgnoreCase("CLASS");
        assertFalse(attributes.hasKey("class"));
        assertFalse(attributes.size() > 0);

        attributes.removeIgnoreCase("nonExistentAttribute");
        assertTrue(attributes.size() == 1);
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKey("data-test"));
        assertFalse(attributes.hasKey("nonExistentAttribute"));

        attributes.put("newKey", "newValue");
        assertTrue(attributes.hasKey("newKey"));
        assertFalse(attributes.hasKey("NEWKEY"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertTrue(attributes.hasKeyIgnoreCase("CLASS"));
        assertTrue(attributes.hasKeyIgnoreCase("data-test"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentAttribute"));

        attributes.putIgnoreCase("newKey", "newValue");
        assertTrue(attributes.hasKeyIgnoreCase("newKey"));
        assertFalse(attributes.hasKeyIgnoreCase("NEWKEY"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("id", "testId");
        attributes.put("class", "testClass");
        attributes.put("data-test", "testData");
        assertEquals(3, attributes.size());

        attributes.remove("id");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey1", "newValue1");
        newAttributes.put("newKey2", "newValue2");

        attributes.addAll(newAttributes);
        assertEquals(5, attributes.size());

        assertTrue(attributes.hasKey("newKey1"));
        assertTrue(attributes.hasKey("newKey2"));
        assertEquals("newValue1", attributes.get("newKey1"));
        assertEquals("newValue2", attributes.get("newKey2"));
    }

    @Test
    public void testToString() {
        assertEquals("id=\"testId\" class=\"testClass\" data-test=\"testData\"", attributes.toString());
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        attributes.html(sb, new Document.OutputSettings());
        assertEquals("id=\"testId\" class=\"testClass\" data-test=\"testData\"", sb.toString());
    }
}