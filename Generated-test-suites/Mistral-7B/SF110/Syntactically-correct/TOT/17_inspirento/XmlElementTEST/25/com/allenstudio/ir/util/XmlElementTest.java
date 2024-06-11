package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 XmlElement element3 = new XmlElement("element3", "data");
 XmlElement element4 = new XmlElement("element4", null);
 XmlElement element5 = new XmlElement("element5", null, new Hashtable<String, String>());

 assertEquals("element1", element1.getName());
 assertEquals(0, element1.count());
 assertEquals(null, element1.getData());
 assertEquals(null, element1.getParent());
 assertNull(element1.getAttributes());

 assertEquals("element2", element2.getName());
 assertEquals(0, element2.count());
 assertEquals(null, element2.getData());
 assertEquals(null, element2.getParent());
 assertNull(element2.getAttributes());

 assertEquals("element3", element3.getName());
 assertEquals(0, element3.count());
 assertEquals("data", element3.getData());
 assertEquals(null, element3.getParent());
 assertNull(element3.getAttributes());

 assertEquals("element4", element4.getName());
 assertEquals(0, element4.count());
 assertEquals(null, element4.getData());
 assertEquals(null, element4.getParent());
 assertNotNull(element4.getAttributes());
 assertEquals(0, element4.getAttributes().size());

 assertEquals("element5", element5.getName());
 assertEquals(0, element5.count());
 assertEquals(null, element5.getData());
 assertEquals(null, element5.getParent());
 assertNotNull(element5.getAttributes());
 assertEquals(10, element5.getAttributes().capacity());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "testValue");
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("test", null);
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 }

 @Test
 public void testGetAttributeNull() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("test1", "testValue1");
 root.addAttribute("test2", "testValue2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("testValue1", attributes.get("test1"));
 assertEquals("testValue2", attributes.get("test2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("test1", "testValue1");
 root.setAttributes(attributes);
 assertEquals("testValue1", root.getAttribute("test1"));
 }

 @Test
 public void testGetAttributeNames() {
 root.addAttribute("test1", "testValue1");
 root.addAttribute("test2", "testValue2");
 final Enumeration<String> attributeNames = root.getAttributeNames();
 String name;
 while (attributeNames.hasMoreElements()) {
 name = attributeNames.nextElement();
 assertEquals("test1", name);
 assertEquals("test2", name);
 }
 }

 @Test
 public void testAddElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 assertEquals(1, root.count());
 assertEquals(child, root.getElement(0));
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 assertEquals(1, root.count());
 root.removeElement(child);
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 root.removeElement(1);
 assertEquals(1, root.count());
 assertEquals(child1, root.getElement(0));
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.removeFromParent();
 assertEquals(0, root.count());
 assertEquals(null, child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("child");
 root.append(child);
 assertEquals(1, root.count());
 assertEquals(child, root.getElement(0));
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.insertElement(child2, 0);
 assertEquals(2, root.count());
 assertEquals(child2, root.getElement(0));
 assertEquals(child1, root.getElement(1));
 }

 @Test
 public void testGetElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 List<XmlElement> elements = root.getElements();
 assertEquals(2, elements.size());
 assertEquals(child1, elements.get(0));
 assertEquals(child2, elements.get(1));
 }

 @Test
 public void testCount() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 XmlElement clone = (XmlElement)original.clone();
 assertTrue(original != clone);
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 assertEquals(original.getAttributes(), clone.getAttributes());
 assertEquals(original.count(), clone.count());
 for (int i = 0; i < original.count(); ++i) {
 XmlElement child1 = original.getElement(i);
 XmlElement child2 = clone.getElement(i);
 assertEquals(child1.getName(), child2.getName());
 assertEquals(child1.getData(), child2.getData());
 assertEquals(child1.getAttributes(), child2.getAttributes());
 }
 }

 @Test
 public void testNotifyObservers() {
 XmlElement observer = new XmlElement();
 root.addObserver(observer);
 root.setName("newName");
 observer.notifyObservers();
 assertEquals("newName", observer.getName());
 }
}