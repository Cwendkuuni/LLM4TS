import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for constructor and basic functionality of get() method
 @Test
 public void testGetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of getIgnoreCase() method
 @Test
 public void testGetIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of put() method
 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of putIgnoreCase() method
 @Test
 public void testPutIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of put() method with boolean value
 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean actual = attributes.get("key").equals(Attributes.EmptyString);
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of remove() method
 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 String actual = attributes.get("key");
 String expected = Attributes.EmptyString;
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of hasKey() method
 @Test
 public void testHasKeyAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 boolean actual = attributes.hasKeyIgnoreCase("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and basic functionality of size() method
 @Test
 public void testSizeAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 Assert.assertEquals(expected, actual);
 }

 // Test case for constructor and adding all attributes from another set
 @Test
 public void testAddAllAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes result = new Attributes();
 result.addAll(attributes1);
 result.addAll(attributes2);
 String actualKey1 = result.get("key1");
 String expectedValue1 = "value1";
 String actualKey2 = result.get("key2");
 String expectedValue2 = "value2";
 String actualKey3 = result.get("key3"); // non-existing key
 String expectedEmptyString = Attributes.EmptyString;
 Assert.assertEquals(expectedValue1, actualKey1);
 Assert.assertEquals(expectedValue2, actualKey2);
 Assert.assertEquals(expectedEmptyString, actualKey3);
 }

 // Test case for constructor and dataset() method
 @Test
 public void testDatasetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 String actualKey1 = dataset.get("key1");
 String expectedValue1 = "value1";
 String actualKey2 = dataset.get("key2");
 String expectedValue2 = "value2";
 Assert.assertEquals(expectedValue1, actualKey1);
 Assert.assertEquals(expectedValue2, actualKey2);
 }

 // Test case for constructor and html() method
 @Test
 public void testHtmlAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append(" key1=\"value1\" key2=\"value2\" ");
 String actualHtml = attributes.html();
 Assert.assertEquals(expectedHtml.toString(), actualHtml);
 }

 // Test case for constructor and equals() method
 @Test
 public void testEqualsAttribute() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");
 boolean actual12 = attributes1.equals(attributes2);
 boolean expected12 = true;
 boolean actual13 = attributes1.equals(attributes3);
 boolean expected13 = false;
 Assert.assertEquals(expected12, actual12);
 Assert.assertEquals(expected13, actual13);
 }

 // Test case for constructor and clone() method
 @Test
 public void testCloneAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 String actualKey1 = clonedAttributes.get("key1");
 String expectedValue1 = "value1";
 Assert.assertEquals(expectedValue1, actualKey1);
 }
}