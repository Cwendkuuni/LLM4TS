import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

 // Test case for get method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase method
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for put method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for put method with boolean value
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean actual = attributes.getBoolean("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for put method with existing key
 @Test
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 Assert.assertEquals(expected, actual);
 }

 // Test case for size method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 Assert.assertEquals(expected, actual);
 }

 // Test case for addAll method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 String actual1 = attributes1.get("key1");
 String expected1 = "value1";
 String actual2 = attributes1.get("key2");
 String expected2 = "value2";
 Assert.assertEquals(expected1, actual1);
 Assert.assertEquals(expected2, actual2);
 }

 // Test case for remove method
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 String actual = attributes.get("key");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 // Test case for removeIgnoreCase method
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 attributes.removeIgnoreCase("key");
 String actual = attributes.get("key");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 // Test case for hasKey method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for hasKeyIgnoreCase method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 boolean actual = attributes.hasKeyIgnoreCase("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for dataset method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "data-value");
 Map<String, String> actual = attributes.dataset();
 Map<String, String> expected = new HashMap<>();
 expected.put("data-key", "data-value");
 Assert.assertEquals(expected, actual);
 }

 // Test case for html method
 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.html();
 String expected = " key=\"value\"";
 Assert.assertEquals(expected, actual);
 }

 // Test case for equals method
 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 boolean actual = attributes1.equals(attributes2);
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for clone method
 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Attributes clonedAttributes = attributes.clone();
 String actual = clonedAttributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for normalize method
 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 attributes.normalize();
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for getInt method (assuming getInt method is present in the class)
 @Test
 public void testGetInt() {
 Attributes attributes = new Attributes();
 attributes.put("key", "10");
 int actual = attributes.getInt("key");
 int expected = 10;
 Assert.assertEquals(expected, actual);
 }

 // Test case for getInt method with non-numeric value
 @Test (expected = NumberFormatException.class)
 public void testGetIntNonNumeric() {
 Attributes attributes = new Attributes();
 attributes.put("key", "notANumber");
 int actual = attributes.getInt("key");
 }

 // Test case for getBoolean method (assuming getBoolean method is present in the class)
 @Test
 public void testGetBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", "true");
 boolean actual = attributes.getBoolean("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for getBoolean method with non-boolean value
 @Test (expected = IllegalArgumentException.class)
 public void testGetBooleanNonBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", "notABoolean");
 boolean actual = attributes.getBoolean("key");
 }

 // Test case for getBoolean method with null value
 @Test (expected = NullPointerException.class)
 public void testGetBooleanNull() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 boolean actual = attributes.getBoolean("key");
 }
}