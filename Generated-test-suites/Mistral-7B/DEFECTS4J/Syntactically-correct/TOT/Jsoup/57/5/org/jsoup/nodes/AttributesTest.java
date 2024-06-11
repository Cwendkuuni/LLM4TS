import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");
        attributes.put("data-testData", "testData");
    }

    @Test
    public void testGet() {
        assertEquals("testId", attributes.get("id"));
        assertEquals("testClass", attributes.get("class"));
        assertEquals("testData", attributes.get("data-testData"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("testId", attributes.getIgnoreCase("id"));
        assertEquals("testClass", attributes.getIgnoreCase("CLASS"));
        assertEquals("testData", attributes.getIgnoreCase("data-testData"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("testBool", false);
        assertFalse(attributes.hasKey("testBool"));
        attributes.put("testBool", true);
        assertTrue(attributes.hasKey("testBool"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("newKey", "newValue");
        attributes.put(attr);
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testRemove() {
        attributes.remove("id");
        assertFalse(attributes.hasKey("id"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("ID");
        assertFalse(attributes.hasKey("id"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertTrue(attributes.hasKeyIgnoreCase("CLASS"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
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
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals("testData", dataset.get("data-testData"));
        assertEquals(1, dataset.size());
    }

    @Test(expected = SerializationException.class)
    public void testHtml() throws SerializationException {
        attributes.html();
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("id", "testId");
        attributes2.put("class", "testClass");
        attributes2.put("data-testData", "testData");
        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        for (String key : attributes.keySet()) {
            assertEquals(attributes.get(key), clonedAttributes.get(key));
        }
    }
}