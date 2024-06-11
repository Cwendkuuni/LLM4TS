package com.allenstudio.ir.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.*;

public class XmlElementTest {

 @Before
 public void setUp() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.addAttribute("attr1", "value1");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 xmlElement.addElement(child1);
 xmlElement.addElement(child2);
 }

 @Test
 public void testAddAttribute() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute(null, "value1");
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetAttribute() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals(null, xmlElement.getAttribute("nonExistentAttr"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", null);
 assertEquals("defaultValue", xmlElement.getAttribute("attr1", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = new XmlElement();
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 xmlElement.setAttributes(attributes);
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 Enumeration<String> attributeNames = xmlElement.getAttributeNames();
 assertTrue(attributeNames.hasMoreElements());
 assertEquals("attr1", attributeNames.nextElement());
 assertEquals("attr2", attributeNames.nextElement());
 }

 @Test
 public void testAddElement() {
 XmlElement parent = mock(XmlElement.class);
 XmlElement child = new XmlElement("child");
 assertTrue(parent.addElement(child));
 assertEquals(child, parent.getElements().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullChild() {
 XmlElement parent = new XmlElement();
 try {
 parent.addElement(null);
 fail("Should throw NullPointerException");
 }
 catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveElement() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement();
 parent.addElement(child);
 assertEquals(1, parent.getElements().size());
 parent.removeElement(child);
 assertEquals(0, parent.getElements().size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement();
 parent.addElement(child);
 parent.removeElement(1);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement();
 XmlElement child2 = new XmlElement();
 parent.addElement(child1);
 parent.addElement(child2);
 parent.removeAllElements();
 assertEquals(0, parent.getElements().size());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement();
 parent.addElement(child);
 child.removeFromParent();
 assertEquals(0, parent.getElements().size());
 child.setParent(parent);
 parent.addElement(child);
 assertEquals(1, parent.getElements().size());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement();
 parent.append(child);
 assertEquals(1, parent.getElements().size());
 assertEquals(child, parent.getElements().get(0));
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement();
 XmlElement child2 = new XmlElement();
 parent.addElement(child1);
 parent.insertElement(child2, 0);
 assertEquals(2, parent.getElements().size());
 assertEquals(child2, parent.getElements().get(0));
 assertEquals(child1, parent.getElements().get(1));
 }

 @Test
 public void testGetElements() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement();
 XmlElement child2 = new XmlElement();
 xmlElement.addElement(child1);
 xmlElement.addElement(child2);
 List<XmlElement> elements = xmlElement.getElements();
 assertEquals(2, elements.size());
 assertEquals(child1, elements.get(0));
 assertEquals(child2, elements.get(1));
 }

 @Test
 public void testCount() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement();
 XmlElement child2 = new XmlElement();
 xmlElement.addElement(child1);
 xmlElement.addElement(child2);
 assertEquals(2, xmlElement.count());
 }

 @Test
 public void testElement() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 xmlElement.addElement(child1);
 assertEquals(child1, xmlElement.getElement("child1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testElementInvalidPath() {
 XmlElement xmlElement = new XmlElement();
 xmlElement.addElement(new XmlElement("child1"));
 xmlElement.addElement(new XmlElement("child2"));
 xmlElement.addElement(new XmlElement("child3"));
 xmlElement.addElement(new XmlElement("child4"));
 xmlElement.addElement(new XmlElement("child5"));
 assertEquals(null, xmlElement.getElement("child6"));
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testElementByIndex() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 xmlElement.addElement(child1);
 xmlElement.addElement(child2);
 assertEquals(child1, xmlElement.getElement(0));
 assertEquals(child2, xmlElement.getElement(1));
 }

 @Test
 public void testAddSubElement() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 xmlElement.addSubElement(child1);
 assertEquals(child1, xmlElement.getElements().get(0));
 xmlElement.addSubElement("child3");
 XmlElement child3 = xmlElement.getElement("child3");
 assertEquals("child3", child3.getName());
 assertEquals(null, child3.getData());
 }

 @Test
 public void testAddSubElementWithData() {
 XmlElement xmlElement = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 xmlElement.addSubElement("child3", "data");
 assertEquals("child3", xmlElement.getElements().get(0).getName());
 assertEquals("data", xmlElement.getElements().get(0).getData());
 }
}