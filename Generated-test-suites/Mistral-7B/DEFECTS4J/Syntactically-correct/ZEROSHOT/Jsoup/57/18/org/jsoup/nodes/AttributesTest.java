import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testGetEmpty() {
 assertEquals("", attributes.get(""));
 }

 @Test
 public void testGet() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCase() {
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPut() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testPutBoolean() {
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.get("key").equals("true"));
 }

 @Test
 public void testPutNull() {
 attributes.put(null, null);
 assertNull(attributes.get(null));
 }

 @Test
 public void testHasKey() {
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.hasKey("other"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testSize() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes other = new Attributes();
 other.put("otherKey", "otherValue");
 attributes.addAll(other);
 assertEquals(2, attributes.size());
 assertEquals("otherValue", attributes.getIgnoreCase("otherkey"));
 }

 @Test
 public void testRemove() {
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 attributes.put("Key", "Value");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testDataset() {
 attributes.put("data-key", "data-value");
 Map<String, String> dataset = attributes.dataset();
 assertEquals("data-value", dataset.get("key"));
 }

 @Test
 public void testHtml() {
 StringBuilder accum = new StringBuilder();
 String html = attributes.html();
 assertEquals(html, attributes.toString());
 }

 @Test
 public void testClone() {
 Attributes cloned = attributes.clone();
 assertEquals(attributes, cloned);
 }
}