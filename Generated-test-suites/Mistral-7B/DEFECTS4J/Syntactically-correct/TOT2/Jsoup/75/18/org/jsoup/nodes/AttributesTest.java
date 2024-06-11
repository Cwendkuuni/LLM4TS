import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("Value", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("Value", result);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        assertTrue(result);
        result = attributes.get("key") == null;
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        assertEquals(Attributes.EmptyString, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
        result = attributes.hasKey("other");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertTrue(result);
        result = attributes.hasKeyIgnoreCase("Other");
        assertFalse(result);
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
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int result1Size = attributes1.size();
        assertEquals(2, result1Size);
        int result2Size = attributes2.size();
        assertEquals(0, result2Size);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> result = attributes.asList();
        assertEquals(2, result.size());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> result = attributes.dataset();
        assertEquals(2, result.size());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethod() throws SerializationException {
        Attributes attributes = new Attributes();
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
    }
}