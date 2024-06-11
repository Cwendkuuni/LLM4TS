package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

 private XmlElement createXmlElement() {
 return new XmlElement("elementName");
 }

 @Test
 public void testXmlElementConstructorWithName() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("elementName", xmlElement.getName());
 }

 @Test
 public void testXmlElementConstructorWithNameAndAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 XmlElement xmlElement = new XmlElement("elementName", attributes);
 assertEquals("elementName", xmlElement.getName());
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 }

 @Test
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement xmlElement = new XmlElement("elementName", "data");
 assertEquals("elementName", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value");
 fail("Expected NullPointerException, but no exception was thrown.");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("name", null);
 fail("Expected NullPointerException, but no exception was thrown.");
 }

 @Test
 public void testGetAttributeWithValidName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("key", "value");
 assertEquals("value", xmlElement.getAttribute("key"));
 }

 @Test
 public void testGetAttributeWithInvalidName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("key", "value");
 assertEquals(null, xmlElement.getAttribute("invalidName"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("key", null);
 assertEquals(null, xmlElement.getAttribute("key"));
 xmlElement.addAttribute("key", "defaultValue");
 assertEquals("defaultValue", xmlElement.getAttribute("key", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("key1", "value1");
 xmlElement.addAttribute("key2", "value2");
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = createXmlElement();
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 xmlElement.setAttributes(attributes);
 assertEquals(2, xmlElement.getAttributes().size());
 assertEquals("value1", xmlElement.getAttribute("key1"));
 assertEquals("value2", xmlElement.getAttribute("key2"));
 }

 @Test
 public void testAddElementWithNullXmlElement() {
 XmlElement xmlElement = createXmlElement();
 assertFalse(xmlElement.addElement(null));
 }

 @Test
 public void testAddElementWithEmptyList() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 assertFalse(xmlElement.addElement(new XmlElement("subElement")));
 assertEquals(0, xmlElement.getSubElements().size());
 }

 @Test
 public void testAddElementWithValidXmlElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("childElement");
 assertTrue(parentXmlElement.addElement(childXmlElement));
 assertEquals(1, parentXmlElement.getSubElements().size());
 assertSame(parentXmlElement, childXmlElement.getParent());
 }

 @Test
 public void testRemoveElementWithNullXmlElement() {
 XmlElement xmlElement = createXmlElement();
 assertFalse(xmlElement.removeElement(null));
 }

 @Test
 public void testRemoveElementWithInvalidIndex() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 assertFalse(xmlElement.removeElement(-1));
 }

 @Test
 public void testRemoveElementWithValidIndex() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 XmlElement removedElement = xmlElement.removeElement(1);
 assertEquals("subElement2", removedElement.getName());
 assertEquals(1, xmlElement.getSubElements().size());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 xmlElement.removeAllElements();
 assertEquals(0, xmlElement.getSubElements().size());
 }

 @Test
 public void testRemoveFromParentWithNullXmlElement() {
 XmlElement xmlElement = createXmlElement();
 assertEquals(0, xmlElement.getParentCount());
 }

 @Test
 public void testRemoveFromParentWithValidXmlElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("childElement");
 parentXmlElement.addElement(childXmlElement);
 assertEquals(1, parentXmlElement.getSubElements().size());
 assertEquals(childXmlElement, parentXmlElement.getSubElements().get(0));
 childXmlElement.removeFromParent();
 assertEquals(0, parentXmlElement.getSubElements().size());
 assertEquals(null, childXmlElement.getParent());
 }

 @Test
 public void testAppendWithNullXmlElement() {
 XmlElement xmlElement = createXmlElement();
 assertFalse(xmlElement.append(null));
 }

 @Test
 public void testInsertElementWithInvalidIndex() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 assertFalse(xmlElement.insertElement(-1, new XmlElement("newSubElement")));
 }

 @Test
 public void testInsertElementWithValidIndex() {
 XmlElement xmlElement = createXmlElement();
 List<XmlElement> subElements = new Vector<>();
 xmlElement.setSubElements(subElements);
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 XmlElement newSubElement = new XmlElement("newSubElement");
 xmlElement.insertElement(1, newSubElement);
 assertEquals(3, xmlElement.getSubElements().size());
 assertEquals(newSubElement, xmlElement.getSubElements().get(1));
 }

 @Test
 public void testGetElements() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 List<XmlElement> subElements = xmlElement.getElements();
 assertEquals(2, subElements.size());
 }

 @Test
 public void testCount() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(new XmlElement("subElement1"));
 xmlElement.addElement(new XmlElement("subElement2"));
 assertEquals(2, xmlElement.count());
 }

 @Test
 public void testGetElementWithValidPath() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(new XmlElement("element1"));
 xmlElement.addElement(new XmlElement("element2"));
 xmlElement.addElement(new XmlElement("element3"));
 XmlElement element = xmlElement.getElement("element1.element2.element3");
 assertEquals("element3", element.getName());
 }

 @Test
 public void testGetElementWithInvalidPath() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(new XmlElement("element1"));
 xmlElement.addElement(new XmlElement("element2"));
 XmlElement element = xmlElement.getElement("element1.element4");
 assertEquals(null, element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementWithEmptyPath() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.getElement("");
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementWithNullPath() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.getElement(null);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testAddSubElementWithValidPath() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child1 = xmlElement.addSubElement("child1");
 XmlElement child2 = xmlElement.addSubElement("child1.child2");
 XmlElement child3 = xmlElement.addSubElement("child1.child2.child3");
 assertEquals("child3", child3.getName());
 assertEquals(xmlElement, child3.getParent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithEmptyPath() {
 XmlElement xmlElement = createXmlElement();
 try {
 xmlElement.addSubElement("");
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithNullPath() {
 XmlElement xmlElement = createXmlElement();
 try {
 xmlElement.addSubElement(null);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClone() {
 XmlElement xmlElement = createXmlElement();
 XmlElement clonedXmlElement = (XmlElement)xmlElement.clone();
 assertEquals(xmlElement.getName(), clonedXmlElement.getName());
 assertEquals(xmlElement.getData(), clonedXmlElement.getData());
 assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
 assertEquals(xmlElement.getSubElements(), clonedXmlElement.getSubElements());
 }

 @Test
 public void testSetName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.setName("newName");
 assertEquals("newName", xmlElement.getName());
 }

 @Test
 public void testNotifyObservers() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addObserver(e -> {});
 xmlElement.notifyObservers();
 }

 @Test
 public void testEqualsWithSameObject() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = xmlElement1;
 assertTrue(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithDifferentObjectType() {
 XmlElement xmlElement1 = createXmlElement();
 String string = "test";
 assertFalse(xmlElement1.equals(string));
 }

 @Test
 public void testEqualsWithDifferentAttributes() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.addAttribute("attr1", "value1");
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.addAttribute("attr1", "value2");
 xmlElement2.addAttribute("attr2", "value3");
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithDifferentData() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.setData("data1");
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.setData("data2");
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithDifferentName() {
 XmlElement xmlElement1 = new XmlElement("name1");
 XmlElement xmlElement2 = new XmlElement("name2");
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithDifferentSubElements() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.addElement(new XmlElement("subElement1"));
 xmlElement1.addElement(new XmlElement("subElement2"));
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.addElement(new XmlElement("subElement3"));
 xmlElement2.addElement(new XmlElement("subElement4"));
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testHashCodeWithSameObject() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = xmlElement1;
 assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentObjects() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = new XmlElement("name2");
 assertNotSame(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithSameAttributes() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.setAttributes(new Hashtable<String, String>());
 xmlElement1.addAttribute("attr1", "value1");
 Hashtable<String, String> attributes = new Hashtable<String, String>(xmlElement1.getAttributes());
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.setAttributes(attributes);
 assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentAttributes() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.addAttribute("attr1", "value1");
 xmlElement1.addAttribute("attr2", "value2");
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.addAttribute("attr1", "value1");
 xmlElement2.addAttribute("attr3", "value3");
 assertNotSame(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentData() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.setData("data1");
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.setData("data2");
 assertNotSame(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentName() {
 XmlElement xmlElement1 = new XmlElement("name1");
 XmlElement xmlElement2 = new XmlElement("name2");
 assertNotSame(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentSubElements() {
 XmlElement xmlElement1 = new XmlElement("name1");
 xmlElement1.addElement(new XmlElement("subElement1"));
 xmlElement1.addElement(new XmlElement("subElement2"));
 XmlElement xmlElement2 = new XmlElement("name2");
 xmlElement2.addElement(new XmlElement("subElement3"));
 xmlElement2.addElement(new XmlElement("subElement4"));
 assertNotSame(xmlElement1.hashCode(), xmlElement2.hashCode());
 }
}