import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String expectedValue = "value1";
 String actualValue = attributes.get("key1");
 assertEquals(expectedValue, actualValue);
 }

 // Test case for get() method with case insensitive key
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String expectedValue = "value1";
 String actualValue = attributes.getIgnoreCase("key1");
 assertEquals(expectedValue, actualValue);
 }

 // Test case for get() method with non-existent key
 @Test
 public void testGetNotFound() {
 Attributes attributes = new Attributes();
 String actualValue = attributes.get("nonExistentKey");
 assertEquals(Attributes.EMPTY_STRING, actualValue);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 String expectedValue = value1;
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for put() method with existing key
 @Test
 public void testPutOverwrite() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 String newValue = "newValue";
 attributes.put(key1, newValue);
 String expectedValue = newValue;
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for put() method with null value
 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 attributes.put(key1, null);
 String expectedValue = null;
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for putIgnoreCase() method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 String newKey = "Key1";
 String newValue = "newValue";
 attributes.putIgnoreCase(newKey, newValue);
 String expectedValue = newValue;
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for putIgnoreCase() method with existing key and different case
 @Test
 public void testPutIgnoreCaseOverwrite() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 String newKey = "Key1";
 String newValue = "newValue";
 attributes.putIgnoreCase(newKey, newValue);
 String expectedValue = newValue;
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for put() method with empty string value
 @Test
 public void testPutEmptyStringValue() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 attributes.put(key1, "");
 String expectedValue = "";
 String actualValue = attributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }

 // Test case for hasKey() method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 boolean expectedResult = true;
 boolean actualResult = attributes.hasKey("key1");
 assertEquals(expectedResult, actualResult);
 }

 // Test case for hasKey() method with non-existent key
 @Test
 public void testHasKeyNotFound() {
 Attributes attributes = new Attributes();
 boolean expectedResult = false;
 boolean actualResult = attributes.hasKey("nonExistentKey");
 assertEquals(expectedResult, actualResult);
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 boolean expectedResult = true;
 boolean actualResult = attributes.hasKeyIgnoreCase("key1");
 assertEquals(expectedResult, actualResult);
 }

 // Test case for hasKeyIgnoreCase() method with non-existent key
 @Test
 public void testHasKeyIgnoreCaseNotFound() {
 Attributes attributes = new Attributes();
 boolean expectedResult = false;
 boolean actualResult = attributes.hasKeyIgnoreCase("nonExistentKey");
 assertEquals(expectedResult, actualResult);
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 int expectedSize = 1;
 int actualSize = attributes.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes1.put(key1, value1);
 String key2 = "key2";
 String value2 = "value2";
 attributes2.put(key2, value2);
 attributes1.addAll(attributes2);
 int expectedSize = 2;
 int actualSize = attributes1.size();
 assertEquals(expectedSize, actualSize);
 String expectedValue1 = value1;
 String actualValue1 = attributes1.get(key1);
 assertEquals(expectedValue1, actualValue1);
 String expectedValue2 = value2;
 String actualValue2 = attributes1.get(key2);
 assertEquals(expectedValue2, actualValue2);
 }

 // Test case for remove() method with existing key
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 int expectedSize = 0;
 attributes.remove(key1);
 int actualSize = attributes.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNotFound() {
 Attributes attributes = new Attributes();
 int expectedSize = 0;
 attributes.remove("nonExistentKey");
 int actualSize = attributes.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test case for removeIgnoreCase() method with existing key
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.putIgnoreCase(key1, value1);
 int expectedSize = 0;
 attributes.removeIgnoreCase("key1");
 int actualSize = attributes.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test case for removeIgnoreCase() method with non-existent key
 @Test
 public void testRemoveIgnoreCaseNotFound() {
 Attributes attributes = new Attributes();
 int expectedSize = 0;
 attributes.removeIgnoreCase("nonExistentKey");
 int actualSize = attributes.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test case for clone() method
 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 Attributes clonedAttributes = attributes.clone();
 int expectedSize = 1;
 int actualSize = clonedAttributes.size();
 assertEquals(expectedSize, actualSize);
 String expectedValue = value1;
 String actualValue = clonedAttributes.get(key1);
 assertEquals(expectedValue, actualValue);
 }
}