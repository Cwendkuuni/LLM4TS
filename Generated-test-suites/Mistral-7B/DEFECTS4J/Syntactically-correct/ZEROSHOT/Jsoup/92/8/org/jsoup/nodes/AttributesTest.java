import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();

 // Test setting and getting attributes
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.get("key1"));

 // Test setting and getting case-insensitive attributes
 attributes.putIgnoreCase("KEY1", "value2");
 assertEquals("value2", attributes.getIgnoreCase("KEY1"));

 // Test setting and getting boolean attributes
 attributes.put("booleanKey", true);
 assertTrue(attributes.hasKey("booleanKey"));
 assertFalse(attributes.get("booleanKey").isEmpty());
 assertFalse(attributes.get("booleanKey").equals("true"));

 // Test removing attributes
 attributes.put("key1", "value1");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));

 // Test removing case-insensitive attributes
 attributes.putIgnoreCase("KEY1", "value1");
 attributes.removeIgnoreCase("KEY1");
 assertFalse(attributes.hasKeyIgnoreCase("KEY1"));

 // Test getting non-existent attributes
 assertEquals(Attributes.EMPTY_STRING, attributes.get("nonExistentKey"));

 // Test checking for existence of non-existent attributes
 assertFalse(attributes.hasKey("nonExistentKey"));
 assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));

 // Test adding multiple attributes
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes2.put("key3", "value3");
 attributes.addAll(attributes2);
 assertEquals(3, attributes.size());

 // Test iterator
 List<Attribute> attributesList = attributes.asList();
 for (Attribute attribute : attributesList) {
 System.out.println(attribute.getKey() + "=" + attribute.getValue());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals("value1", dataset.get("data-key1"));
 assertEquals("value2", dataset.get("data-key2"));
 }
}