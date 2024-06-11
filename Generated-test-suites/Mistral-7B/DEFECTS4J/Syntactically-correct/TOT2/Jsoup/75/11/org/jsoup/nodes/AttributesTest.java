import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

// Test case for get method
@Test
public void testGetMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String actual = attributes.get("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for getIgnoreCase method
@Test
public void testGetIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value");
String actual = attributes.getIgnoreCase("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for put method
@Test
public void testPutMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value1");
attributes.put("key", "value2");
String actual = attributes.get("key");
String expected = "value2";
assertEquals(expected, actual);
}

// Test case for putIgnoreCase method
@Test
public void testPutIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value1");
attributes.put("key", "Value2");
String actual = attributes.getIgnoreCase("key");
String expected = "value2";
assertEquals(expected, actual);
}

// Test case for put method with boolean value
@Test
public void testPutBooleanMethod() {
Attributes attributes = new Attributes();
attributes.put("key", true);
assertTrue(attributes.get("key").equals(""));
attributes.put("key", false);
assertFalse(attributes.hasKey("key"));
}

// Test case for put method with Attribute object
@Test
public void testPutAttributeObjectMethod() {
Attributes attributes = new Attributes();
String key = "key";
Attributes attribute = new Attributes();
attribute.put(key, "value");
attributes.put(key, attribute);
Attributes actual = attributes.get(key);
assertEquals(attribute, actual);
}

// Test case for remove method
@Test
public void testRemoveMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
attributes.remove("key");
assertFalse(attributes.hasKey("key"));
}

// Test case for removeIgnoreCase method
@Test
public void testRemoveIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value");
attributes.removeIgnoreCase("key");
assertFalse(attributes.hasKey("Key"));
}

// Test case for hasKey method
@Test
public void testHasKeyMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
assertTrue(attributes.hasKey("key"));
}

// Test case for hasKeyIgnoreCase method
@Test
public void testHasKeyIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value");
assertTrue(attributes.hasKeyIgnoreCase("key"));
}

// Test case for size method
@Test
public void testSizeMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
int actual = attributes.size();
int expected = 2;
assertEquals(expected, actual);
}

// Test case for addAll method
@Test
public void testAddAllMethod() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key2", "value2");
attributes1.addAll(attributes2);
assertEquals(attributes2.size(), attributes1.size());
}

// Test case for asList method
@Test
public void testAsListMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
List<Attribute> actual = attributes.asList();
assertEquals(2, actual.size());
}

// Test case for dataset method
@Test
public void testDatasetMethod() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "value1");
attributes.put("data-key2", "value2");
Map<String, String> actual = attributes.dataset();
assertEquals(2, actual.size());
}

// Test case for html method
@Test
public void testHtmlMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
String expected = " key1=\"value1\" key2=\"value2\"";
try {
String actual = attributes.html();
assertEquals(expected, actual);
} catch (IOException e) {
e.printStackTrace();
fail("IOException occurred");
}
}

// Test case for equals method
@Test
public void testEqualsMethod() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key1", "value1");
assertTrue(attributes1.equals(attributes2));
}

// Test case for clone method
@Test
public void testCloneMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
Attributes clonedAttributes = attributes.clone();
assertEquals(attributes.size(), clonedAttributes.size());
}
}