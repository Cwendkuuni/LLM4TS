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
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attr1", "value1");
 assertEquals("value1", element.getAttribute("attr1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement element = new XmlElement("element");
 element.addAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", null);
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 }

 @Test
 public void testAddElementWithPath() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement("child1", child1);
 root.addElement("child1/child2", child2);
 assertEquals(2, root.count());
 }

 @Test
 public void testAddSubElementWithPath() {
 XmlElement child1 = new XmlElement("child1");
 root.addSubElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addSubElement("child1/child2", child2);
 assertEquals(2, root.count());
 }

 @Test
 public void testGetElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement("child1", child1);
 root.addElement("child1/child2", child2);
 XmlElement element = root.getElement("child1/child2");
 assertEquals(child2, element);
 }

 @Test(expected = NullPointerException.class)
 public void testGetElementNullPath() {
 root.getElement(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementEmptyPath() {
 root.getElement("");
 }

 @Test
 public void testPrintNode() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 child2.setAttribute("attr1", "value1");
 root.addElement("child1", child1);
 root.addElement("child1/child2", child2);
 String expected = "root\nchild1 = null\nchild1:\nchild2:attr1:value1\n";
 XmlElement.printNode(root, "");
 assertEquals(expected, System.out.getBuffer().toString());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("root");
 original.addAttribute("attr1", "value1");
 XmlElement cloned = (XmlElement)original.clone();
 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getAttributes(), cloned.getAttributes());
 assertEquals(original.getElements(), cloned.getElements());
 }

 @Test
 public void testCloneWithSubElements() {
 XmlElement original = new XmlElement("root");
 XmlElement child1 = new XmlElement("child1");
 original.addElement("child1", child1);
 XmlElement cloned = (XmlElement)original.clone();
 XmlElement clonedChild1 = cloned.getElement("child1");
 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getAttributes(), cloned.getAttributes());
 assertEquals(original.getElements(), cloned.getElements());
 assertEquals(original.subElements.size(), cloned.subElements.size());
 assertEquals(original.subElements.get(0), cloned.subElements.get(0));
 }
}