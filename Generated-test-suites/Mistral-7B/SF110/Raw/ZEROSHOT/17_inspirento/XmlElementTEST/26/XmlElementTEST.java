package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

 private XmlElement createXmlElement() {
 return new XmlElement("root");
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("root", xmlElement.getName());
 assertEquals(0, xmlElement.count());
 }

 @Test
 public void testXmlElementAddAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementNullAttributeName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value1");
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementNullAttributeValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", null);
 }

 @Test
 public void testXmlElementGetAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals(null, xmlElement.getAttribute("attr2"));
 }

 @Test
 public void testXmlElementGetAttributeWithDefaultValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1", "defaultValue"));
 }

 @Test
 public void testXmlElementGetAttributes() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testXmlElementSetAttributes() {
 XmlElement xmlElement = createXmlElement();
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("attr1", "value1");
 xmlElement.setAttributes(attributes);
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 }

 @Test
 public void testXmlElementgetAttributeNames() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 Enumeration<String> attributeNames = xmlElement.getAttributeNames();
 List<String> attributeNamesList = new ArrayList<>();
 while (attributeNames.hasMoreElements()) {
 attributeNamesList.add((String)attributeNames.nextElement());
 }
 assertEquals(2, attributeNamesList.size());
 assertEquals("attr1", attributeNamesList.get(0));
 assertEquals("attr2", attributeNamesList.get(1));
 }

 @Test
 public void testXmlElementAddElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child");
 parentXmlElement.addElement(childXmlElement);
 assertEquals(1, parentXmlElement.count());
 assertEquals(childXmlElement, parentXmlElement.getElement(0));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementNullChildElement() {
 XmlElement parentXmlElement = createXmlElement();
 parentXmlElement.addElement(null);
 }

 @Test
 public void testXmlElementRemoveElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child");
 parentXmlElement.addElement(childXmlElement);
 parentXmlElement.removeElement(childXmlElement);
 assertEquals(0, parentXmlElement.count());
 }

 @Test
 public void testXmlElementRemoveElementByIndex() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement child1XmlElement = new XmlElement("child1");
 XmlElement child2XmlElement = new XmlElement("child2");
 parentXmlElement.addElement(child1XmlElement);
 parentXmlElement.addElement(child2XmlElement);
 parentXmlElement.removeElement(1);
 assertEquals(1, parentXmlElement.count());
 assertEquals(child1XmlElement, parentXmlElement.getElement(0));
 }

 @Test
 public void testXmlElementRemoveAllElements() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement child1XmlElement = new XmlElement("child1");
 XmlElement child2XmlElement = new XmlElement("child2");
 parentXmlElement.addElement(child1XmlElement);
 parentXmlElement.addElement(child2XmlElement);
 parentXmlElement.removeAllElements();
 assertEquals(0, parentXmlElement.count());
 }

 @Test
 public void testXmlElementRemoveFromParent() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child");
 parentXmlElement.addElement(childXmlElement);
 childXmlElement.removeFromParent();
 assertEquals(0, parentXmlElement.count());
 assertEquals(null, childXmlElement.getParent());
 }

 @Test
 public void testXmlElementAppend() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child");
 parentXmlElement.append(childXmlElement);
 assertEquals(1, parentXmlElement.count());
 assertEquals(childXmlElement, parentXmlElement.getElement(0));
 }

 @Test
 public void testXmlElementInsertElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement child1XmlElement = new XmlElement("child1");
 XmlElement child2XmlElement = new XmlElement("child2");
 parentXmlElement.addElement(child1XmlElement);
 parentXmlElement.insertElement(child2XmlElement, 0);
 assertEquals(2, parentXmlElement.count());
 assertEquals(child2XmlElement, parentXmlElement.getElement(0));
 assertEquals(child1XmlElement, parentXmlElement.getElement(1));
 }

 @Test
 public void testXmlElementGetElements() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement child1XmlElement = new XmlElement("child1");
 XmlElement child2XmlElement = new XmlElement("child2");
 parentXmlElement.addElement(child1XmlElement);
 parentXmlElement.addElement(child2XmlElement);
 List<XmlElement> elements = parentXmlElement.getElements();
 assertEquals(2, elements.size());
 assertEquals(child1XmlElement, elements.get(0));
 assertEquals(child2XmlElement, elements.get(1));
 }

 @Test
 public void testXmlElementCount() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addElement(new XmlElement("child1"));
 xmlElement.addElement(new XmlElement("child2"));
 xmlElement.addElement(new XmlElement("child3"));
 assertEquals(3, xmlElement.count());
 }

 @Test
 public void testXmlElementGetElement() {
 XmlElement xmlElement = createXmlElement();
 XmlElement child1XmlElement = new XmlElement("child1");
 XmlElement child2XmlElement = new XmlElement("child2");
 xmlElement.addElement(child1XmlElement);
 xmlElement.addElement(child2XmlElement);
 assertEquals(child1XmlElement, xmlElement.getElement("child1"));
 assertEquals(child2XmlElement, xmlElement.getElement("child2"));
 assertEquals(null, xmlElement.getElement("nonExistentChild"));
 }

 @Test
 public void testXmlElementAddSubElement() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = parentXmlElement.addSubElement("child/grandchild");
 assertEquals("grandchild", childXmlElement.getName());
 assertEquals(null, childXmlElement.getParent());
 assertEquals(0, parentXmlElement.count());
 parentXmlElement.printNode(null);
 }

 @Test
 public void testXmlElementAddSubElementWithParent() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child");
 parentXmlElement.addElement(childXmlElement);
 XmlElement grandchildXmlElement = parentXmlElement.addSubElement("child/grandchild");
 assertEquals("grandchild", grandchildXmlElement.getName());
 assertEquals(childXmlElement, grandchildXmlElement.getParent());
 assertEquals(1, parentXmlElement.count());
 parentXmlElement.printNode(null);
 }

 @Test
 public void testXmlElementAddSubElementWithData() {
 XmlElement parentXmlElement = createXmlElement();
 XmlElement childXmlElement = new XmlElement("child", "data");
 parentXmlElement.addElement(childXmlElement);
 XmlElement grandchildXmlElement = parentXmlElement.addSubElement("child/grandchild");
 assertEquals("grandchild", grandchildXmlElement.getName());
 assertEquals("data", parentXmlElement.getData());
 assertEquals(1, parentXmlElement.count());
 parentXmlElement.printNode(null);
 }

 @Test
 public void testXmlElementClone() {
 XmlElement originalXmlElement = createXmlElement();
 originalXmlElement.addAttribute("attr1", "value1");
 originalXmlElement.addAttribute("attr2", "value2");
 originalXmlElement.addElement(new XmlElement("child1"));
 originalXmlElement.addElement(new XmlElement("child2"));
 XmlElement clonedXmlElement = (XmlElement)originalXmlElement.clone();
 assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
 assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());
 assertEquals(originalXmlElement.count(), clonedXmlElement.count());
 assertEquals(originalXmlElement.getElements(), clonedXmlElement.getElements());
 }

 @Test
 public void testXmlElementSetName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.setName("newName");
 assertEquals("newName", xmlElement.getName());
 }

 @Test
 public void testXmlElementNotifyObservers() {
 XmlElement xmlElement = new XmlElement("root") {
 @Override
 public void notifyObservers() {
 super.notifyObservers();
 fail("Should not call super.notifyObservers()");
 }
 };
 XmlElement observer = new XmlElement("observer");
 xmlElement.addObserver(observer);
 xmlElement.setName("newName");
 }

 @Test
 public void testXmlElementEquals() {
 XmlElement xmlElement1 = createXmlElement();
 xmlElement1.addAttribute("attr1", "value1");
 xmlElement1.addAttribute("attr2", "value2");
 XmlElement xmlElement2 = createXmlElement();
 xmlElement2.addAttribute("attr1", "value1");
 xmlElement2.addAttribute("attr2", "value2");
 XmlElement xmlElement3 = new XmlElement("newName");
 assertTrue(xmlElement1.equals(xmlElement1));
 assertTrue(xmlElement1.equals(xmlElement2));
 assertFalse(xmlElement1.equals(xmlElement3));
 }

 @Test
 public void testXmlElementHashCode() {
 XmlElement xmlElement1 = createXmlElement();
 xmlElement1.addAttribute("attr1", "value1");
 xmlElement1.addAttribute("attr2", "value2");
 XmlElement xmlElement2 = createXmlElement();
 xmlElement2.addAttribute("attr1", "value1");
 xmlElement2.addAttribute("attr2", "value2");
 XmlElement xmlElement3 = new XmlElement("newName");
 int hashCode1 = xmlElement1.hashCode();
 int hashCode2 = xmlElement2.hashCode();
 int hashCode3 = xmlElement3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}