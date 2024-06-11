import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

// Test case for get method
@Test
public void testGet() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String result = attributes.get("key");
String expected = "value";
assertEquals(expected, result);
}

// Test case for getIgnoreCase method
@Test
public void testGetIgnoreCase() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String result = attributes.getIgnoreCase("key");
String expected = "value";
assertEquals(expected, result);
}

// Test case for put method
@Test
public void testPut() {
Attributes attributes = new Attributes();
attributes.put("key", "value1");
attributes.put("key", "value2");
String expected = "value2";
assertEquals(expected, attributes.get("key"));
}

// Test case for putIgnoreCase method
@Test
public void testPutIgnoreCase() {
Attributes attributes = new Attributes();
attributes.putIgnoreCase("Key", "value1");
attributes.putIgnoreCase("Key", "value2");
String expected = "value2";
assertEquals(expected, attributes.getIgnoreCase("Key"));
}

// Test case for put boolean attribute
@Test
public void testPutBooleanAttribute() {
Attributes attributes = new Attributes();
attributes.put("booleanKey", true);
assertTrue(attributes.hasKey("booleanKey"));
assertFalse(attributes.get("booleanKey").equals("booleanKey"));
}

// Test case for remove method
@Test
public void testRemove() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
attributes.remove("key");
assertFalse(attributes.hasKey("key"));
}

// Test case for removeIgnoreCase method
@Test
public void testRemoveIgnoreCase() {
Attributes attributes = new Attributes();
attributes.put("keyIgnoreCase", "value");
attributes.removeIgnoreCase("keyIgnoreCase");
assertFalse(attributes.hasKeyIgnoreCase("keyIgnoreCase"));
}

// Test case for hasKey method
@Test
public void testHasKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
assertTrue(attributes.hasKey("key"));
}

// Test case for hasKeyIgnoreCase method
@Test
public void testHasKeyIgnoreCase() {
Attributes attributes = new Attributes();
attributes.put("Key", "value");
assertTrue(attributes.hasKeyIgnoreCase("Key"));
}

// Test case for size method
@Test
public void testSize() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
int expected = 2;
assertEquals(expected, attributes.size());
}

// Test case for addAll method
@Test
public void testAddAll() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
Attributes attributes2 = new Attributes();
attributes2.put("key2", "value2");
attributes1.addAll(attributes2);
assertEquals(2, attributes1.size());
assertTrue(attributes1.hasKey("key1"));
assertTrue(attributes1.hasKey("key2"));
}

// Test case for asList method
@Test
public void testAsList() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
List<Attribute> expected = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
List<Attribute> result = attributes.asList();
assertEquals(expected, result);
}

// Test case for dataset method
@Test
public void testDataset() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "value1");
attributes.put("data-key2", "value2");
Map<String, String> expected = new HashMap<String, String>() {{
put("data-key1", "value1");
put("data-key2", "value2");
}};
Map<String, String> result = attributes.dataset();
assertEquals(expected, result);
}

// Test case for html method
@Test
public void testHtml() throws IOException {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String expected = " key=\"value\"";
String result = attributes.html();
assertEquals(expected, result);
}

// Test case for equals method
@Test
public void testEquals() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key1", "value1");
assertTrue(attributes1.equals(attributes2));
}

// Test case for clone method
@Test
public void testClone() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
Attributes clonedAttributes = attributes.clone();
assertEquals(attributes, clonedAttributes);
}

// Test case for normalize method
@Test
public void testNormalize() {
Attributes attributes = new Attributes();
attributes.put("Key", "value");
attributes.normalize();
assertTrue(attributes.hasKey("key"));
}

// Test case for empty constructor
@Test
public void testEmptyConstructor() {
Attributes attributes = new Attributes();
assertTrue(attributes.size() == 0);
}
}