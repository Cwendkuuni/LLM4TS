import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

 // Test constructor with valid arguments
 @Test
 public void testAttributeConstruction() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value, null);
assertEquals("class", attribute.getKey());
assertEquals("active", attribute.getValue());
 }

 // Test constructor with null argument
 @Test(expected = NullPointerException.class)
 public void testAttributeConstructionWithNullKey() {
 String key = null;
 String value = "value";
 new Attribute(key, value, null);
 }

 // Test constructor with empty argument
 @Test(expected = IllegalArgumentException.class)
 public void testAttributeConstructionWithEmptyKey() {
 String key = "";
 String value = "value";
 new Attribute(key, value, null);
 }

 // Test constructor with invalid argument
 @Test(expected = IllegalArgumentException.class)
 public void testAttributeConstructionWithInvalidKey() {
 String key = "&%$!";
 String value = "value";
 new Attribute(key, value, null);
 }

 // Test setKey method
 @Test
 public void testSetKeyMethod() {
 Attribute attribute = new Attribute("class", "active", null);
 attribute.setKey("id");
 assertEquals("id", attribute.getKey());
 }

 // Test setKey method with null argument
 @Test(expected = NullPointerException.class)
 public void testSetKeyMethodWithNullKey() {
 Attribute attribute = new Attribute("class", "active", null);
 attribute.setKey(null);
 }

 // Test setKey method with empty argument
 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyMethodWithEmptyKey() {
 Attribute attribute = new Attribute("class", "active", null);
 attribute.setKey("");
 }

 // Test setKey method with same key but different case
 @Test
 public void testSetKeyMethodWithSameKeyButDifferentCase() {
 Attribute attribute = new Attribute("class", "active", null);
 attribute.setKey("Class");
 assertEquals("Class", attribute.getKey());
 }

 // Test setKey method with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyMethodWithInvalidKey() {
 Attribute attribute = new Attribute("class", "active", null);
 attribute.setKey("&%$!");
 }

 // Test getKey method
 @Test
 public void testGetKeyMethod() {
 Attribute attribute = new Attribute("class", "active", null);
 assertEquals("class", attribute.getKey());
 }

 // Test getValue method
 @Test
 public void testGetValueMethod() {
 Attribute attribute = new Attribute("class", "active", null);
 assertEquals("active", attribute.getValue());
 }

 // Test setValue method
 @Test
 public void testSetValueMethod() {
 Attribute attribute = new Attribute("class", "active", null);
 String newValue = "newValue";
 assertEquals("active", attribute.getValue());
 attribute.setValue(newValue);
 assertEquals("newValue", attribute.getValue());
 }

 // Test setValue method with null argument
 @Test(expected = NullPointerException.class)
 public void testSetValueMethodWithNullValue() {
 Attribute attribute = new Attribute("class", "active", null);
 String newValue = null;
 assertEquals("active", attribute.getValue());
 attribute.setValue(newValue);
 }

 // Test setValue method with empty value
 @Test(expected = IllegalArgumentException.class)
 public void testSetValueMethodWithEmptyValue() {
 Attribute attribute = new Attribute("class", "active", null);
 String newValue = "";
 assertEquals("active", attribute.getValue());
 attribute.setValue(newValue);
 }

 // Test html method
 @Test
 public void testHtmlMethod() throws IOException {
 Attribute attribute = new Attribute("class", "active", null);
 StringBuilder expectedOutput = new StringBuilder();
 attribute.html(expectedOutput, null);
 assertEquals("class=\"active\"", expectedOutput.toString());
 }

 // Test html method with null input
 @Test(expected = NullPointerException.class)
 public void testHtmlMethodWithNullAccumulator() {
 Attribute attribute = new Attribute("class", "active", null);
 StringBuilder accumulator = null;
 attribute.html(accumulator, null);
 }

 // Test html method with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testHtmlMethodWithEmptyAccumulator() {
 Attribute attribute = new Attribute("class", "active", null);
 StringBuilder accumulator = new StringBuilder();
 attribute.html(accumulator, null);
 }

 // Test html method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testHtmlMethodWithInvalidAccumulator() {
 Attribute attribute = new Attribute("class", "active", null);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 attribute.html(outputStream, null);
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("class", "active", null);
 assertEquals("class=\"active\"", attribute.toString());
 }

 // Test createFromEncoded method
 @Test
 public void testCreateFromEncodedMethod() {
 String unencodedKey = "class";
 String encodedValue = "class=\"active\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 assertEquals("class", attribute.getKey());
 assertEquals("active", attribute.getValue());
 }

 // Test isDataAttribute method
 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("id", "value", null);
 assertFalse(attribute.isDataAttribute());
 
 Attribute dataAttribute = new Attribute("data-test", "value", null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 // Test shouldCollapseAttribute method
 @Test
 public void testShouldCollapseAttributeMethod() throws IOException {
 Attribute attribute = new Attribute("checked", null, null);
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 
 attribute.setValue("checked");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 // Test shouldCollapseAttribute method with null output settings
 @Test(expected = NullPointerException.class)
 public void testShouldCollapseAttributeMethodWithNullOutputSettings() {
 Attribute attribute = new Attribute("checked", null, null);
 boolean result = attribute.shouldCollapseAttribute(null);
 }

 // Test shouldCollapseAttribute method with empty output settings
 @Test(expected = IllegalArgumentException.class)
 public void testShouldCollapseAttributeMethodWithEmptyOutputSettings() {
 Attribute attribute = new Attribute("checked", null, null);
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.HTML_PARTIAL);
 boolean result = attribute.shouldCollapseAttribute(outputSettings);
 }

 // Test isBooleanAttribute method
 @Test
 public void testIsBooleanAttributeMethod() {
 Attribute attribute = new Attribute("class", null, null);
 assertFalse(attribute.isBooleanAttribute());
 
 Attribute booleanAttribute = new Attribute("checked", null, null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 // Test equals method
 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("class", "active", null);
 Attribute attribute2 = new Attribute("class", "active", null);
 Attribute attribute3 = new Attribute("id", "value", null);
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 // Test hashCode method
 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("class", "active", null);
 Attribute attribute2 = new Attribute("class", "active", null);
 Attribute attribute3 = new Attribute("id", "value", null);
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 // Test clone method
 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("class", "active", null);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}