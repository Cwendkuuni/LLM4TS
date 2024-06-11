package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSettingEmptyKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Appendable appendable = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(appendable, null);
 String actual = appendable.toString();
 String expected = "key=\"value\"";
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 String actual = attribute.toString();
 String expected = "key=\"value\"";
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateFromEncodedMethod() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());

 Attribute attributeWithoutPrefix = new Attribute("key", null);
 assertFalse(attributeWithoutPrefix.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));

 Attribute booleanAttribute = new Attribute("booleanAttribute", null);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 Attribute attribute = new Attribute("booleanAttribute", null);
 assertFalse(attribute.isBooleanAttribute());

 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithValueMethod() {
 Attribute attribute = new Attribute("booleanAttribute", "true");
 assertFalse(attribute.isBooleanAttribute());

 Attribute booleanAttribute = new Attribute("checked", "checked");
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
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
 assertEquals(hashCode1, hashCode2); // should be equal, as per equals test
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testCloningMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

}