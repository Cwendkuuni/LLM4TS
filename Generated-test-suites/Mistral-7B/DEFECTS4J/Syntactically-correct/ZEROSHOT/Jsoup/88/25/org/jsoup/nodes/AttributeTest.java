package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

 private static Attribute createAttribute(String key, String value) {
 return new Attribute(key, value);
 }

 @Test
 public void testAttributeCreation() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 String key = "key";
 String value = "value";
 Attributes parent = new Attributes();
 Attribute attribute = createAttribute(key, value, parent);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attribute, parent.put(key, value));
 }

 @Test
 public void testSetKey() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 String key = "key";
 String oldValue = "oldValue";
 String newValue = "newValue";
 Attribute attribute = createAttribute(key, oldValue);
 assertEquals(oldValue, attribute.getValue());
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertTrue(html.startsWith(key + "=\"" + value + "\""));
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 assertTrue(attribute.shouldCollapseAttribute(settings));
 assertTrue(attribute.shouldCollapseAttribute(settings, true));
 settings.syntax(Document.OutputSettings.Syntax.xml);
 assertFalse(attribute.shouldCollapseAttribute(settings));
 assertFalse(attribute.shouldCollapseAttribute(settings, true));
 }

 @Test
 public void testIsBooleanAttribute() {
 String key = "key";
 assertTrue(Attribute.isBooleanAttribute(key));
 String booleanKey = "checkbox";
 assertTrue(Attribute.isBooleanAttribute(booleanKey));
 assertFalse(Attribute.isBooleanAttribute(""));
 assertFalse(Attribute.isBooleanAttribute(null));
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "key=\"value\"";
 Attribute attribute = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testMapEntryMethods() {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attribute.getKey(), attribute.getEntry().getKey());
 assertEquals(attribute.getValue(), attribute.getEntry().getValue());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 String key = "key";
 String value = "value";
 Attribute attribute = createAttribute(key, value);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getClass(), clonedAttribute.getClass());
 assertEquals(key, clonedAttribute.getKey());
 assertEquals(value, clonedAttribute.getValue());
 }
}