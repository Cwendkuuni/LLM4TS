import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 // Test case for get() method with a valid key
 @Test
 public void testGetValidKey() {
 Attributes attributes = new Attributes().put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for get() method with an invalid key
 @Test
 public void testGetInvalidKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("invalidKey");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for getIgnoreCase() method with a valid key
 @Test
 public void testGetIgnoreCaseValidKey() {
 Attributes attributes = new Attributes().put("Key", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method with an invalid key
 @Test
 public void testGetIgnoreCaseInvalidKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.getIgnoreCase("InvalidKey");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for put() method with a valid key and value
 @Test
 public void testPutValidKeyValue() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 String actual = attributes.get(key);
 String expected = value;
 assertEquals(expected, actual);
 }

 // Test case for put() method with a valid key and null value (boolean attribute)
 @Test
 public void testPutValidKeyNullValue() {
 Attributes attributes = new Attributes();
 String key = "key";
 attributes.put(key, null);
 boolean actual = attributes.hasKey(key);
 assertTrue(actual);
 }

 // Test case for put() method with a duplicate key
 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKey() {
 Attributes attributes = new Attributes().put("key", "value1").put("key", "value2");
 }

 // Test case for putIgnoreCase() method with a valid key and value
 @Test
 public void testPutIgnoreCaseValidKeyValue() {
 Attributes attributes = new Attributes();
 String key = "Key";
 String value = "value";
 attributes.putIgnoreCase(key, value);
 String actual = attributes.get(key);
 String expected = value;
 assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase() method with a duplicate key (case changed)
 @Test
 public void testPutIgnoreCaseDuplicateKey() {
 Attributes attributes = new Attributes().putIgnoreCase("Key", "value1").putIgnoreCase("key", "value2");
 }

 // Test case for remove() method with a valid key
 @Test
 public void testRemoveValidKey() {
 Attributes attributes = new Attributes().put("key", "value");
 attributes.remove("key");
 boolean actual = attributes.hasKey("key");
 assertFalse(actual);
 }

 // Test case for remove() method with an invalid key
 @Test
 public void testRemoveInvalidKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("invalidKey");
 assertFalse(actual);
 attributes.remove("invalidKey");
 boolean actual2 = attributes.hasKey("invalidKey");
 assertFalse(actual2);
 }

 // Test case for hasKey() method with a valid key
 @Test
 public void testHasKeyValidKey() {
 Attributes attributes = new Attributes().put("key", "value");
 boolean actual = attributes.hasKey("key");
 assertTrue(actual);
 }

 // Test case for hasKey() method with an invalid key
 @Test
 public void testHasKeyInvalidKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("invalidKey");
 assertFalse(actual);
 }

 // Test case for hasKeyIgnoreCase() method with a valid key
 @Test
 public void testHasKeyIgnoreCaseValidKey() {
 Attributes attributes = new Attributes().put("Key", "value");
 boolean actual = attributes.hasKeyIgnoreCase("key");
 assertTrue(actual);
 }

 // Test case for hasKeyIgnoreCase() method with an invalid key
 @Test
 public void testHasKeyIgnoreCaseInvalidKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKeyIgnoreCase("InvalidKey");
 assertFalse(actual);
 }

 // Test case for size() method with an empty Attributes object
 @Test
 public void testSizeEmpty() {
 Attributes attributes = new Attributes();
 int actual = attributes.size();
 int expected = 0;
 assertEquals(expected, actual);
 }

 // Test case for size() method with a non-empty Attributes object
 @Test
 public void testSizeNonEmpty() {
 Attributes attributes = new Attributes().put("key", "value");
 int actual = attributes.size();
 int expected = 1;
 assertEquals(expected, actual);
 }

 // Test case for addAll() method with an empty Attributes object
 @Test
 public void testAddAllEmpty() {
 Attributes attributes = new Attributes();
 Attributes incoming = new Attributes().put("key1", "value1").put("key2", "value2");
 attributes.addAll(incoming);
 assertEquals(0, attributes.size());
 }

 // Test case for addAll() method with a non-empty Attributes object
 @Test
 public void testAddAllNonEmpty() {
 Attributes attributes = new Attributes().put("key1", "value1");
 Attributes incoming = new Attributes().put("key2", "value2").put("key3", "value3");
 attributes.addAll(incoming);
 int expectedSize = 3;
 assertEquals(expectedSize, attributes.size());
 }
}