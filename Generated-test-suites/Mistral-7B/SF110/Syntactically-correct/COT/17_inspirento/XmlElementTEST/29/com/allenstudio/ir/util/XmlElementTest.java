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
 public void testXmlElementAddAttributeNullName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddAttributeNullValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("name", null);
 }

 @Test
 public void testXmlElementGetAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals(null, xmlElement.getAttribute("nonexistentattr"));
 }

 @Test
 public void testXmlElementGetAttributeWithDefaultValue() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("", xmlElement.getAttribute("attr1", "defaultValue"));
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
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 xmlElement.setAttributes(attributes);
 assertEquals(2, xmlElement.countAttributes());
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementSetAttributesNullAttributes() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.setAttributes(null);
 }

 @Test
 public void testXmlElementAddElement() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 assertEquals(1, parent.count());
 assertEquals(child, parent.getElement(0));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddElementNullChild() {
 XmlElement parent = createXmlElement();
 XmlElement child = null;
 parent.addElement(child);
 }

 @Test
 public void testXmlElementRemoveElement() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 parent.removeElement(child);
 assertEquals(0, parent.count());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testXmlElementRemoveElementInvalidIndex() {
 XmlElement parent = createXmlElement();
 parent.removeElement(1);
 }

 @Test
 public void testXmlElementRemoveAllElements() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 parent.removeAllElements();
 assertEquals(0, parent.count());
 }

 @Test
 public void testXmlElementAddSubElement() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addSubElement(child);
 assertEquals(1, parent.count());
 assertEquals(child, parent.getElement(0));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddSubElementNullChild() {
 XmlElement parent = createXmlElement();
 XmlElement child = null;
 parent.addSubElement(child);
 }

 @Test
 public void testXmlElementAddSubElementWithPath() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 child1.addElement(child2);
 XmlElement grandChild = new XmlElement("grandChild");
 parent.addSubElement("child1/grandChild", grandChild);
 assertEquals(3, parent.count());
 assertEquals(grandChild, parent.getElement("child1/grandChild"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlElementAddSubElementWithInvalidPath() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 parent.addSubElement("child/child", child);
 }

 @Test
 public void testXmlElementClone() throws CloneNotSupportedException {
 XmlElement original = createXmlElement();
 XmlElement clone = (XmlElement)original.clone();
 assertEquals(original.getAttributes(), clone.getAttributes());
 assertEquals(original.getData(), clone.getData());
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.subElements.size(), clone.subElements.size());
 }

 @Test
 public void testXmlElementSetName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.setName("newName");
 assertEquals("newName", xmlElement.getName());
 }

 @Test
 public void testXmlElementPrintNode() {
 XmlElement root = new XmlElement("root");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 child1.addAttribute("attr1", "value1");
 child1.addAttribute("attr2", "value2");
 child2.setData("data");
 root.printNode(System.out);
 }
}