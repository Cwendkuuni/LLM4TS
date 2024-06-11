import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test constructor
 @Test
 public void testAttributesConstructor() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 // Test get method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 assertEquals("testValue", attributes.get("testKey"));
 }

 // Test getIgnoreCase method
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 assertEquals("testValue", attributes.getIgnoreCase("testKey"));
 assertEquals("testValue", attributes.getIgnoreCase("TestKey"));
 }

 // Test put method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 assertEquals(2, attributes.size());
 }

 // Test putIgnoreCase method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("TestKey1", "TestValue1");
 attributes.putIgnoreCase("testKey2", "testValue2");
 assertEquals(2, attributes.size());
 }

 // Test put method with existing key
 @Test
 public void testPutWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue1");
 attributes.put("testKey", "testValue2");
 assertEquals("testValue2", attributes.get("testKey"));
 }

 // Test putIgnoreCase method with existing key
 @Test
 public void testPutIgnoreCaseWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("TestKey", "TestValue1");
 attributes.putIgnoreCase("testKey", "testValue2");
 assertEquals("testValue2", attributes.get("testKey"));
 }

 // Test put boolean attribute
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", true);
 assertEquals(true, attributes.getBoolean("testKey"));
 }

 // Test put boolean attribute with existing key
 @Test
 public void testPutBooleanWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", true);
 attributes.put("testKey", false);
 assertEquals(false, attributes.getBoolean("testKey"));
 }

 // Test remove method
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 attributes.remove("testKey1");
 assertEquals("testValue2", attributes.get("testKey2"));
 assertEquals(1, attributes.size());
 }

 // Test removeIgnoreCase method
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("testKey1", "testValue1");
 attributes.putIgnoreCase("testKey2", "testValue2");
 attributes.removeIgnoreCase("testKey1");
 assertEquals("testValue2", attributes.get("testKey2"));
 assertEquals(1, attributes.size());
 }

 // Test remove method with non-existent key
 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNonExistentKey() {
 Attributes attributes = new Attributes();
 attributes.remove("testKey");
 }

 // Test removeIgnoreCase method with non-existent key
 @Test(expected = IllegalArgumentException.class)
 public void testRemoveIgnoreCaseWithNonExistentKey() {
 Attributes attributes = new Attributes();
 attributes.removeIgnoreCase("testKey");
 }

 // Test hasKey method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 assertTrue(attributes.hasKey("testKey"));
 }

 // Test hasKeyIgnoreCase method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 assertTrue(attributes.hasKeyIgnoreCase("testKey"));
 assertTrue(attributes.hasKeyIgnoreCase("TestKey"));
 }

 // Test hasKey method with non-existent key
 @Test
 public void testHasKeyWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("testKey"));
 }

 // Test hasKeyIgnoreCase method with non-existent key
 @Test
 public void testHasKeyIgnoreCaseWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("testKey"));
 }

 // Test size method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 assertEquals(2, attributes.size());
 }

 // Test addAll method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("testKey1", "testValue1");
 attributes2.put("testKey2", "testValue2");
 attributes1.addAll(attributes2);
 assertEquals(2, attributes1.size());
 assertEquals("testValue2", attributes1.get("testKey2"));
 }

 // Test addAll method with empty set
 @Test
 public void testAddAllWithEmptySet() {
 Attributes attributes = new Attributes();
 attributes.addAll(new Attributes());
 assertEquals(0, attributes.size());
 }

 // Test iterator method
 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 Iterator<Attribute> iterator = attributes.iterator();
 Attribute attribute = iterator.next();
 assertEquals("testKey1", attribute.getKey());
 assertEquals("testValue1", attribute.getValue());
 attribute = iterator.next();
 assertEquals("testKey2", attribute.getKey());
 assertEquals("testValue2", attribute.getValue());
 }

 // Test asList method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 List<Attribute> attributeList = attributes.asList();
 assertEquals(2, attributeList.size());
 Attribute attribute = attributeList.get(0);
 assertEquals("testKey1", attribute.getKey());
 assertEquals("testValue1", attribute.getValue());
 attribute = attributeList.get(1);
 assertEquals("testKey2", attribute.getKey());
 assertEquals("testValue2", attribute.getValue());
 }

 // Test dataset method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("testDataKey1", "testDataValue1");
 attributes.put("testKey", "testValue");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(1, dataset.size());
 assertEquals("testDataValue1", dataset.get("testDataKey1"));
 }

 // Test html method
 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 StringBuilder sb = new StringBuilder();
 attributes.html(sb, null);
 String html = sb.toString();
 assertTrue(html.contains("testKey1"));
 assertTrue(html.contains("testValue1"));
 assertTrue(html.contains("testKey2"));
 assertTrue(html.contains("testValue2"));
 }

 // Test equals method
 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("testKey1", "testValue1");
 attributes2.put("testKey1", "testValue1");
 assertTrue(attributes1.equals(attributes2));
 attributes2.put("testKey2", "testValue2");
 assertFalse(attributes1.equals(attributes2));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("testKey1", "testValue1");
 attributes2.put("testKey1", "testValue1");
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 attributes2.put("testKey2", "testValue2");
 assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test clone method
 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 }
}