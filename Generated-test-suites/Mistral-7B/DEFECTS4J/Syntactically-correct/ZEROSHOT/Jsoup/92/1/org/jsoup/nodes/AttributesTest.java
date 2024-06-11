import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 Attributes attributes = new Attributes();
 assertNull(attributes.get(null));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 assertNull(attributes.get("key"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutOverwrite() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertNull(attributes.get("key"));
 }

 @Test
 public void testPutBooleanRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 attributes.put("key", false);
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testPutIgnoreCaseBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("Key", true);
 attributes.putIgnoreCase("key", false);
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testHasKeyIgnoreCaseFalse() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testIsEmpty() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertEquals(2, attributes3.size());
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> entryList = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes) {
 entryList.add(entry);
 }
 assertEquals(2, entryList.size());
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes(); attributes.put("key", "value"); String html = attributes.html(); assertEquals("<key>value</key>", html); }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes(); attributes.put("key", "value"); assertEquals("<key>value</key>", attributes.toString()); }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes(); attributes1.put("key1", "value1"); Attributes attributes2 = new Attributes(); attributes2.put("key1", "value1"); assertTrue(attributes1.equals(attributes2)); }

 @Test
 public void testEqualsFalse() {
 Attributes attributes1 = new Attributes(); attributes1.put("key1", "value1"); Attributes attributes2 = new Attributes(); attributes2.put("key2", "value2"); assertFalse(attributes1.equals(attributes2)); }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes(); attributes1.put("key1", "value1"); Attributes attributes2 = new Attributes(); attributes2.put("key1", "value1"); int hashcode1 = attributes1.hashCode(); int hashcode2 = attributes2.hashCode(); assertEquals(hashcode1, hashcode2); }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes(); attributes.put("key1", "value1"); Attributes clonedAttributes = attributes.clone(); assertEquals(attributes.toString(), clonedAttributes.toString()); }

 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes(); attributes.put("Key", "Value"); attributes.normalize(); assertEquals("key", attributes.get("key")); }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes(); attributes.put("key1", "value1"); attributes.remove("key1"); assertFalse(attributes.hasKey("key1")); }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes(); attributes.put("Key", "Value"); attributes.removeIgnoreCase("key"); assertFalse(attributes.hasKey("key")); }
}