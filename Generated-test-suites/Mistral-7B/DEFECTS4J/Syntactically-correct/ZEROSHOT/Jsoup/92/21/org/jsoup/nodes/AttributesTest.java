import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 @Test
 public void testGetAndPut() {
 Attributes attributes = new Attributes();
 assertEquals(EmptyString, attributes.get("attributeName"));
 attributes.put("attributeName", "attributeValue");
 assertEquals("attributeValue", attributes.get("attributeName"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("AttributeName", "attributeValue");
 assertEquals("attributeValue", attributes.get("AttributeName"));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("attributeName", true);
 assertFalse(attributes.get("attributeName").isEmpty());
 assertNull(attributes.get("attributeName").getValue());
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes().put("attributeName", "attributeValue");
 assertEquals("attributeValue", attributes.get("attributeName"));
 attributes.remove("attributeName");
 assertEquals(EmptyString, attributes.get("attributeName"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes().putIgnoreCase("AttributeName", "attributeValue");
 assertEquals("attributeValue", attributes.get("AttributeName"));
 attributes.removeIgnoreCase("AttributeName");
 assertEquals(EmptyString, attributes.get("AttributeName"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes().put("attributeName", "attributeValue");
 assertTrue(attributes.hasKey("attributeName"));
 assertFalse(attributes.hasKey("AttributeName"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes().putIgnoreCase("AttributeName", "attributeValue");
 assertTrue(attributes.hasKeyIgnoreCase("AttributeName"));
 assertFalse(attributes.hasKeyIgnoreCase("attributeName"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes().put("attribute1", "value1").put("attribute2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes().put("attribute1", "value1");
 Attributes attributes2 = new Attributes().put("attribute2", "value2").put("attribute3", "value3");
 attributes1.addAll(attributes2);
 assertEquals(3, attributes1.size());
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes().put("attribute1", "value1").put("attribute2", "value2");
 List<Map.Entry<String, String>> list = attributes.asList();
 for (Map.Entry<String, String> entry : list) {
 System.out.println(entry.getKey() + ": " + entry.getValue());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes().put("data-test", "testValue").put("data-test2", "testValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals("testValue", dataset.get("data-test"));
 assertEquals("testValue2", dataset.get("data-test2"));
 }
}