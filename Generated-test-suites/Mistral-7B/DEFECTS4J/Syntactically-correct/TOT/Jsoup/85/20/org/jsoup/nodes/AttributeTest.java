import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Attribute anotherAttribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 anotherAttribute = new Attribute("anotherKey", "anotherValue");
 attributes = new Attributes();
 }

 @Test
 public void testAttributeCreation() {
 // Test creating an Attribute with null key or value
 assertNull(new Attribute(null, "value"));
 assertNull(new Attribute("key", null));

 // Test valid Attribute creation
 Attribute validAttribute = new Attribute("validKey", "validValue");
 assertEquals("validKey", validAttribute.getKey());
 assertEquals("validValue", validAttribute.getValue());

 // Test invalid Attribute creation with empty key
 assertNull(new Attribute("", "value"));

 // Test invalid Attribute creation with empty value
 assertNull(new Attribute("key", ""));

 // Test creating an Attribute with parent
 Attribute parentAttribute = new Attribute("parentKey", "parentValue");
 Attribute withParent = new Attribute("childKey", "childValue", parentAttribute);
 assertEquals(parentAttribute, withParent.parent);
 }

 @Test
 public void testSettingKey() {
 // Test setting key to null
 Validate.notNull(attribute);
 attribute.setKey(null);
 assertNull(attribute.getKey());

 // Test setting key to an empty string
 attribute.setKey("");
 assertSame(attribute, new Attribute("", "value"));

 // Test setting key to a new value
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 // Test setting value to null
 assertNull(attribute.getValue());
 attribute.setValue(null);
 assertNull(attribute.getValue());

 // Test setting value to an empty string
 attribute.setValue("");
 assertEquals("", attribute.getValue());

 // Test setting value to a new string
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 String html = attribute.html();
 assertEquals("key=\"value\"", html);

 String htmlWithParent = new Attribute("key", "value", attributes).html();
 assertEquals("key=\"value\"", htmlWithParent);
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(equalAttribute));
 }

 @Test
 public void testNotEquals() {
 Attribute notEqualAttribute = new Attribute("anotherKey", "anotherValue");
 assertFalse(attribute.equals(notEqualAttribute));
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 attribute.setValue("newValue");
 assertNotSame(attribute, clonedAttribute);
 assertEquals("value", clonedAttribute.getValue());
 }

 @Test
 public void testIsBooleanAttribute() {
 // Test isBooleanAttribute returns true for known boolean attributes
 assertTrue(attribute.isBooleanAttribute("checked"));

 // Test isBooleanAttribute returns false for non-boolean attributes
 assertFalse(attribute.isBooleanAttribute("key"));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 assertFalse(anotherAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedValue = "checked";
 Attribute decodedAttribute = Attribute.createFromEncoded("key", encodedValue);
 assertTrue(decodedAttribute.isBooleanAttribute());
 }
}