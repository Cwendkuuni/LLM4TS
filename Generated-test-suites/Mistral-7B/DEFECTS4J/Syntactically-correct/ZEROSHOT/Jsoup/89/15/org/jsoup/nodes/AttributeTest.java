package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value");
 attributes.put(attribute);
 }

 @After
 public void tearDown() {
 attribute = null;
 attributes = null;
 }

 @Test
 public void testCreateAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attributes, attribute.parent);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateAttributeInvalidKey() {
 Attribute attribute = new Attribute(null, "value");
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testCreateAttributeInvalidValue() {
 Attribute attribute = new Attribute("key", null);
 fail();
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyInvalidKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 fail();
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueInvalidValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 fail();
 }

 @Test
 public void testGetHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 out.setSyntax(Document.OutputSettings.Syntax.xml);
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute1.hashCode();
 int hashCode3 = attribute2.hashCode();
 int hashCode4 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertEquals(attribute.parent, clonedAttribute.parent);
 }
}