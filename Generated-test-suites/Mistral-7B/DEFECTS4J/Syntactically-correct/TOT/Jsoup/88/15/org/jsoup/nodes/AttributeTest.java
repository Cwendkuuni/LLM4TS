package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Document document;

 @Before
 public void setUp() throws IOException {
 document = new Document();
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testNewAttribute() {
 Attribute newAttribute = new Attribute("newKey", "newValue");
 assertEquals("newKey", newAttribute.getKey());
 assertEquals("newValue", newAttribute.getValue());
 assertNull(newAttribute.parent);
 }

 @Test
 public void testNewAttributeWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute newAttribute = new Attribute("newKey", "newValue", parentAttributes);
 assertEquals("newKey", newAttribute.getKey());
 assertEquals("newValue", newAttribute.getValue());
 assertSame(parentAttributes, newAttribute.parent);
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, document.outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testHtmlEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue("");
 attribute.html(sb, document.outputSettings());
 assertEquals("key=\"\"", sb.toString());
 }

 @Test
 public void testHtmlEqualKeyAndValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue("key");
 attribute.html(sb, document.outputSettings());
 assertEquals("key=\"\"", sb.toString());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 assertTrue(attribute.shouldCollapseAttribute(document.outputSettings()));
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(document.outputSettings()));
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(document.outputSettings()));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testHashCode() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertEquals(attribute.hashCode(), anotherAttribute.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertNull(clonedAttribute.parent);
 }
}