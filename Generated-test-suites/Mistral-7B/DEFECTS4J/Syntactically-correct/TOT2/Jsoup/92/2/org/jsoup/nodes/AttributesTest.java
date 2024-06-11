import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 // Test case for get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String expected = "value";
 String actual = attributes.get("key");
 assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String expected = "Value";
 String actual = attributes.getIgnoreCase("key");
 assertEquals(expected, actual);
 }

 // Test case for put method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 String expected1 = "value1";
 String expected2 = "value2";
 String actual1 = attributes.get("key1");
 String actual2 = attributes.get("key2");
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 }

 // Test case for put method with the same key
 @Test(expected = IllegalStateException.class)
 public void testPutMethodWithSameKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 }

 // Test case for putIgnoreCase method
 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 attributes.putIgnoreCase("key1", "newValue1");
 String expected1 = "newValue1";
 String actual1 = attributes.getIgnoreCase("key1");
 assertEquals(expected1, actual1);
 }

 // Test case for put method with boolean value
 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean expected = true;
 boolean actual = attributes.get("key").equals(EmptyString);
 assertFalse(actual);
 }

 // Test case for remove method
 @Test
 public void testRemoveMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 String expected1 = EmptyString;
 String actual1 = attributes.get("key1");
 String expected2 = "value2";
 String actual2 = attributes.get("key2");
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 }

 // Test case for removeIgnoreCase method
 @Test
 public void testRemoveIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 attributes.removeIgnoreCase("key1");
 String expected1 = EmptyString;
 String actual1 = attributes.getIgnoreCase("key1");
 String expected2 = "Value2";
 String actual2 = attributes.getIgnoreCase("key2");
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 }

 // Test case for hasKey method
 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for hasKey method with non-existent key
 @Test
 public void testHasKeyMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test case for addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");
 attributes2.put("key3", "value3");
 attributes2.put("key4", "value4");
 attributes1.addAll(attributes2);
 assertEquals(4, attributes1.size());
 }

 // Test case for iterator method
 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> expectedList = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"));
 List<Map.Entry<String, String>> actualList = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes) {
 actualList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
 }
 assertEquals(expectedList, actualList);
 }

 // Test case for asList method
 @Test
 public void testAsListMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> expectedList = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
 List<Attribute> actualList = attributes.asList();
 assertEquals(expectedList, actualList);
 }

 // Test case for dataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("data-key2", "value2");
 Map<String, String> actualMap = attributes.dataset();
 assertEquals(expectedMap, actualMap);
 }

 // Test case for html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 String expected = " key1=\"value1\" key2=\"value2\"";
 String actual = attributes.html();
 assertEquals(expected, actual);
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

 // Test case for equals method with different sizes
 @Test
 public void testEqualsMethodWithDifferentSizes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 assertFalse(attributes1.equals(attributes2));
 }

 // Test case for equals method with different keys
 @Test
 public void testEqualsMethodWithDifferentKeys() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value1");
 assertFalse(attributes1.equals(attributes2));
 }

 // Test case for equals method with different values
 @Test
 public void testEqualsMethodWithDifferentValues() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value2");
 assertFalse(attributes1.equals(attributes2));
 }

 // Test case for hashCode method
 @Test
 public void testHashCodeMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test case for hashCode method with different sizes
 @Test
 public void testHashCodeMethodWithDifferentSizes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test case for hashCode method with different keys
 @Test
 public void testHashCodeMethodWithDifferentKeys() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value1");
 assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test case for hashCode method with different values
 @Test
 public void testHashCodeMethodWithDifferentValues() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value2");
 assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test case for clone method
 @Test
 public void testCloneMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes, clonedAttributes);
 }
}