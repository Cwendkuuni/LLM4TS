package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"value\"");
 assertEquals("unencodedKey", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());

 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "dataKey", "value");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("booleanAttribute", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(out));

 Attribute nonBooleanAttribute = new Attribute("nonBooleanAttribute", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("booleanAttribute", "");
 assertTrue(booleanAttribute.isBooleanAttribute());

 Attribute nonBooleanAttribute = new Attribute("nonBooleanAttribute", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");

 assertEquals(attribute1, attribute1);
 assertEquals(attribute1, attribute2);
 assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}