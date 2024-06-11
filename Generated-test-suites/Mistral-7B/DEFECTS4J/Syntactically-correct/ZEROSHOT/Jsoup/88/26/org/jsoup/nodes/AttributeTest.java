import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyInAttributeConstructor() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueInAttributeConstructor() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKeyMethod() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetKeyMethodWithEmptyString() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSetKeyMethodWithTrimmedEmptyString() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(" ");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testParentIsUpdatedWhenKeyIsChanged() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key1", "value1", attributes);
 attribute.setKey("key2");
 assertEquals("key2", attributes.getKey(0));
 }

 @Test
 public void testHtmlMethodWithEmptyParentOutputSettings() {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder output = StringUtil.borrowBuilder();
 try {
 attribute.html(output, null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String result = output.toString();
 assertEquals("key=\"value\"", result);
 }

 @Test
 public void testHtmlMethodWithOutputSettings() {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder output = StringUtil.borrowBuilder();
 Document.OutputSettings settings = new Document.OutputSettings();
 try {
 attribute.html(output, settings);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String result = output.toString();
 assertEquals("key=\"value\"", result);
 }

 @Test
 public void testHtmlMethodWithCollapsibleBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 StringBuilder output = StringUtil.borrowBuilder();
 Document.OutputSettings settings = new Document.OutputSettings();
 try {
 attribute.html(output, settings);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String result = output.toString();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testHtmlMethodWithNonCollapsibleBooleanAttribute() {
 Attribute attribute = new Attribute("notchecked", null);
 StringBuilder output = StringUtil.borrowBuilder();
 Document.OutputSettings settings = new Document.OutputSettings();
 try {
 attribute.html(output, settings);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String result = output.toString();
 assertEquals("notchecked", result);
 }

 @Test
 public void testHtmlMethodWithDataAttribute() {
 Attribute attribute = new Attribute("data-key", "data-value");
 StringBuilder output = StringUtil.borrowBuilder();
 Document.OutputSettings settings = new Document.OutputSettings();
 try {
 attribute.html(output, settings);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String result = output.toString();
 assertEquals("data-key=\"data-value\"", result);
 }

 @Test
 public void testCreateFromEncodedMethod() {
 String encodedKey = "data-key";
 String encodedValue = "data-value";
 Attribute attribute = Attribute.createFromEncoded("key", encodedValue);
 assertEquals("key", attribute.getKey());
 assertEquals("data-value", attribute.getValue());
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", "value");
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Attribute attribute = new Attribute("checked", null);
 Document.OutputSettings settings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(settings));
 attribute.setValue("checked");
 assertFalse(attribute.shouldCollapseAttribute(settings));
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeMethodWithEmptyValue() {
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}