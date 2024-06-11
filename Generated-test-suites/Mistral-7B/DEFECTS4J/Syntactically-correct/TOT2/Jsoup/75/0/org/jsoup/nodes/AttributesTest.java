import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMethodWithEmptyKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("key");
 String expected = "";
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMethodWithNullKey() {
 Attributes attributes = new Attributes();
 String key = null;
 attributes.get(key);
 }

 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 @Test
 public void testPutMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 assertEquals(expected, actual);
 }

 @Test
 public void testPutMethodWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 String actual = attributes.get("key");
 assertNull(actual);
 }

 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", false);
 boolean actual = attributes.hasKey("key");
 assertTrue(actual);
 }

 @Test
 public void testPutMethodWithNullKey() {
 Attributes attributes = new Attributes();
 String key = null;
 attributes.put(key, "value");
 }

 @Test
 public void testPutMethodWithEmptyKey() {
 Attributes attributes = new Attributes();
 String key = "";
 attributes.put(key, "value");
 String actual = attributes.get(key);
 String expected = "";
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 String actual = attributes.get("key");
 assertNull(actual);
 }

 @Test
 public void testRemoveMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("key");
 assertNull(actual);
 attributes.remove("key");
 }

 @Test
 public void testRemoveMethodWithNullKey() {
 Attributes attributes = new Attributes();
 String key = null;
 attributes.remove(key);
 }

 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 assertTrue(actual);
 }

 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 boolean actual = attributes.hasKeyIgnoreCase("key");
 assertTrue(actual);
 }

 @Test
 public void testHasKeyMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("key");
 assertFalse(actual);
 }

 @Test
 public void testHasKeyIgnoreCaseMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKeyIgnoreCase("key");
 assertFalse(actual);
 }

 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 assertEquals(expected, actual);
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
 String actual1 = attributes3.get("key1");
 String expected1 = "value1";
 assertEquals(expected1, actual1);
 String actual2 = attributes3.get("key2");
 String expected2 = "value2";
 assertEquals(expected2, actual2);
 }

 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> actual = attributes.asList();
 Map.Entry<String, String> expected1 = new AbstractMap.SimpleEntry<>("key1", "value1");
 Map.Entry<String, String> expected2 = new AbstractMap.SimpleEntry<>("key2", "value2");
 List<Map.Entry<String, String>> expected = new ArrayList<>();
 expected.add(expected1);
 expected.add(expected2);
 assertEquals(expected, actual);
 }

 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> actual = attributes.dataset();
 Map<String, String> expected = new HashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 assertEquals(expected, actual);
 }

 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder expected = new StringBuilder();
 expected.append(" key=\"value\"");
 String actual = attributes.html();
 assertEquals(expected.toString(), actual);
 }

 @Test
 public void testHashCodeMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 int actual1 = attributes1.hashCode();
 int actual2 = attributes2.hashCode();
 int expected = attributes1.hashCode(); // assuming attributes1's hashCode is calculated correctly
 assertEquals(expected, actual1);
 assertEquals(expected, actual2);
 }

 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 Attributes attributes3 = new Attributes();
 attributes3.put("key1", "value2");
 boolean actual1 = attributes1.equals(attributes2);
 boolean expected1 = true;
 assertEquals(expected1, actual1);
 boolean actual2 = attributes1.equals(attributes3);
 boolean expected2 = false;
 assertEquals(expected2, actual2);
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 String actual1 = attributes.get("key1");
 String expected1 = "value1";
 assertEquals(expected1, actual1);
 String actual2 = clonedAttributes.get("key1");
 String expected2 = "value1";
 assertEquals(expected2, actual2);
 }

 @Test
 public void testNormalizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "VALUE");
 attributes.normalize();
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }
}