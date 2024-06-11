import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String result = attributes.get("key");
        assertEquals(Attributes.EmptyString, result);
    }

    @Test
    public void testPutBooleanAttributeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        assertTrue(result);
        String emptyResult = attributes.get("key");
        assertEquals(Attributes.EmptyString, emptyResult);
    }

    @Test
    public void testPutBooleanAttributeRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.remove("key");
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testPutAttributeMethodWithSameKeyMultipleTimes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result1 = attributes.get("key");
        String expected1 = "value2";
        assertEquals(expected1, result1);
        String result2 = attributes.getIgnoreCase("key");
        String expected2 = "value2";
        assertEquals(expected2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeMethodWithSameKeyAndDifferentValueButDifferentCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("Key", "value2");
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        int size = attributes3.size();
        assertEquals(2, size);
        String result1 = attributes3.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, result1);
        String result2 = attributes3.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, result2);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        int size = attributes.size();
        assertEquals(1, size);
        String result = attributes.get("key2");
        String expected = "value2";
        assertEquals(expected, result);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("Key1");
        int size = attributes.size();
        assertEquals(1, size);
        String result = attributes.get("Key2");
        String expected = "value2";
        assertEquals(expected, result);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethodWithNonExistentKeycase() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKeyIgnoreCase("KEY");
        assertFalse(result);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int size = attributes.size();
        assertEquals(2, size);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-custom", "custom1");
        attributes.put("data-custom2", "custom2");
        Map<String, String> dataset = attributes.dataset();
        int size = dataset.size();
        assertEquals(2, size);
        String result1 = dataset.get("data-custom");
        String expected1 = "custom1";
        assertEquals(expected1, result1);
        String result2 = dataset.get("data-custom2");
        String expected2 = "custom2";
        assertEquals(expected2, result2);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attributes.html(sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String result = StringUtil.releaseBuilder(sb);
        String expected = "key=\"value\"";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.toString();
        String expected = "key=\"value\"";
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        boolean result = attributes1.equals(attributes2);
        assertTrue(result);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        int result1 = attributes1.hashCode();
        int result2 = attributes2.hashCode();
        assertEquals(result1, result2);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        int size1 = attributes.size();
        int size2 = clonedAttributes.size();
        assertEquals(size1, size2);
        String result1 = attributes.get("key");
        String expected1 = "value";
        String result2 = clonedAttributes.get("key");
        String expected2 = "value";
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }
}