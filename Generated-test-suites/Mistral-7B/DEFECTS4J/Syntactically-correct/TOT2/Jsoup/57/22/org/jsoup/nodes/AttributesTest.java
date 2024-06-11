import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

public class AttributesTest {
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInvalidKey() {
        Attributes attributes = new Attributes();
        attributes.put("invalidKey", "value");
    }

    @Test
    public void testPutDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("key");
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.clear();
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasKeyInvalidKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllEmpty() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.addAll(attributes2);
        assertTrue(attributes1.isEmpty());
    }

    @Test
    public void testAddAllNonEmpty() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNull() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("data-value", dataset.get("data-key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Document document = new Document("");
        try {
            attributes.html(document.appendBody().appendText(""), document.outputSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String html = document.body().text();
        assertTrue(html.contains(" key=\"value\""));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("{\"key\":\"value\"}", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value", clonedAttributes.get("key"));
    }
}