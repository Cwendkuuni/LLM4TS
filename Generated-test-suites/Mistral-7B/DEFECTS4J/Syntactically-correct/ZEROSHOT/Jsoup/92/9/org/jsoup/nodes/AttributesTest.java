import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGetEmpty() {
 Attributes attributes = new Attributes();
 assertEquals("", attributes.get(""));
 }

 @Test
 public void testGetSet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("KEY2", "value2");
 assertEquals("value1", attributes.get("Key1"));
 assertEquals("value2", attributes.get("KEY2"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertFalse(attributes.get("key").isEmpty());
 }

 @Test
 public void testPutIgnoreCaseBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("Key", true);
 assertFalse(attributes.get("Key").isEmpty());
 }

 @Test
 public void testPutWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testPutWithExistingKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("Key1"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testEmpty() {
 Attributes attributes = new Attributes();
 assertTrue(attributes.isEmpty());
 attributes.put("key1", "value1");
 assertFalse(attributes.isEmpty());
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey(""));
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase(""));
 attributes.put("Key1", "value1");
 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.removeIgnoreCase("Key1");
 assertFalse(attributes.hasKey("Key1"));
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> list = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes) {
 list.add(entry);
 }
 assertEquals(2, list.size());
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
 Attributes attributes = new Attributes();attributes.put("key1", "value1");attributes.put("key2", "value2");String html = attributes.html();assertTrue(html.contains("key1=\"value1\""));assertTrue(html.contains("key2=\"value2\"")); }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");attributes.put("key2", "value2");assertEquals(attributes.toString(), attributes.html()); }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");Attributes clonedAttributes = attributes.clone();assertEquals(attributes.toString(), clonedAttributes.toString()); }
}