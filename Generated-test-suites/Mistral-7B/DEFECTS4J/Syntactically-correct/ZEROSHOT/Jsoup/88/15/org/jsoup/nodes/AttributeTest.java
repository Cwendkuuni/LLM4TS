import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testAttributeCreation() {
 // Testing the constructor that takes a key and a value
 Attribute attribute1 = new Attribute("key1", "value1");
 // Testing the constructor that takes a key, a value and a parent
 Attribute attribute2 = new Attribute("key2", "value2", attributes);
 // Testing the equals method
 assertEquals(attribute1, attribute1);
 assertEquals(attribute1, attribute1);
 assertNotEquals(attribute1, attribute2);
 // Testing the hashCode method
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 // Testing the clone method
 Attribute clonedAttribute = attribute1.clone();
 assertEquals(attribute1, clonedAttribute);
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 fail("Should throw an IllegalArgumentException when trying to set a null key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyEmpty() {
 attribute.setKey("");
 fail("Should throw an IllegalArgumentException when trying to set an empty key");
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 fail("Should throw an IllegalArgumentException when trying to set a null value");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, null);
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithOutputSettings() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 attribute.html(sb, outputSettings);
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 String booleanAttribute = "checked";
 Attribute booleanAttributeAttribute = new Attribute(booleanAttribute, null, attributes);
 assertFalse(booleanAttributeAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithValueEqualToKey() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("key");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsDataAttribute() {
 String dataAttributeKey = "data-test";
 Attribute dataAttribute = new Attribute(dataAttributeKey, "test", attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithNonDataAttributeKey() {
 Attribute nonDataAttribute = new Attribute("key", "value", attributes);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 String booleanAttributeKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanAttributeKey, null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 String nonBooleanAttributeKey = "key";
 Attribute nonBooleanAttribute = new Attribute(nonBooleanAttributeKey, "value", attributes);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }
}