package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AttributesTest {

    private Attributes attributes;

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new Attributes()},
                {new Attributes().put("foo", "bar")},
                {new Attributes().put("foo", "bar").put("baz", "qux")}
        });
    }

    public AttributesTest(Attributes attributes) {
        this.attributes = attributes;
    }

    @Test
    public void testGet() {
        assertEquals("", attributes.get(""));
        assertEquals("bar", attributes.get("foo"));
        assertNull(attributes.get("baz"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("bar", attributes.getIgnoreCase("foo"));
        assertEquals("", attributes.getIgnoreCase(""));
        assertNull(attributes.getIgnoreCase("baz"));
    }

    @Test
    public void testPut() {
        attributes.put("foo", "bar");
        assertEquals("bar", attributes.get("foo"));
        attributes.put("foo", "qux");
        assertEquals("qux", attributes.get("foo"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("foo", true);
        assertTrue(attributes.hasKey("foo"));
        assertFalse(attributes.hasKey("foo")); // will be removed if value is false
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("foo", "bar");
        attributes.put(attr);
        assertEquals("bar", attributes.get("foo"));
    }

    @Test
    public void testRemove() {
        attributes.put("foo", "bar");
        assertEquals("bar", attributes.get("foo"));
        attributes.remove("foo");
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("foo", "bar");
        assertEquals("bar", attributes.get("foo"));
        attributes.removeIgnoreCase("Foo");
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKey("foo"));
        attributes.put("foo", "bar");
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertFalse(attributes.hasKeyIgnoreCase(""));
        assertFalse(attributes.hasKeyIgnoreCase("foo"));
        attributes.put("foo", "bar");
        assertTrue(attributes.hasKeyIgnoreCase("foo"));
        assertTrue(attributes.hasKeyIgnoreCase("FOO"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("foo", "bar");
        assertEquals(1, attributes.size());
        attributes.put("baz", "qux");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes().put("baz", "qux");
        attributes.addAll(incoming);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("foo"));
        assertTrue(attributes.hasKey("baz"));
    }

    @Test
    public void testIterator() {
        for (Attribute attr : attributes) {
            assertTrue(attributes.hasKey(attr.getKey()));
        }
    }

    @Test
    public void testAsList() {
        List<Attribute> list = attributes.asList();
        for (Attribute attr : list) {
            assertTrue(attributes.hasKey(attr.getKey()));
        }
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            assertTrue(attributes.hasKeyIgnoreCase(entry.getKey()));
        }
    }

    @Test
    public void testHtml() {
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            fail("Exception expected");
        } catch (IOException e) {
            // Expected
        }

        String html = attributes.html();
        assertTrue(html.length() > 0);
    }

    @Test
    public void testToString() {
        String toString = attributes.toString();
        String html = attributes.html();
        assertTrue(toString.equals(html));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes().put("foo", "bar");
        Attributes attributes2 = new Attributes().put("foo", "bar");
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes().put("foo", "bar").put("baz", "qux");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes().put("foo", "bar");
        Attributes attributes2 = new Attributes().put("foo", "bar");
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes().put("foo", "bar").put("baz", "qux");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes4.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes().put("foo", "bar");
        Attributes attributes2 = attributes1.clone();
        Attributes attributes3 = attributes1.clone();

        attributes1.put("baz", "qux");

        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testDatasetIterator() {
        for (Map.Entry<String, String> entry : attributes.dataset().entrySet()) {
            assertTrue(attributes.hasKeyIgnoreCase(entry.getKey()));
        }
    }
}