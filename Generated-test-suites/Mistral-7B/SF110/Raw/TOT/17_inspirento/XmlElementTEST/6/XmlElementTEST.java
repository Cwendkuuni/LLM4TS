package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setup() {
 root = new XmlElement("root");;
 }

 @Test
 public void testConstructor() {
 XmlElement e1 = new XmlElement("element1");;
 XmlElement e2 = new XmlElement("element2", new Hashtable<String, String>());
 XmlElement e3 = new XmlElement("element3", "data");
 XmlElement e4 = new XmlElement("element4", "data", new Hashtable<String, String>());
 XmlElement e5 = new XmlElement("element5", "data1", "attribute1", "attribute2");

 assertEquals("element1", e1.getName());
 assertEquals(0, e1.count());
 assertEquals(null, e1.getParent());

 assertEquals("element2", e2.getName());
 assertEquals(0, e2.count());
 assertEquals(null, e2.getParent());

 assertEquals("element3", e3.getName());
 assertEquals(0, e3.count());
 assertEquals("data", e3.getData());
 assertEquals(null, e3.getParent());

 assertEquals("element4", e4.getName());
 assertEquals(0, e4.count());
 assertEquals("data", e4.getData());
 assertEquals(null, e4.getParent());

 assertEquals("element5", e5.getName());
 assertEquals(0, e5.count());
 assertEquals("data1", e5.getData());
 assertEquals("attribute1", e5.getAttribute("attribute1"));
 assertEquals("attribute2", e5.getAttribute("attribute2"));
 assertEquals(null, e5.getParent());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("test1", "value1");
 assertEquals("value1", root.getAttribute("test1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("name", null);
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.count());
 assertEquals(child1, root.getElement(0));
 assertEquals(child2, root.getElement(1));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullElement() {
 root.addElement(null);
 }

 @Test
 public void testGetElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(child1, root.getElement("child1"));
 assertEquals(child2, root.getElement("child2"));
 assertEquals(null, root.getElement("nonexistent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementInvalidPath() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 root.addElement(new XmlElement("child3"));

 assertEquals(null, root.getElement("child1child3"));
 }

 @Test
 public void testGetElementRecursive() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 XmlElement child3 = new XmlElement("child3");
 XmlElement child4 = new XmlElement("child4");
 root.addElement(child1);
 child1.addElement(child2);
 child2.addElement(child3);
 child3.addElement(child4);

 assertEquals(child4, root.getElement("child1/child2/child3/child4"));
 }

 @Test
 public void testRemoveElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.removeElement(child2);

 assertEquals(1, root.count());
 assertNull(root.getElement(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 root.removeElement(-1);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.removeAllElements();

 assertEquals(0, root.count());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 XmlElement clone = original.clone();

 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 assertEquals(original.getAttributes().size(), clone.getAttributes().size());
 assertEquals(original.subElements.size(), clone.subElements.size());

 for (String key : original.getAttributes().keySet()) {
 assertEquals(original.getAttribute(key), clone.getAttribute(key));
 }

 for (XmlElement subElement : original.getElements()) {
 XmlElement subClone = clone.clone();
 assertEquals(subElement.getName(), subClone.getName());
 assertEquals(subElement.getData(), subClone.getData());
 assertEquals(subElement.getAttributes().size(), subClone.getAttributes().size());
 assertEquals(subElement.subElements.size(), subClone.subElements.size());
 }
 }

 @Test
 public void testEquals() {
 XmlElement e1 = new XmlElement("element1");
 XmlElement e2 = new XmlElement("element1");
 XmlElement e3 = new XmlElement("element2");

 assertTrue(e1.equals(e1));
 assertTrue(e1.equals(e2));
 assertFalse(e1.equals(e3));
 }

 @Test
 public void testHashCode() {
 XmlElement e1 = new XmlElement("element1");
 XmlElement e2 = new XmlElement("element1");
 XmlElement e3 = new XmlElement("element2");

 int hashCode1 = e1.hashCode();
 int hashCode2 = e2.hashCode();
 int hashCode3 = e3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }
}