package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 Assert.assertNull(attribute.getParent());
 }

 @Test
 public void testAttributeCreationWithParent() {
 String key = "testKey";
 String value = "testValue";
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(key, value, parent);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 Assert.assertEquals(parent, attribute.getParent());
 }

 @Test
 public void testAttributeSetKey() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 String newKey = "newTestKey";
 attribute.setKey(newKey);
 Assert.assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithNull() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 attribute.setKey(null);
 }

 @Test
 public void testAttributeSetValue() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 String newValue = "newTestValue";
 attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNull() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = String.format("%s=\"%s\"", key, value);
 Assert.assertEquals(expected, sb.toString());
 }

 @Test
 public void testIsBooleanAttributeTrue() {
 String key = "checked";
 boolean result = Attribute.isBooleanAttribute(key);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 String key = "testKey";
 boolean result = Attribute.isBooleanAttribute(key);
 Assert.assertFalse(result);
 }

 @Test
 public void testShouldCollapseAttributeTrue() {
 String key = "checked";
 boolean result = Attribute.shouldCollapseAttribute(new Document.OutputSettings(), key, null);
 Assert.assertTrue(result);
 }

 @Test
 public void testShouldCollapseAttributeFalse() {
 String key = "testKey";
 String value = "testValue";
 boolean result = Attribute.shouldCollapseAttribute(new Document.OutputSettings(), key, value);
 Assert.assertFalse(result);
 }

 @Test
 public void testEquals() {
 String key1 = "testKey1";
 String value1 = "testValue1";
 String key2 = "testKey2";
 String value2 = "testValue1";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 Attribute clone = attribute1.clone();
 Attribute attribute3 = new Attribute(key1, value1);
 Attribute attribute4 = new Attribute(key1, value2);
 Assert.assertTrue(attribute1.equals(attribute1));
 Assert.assertTrue(attribute1.equals(clone));
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 Assert.assertFalse(attribute1.equals(attribute4));
 }
}