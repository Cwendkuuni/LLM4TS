import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with unencoded key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Test creating a new Attribute with unencoded key, value, and parent
 Attribute attributeParent = new Attributes();
 Attribute attributeParentWithParent = new Attribute("key", "value", attributeParent);
 assertEquals("key", attributeParentWithParent.getKey());
 assertEquals("value", attributeParentWithParent.getValue());
 assertEquals(attributeParent, attributeParentWithParent.getParent());
 }

 @Test
 public void testAttributeSetters() {
 // Test setting key
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());

 // Test setting value
 Attribute attributeValue = new Attribute("key", "value");
 String newValue = "newValue";
 assertEquals("value", attributeValue.getValue());
 attributeValue.setValue(newValue);
 assertEquals("newValue", attributeValue.getValue());
 }

 @Test
 public void testAttributeHtml() throws IOException {
 // Test creating Attribute from encoded key and value and checking html output
 String encodedKey = "key%3D%22value%22";
 Attribute attribute = Attribute.createFromEncoded("key", encodedKey);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String htmlOutput = StringUtil.releaseBuilder(sb);
 assertEquals("<key=\"value\" />", htmlOutput);
 }

 @Test
 public void testIsDataAttribute() {
 // Test checking if attribute is data attribute
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 // Test checking if attribute should collapse
 Attribute attribute = new Attribute("key", "value");
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("booleanAttribute", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Attribute nonBooleanAttribute = new Attribute("nonBooleanAttribute", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 // Test checking if attribute is boolean attribute
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 // Test checking if Attributes are equal
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 // Test checking if Attributes have same hashcode if they are equal
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 assertEquals(hashCode1, hashCode2);
 int hashCodeDifferent = attribute1.hashCode();
 assertNotEquals(hashCode1, hashCodeDifferent);
 }

 @Test
 public void testClone() {
 // Test cloning an Attribute
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 }
}