import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;
import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.Empty;
import static org.jsoup.nodes.Attributes.NotFound;
import static org.jsoup.nodes.Attributes.EmptyString;
import static org.jsoup.helper.Validate.notNull;

public class AttributesTest {

 // Test for constructor and initial state
 @Test
 public void testConstructorInitialState() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(InitialCapacity, attributes.size());
 Assert.assertArrayEquals(Attributes.Empty, attributes.keys);
 Assert.assertArrayEquals(Attributes.Empty, attributes.vals);
 }

 // Test for get method with existing key
 @Test
 public void testGetMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test for get method with non-existing key
 @Test
 public void testGetMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("nonExistingKey");
 Assert.assertEquals(EmptyString, actual);
 }

 // Test for getIgnoreCase method with existing key
 @Test
 public void testGetIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test for getIgnoreCase method with non-existing key
 @Test
 public void testGetIgnoreCaseMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.getIgnoreCase("NonExistingKey");
 Assert.assertEquals(EmptyString, actual);
 }

 // Test for add method with existing key
 @Test(expected = IllegalArgumentException.class)
 public void testAddMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.add("key", "newValue");
 }

 // Test for add method with non-existing key
 @Test
 public void testAddMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.add("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test for put method with existing key
 @Test
 public void testPutMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 Assert.assertEquals(expected, actual);
 }

 // Test for put method with new key
 @Test
 public void testPutMethodWithNewKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String actual1 = attributes.get("key1");
 String expected1 = "value1";
 Assert.assertEquals(expected1, actual1);
 attributes.put("key2", "value2");
 String actual2 = attributes.get("key2");
 String expected2 = "value2";
 Assert.assertEquals(expected2, actual2);
 }

 // Test for put method with boolean value
 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertFalse(attributes.get("key").equals(EmptyString));
 attributes.remove("key");
 }

 // Test for put method with Attribute object
 @Test
 public void testPutMethodWithAttributeObject() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test for remove method with existing key
 @Test
 public void testRemoveMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test for remove method with non-existing key
 @Test
 public void testRemoveMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.remove("nonExistingKey");
 Assert.assertFalse(attributes.hasKey("nonExistingKey"));
 }

 // Test for hasKey method with existing key
 @Test
 public void testHasKeyMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test for hasKey method with non-existing key
 @Test
 public void testHasKeyMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("nonExistingKey");
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test for hasKeyIgnoreCase method with existing key
 @Test
 public void testHasKeyIgnoreCaseMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKeyIgnoreCase("Key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test for hasKeyIgnoreCase method with non-existing key
 @Test
 public void testHasKeyIgnoreCaseMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKeyIgnoreCase("NonExistingKey");
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test for size method with initial state
 @Test
 public void testSizeMethodWithInitialState() {
 Attributes attributes = new Attributes();
 int actual = attributes.size();
 int expected = InitialCapacity;
 Assert.assertEquals(expected, actual);
 }

 // Test for size method after adding attributes
 @Test
 public void testSizeMethodAfterAddingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = InitialCapacity + 2;
 Assert.assertEquals(expected, actual);
 }

 // Test for addAll method with empty attributes
 @Test
 public void testAddAllMethodWithEmptyAttributes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.addAll(attributes2);
 int actual = attributes1.size();
 int expected = InitialCapacity;
 Assert.assertEquals(expected, actual);
 }

 // Test for addAll method with attributes containing keys
 @Test
 public void testAddAllMethodWithAttributesContainingKeys() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 int actual = attributes1.size();
 int expected = InitialCapacity + 2;
 Assert.assertEquals(expected, actual);
 String actualKey1 = attributes1.get("key1");
 String expectedKey1 = "value1";
 Assert.assertEquals(expectedKey1, actualKey1);
 String actualKey2 = attributes1.get("key2");
 String expectedKey2 = "value2";
 Assert.assertEquals(expectedKey2, actualKey2);
 }

 // Test for iterator method
 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
 Map.Entry<String, String> entry;
 while (iterator.hasNext()) {
 entry = iterator.next();
 String key = entry.getKey();
 String value = entry.getValue();
 System.out.println("Key: " + key + ", Value: " + value);
 }
 }

 // Test for asList method
 @Test
 public void testAsListMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> list = attributes.asList();
 for (Map.Entry<String, String> entry : list) {
 String key = entry.getKey();
 String value = entry.getValue();
 System.out.println("Key: " + key + ", Value: " + value);
 }
 }

 // Test for dataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 for (Map.Entry<String, String> entry : dataset.entrySet()) {
 String key = entry.getKey();
 String value = entry.getValue();
 System.out.println("Key: " + key + ", Value: " + value);
 }
 }

 // Test for html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append(" key=\"value\"");
 String actualHtml = attributes.html();
 Assert.assertEquals(expectedHtml.toString(), actualHtml);
 }

 // Test for equals method with same attributes
 @Test
 public void testEqualsMethodWithSameAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 boolean actual = attributes1.equals(attributes2);
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test for equals method with different attributes
 @Test
 public void testEqualsMethodWithDifferentAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 boolean actual = attributes1.equals(attributes2);
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test for hashCode method with same attributes
 @Test
 public void testHashCodeMethodWithSameAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 int actual1 = attributes1.hashCode();
 int actual2 = attributes2.hashCode();
 int expected = InitialCapacity + attributes1.size();
 Assert.assertEquals(expected, actual1);
 Assert.assertEquals(expected, actual2);
 }

 // Test for hashCode method with different attributes
 @Test
 public void testHashCodeMethodWithDifferentAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 int actual1 = attributes1.hashCode();
 int actual2 = attributes2.hashCode();
 int expected = InitialCapacity + attributes1.size() + attributes2.size();
 Assert.assertNotEquals(expected, actual1);
 Assert.assertNotEquals(expected, actual2);
 }

 // Test for clone method
 @Test
 public void testCloneMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 int actualSize1 = attributes.size();
 int actualSize2 = clonedAttributes.size();
 int actualKeys1Length = attributes.keys.length;
 int actualKeys2Length = clonedAttributes.keys.length;
 int actualVals1Length = attributes.vals.length;
 int actualVals2Length = clonedAttributes.vals.length;
 Assert.assertEquals(actualSize1, actualSize2);
 Assert.assertEquals(actualKeys1Length, actualKeys2Length);
 Assert.assertEquals(actualVals1Length, actualVals2Length);
 }

 // Test for normalize method
 @Test
 public void testNormalizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 attributes.normalize();
 String actualKey = attributes.get("key");
 String expectedKey = "key";
 Assert.assertEquals(expectedKey, actualKey);
 Assert.assertFalse(attributes.hasKey("KEY"));
 }

 // Test for removeDuplicates method with case-sensitive keys
 @Test
 public void testRemoveDuplicatesMethodWithCaseSensitiveKeys() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 attributes.removeDuplicates(true);
 int actualSize = attributes.size();
 int expectedSize = 1;
 Assert.assertEquals(expectedSize, actualSize);
 String actualValue = attributes.get("key1");
 String expectedValue = "value1"; // or "value2"
 Assert.assertEquals(expectedValue, actualValue);
 }

 // Test for removeDuplicates method with case-insensitive keys
 @Test
 public void testRemoveDuplicatesMethodWithCaseInsensitiveKeys() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("Key1", "value3");
 attributes.removeDuplicates(false);
 int actualSize = attributes.size();
 int expectedSize = 2;
 Assert.assertEquals(expectedSize, actualSize);
 String actualValue1 = attributes.get("key1");
 String expectedValue1 = "value1"; // or "value3"
 Assert.assertEquals(expectedValue1, actualValue1);
 String actualValue2 = attributes.get("key2");
 String expectedValue2 = "value2";
 Assert.assertEquals(expectedValue2, actualValue2);
 }
}