import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put("key", "newValue");
        actual = attributes.get("key");
        expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);

        attributes.put("key", false);
        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testPutNull() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String actual = attributes.get("key");
        assertNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);

        attributes.remove("key");
        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);

        attributes.remove("Key");
        actual = attributes.hasKeyIgnoreCase("key");
        assertFalse(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);

        String actual1 = attributes1.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);

        String actual2 = attributes1.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals("", actual);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        attributes.removeIgnoreCase("key");
        String actual = attributes.getIgnoreCase("key");
        assertEquals("", actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        String actual = dataset.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() throws IOException {
        Document doc = new Document();
        Element element = doc.createElement("div");
        Attributes attributes = element.attributes();
        attributes.put("key", "value");
        String actual = attributes.html();
        String expected = " key=\"value\"";
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = attributes1;

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes1.equals(attributes3));

        attributes1.remove("key1");
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();

        attributes.remove("key");
        assertFalse(attributes.equals(clonedAttributes));

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, clonedAttributes.get("key"));
    }
}