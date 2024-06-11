package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testConstructor() {
 XmlElement element = new XmlElement();
 assertEquals("", element.getData());
 assertEquals(10, element.getAttributes().capacity());
 assertTrue(element.subElements instanceof Vector);
 assertNull(element.getName());

 element = new XmlElement("elementName");
 assertEquals("elementName", element.getName());
 assertTrue(element.subElements instanceof Vector);
 assertEquals(10, element.getAttributes().capacity());
 assertNull(element.getData());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", null);
 root.addAttribute("key3", "value3");

 assertEquals("value1", root.getAttribute("key1"));
 assertNull(root.getAttribute("key2"));
 assertEquals("value3", root.getAttribute("key3"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 assertEquals("value1", root.getAttribute("key1"));
 assertEquals("value2", root.getAttribute("key2", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 root.setAttributes(attributes);
 assertEquals("value1", root.getAttribute("key1"));
 assertEquals("value2", root.getAttribute("key2"));
 }

 @Test
 public void testAttributeNames() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 Enumeration<String> names = root.getAttributeNames();
 String name;
 while (names.hasMoreElements()) {
 name = names.nextElement();
 assertEquals("key1", name);
 assertEquals("key2", name);
 }
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");

 root.addElement(child1);
 root.addElement(child2);

 assertTrue(root.subElements.contains(child1));
 assertTrue(root.subElements.contains(child2));
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);

 XmlElement removedChild = root.removeElement(child);
 assertTrue(root.subElements.remove(child));
 assertEquals(child, removedChild);
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.removeFromParent();
 assertNull(child.getParent());
 }

 @Test(expected = NullPointerException.class)
 public void testNullParent() {
 XmlElement child = new XmlElement("child");
 child.setParent(null);
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 XmlElement clone = original.clone();

 assertEquals(original.getData(), clone.getData());
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getAttributes().size(), clone.getAttributes().size());

 for (Map.Entry<String, String> entry : original.getAttributes().entrySet()) {
 assertEquals(entry.getKey(), clone.getAttribute(entry.getKey()));
 }

 List<XmlElement> originalChildren = original.getElements();
 List<XmlElement> cloneChildren = clone.getElements();

 assertEquals(originalChildren.size(), cloneChildren.size());
 for (XmlElement child : originalChildren) {
 XmlElement cloneChild = clone.getElement(child.getName());
 assertEquals(child.getData(), cloneChild.getData());
 assertEquals(child.getName(), cloneChild.getName());
 }
 }
}