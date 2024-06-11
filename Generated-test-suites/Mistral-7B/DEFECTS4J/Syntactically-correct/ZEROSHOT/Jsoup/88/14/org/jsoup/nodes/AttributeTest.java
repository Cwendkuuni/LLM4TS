package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

 private static Attribute createAttribute(String key, String value) {
 return new Attribute(key, value);
 }

 @Test
 public void testCreateAttribute() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);

 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 String key = "key";
 String value = "value";
 Attributes parent = new Attributes();
 Attribute attribute = createAttribute(key, value, parent);

 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertSame(parent, attribute.parent);
 }

 @Test
 public void testSetKey() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);

 String newKey = "newKey";
 attribute.setKey(newKey);

 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSetKeyWithEmptyString() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);

 attribute.setKey("");

 assertNull(attribute.getKey());
 }

 @Test
 public void testSetKeyWithNull() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);

 attribute.setKey(null);

 assertNull(attribute.getKey());
 }

 @Test
 public void testSetValue() {
 String key = "key";
 String oldValue = "oldValue";
 String newValue = "newValue";
 Attribute attribute = createAttribute(key, oldValue);

 String value = attribute.setValue(newValue);

 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, value);
 }

 @Test
 public void testSetValueWithNull() {
 String key = "key";
 String oldValue = "oldValue";
 Attribute attribute = createAttribute(key, oldValue);

 attribute.setValue(null);

 assertNull(attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);

 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());

 String expected = String.format("%s=\"%s\"", key, value);
 String actual = StringUtil.releaseBuilder(sb);

 assertEquals(expected, actual);
 }

 @Test
 public void testIsDataAttribute() {
 String key = "key";

 assertTrue(attribute.isDataAttribute(key));
 assertTrue(attribute.isDataAttribute("data:" + key));

 assertFalse(attribute.isDataAttribute(""));
 assertFalse(attribute.isDataAttribute(null));
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();

 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key2", "");
 Attribute attribute3 = createAttribute("boolean", "");
 Attribute attribute4 = createAttribute("boolean", "false");
 Attribute attribute5 = createAttribute("boolean", "true");

 assertTrue(attribute1.shouldCollapseAttribute(out));
 assertFalse(attribute2.shouldCollapseAttribute(out));
 assertFalse(attribute3.shouldCollapseAttribute(out));
 assertFalse(attribute4.shouldCollapseAttribute(out));
 assertFalse(attribute5.shouldCollapseAttribute(out));
 }
}