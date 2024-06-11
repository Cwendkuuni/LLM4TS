package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 // Test creation with null key and value
 try {
 new Attribute(null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test creation with valid key and value
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());

 // Test creation with valid key, value and parent
 Attributes attributes = new Attributes();
 Attribute attrParent = new Attribute("parentKey", "parentValue", attributes);
 Attribute attrChild = new Attribute("childKey", "childValue", attrParent);
 assertEquals("parentKey", attrParent.getKey());
 assertEquals("parentValue", attrParent.getValue());
 assertEquals("childKey", attrChild.getKey());
 assertEquals("childValue", attrChild.getValue());
 assertEquals(attrChild, attrParent.get(attrChild.getKey()));
 }

 @Test
 public void testAttributeKeySetter() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeKeySetterNull() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey(null);
 }

 @Test
 public void testShouldCollapseAttribute() throws Exception {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attrBoolean = new Attribute("checked", "");
 assertTrue(attrBoolean.shouldCollapseAttribute(outputSettings));
 Attribute attrNotBoolean = new Attribute("key", "value");
 assertFalse(attrNotBoolean.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attrBoolean = new Attribute("checked", "");
 assertTrue(attrBoolean.isBooleanAttribute());
 Attribute attrNotBoolean = new Attribute("key", "value");
 assertFalse(attrNotBoolean.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithNullValue() {
 Attribute attrBoolean = new Attribute("checked", null);
 assertTrue(attrBoolean.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key2", "value2");
 Attribute attr3 = new Attribute("key1", "value1");
 Attribute attr4 = new Attribute("key1", "value1");
 Attribute attr5 = new Attribute("key1", "value2");

 assertEquals(attr1, attr3);
 assertEquals(attr1, attr4);
 assertNotEquals(attr1, attr2);
 assertNotEquals(attr1, attr5);
 }

 @Test
 public void testHashCode() {
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key2", "value2");
 Attribute attr3 = new Attribute("key1", "value1");
 Attribute attr4 = new Attribute("key1", "value1");
 Attribute attr5 = new Attribute("key1", "value2");

 int hashCode1 = attr1.hashCode();
 int hashCode2 = attr2.hashCode();
 int hashCode3 = attr3.hashCode();
 int hashCode4 = attr4.hashCode();
 int hashCode5 = attr5.hashCode();

 assertEquals(hashCode1, hashCode3);
 assertEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attr = new Attribute("key", "value");
 Attribute clonedAttr = attr.clone();
 assertEquals(attr, clonedAttr);
 }
}