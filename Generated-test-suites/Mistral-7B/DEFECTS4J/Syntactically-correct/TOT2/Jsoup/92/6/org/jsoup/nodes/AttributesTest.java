import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

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

// Test case for put method with existing key
@Test
public void testPutMethodWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value1");
attributes.put("key", "value2");
String actual = attributes.get("key");
String expected = "value2";
assertEquals(expected, actual);
}

// Test case for put method with new key
@Test
public void testPutMethodWithNewKey() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
String actual = attributes.get("key2");
assertEquals(Attributes.EmptyString, actual);
}

// Test case for put method with boolean value
@Test
public void testPutMethodWithBooleanValue() {
Attributes attributes = new Attributes();
attributes.put("key", true);
assertTrue(attributes.get("key").equalsIgnoreCase("true"));
}

// Test case for remove method with existing key
@Test
public void testRemoveMethodWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
attributes.remove("key");
String actual = attributes.get("key");
assertEquals(Attributes.EmptyString, actual);
}

// Test case for remove method with non-existing key
@Test
public void testRemoveMethodWithNonExistingKey() {
Attributes attributes = new Attributes();
String actual = attributes.get("key");
assertEquals(Attributes.EmptyString, actual);
attributes.remove("key");
String expected = Attributes.EmptyString;
assertEquals(expected, actual);
}

// Test case for hasKey method with existing key
@Test
public void testHasKeyMethodWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
boolean actual = attributes.hasKey("key");
assertTrue(actual);
}

// Test case for hasKey method with non-existing key
@Test
public void testHasKeyMethodWithNonExistingKey() {
Attributes attributes = new Attributes();
boolean actual = attributes.hasKey("key");
assertFalse(actual);
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
attributes1.put("key1", "value1");
Attributes attributes2 = new Attributes();
attributes2.put("key2", "value2");
attributes1.addAll(attributes2);
String actual1 = attributes1.get("key1");
String expected1 = "value1";
assertEquals(expected1, actual1);
String actual2 = attributes1.get("key2");
String expected2 = "value2";
assertEquals(expected2, actual2);
}

// Test case for dataset method
@Test
public void testDatasetMethod() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "data-value1");
attributes.put("data-key2", "data-value2");
Map<String, String> actual = attributes.dataset();
Map<String, String> expected = new HashMap<>();
expected.put("key1", "value1");
expected.put("key2", "value2");
assertEquals(expected, actual);
}

// Test case for html method
@Test
public void testHtmlMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String actual = attributes.html();
String expected = " key=\"value\"";
assertEquals(expected, actual);
}

// Test case for equals method
@Test
public void testEqualsMethod() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
Attributes attributes2 = new Attributes();
attributes2.put("key1", "value1");
assertTrue(attributes1.equals(attributes2));

Attributes attributes3 = new Attributes();
attributes3.put("key1", "value2");
assertFalse(attributes1.equals(attributes3));}

// Test case for hashCode method
@Test
public void testHashCodeMethod() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
Attributes attributes2 = new Attributes();
attributes2.put("key1", "value1");
assertEquals(attributes1.hashCode(), attributes2.hashCode());

Attributes attributes3 = new Attributes();
attributes3.put("key1", "value2");
assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
}

// Test case for clone method
@Test
public void testCloneMethod() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
Attributes attributes2 = attributes1.clone();
attributes1.put("key2", "value2");
String actual1 = attributes1.get("key1");
String expected1 = "value1";
assertEquals(expected1, actual1);
String actual2 = attributes2.get("key1");
assertEquals(expected1, actual2);
String actual3 = attributes2.get("key2");
assertEquals(Attributes.EmptyString, actual3);
}

// Test case for normalize method
@Test
public void testNormalizeMethod() {
Attributes attributes = new Attributes();
attributes.put("KEY", "VALUE");
attributes.normalize();
String actual = attributes.get("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for add method with existing key
@Test(expected = IllegalArgumentException.class)
public void testAddMethodWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
attributes.add("key", "newValue");
}

// Test case for add method with null key
@Test(expected = NullPointerException.class)
public void testAddMethodWithNullKey() {
Attributes attributes = new Attributes();
attributes.add(null, "value");
}

// Test case for add method with null value
@Test(expected = NullPointerException.class)
public void testAddMethodWithNullValue() {
Attributes attributes = new Attributes();
attributes.add("key", null);
}
}