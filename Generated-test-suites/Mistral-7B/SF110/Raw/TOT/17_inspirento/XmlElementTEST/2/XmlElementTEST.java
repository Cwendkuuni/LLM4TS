package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setup() {
 root = new XmlElement("root");;
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attribute", "value");
 assertTrue(element.getAttribute("attribute").equals("value"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute(null, "value");
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attribute", null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGetAttributeNullName() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttribute() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attribute", "value");
 assertEquals("value", root.getElementById("element").getAttribute("attribute"));
 }

 @Test
 public void testGetAttributeDefaultValue() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 assertEquals("defaultValue", root.getElementById("element").getAttribute("attribute", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 Hashtable<String, String> attributes = element.getAttributes();
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 element.setAttributes(attributes);
 assertEquals("value1", element.getAttribute("attribute1"));
 assertEquals("value2", element.getAttribute("attribute2"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 Enumeration<String> attributeNames = element.getAttributeNames();
 List<String> attributeNamesList = new ArrayList<String>();
 while (attributeNames.hasMoreElements()) {
 attributeNamesList.add((String)attributeNames.nextElement());
 }
 assertTrue(attributeNamesList.containsAll(Arrays.asList("attribute1", "attribute2")));
 }

 @Test
 public void testAddElementNullElement() {
 XmlElement rootElement = new XmlElement("root");;
 assertFalse(rootElement.addElement(null));
 }

 @Test
 public void testRemoveElementNullElement() {
 XmlElement rootElement = new XmlElement("root");;
 rootElement.addElement(new XmlElement("element"));
 assertNull(rootElement.removeElement(null));
 }

 @Test
 public void testRemoveElementNotExist() {
 XmlElement rootElement = new XmlElement("root");;
 rootElement.addElement(new XmlElement("element1"));
 rootElement.addElement(new XmlElement("element2"));
 assertNull(rootElement.removeElement("nonExistentElement"));
 }

 @Test
 public void testRemoveElement() {
 XmlElement rootElement = new XmlElement("root");;
 rootElement.addElement(new XmlElement("element1"));
 rootElement.addElement(new XmlElement("element2"));
 XmlElement elementToRemove = rootElement.getElementById("element1");
 rootElement.removeElement(elementToRemove);
 assertTrue(rootElement.subElements.size() == 1);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement rootElement = new XmlElement("root");;
 rootElement.addElement(new XmlElement("element1"));
 rootElement.addElement(new XmlElement("element2"));
 rootElement.removeAllElements();
 assertTrue(rootElement.subElements.isEmpty());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child = new XmlElement("child");;
 parent.addElement(child);
 child.removeFromParent();
 assertNull(child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.append(child2);
 assertEquals(2, parent.subElements.size());
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.insertElement(child2, 0);
 assertEquals(2, parent.subElements.size());
 assertEquals(child2, parent.getElements().get(0));
 }

 @Test
 public void testGetElements() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(2, parent.count());
 }

 @Test
 public void testCount() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(2, parent.count());
 }

 @Test
 public void testGetElementNullPath() {
 assertEquals(null, root.getElementById(null));
 }

 @Test
 public void testGetElementPathNotExist() {
 assertEquals(null, root.getElementById("nonExistentElement"));
 }

 @Test
 public void testGetElementPath() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(child2, root.getElementById("parent/child2"));
 }

 @Test
 public void testGetElementIndex() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(child2, parent.getElement(1));
 }

 @Test
 public void testAddSubElementNullName() {
 XmlElement parent = new XmlElement("parent");;
 assertNull(parent.addSubElement(null));
 }

 @Test
 public void testAddSubElementNullData() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement element = new XmlElement("element");;
 parent.addElement(element);
 assertNull(parent.addSubElement("element", null));
 }

 @Test
 public void testAddSubElementValid() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child = new XmlElement("child");;
 parent.addElement(child);
 assertEquals(child, parent.addSubElement("child"));
 }

 @Test
 public void testAddSubElementPathInvalid() {
 XmlElement parent = new XmlElement("parent");;
 assertNull(parent.addSubElement("nonExistentElement"));
 }

 @Test
 public void testAddSubElementPathValid() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 parent.addElement(child1);
 assertEquals(child2, parent.addSubElement("child1/child2"));
 }

 @Test
 public void testAddSubElementPathMultiLevel() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 XmlElement child3 = new XmlElement("child3");;
 parent.addElement(child1);
 assertEquals(child3, parent.addSubElement("child1/child2/child3"));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");;
 XmlElement cloned = original.clone();
 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getData(), cloned.getData());
 assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
 assertTrue(original.getAttributes().keySet().equals(cloned.getAttributes().keySet()));
 assertEquals(original.subElements.size(), cloned.subElements.size());
 assertEquals(original.getElements(), cloned.getElements());
 }

 @Test
 public void testEqualsSelf() {
 XmlElement element = new XmlElement("element");;
 assertTrue(element.equals(element));
 }

 @Test
 public void testEqualsDifferentObjects() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2");;
 assertFalse(element1.equals(element2));
 }

 @Test
 public void testEqualsSameAttributesButDifferentData() {
 XmlElement element1 = new XmlElement("element1", "data1");;
 XmlElement element2 = new XmlElement("element1", "data2");;
 assertFalse(element1.equals(element2));
 }

 @Test
 public void testEqualsSameAttributesAndDataButDifferentSubElements() {
 XmlElement element1 = new XmlElement("element1", "data1");;
 XmlElement subElement1 = new XmlElement("subElement1");;
 XmlElement subElement2 = new XmlElement("subElement2");;
 element1.addElement(subElement1);
 XmlElement element2 = new XmlElement("element1", "data1");;
 XmlElement subElement3 = new XmlElement("subElement3");;
 element2.addElement(subElement3);
 assertFalse(element1.equals(element2));
 }

 @Test
 public void testEqualsSameAttributesAndDataAndSubElements() {
 XmlElement element1 = new XmlElement("element1", "data1");;
 XmlElement subElement1 = new XmlElement("subElement1");;
 XmlElement subElement2 = new XmlElement("subElement2");;
 element1.addElement(subElement1);
 element1.addElement(subElement2);
 XmlElement element2 = new XmlElement("element1", "data1");;
 XmlElement subElement1Copy = new XmlElement("subElement1").clone();
 XmlElement subElement2Copy = new XmlElement("subElement2").clone();
 element2.addElement(subElement1Copy);
 element2.addElement(subElement2Copy);
 assertTrue(element1.equals(element2));
 }
}