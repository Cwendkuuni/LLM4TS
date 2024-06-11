import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.jsoup.nodes.Attributes;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttributesTest {

 @BeforeAll
 static void setupBeforeAll() {
 // Initialize any static variables or setup any external resources here
 }

 @AfterAll
 static void teardownAfterAll() {
 // Cleanup any external resources here
 }

 @BeforeEach
 void setupBeforeEach() {
 // Initialize any instance variables here
 Attributes attributes = new Attributes();
 }

 @AfterEach
 void teardownAfterEach() {
 // Cleanup any instance variables here
 }

 /**
 * Test basic functionality of get method
 */
 @Test
 void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.get("key1"));
 }

 /**
 * Test basic functionality of getIgnoreCase method
 */
 @Test
 void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.getIgnoreCase("key1"));
 assertEquals("value1", attributes.getIgnoreCase("KEY1"));
 }

 /**
 * Test functionality of put method
 */
 @Test
 void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 /**
 * Test functionality of putIgnoreCase method
 */
 @Test
 void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.putIgnoreCase("Key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 /**
 * Test functionality of put method for boolean attributes
 */
 @Test
 void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key1", true);
 assertTrue(attributes.hasKey("key1"));
 assertEquals("", attributes.get("key1"));
 attributes.put("key1", false);
 assertFalse(attributes.hasKey("key1"));
 }

 /**
 * Test functionality of put method for removing boolean attributes
 */
 @Test
 void testPutBooleanRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", true);
 assertTrue(attributes.hasKey("key1"));
 assertEquals("", attributes.get("key1"));
 attributes.put("key1", false);
 assertFalse(attributes.hasKey("key1"));
 attributes.put("key1", true);
 assertTrue(attributes.hasKey("key1"));
 }

 /**
 * Test functionality of remove method
 */
 @Test
 void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 /**
 * Test functionality of removeIgnoreCase method
 */
 @Test
 void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.removeIgnoreCase("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 /**
 * Test functionality of hasKey method
 */
 @Test
 void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 /**
 * Test functionality of hasKeyIgnoreCase method
 */
 @Test
 void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 }

 /**
 * Test functionality of size method
 */
 @Test
 void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertEquals(1, attributes.size());
 }

 /**
 * Test functionality of addAll method
 */
 @Test
 void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertEquals(2, attributes3.size());
 assertTrue(attributes3.hasKey("key1"));
 assertTrue(attributes3.hasKey("key2"));
 }

 /**
 * Test functionality of iterator method
 */
 @Test
 void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Iterator<Attribute> iterator = attributes.iterator();
 assertTrue(iterator.hasNext());
 Attribute attribute = iterator.next();
 assertEquals("key1", attribute.getKey());
 assertEquals("value1", attribute.getValue());
 }

 /**
 * Test functionality of asList method
 */
 @Test
 void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 List<Attribute> list = attributes.asList();
 assertEquals(1, list.size());
 assertTrue(list.get(0).isDataAttribute());
 }

 /**
 * Test edge case for get method with empty attributes
 */
 @Test
 void testGetEmpty() {
 Attributes attributes = new Attributes();
 assertEquals("", attributes.get("key1"));
 }

 /**
 * Test edge case for getIgnoreCase method with empty attributes
 */
 @Test
 void testGetIgnoreCaseEmpty() {
 Attributes attributes = new Attributes();
 assertEquals("", attributes.getIgnoreCase("key1"));
 }

 /**
 * Test edge case for put method with empty key
 */
 @Test
 void testPutEmptyKey() {
 Attributes attributes = new Attributes();
 attributes.put("", "value1");
 assertEquals("", attributes.get(""));
 }

 /**
 * Test edge case for put method with null value
 */
 @Test
 void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key1", null);
 assertEquals("", attributes.get("key1"));
 }

 /**
 * Test edge case for put method with duplicate key
 */
 @Test
 void testPutDuplicateKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 /**
 * Test edge case for remove method with non-existent key
 */
 @Test
 void testRemoveNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.remove("key1"));
 }

 /**
 * Test edge case for remove method with null key
 */
 @Test
 void testRemoveNullKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.remove(null));
 }

 /**
 * Test edge case for removeIgnoreCase method with non-existent key
 */
 @Test
 void testRemoveIgnoreCaseNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.removeIgnoreCase("key1"));
 }

 /**
 * Test edge case for removeIgnoreCase method with null key
 */
 @Test
 void testRemoveIgnoreCaseNullKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.removeIgnoreCase(null));
 }

 /**
 * Test edge case for hasKey method with null key
 */
 @Test
 void testHasKeyNullKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey(null));
 }

 /**
 * Test edge case for hasKeyIgnoreCase method with null key
 */
 @Test
 void testHasKeyIgnoreCaseNullKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase(null));
 }

 /**
 * Test edge case for size method with empty attributes
 */
 @Test
 void testSizeEmpty() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 /**
 * Test edge case for addAll method with empty attributes to add
 */
 @Test
 void testAddAllEmpty() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes2.addAll(attributes1);
 assertEquals(0, attributes2.size());
 }

 /**
 * Test exception handling for get method with null attributes
 */
 @Test
 void testGetNullAttributes() {
 assertThrows(NullPointerException.class, () -> new Attributes().get("key1"));
 }

 /**
 * Test exception handling for getIgnoreCase method with null attributes
 */
 @Test
 void testGetIgnoreCaseNullAttributes() {
 assertThrows(NullPointerException.class, () -> new Attributes().getIgnoreCase("key1"));
 }

 /**
 * Test exception handling for put method with null key and non-null value
 */
 @Test
 void testPutNullKeyNonNullValue() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.put(null, "value1"));
 }

 /**
 * Test exception handling for put method with null key and null value
 */
 @Test
 void testPutNullKeyNullValue() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.put(null, null));
 }

 /**
 * Test exception handling for put method with non-null key and null value
 */
 @Test
 void testPutNonnullKeyNullValue() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.put("key1", null));
 }

 /**
 * Test exception handling for put method with duplicate non-null key
 */
 @Test
 void testPutDuplicateNonnullKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertThrows(IllegalArgumentException.class, () -> attributes.put("key1", "value2"));
 }

 /**
 * Test exception handling for remove method with null key
 */
 @Test
 void testRemoveNullKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.remove(null));
 }

 /**
 * Test exception handling for remove method with non-existent key
 */
 @Test
 void testRemoveNonExistentKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(UnsupportedOperationException.class, () -> attributes.remove("key1"));
 }

 /**
 * Test exception handling for removeIgnoreCase method with null key
 */
 @Test
 void testRemoveIgnoreCaseNullKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.removeIgnoreCase(null));
 }

 /**
 * Test exception handling for removeIgnoreCase method with non-existent key
 */
 @Test
 void testRemoveIgnoreCaseNonExistentKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(UnsupportedOperationException.class, () -> attributes.removeIgnoreCase("key1"));
 }

 /**
 * Test exception handling for hasKey method with null key
 */
 @Test
 void testHasKeyNullKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.hasKey(null));
 }

 /**
 * Test exception handling for hasKeyIgnoreCase method with null key
 */
 @Test
 void testHasKeyIgnoreCaseNullKeyException() {
 Attributes attributes = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes.hasKeyIgnoreCase(null));
 }

 /**
 * Test exception handling for size method with null attributes
 */
 @Test
 void testSizeNullAttributes() {
 assertThrows(NullPointerException.class, () -> new Attributes().size());
 }

 /**
 * Test exception handling for addAll method with null attributes to add
 */
 @Test
 void testAddAllNull() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 assertThrows(NullPointerException.class, () -> attributes2.addAll(attributes1));
 }
}