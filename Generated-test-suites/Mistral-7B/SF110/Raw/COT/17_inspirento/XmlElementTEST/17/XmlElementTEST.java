package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

 private XmlElement createXmlElement() {
 return new XmlElement("root") {
 {
 addAttribute("attr1", "value1");
 addAttribute("attr2", "value2");
 addElement(new XmlElement("child1"));
 addElement(new XmlElement("child2"));
 }
 };
 }

 @Test
 public void testXmlElementConstructor() {
 XmlElement xmlElement = new XmlElement();
 assertNotNull(xmlElement);
 assertNull(xmlElement.getName());
 assertNull(xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementNameConstructor() {
 XmlElement xmlElement = new XmlElement("name");
 assertEquals("name", xmlElement.getName());
 assertNull(xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementNameAttributesConstructor() {
 XmlElement xmlElement = new XmlElement("name", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 assertEquals("name", xmlElement.getName());
 assertNotNull(xmlElement.getData());
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testXmlElementNameDataConstructor() {
 XmlElement xmlElement = new XmlElement("name", "data");
 assertEquals("name", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testAddAttribute() {
 XmlElement xmlElement = createXmlElement();
 String value = "newValue";
 xmlElement.addAttribute("attr3", value);
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 assertEquals(value, xmlElement.getAttribute("attr3"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value");
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("name", null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetAttribute() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 assertNull(xmlElement.getAttribute("notExists"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2", "defaultValue"));
 assertEquals("defaultValue", xmlElement.getAttribute("notExists", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement xmlElement = createXmlElement();
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = createXmlElement();
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("attr1", "newValue1");
 put("attr2", "newValue2");
 put("attr3", "newValue3");
 }};
 xmlElement.setAttributes(attributes);
 assertEquals("newValue1", xmlElement.getAttribute("attr1"));
 assertEquals("newValue2", xmlElement.getAttribute("attr2"));
 assertEquals("newValue3", xmlElement.getAttribute("attr3"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement xmlElement = createXmlElement();
 Enumeration<String> attributeNames = xmlElement.getAttributeNames();
 List<String> names = new ArrayList<>();
 while (attributeNames.hasMoreElements()) {
 names.add((String)attributeNames.nextElement());
 }
 assertEquals(2, names.size());
 assertEquals("attr1", names.get(0));
 assertEquals("attr2", names.get(1));
 }

 @Test
 public void testAddElement() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child = new XmlElement("child");
 xmlElement.addElement(child);
 assertTrue(xmlElement.hasSubElements());
 assertEquals(2, xmlElement.subElements.size());
 assertEquals(child, xmlElement.subElements.get(1));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullElement() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testRemoveElement() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child = xmlElement.subElements.get(0);
 xmlElement.removeElement(child);
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child = xmlElement.subElements.get(0);
 xmlElement.removeElement(0);
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.removeAllElements();
 assertFalse(xmlElement.hasSubElements());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 child.removeFromParent();
 assertFalse(parent.hasSubElements());
 assertNull(child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child = new XmlElement("child");
 parent.append(child);
 assertTrue(parent.hasSubElements());
 assertEquals(child, parent.subElements.get(0));
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.insertElement(child1, 0);
 parent.insertElement(child2, 1);
 assertTrue(parent.hasSubElements());
 assertEquals(child1, parent.subElements.get(0));
 assertEquals(child2, parent.subElements.get(1));
 }

 @Test
 public void testGetElements() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> elements = xmlElement.getElements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testCount() {
 XmlElement xmlElement = createXmlElement();
 assertEquals(3, xmlElement.count());
 }

 @Test
 public void testGetElement() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child1 = xmlElement.getElement("child1");
 assertEquals("child1", child1.getName());
 }

 @Test
 public void testGetElementWithNullPath() {
 XmlElement xmlElement = createXmlElement();
 assertNull(xmlElement.getElement(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementWithEmptyPath() {
 XmlElement xmlElement = createXmlElement();
 assertNull(xmlElement.getElement(""));
 }

 @Test
 public void testGetElementWithInvalidPath() {
 XmlElement xmlElement = createXmlElement();
 assertNull(xmlElement.getElement("child1child2"));
 }

 @Test
 public void testGetElementWithValidPath() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child2 = xmlElement.getElement("child2");
 assertEquals("child2", child2.getName());
 }

 @Test
 public void testAddSubElement() {
 XmlElement xmlElement = new XmlElement("root");
 XmlElement child = xmlElement.addSubElement("child1");
 assertEquals("child1", child.getName());
 assertEquals(1, xmlElement.count());
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementNullPath() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.addSubElement(null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementEmptyPath() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.addSubElement("");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAddSubElementInvalidPath() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.addSubElement("child1child2");
 assertEquals(0, xmlElement.count());
 }

 @Test
 public void testAddSubElementValidPath() {
 XmlElement xmlElement = new XmlElement("root");
 XmlElement child1 = xmlElement.addSubElement("child1");
 assertEquals("child1", child1.getName());
 assertEquals(1, xmlElement.count());
 XmlElement child2 = xmlElement.addSubElement("child1child2");
 assertEquals("child1child2", child2.getName());
 assertEquals(2, xmlElement.count());
 }

 @Test
 public void testAddSubElementWithData() {
 XmlElement xmlElement = new XmlElement("root");
 XmlElement child = xmlElement.addSubElement("child", "data");
 assertEquals("child", child.getName());
 assertEquals("data", child.getData());
 assertEquals(1, xmlElement.count());
 }

 @Test
 public void testClone() {
 XmlElement xmlElement = createXmlElement();
 XmlElement cloned = (XmlElement)xmlElement.clone();
 assertEquals(xmlElement.getName(), cloned.getName());
 assertEquals(xmlElement.getData(), cloned.getData());
 assertEquals(xmlElement.getAttributes(), cloned.getAttributes());
 assertEquals(xmlElement.subElements.size(), cloned.subElements.size());
 for (XmlElement element : xmlElement.subElements) {
 assertEquals(element, cloned.getElement(element.getName()));
 }
 }

 @Test
 public void testEquals() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = createXmlElement();
 XmlElement xmlElement3 = createXmlElement();
 XmlElement xmlElement4 = createXmlElement();
 XmlElement xmlElement5 = xmlElement1;
 assertTrue(xmlElement1.equals(xmlElement1));
 assertTrue(xmlElement1.equals(xmlElement5));
 assertFalse(xmlElement1.equals(xmlElement2));
 assertFalse(xmlElement1.equals(xmlElement3));
 assertFalse(xmlElement1.equals(xmlElement4));
 }

 @Test
 public void testHashCode() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = createXmlElement();
 XmlElement xmlElement3 = createXmlElement();
 XmlElement xmlElement4 = createXmlElement();
 XmlElement xmlElement5 = xmlElement1;
 int hashCode1 = xmlElement1.hashCode();
 int hashCode2 = xmlElement2.hashCode();
 int hashCode3 = xmlElement3.hashCode();
 int hashCode4 = xmlElement4.hashCode();
 int hashCode5 = xmlElement5.hashCode();
 assertEquals(hashCode1, hashCode5);
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }

}