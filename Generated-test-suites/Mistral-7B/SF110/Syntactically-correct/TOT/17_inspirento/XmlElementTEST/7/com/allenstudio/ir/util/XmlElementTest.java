package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testConstructor() {
 XmlElement element = new XmlElement();
 assertNotNull(element);
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement();
 element.addAttribute("test", "testValue");
 assertEquals("testValue", element.getAttribute("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement element = new XmlElement();
 element.addAttribute(null, "testValue");
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement element = new XmlElement();
 element.addAttribute("test", null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetAttributeNullName() {
 XmlElement element = new XmlElement();
 assertNull(element.getAttribute(null));
 }

 @Test
 public void testGetAttributeDefaultValue() {
 XmlElement element = new XmlElement();
 assertEquals("", element.getAttribute("test", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement element = new XmlElement();
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("test1", "testValue1");
 attributes.put("test2", "testValue2");
 element.setAttributes(attributes);
 Hashtable<String, String> actualAttributes = element.getAttributes();
 assertEquals(attributes, actualAttributes);
 }

 @Test(expected = NullPointerException.class)
 public void testSetAttributesNullHashtable() {
 XmlElement element = new XmlElement();
 element.setAttributes(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement element = new XmlElement();
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("test1", "testValue1");
 attributes.put("test2", "testValue2");
 element.setAttributes(attributes);
 Enumeration<String> names = element.getAttributeNames();
 String name = names.nextElement();
 assertEquals("test1", name);
 }

 @Test
 public void testAddElement() {
 XmlElement parent = root;
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 assertEquals(1, parent.count());
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullChild() {
 XmlElement parent = root;
 XmlElement child = null;
 parent.addElement(child);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testRemoveElement() {
 XmlElement parent = root;
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 assertEquals(1, parent.count());
 parent.removeElement(child);
 assertEquals(0, parent.count());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 XmlElement parent = root;
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 parent.removeElement(1);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parent = root;
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 parent.removeAllElements();
 assertEquals(0, parent.count());
 }

 @Test
 public void testAddSubElement() {
 XmlElement parent = root;
 XmlElement child = new XmlElement("child");
 parent.addSubElement(child);
 assertEquals(1, parent.count());
 assertEquals("child", child.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementInvalidPath() {
 XmlElement parent = root;
 parent.addSubElement("child/child");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testClone() {
 XmlElement element = new XmlElement("element");
 XmlElement clone = (XmlElement)element.clone();
 assertEquals(element.getName(), clone.getName());
 assertEquals(element.getAttributes(), clone.getAttributes());
 assertEquals(element.getData(), clone.getData());
 assertEquals(element.count(), clone.count());
 }

 @Test
 public void testEquals() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element1");
 assertEquals(element1, element2);
 }

 @Test
 public void testEqualsDifferentObjects() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 assertNotEquals(element1, element2);
 }

 @Test
 public void testEqualsSameObjects() {
 XmlElement element = new XmlElement("element");
 assertEquals(element, element);
 }

 @Test
 public void testHashCode() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 assertNotEquals(element1.hashCode(), element2.hashCode());
 }

 @Test
 public void testHashCodeSameObjects() {
 XmlElement element = new XmlElement("element");
 assertEquals(element.hashCode(), element.hashCode());
 }
}