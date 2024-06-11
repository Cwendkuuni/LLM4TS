import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 @Test
 public void testInitialization() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertNull(attributes.get(null));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
 assertEquals("value", attributes.getIgnoreCase("Key"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key1", "Value1");
 attributes.putIgnoreCase("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertEquals(1, attributes.size());
 assertFalse(attributes.get("key").isEmpty());
 }

 @Test
 public void testPutBooleanRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 attributes.put("key", false);
 assertEquals(0, attributes.size());
 }

 @Test
 public void testPutDuplicate() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.hasKey("otherKey"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("Key"));
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
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 for (Attribute attribute : list) {
 System.out.println(attribute.getKey() + "=" + attribute.getValue());
 }
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
 Attributes attributes = new Attributes();attributes.put("key", "value");String html = attributes.html();assertEquals("<key>value</key>", html); }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();attributes.put("key", "value");assertEquals("<key>value</key>", attributes.toString()); }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");
 assertEquals(attributes1.hashCode(), attributes1.hashCode());
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 assertNotEquals(attributes.hashCode(), clonedAttributes.hashCode());
 }

}