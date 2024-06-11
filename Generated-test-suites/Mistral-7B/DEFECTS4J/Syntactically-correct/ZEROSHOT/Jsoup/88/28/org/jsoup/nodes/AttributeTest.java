package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Attribute} class
 */
public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testConstructorWithNullKey() {
 try {
 new Attribute(null, "value", attributes);
 fail("Exception expected");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testConstructorWithEmptyKey() {
 try {
 new Attribute("", "value", attributes);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructorWithEmptyValue() {
 new Attribute("key", "", attributes);
 assertEquals("key", attribute.getKey());
 assertNull(attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValueAndParent() {
 Attribute attributeWithParent = new Attribute("key", "value", attributes);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertSame(attributes, attributeWithParent.parent);
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 attribute.setKey(null);
 fail("Exception expected");
 }

 @Test
 public void testSetKeyWithEmptyKey() {
 try {
 attribute.setKey("");
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testHashCodeAndEquals() {
 Attribute sameAttribute = new Attribute("key", "value", attributes);
 Attribute differentAttribute = new Attribute("differentKey", "value", attributes);
 Attribute sameAttributeDifferentParent = new Attribute("key", "value", new Attributes());

 assertEquals(attribute.hashCode(), sameAttribute.hashCode());
 assertEquals(attribute, sameAttribute);
 assertFalse(attribute.equals(differentAttribute));
 assertFalse(attribute.equals(sameAttributeDifferentParent));
 }

 @Test
 public void testToString() {
 assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertSame(attributes, clonedAttribute.parent);
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute encodedAttribute = new Attribute("key=\"value\"", null);
 Attribute decodedAttribute = Attribute.createFromEncoded("key", encodedAttribute.html());
 assertEquals(decodedAttribute.getKey(), "key");
 assertEquals(decodedAttribute.getValue(), "value");
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null, attributes);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", null, attributes);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertEquals(true, attribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 String booleanKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanKey, null, attributes);
 assertEquals(true, booleanAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanAttribute() {
 String nonBooleanKey = "key";
 Attribute nonBooleanAttribute = new Attribute(nonBooleanKey, "value", attributes);
 assertEquals(false, nonBooleanAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Attribute emptyValueAttribute = new Attribute("key", "", attributes);
 assertEquals(false, emptyValueAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithValueEqualToKey() {
 Attribute valueEqualToKeyAttribute = new Attribute("key", "key", attributes);
 assertEquals(true, valueEqualToKeyAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithNullValue() {
 Attribute nullValueAttribute = new Attribute("key", null, attributes);
 assertEquals(false, nullValueAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithOutputSettings() {
 Attribute outputSettingsAttribute = new Attribute("outputSettings", null, attributes);
 Map<String, String> outputSettings = new HashMap<>();
 outputSettings.put("syntax", "html");
 Document.OutputSettings settings = new Document.OutputSettings(outputSettings);
 assertEquals(true, outputSettingsAttribute.shouldCollapseAttribute(settings));
 }
}