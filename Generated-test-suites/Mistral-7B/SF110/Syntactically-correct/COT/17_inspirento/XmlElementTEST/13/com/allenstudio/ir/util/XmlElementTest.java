package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setup() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstructorEmpty() {
 XmlElement xmlElement = new XmlElement();
 assertEquals("", xmlElement.getName());
 assertNull(xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementConstructorWithName() {
 XmlElement xmlElement = new XmlElement("elementName");
 assertEquals("elementName", xmlElement.getName());
 assertNull(xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementConstructorWithNameAndAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 XmlElement xmlElement = new XmlElement("elementName", attributes);
 assertEquals("elementName", xmlElement.getName());
 assertNotNull(xmlElement.getData());
 assertEquals(attributes, xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement xmlElement = new XmlElement("elementName", "data");
 assertEquals("elementName", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");
 Map<String, String> expectedAttributes = new HashMap<>();
 expectedAttributes.put("key1", "value1");
 expectedAttributes.put("key2", "value2");
 Map<String, String> attributes = root.getAttributes();
 assertEquals(expectedAttributes, attributes);
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "value");
 fail("Exception expected: NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("name", null);
 fail("Exception expected: NullPointerException");
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("key1", "value1");
 assertEquals("value1", root.getAttribute("key1"));
 }

 @Test
 public void testGetAttributeNullName() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("key1", null);
 assertEquals("defaultValue", root.getAttribute("key1", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");
 Map<String, String> expectedAttributes = new HashMap<>();
 expectedAttributes.put("key1", "value1");
 expectedAttributes.put("key2", "value2");
 Map<String, String> attributes = root.getAttributes();
 assertEquals(expectedAttributes, attributes);
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> newAttributes = new Hashtable<>();
 newAttributes.put("key1", "newValue1");
 newAttributes.put("key2", "newValue2");
 root.setAttributes(newAttributes);
 Map<String, String> expectedAttributes = new HashMap<>();
 expectedAttributes.put("key1", "newValue1");
 expectedAttributes.put("key2", "newValue2");
 Map<String, String> attributes = root.getAttributes();
 assertEquals(expectedAttributes, attributes);
 }

 @Test
 public void testGetAttributeNames() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");
 List<String> expectedAttributeNames = Arrays.asList("key1", "key2");
 List<String> attributeNames = new ArrayList<>();
 while (root.getAttributeNames().hasMoreElements()) {
 attributeNames.add((String)root.getAttributeNames().nextElement());
 }
 assertEquals(expectedAttributeNames, attributeNames);
 }

 @Test
 public void testAddElement() {
 XmlElement childElement = new XmlElement("childElement");
 root.addElement(childElement);
 assertTrue(root.hasSubElements());
 XmlElement child = root.getElement("childElement");
 assertEquals(childElement, child);
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullChild() {
 XmlElement nullChild = null;
 root.addElement(nullChild);
 fail("Exception expected: NullPointerException");
 }

 @Test
 public void testRemoveElement() {
 XmlElement childElement = new XmlElement("childElement");
 root.addElement(childElement);
 XmlElement removedChild = root.removeElement(childElement);
 assertNotNull(removedChild);
 assertFalse(root.hasSubElements());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveElementNullChild() {
 XmlElement nullChild = null;
 root.removeElement(nullChild);
 fail("Exception expected: NullPointerException");
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.addElement(childElement1);
 root.addElement(childElement2);
 root.removeAllElements();
 assertFalse(root.hasSubElements());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement childElement = new XmlElement("childElement");
 root.addElement(childElement);
 childElement.removeFromParent();
 assertFalse(root.hasSubElements());
 assertNull(childElement.getParent());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 XmlElement cloned = original.clone();
 assertEquals(original, cloned);
 }

 @Test
 public void testEquals() {
 XmlElement xmlElement1 = new XmlElement("elementName1");
 XmlElement xmlElement2 = new XmlElement("elementName2");
 assertFalse(xmlElement1.equals(xmlElement2));
 XmlElement xmlElement3 = new XmlElement("elementName1");
 assertTrue(xmlElement1.equals(xmlElement3));
 }

 @Test
 public void testHashCode() {
 XmlElement xmlElement1 = new XmlElement("elementName1");
 XmlElement xmlElement2 = new XmlElement("elementName2");
 assertNotEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
 }
}