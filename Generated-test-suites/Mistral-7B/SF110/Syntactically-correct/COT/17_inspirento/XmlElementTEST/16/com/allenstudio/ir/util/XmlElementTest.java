package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement xmlElement;

 @Before
 public void setUp() {
 xmlElement = new XmlElement("root");
 }

 @Test
 public void testXmlElementConstructor() {
 XmlElement xmlElement = new XmlElement("elementName");
 assertEquals("elementName", xmlElement.getName());
 assertTrue(xmlElement.getAttributes().isEmpty());
 assertTrue(xmlElement.getSubElements().isEmpty());
 }

 @Test
 public void testXmlElementConstructorWithAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 XmlElement xmlElement = new XmlElement("elementName", attributes);
 assertEquals("elementName", xmlElement.getName());
 assertEquals(2, xmlElement.getAttributes().size());
 assertTrue(xmlElement.getSubElements().isEmpty());
 }

 @Test
 public void testXmlElementConstructorWithData() {
 XmlElement xmlElement = new XmlElement("elementName", "data");
 assertEquals("elementName", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 assertTrue(xmlElement.getAttributes().isEmpty());
 assertTrue(xmlElement.getSubElements().isEmpty());
 }

 @Test
 public void testAddAttribute() {
 xmlElement.addAttribute("attribute1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullName() {
 xmlElement.addAttribute(null, "value1");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullValue() {
 xmlElement.addAttribute("attribute1", null);
 }

 @Test
 public void testAddSubElement() {
 XmlElement subElement = new XmlElement("subElement");
 xmlElement.addSubElement(subElement);
 assertEquals(1, xmlElement.getSubElements().size());
 assertEquals(subElement, xmlElement.getSubElements().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementWithNullElement() {
 XmlElement xmlElement = new XmlElement("root");
 try {
 xmlElement.addSubElement(null);
 } catch (NullPointerException e) {
 //Expected
 }
 }

 @Test
 public void testGetElement() {
 XmlElement element1 = new XmlElement("element1");
 xmlElement.addElement(element1);
 assertEquals(element1, xmlElement.getElement("element1"));
 }

 @Test
 public void testGetElementWithPath() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 xmlElement.addElement(element1);
 element1.addElement(element2);
 assertEquals(element2, xmlElement.getElement("element1/element2"));
 }

 @Test
 public void testGetElementWithInvalidPath() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 xmlElement.addElement(element1);
 assertEquals(null, xmlElement.getElement("element2"));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("root");
 original.addAttribute("attribute1", "value1");
 original.addSubElement("element1");
 original.addSubElement("element2");
 XmlElement cloned = (XmlElement)original.clone();
 assertEquals(original, cloned);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneWithCloneNotSupportedException() {
 XmlElement xmlElement = new XmlElement("root");
 throw new CloneNotSupportedException();
 }

}