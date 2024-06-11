import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

 private Attributes createAttributes() {
 return new Attributes().put("key1", "value1").put("key2", "value2").put("key3", "value3");
 }

 @Test
 public void testGet() {
 Attributes attributes = createAttributes();
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 Assert.assertEquals("value3", attributes.get("key3"));
 Assert.assertEquals(Attributes.EmptyString, attributes.get("nonexistentkey"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = createAttributes();
 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
 Assert.assertEquals("value3", attributes.getIgnoreCase("key3"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("nonexistentkey"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 Assert.assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key1", "value1");
 attributes.putIgnoreCase("KEY2", "value2");
 Assert.assertEquals("value2", attributes.get("KEY2"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assert.assertFalse(attributes.get("key").equals(Attributes.EmptyString));
 Assert.assertNull(attributes.get("key").getValue());
 }

 @Test
 public void testPutAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Attributes attributes = new Attributes();
 attributes.put(attribute);
 Assert.assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = createAttributes();
 attributes.remove("key1");
 Assert.assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.removeIgnoreCase("Key1");
 Assert.assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = createAttributes();
 Assert.assertTrue(attributes.hasKey("key1"));
 Assert.assertTrue(attributes.hasKey("key2"));
 Assert.assertFalse(attributes.hasKey("nonexistentkey"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = createAttributes();
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
 }

 @Test
 public void testSize() {
 Attributes attributes = createAttributes();
 Assert.assertEquals(3, attributes.size());
 }

 @Test
 public void testEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = createAttributes();
 Attributes attributes2 = new Attributes().put("key4", "value4");
 attributes1.addAll(attributes2);
 Assert.assertEquals(4, attributes1.size());
 Assert.assertTrue(attributes1.hasKey("key1"));
 Assert.assertTrue(attributes1.hasKey("key2"));
 Assert.assertTrue(attributes1.hasKey("key3"));
 Assert.assertTrue(attributes1.hasKey("key4"));
 }

 @Test
 public void testIterator() {
 Attributes attributes = createAttributes();
 List<Attribute> attributesList = attributes.asList();
 for (Attribute attribute : attributesList) {
 System.out.println(attribute.getKey() + ":" + attribute.getValue());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = createAttributes();
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(2, dataset.size());
 Assert.assertEquals("value1", dataset.get("key1"));
 Assert.assertEquals("value2", dataset.get("key2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = createAttributes();
 String html = attributes.html();
 Assert.assertEquals("<key1>value1</key1><key2>value2</key2><key3>value3</key3>", html);
 }

}