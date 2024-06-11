package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

 private XmlElement createXmlElement() {
 return new XmlElement("ElementName");
 }

 @Test
 public void testXmlElementConstructorWithName() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("ElementName", xmlElement.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementConstructorWithNullName() {
 XmlElement xmlElement = new XmlElement(null);
 fail("Expected NullPointerException, but got: " + xmlElement);
 }

 @Test
 public void testXmlElementConstructorWithNameAndAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attribute1", "value1");
 XmlElement xmlElement = new XmlElement("ElementName", attributes);
 assertEquals("ElementName", xmlElement.getName());
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 }

 @Test
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement xmlElement = new XmlElement("ElementName", "data");
 assertEquals("ElementName", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 }

 @Test
 public void testAddAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value");
 fail("Expected NullPointerException, but got: " + xmlElement);
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", null);
 fail("Expected NullPointerException, but got: " + xmlElement);
 }

 @Test
 public void testGetAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", null);
 assertEquals("defaultValue", xmlElement.getAttribute("attribute1", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", "value1");
 xmlElement.addAttribute("attribute2", "value2");
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = createXmlElement();
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attribute1", "value1");
 xmlElement.setAttributes(attributes);
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 }

 @Test
 public void testAddElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addElement(childXmlElement);
 assertTrue(parentXmlElement.getElements().contains(childXmlElement));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementWithNullChild() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = null;
 parentXmlElement.addElement(childXmlElement);
 fail("Expected NullPointerException, but got: " + parentXmlElement);
 }

 @Test
 public void testRemoveElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addElement(childXmlElement);
 XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement);
 assertNotNull(removedChildXmlElement);
 assertFalse(parentXmlElement.getElements().contains(childXmlElement));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementWithOutOfIndex() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.addElement(new XmlElement("ChildElement1"));
 parentXmlElement.addElement(new XmlElement("ChildElement2"));
 parentXmlElement.removeElement(10);
 fail("Expected IndexOutOfBoundsException, but got: " + parentXmlElement);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement1 = new XmlElement("ChildElement1");
 XmlElement childXmlElement2 = new XmlElement("ChildElement2");
 parentXmlElement.addElement(childXmlElement1);
 parentXmlElement.addElement(childXmlElement2);
 parentXmlElement.removeAllElements();
 assertTrue(parentXmlElement.getElements().isEmpty());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addElement(childXmlElement);
 childXmlElement.removeFromParent();
 assertNull(childXmlElement.getParent());
 assertFalse(parentXmlElement.getElements().contains(childXmlElement));
 }

 @Test
 public void testAppend() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.append(childXmlElement);
 assertTrue(parentXmlElement.getElements().contains(childXmlElement));
 }

 @Test(expected = NullPointerException.class)
 public void testAppendWithNullChild() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = null;
 parentXmlElement.append(childXmlElement);
 fail("Expected NullPointerException, but got: " + parentXmlElement);
 }

 @Test
 public void testInsertElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement1 = new XmlElement("ChildElement1");
 XmlElement childXmlElement2 = new XmlElement("ChildElement2");
 parentXmlElement.insertElement(childXmlElement1, 0);
 parentXmlElement.insertElement(childXmlElement2, 1);
 List<XmlElement> elements = parentXmlElement.getElements();
 assertEquals(childXmlElement1, elements.get(0));
 assertEquals(childXmlElement2, elements.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementWithOutOfIndex() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.insertElement(childXmlElement, 10);
 fail("Expected IndexOutOfBoundsException, but got: " + parentXmlElement);
 }

 @Test
 public void testGetElements() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement1 = new XmlElement("ChildElement1");
 XmlElement childXmlElement2 = new XmlElement("ChildElement2");
 parentXmlElement.addElement(childXmlElement1);
 parentXmlElement.addElement(childXmlElement2);
 List<XmlElement> elements = parentXmlElement.getElements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testCount() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement1 = new XmlElement("ChildElement1");
 XmlElement childXmlElement2 = new XmlElement("ChildElement2");
 parentXmlElement.addElement(childXmlElement1);
 parentXmlElement.addElement(childXmlElement2);
 assertEquals(2, parentXmlElement.count());
 }

 @Test
 public void testElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addElement(childXmlElement);
 XmlElement element = parentXmlElement.getElement("ChildElement");
 assertEquals(childXmlElement, element);
 }

 @Test(expected = NullPointerException.class)
 public void testElementWithNullPath() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.getElement(null);
 fail("Expected NullPointerException, but got: " + parentXmlElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testElementWithEmptyPath() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.getElement("");
 fail("Expected IllegalArgumentException, but got: " + parentXmlElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testElementWithInvalidPathFormat() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.getElement("ChildElement.Attribute");
 fail("Expected IllegalArgumentException, but got: " + parentXmlElement);
 }

 @Test
 public void testAddSubElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addSubElement(childXmlElement);
 assertTrue(parentXmlElement.getElements().contains(childXmlElement));
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementWithNullChild() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = null;
 parentXmlElement.addSubElement(childXmlElement);
 fail("Expected NullPointerException, but got: " + parentXmlElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithEmptyPath() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.addSubElement("");
 fail("Expected IllegalArgumentException, but got: " + parentXmlElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithInvalidPathFormat() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.addSubElement("ChildElement.Attribute");
 fail("Expected IllegalArgumentException, but got: " + parentXmlElement);
 }

 @Test
 public void testAddSubElementWithExistingElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement existingChildXmlElement = new XmlElement("ChildElement");
 parentXmlElement.addElement(existingChildXmlElement);
 XmlElement newChildXmlElement = new XmlElement("NewChildElement");
 parentXmlElement.addSubElement(newChildXmlElement);
 assertEquals(2, parentXmlElement.getElements().size());
 }

 @Test
 public void testClone() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attribute1", "value1");
 xmlElement.addAttribute("attribute2", "value2");
 xmlElement.addElement(new XmlElement("ChildElement1"));
 xmlElement.addElement(new XmlElement("ChildElement2"));
 XmlElement clonedXmlElement = (XmlElement)xmlElement.clone();
 assertEquals(xmlElement.getName(), clonedXmlElement.getName());
 assertEquals(xmlElement.getData(), clonedXmlElement.getData());
 assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
 assertEquals(xmlElement.getElements().size(), clonedXmlElement.getElements().size());
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneNotSupportedException() {
 XmlElement xmlElement = createXmlElement();
 throw new AssertionError("CloneNotSupportedException not thrown");
 }

 @Test
 public void testSetName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.setName("NewName");
 assertEquals("NewName", xmlElement.getName());
 }
}