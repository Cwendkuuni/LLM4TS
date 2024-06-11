package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(null, attribute.getParent());

 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertEquals(new Attributes(), attributeWithParent.getParent());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationNullKey() {
 Attribute newAttribute = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeCreationEmptyKey() {
 Attribute newAttribute = new Attribute("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeCreationNullValue() {
 Attribute newAttribute = new Attribute("key", null);
 }

 @Test
 public void testSettingAttributeKey() {
 Attribute attribute = new Attribute("key", "value");
 String newKey = "newKey";
 attribute.setKey(newKey);

 assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingAttributeNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSettingAttributeValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 attribute.setValue(newValue);

 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingAttributeNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testGettingAndSettingAttributeValuesWithSameParent() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("key1", "value1", attributes);
 Attribute attribute2 = new Attribute("key2", "value2", attributes);

 assertEquals(attributes, attribute1.getParent());
 assertEquals(attributes, attribute2.getParent());

 attribute1.setValue("newValue1");
 assertEquals("newValue1", attribute1.getValue());
 assertEquals("value1", attribute2.getValue());
 }

 @Test
 public void testGettingAndSettingAttributeValuesWithDifferentParents() {
 Attributes attributes1 = new Attributes();
 Attribute attribute1 = new Attribute("key1", "value1", attributes1);
 Attributes attributes2 = new Attributes();
 Attribute attribute2 = new Attribute("key2", "value2", attributes2);

 assertEquals(attributes1, attribute1.getParent());
 assertEquals(attributes2, attribute2.getParent());

 attribute1.setValue("newValue1");
 assertEquals("newValue1", attribute1.getValue());
 assertEquals("value1", attribute2.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Document document = new Document();
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();

 attribute.html(sb, document.outputSettings());

 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 assertTrue(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute2));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();

 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 assertTrue(Attribute.isBooleanAttribute("checked"));
 assertFalse(Attribute.isBooleanAttribute("key"));
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();

 assertTrue(Attribute.shouldCollapseAttribute(outputSettings, "checked", null));
 assertFalse(Attribute.shouldCollapseAttribute(outputSettings, "key", "value"));
 }

 @Test
 public void testCreateFromEncodedMethod() {
 String encodedKey = "data-my-key";
 String decodedKey = "my-key";
 String encodedValue = "data-my-value=\"my value\"";
 String decodedValue = "my value";

 Attribute attribute = Attribute.createFromEncoded(decodedKey, encodedValue);

 assertEquals(decodedKey, attribute.getKey());
 assertEquals(decodedValue, attribute.getValue());
 }
}