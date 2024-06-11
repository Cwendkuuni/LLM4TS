import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "Value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "Value";
 assertEquals(expected, actual);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase() method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 assertEquals(expected, actual);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assert.assertTrue(attributes.get("key").equals(""));
 attributes.put("key", false);
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test case for put() method with Attribute object
 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for remove() method
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test case for hasKey() method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 assertEquals(expected, actual);
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 Assert.assertEquals(attributes1.size(), attributes3.size());
 }

 // Test case for asList() method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> actual = attributes.asList();
 List<Attribute> expected = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

 // Test case for dataset() method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "data-value");
 Map<String, String> actual = attributes.dataset();
 Map<String, String> expected = new HashMap<>();
 expected.put("data-key", "data-value");
 Assert.assertEquals(expected, actual);
 }

 // Test case for html() method
 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder expected = new StringBuilder();
 expected.append(" key=\"value\"");
 try {
 attributes.html(expected, new Document.OutputSettings());
 } catch (IOException e) {
 e.printStackTrace();
 }
 String actual = attributes.html();
 assertEquals(expected.toString(), actual);
 }

 // Test case for toString() method
 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.toString();
 String expected = "key=\"value\"";
 assertEquals(expected, actual);
 }

 // Test case for equals() method
 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 Attributes attributes3 = new Attributes();
 assertFalse(attributes1.equals(attributes3));
 }

 // Test case for hashCode() method
 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 int actual1 = attributes1.hashCode();
 int actual2 = attributes2.hashCode();
 int expected = attributes1.size() + 31 * attributes1.size() + Arrays.hashCode(attributes1.keys) + Arrays.hashCode(attributes1.vals);
 int expected2 = attributes2.size() + 31 * attributes2.size() + Arrays.hashCode(attributes2.keys) + Arrays.hashCode(attributes2.vals);
 assertEquals(expected, actual1);
 assertEquals(expected2, actual2);
 }

 // Test case for clone() method
 @Test
 public void testClone() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 }
}