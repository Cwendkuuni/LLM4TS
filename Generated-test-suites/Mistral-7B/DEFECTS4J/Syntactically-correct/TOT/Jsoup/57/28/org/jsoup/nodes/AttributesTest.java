import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        String result = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInvalidKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInvalidValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result = attributes.get("key");
        String expected = "value2";
        assertEquals(expected, result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    public void testHasKeyMissing() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        int expected = 2;
        assertEquals(expected, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size1 = attributes1.size();
        int expectedSize1 = 2;
        int size2 = attributes2.size();
        assertEquals(expectedSize1, size1);
        assertEquals(size1, size2 + 1);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        boolean result = attributes.hasKey("key");
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        boolean result = attributes.hasKeyIgnoreCase("key");
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Map<String, String> result = attributes.dataset();
        String expected = "data-value";
        assertEquals(expected, result.get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String expected = " key=\"value\"";
        String result = attributes.html();
        assertEquals(expected, result);
    }
}