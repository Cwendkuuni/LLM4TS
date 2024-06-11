import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;
import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.isEmpty;

public class AttributesTest {

 // Test for constructor
 @Test
 public void testAttributesConstructor() {
 Attributes attributes = new Attributes();
 assertEquals(InitialCapacity, attributes.keys.length);
 assertEquals(InitialCapacity, attributes.vals.length);
 }

 // Test for get method with existing key
 @Test
 public void testGetMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 // Test for get method with non-existent key
 @Test
 public void testGetMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertEquals(Attributes.EmptyString, attributes.get("non-existent-key"));
 }

 // Test for getIgnoreCase method with existing key
 @Test
 public void testGetIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 // Test for getIgnoreCase method with non-existent key
 @Test
 public void testGetIgnoreCaseMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("non-existent-key"));
 }

 // Test for put method with existing key
 @Test
 public void testPutMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 // Test for put method with non-existent key
 @Test
 public void testPutMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 // Test for putIgnoreCase method with existing key
 @Test
 public void testPutIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value1");
 attributes.putIgnoreCase("key", "Value2");
 assertEquals("Value2", attributes.getIgnoreCase("key"));
 }

 // Test for putIgnoreCase method with non-existent key
 @Test
 public void testPutIgnoreCaseMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 // Test for put method with boolean value
 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertEquals(Attributes.EmptyString, attributes.get("key"));
 }

 // Test for put method with null value
 @Test(expected = NullPointerException.class)
 public void testPutMethodWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 // Test for addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");

 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes2.put("key3", "value3");

 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);

 assertEquals(6, attributes3.size());
 assertTrue(attributes3.hasKey("key1"));
 assertTrue(attributes3.hasKey("key2"));
 assertTrue(attributes3.hasKey("key3"));
 }

 // Test for hasKey method with existing key
 @Test
 public void testHasKeyMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 // Test for hasKey method with non-existent key
 @Test
 public void testHasKeyMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("non-existent-key"));
 }

 // Test for hasKeyIgnoreCase method with existing key
 @Test
 public void testHasKeyIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test for hasKeyIgnoreCase method with non-existent key
 @Test
 public void testHasKeyIgnoreCaseMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("non-existent-key"));
 }

 // Test for remove method with existing key
 @Test
 public void testRemoveMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 // Test for remove method with non-existent key
 @Test
 public void testRemoveMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.remove("non-existent-key"));
 assertEquals(Attributes.EmptyString, attributes.get("non-existent-key"));
 }

 // Test for removeIgnoreCase method with existing key
 @Test
 public void testRemoveIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 // Test for removeIgnoreCase method with non-existent key
 @Test
 public void testRemoveIgnoreCaseMethodWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.removeIgnoreCase("non-existent-key"));
 assertEquals(Attributes.EmptyString, attributes.get("non-existent-key"));
 }

 // Test for size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test for isEmpty method
 @Test
 public void testIsEmptyMethod() {
 Attributes attributes = new Attributes();
 assertEquals(true, isEmpty(attributes));
 }

 // Test for dataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "data-value1");
 attributes.put("data-key2", "data-value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("data-value1", dataset.get("data-key1"));
 assertEquals("data-value2", dataset.get("data-key2"));
 }

 // Test for html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 StringBuilder sb = StringUtil.borrowBuilder();
 try {
 attributes.html(sb, new Document.OutputSettings());
 } catch (IOException e) {
 throw new SerializationException(e);
 }
 String expectedHtml = " key1=\"value1\" key2=\"value2\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 // Test for equals method
 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 assertTrue(attributes1.equals(attributes2));
 }

 // Test for equals method with different size
 @Test
 public void testEqualsMethodWithDifferentSize() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 assertFalse(attributes1.equals(attributes2));
 }
 }