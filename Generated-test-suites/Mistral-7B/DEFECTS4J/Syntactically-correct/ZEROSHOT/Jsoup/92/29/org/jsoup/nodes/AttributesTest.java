import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

 @Test
 public void testAttributesEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testAttributesSingle() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testAttributesMultiple() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testAttributesCaseInsensitive() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
 }

 @Test
 public void testAttributesBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertFalse(attributes.get("key").isEmpty());
 }

 @Test
 public void testAttributesRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 Assert.assertEquals(1, attributes.size());
 Assert.assertFalse(attributes.hasKey("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testAttributesRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.removeIgnoreCase("Key1");
 Assert.assertEquals(1, attributes.size());
 Assert.assertFalse(attributes.hasKey("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testAttributesHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertTrue(attributes.hasKey("key1"));
 Assert.assertTrue(attributes.hasKey("Key1"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 Assert.assertFalse(attributes.hasKey("key3"));
 }

 @Test
 public void testAttributesEquals() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 Attributes attributes3 = new Attributes();
 attributes3.put("key2", "value1");
 Assert.assertEquals(attributes1, attributes2);
 Assert.assertNotEquals(attributes1, attributes3);
 }

 @Test
 public void testAttributesClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 Assert.assertEquals(attributes, clonedAttributes);
 }

 @Test
 public void testAttributesIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> entries = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
 entries.add(entry);
 }
 Assert.assertEquals(2, entries.size());
 Assert.assertEquals("key1", entries.get(0).getKey());
 Assert.assertEquals("value1", entries.get(0).getValue());
 Assert.assertEquals("key2", entries.get(1).getKey());
 Assert.assertEquals("value2", entries.get(1).getValue());
 }

 @Test
 public void testAttributesDataset() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("data-key3", "value3");
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(3, dataset.size());
 Assert.assertEquals("value1", dataset.get("key1"));
 Assert.assertEquals("value2", dataset.get("key2"));
 Assert.assertEquals("value3", dataset.get("data-key3"));
 }

}