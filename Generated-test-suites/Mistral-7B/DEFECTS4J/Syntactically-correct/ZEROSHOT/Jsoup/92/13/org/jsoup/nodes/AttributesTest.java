import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
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
 attributes.put("key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 assertEquals("Value2", attributes.getIgnoreCase("key2"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertNull(attributes.get("key"));
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.removeIgnoreCase("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
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
 assertTrue(attributes3.hasKey("key1"));
 assertTrue(attributes3.hasKey("key2"));
 }

 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 assertEquals(2, list.size());
 assertTrue(list.contains(new Attribute("key1", "value1")));
 assertTrue(list.contains(new Attribute("key2", "value2")));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertTrue(dataset.containsKey("data-key1"));
 assertTrue(dataset.containsValue("value1"));
 assertTrue(dataset.containsKey("data-key2"));
 assertTrue(dataset.containsValue("value2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals(" key=\"value\"", attributes.html());
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("key=\"value\"", attributes.toString());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");attributes1.put("key2", "value2");Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");attributes2.put("key2", "value2");Attributes attributes3 = new Attributes();attributes3.put("key2", "value2");attributes3.put("key1", "value1");Attributes attributes4 = new Attributes();assertEquals(attributes1, attributes1);assertEquals(attributes1, attributes2);assertEquals(attributes1, attributes3);assertNotEquals(attributes1, attributes4);
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");attributes1.put("key2", "value2");Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");attributes2.put("key2", "value2");Attributes attributes3 = new Attributes();attributes3.put("key2", "value2");attributes3.put("key1", "value1");int hashCode1 = attributes1.hashCode();int hashCode2 = attributes2.hashCode();int hashCode3 = attributes3.hashCode();assertEquals(hashCode1, hashCode2);assertEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");Attributes clonedAttributes = attributes.clone();assertEquals(attributes, clonedAttributes);
 }
}