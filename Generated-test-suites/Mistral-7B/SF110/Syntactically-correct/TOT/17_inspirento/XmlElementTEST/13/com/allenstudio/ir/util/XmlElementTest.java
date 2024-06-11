package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

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
 element.addAttribute(null, "value1");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", null);
 }

 @Test
 public void testGetAttributeNull() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attr1", "value1");
 assertEquals("value1", root.getAttribute("attr1"));
 }

 @Test
 public void testGetAttributeDefaultValue() {
 root.addAttribute("attr1", null);
 assertEquals("", root.getAttribute("attr1", "defaultValue"));
 }

 @Test
 public void testGetAttributesEmpty() {
 assertEquals(0, root.getAttributes().size());
 }

 @Test
 public void testGetAttributesWithAttributes() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 assertEquals(1, root.getElements().size());
 assertEquals(child1, root.getElements().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullElement() {
 XmlElement root = new XmlElement("root");
 root.addElement(null);
 }

 @Test
 public void testRemoveElement() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 assertEquals(1, root.getElements().size());
 root.removeElement(child1);
 assertEquals(0, root.getElements().size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveElementNullElement() {
 XmlElement root = new XmlElement("root");
 root.removeElement(null);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.getElements().size());
 root.removeAllElements();
 assertEquals(0, root.getElements().size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveFromParentNullElement() {
 XmlElement element = new XmlElement("element");
 element.removeFromParent();
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.insertElement(child2, 0);
 assertEquals(2, root.getElements().size());
 assertEquals(child2, root.getElements().get(0));
 assertEquals(child1, root.getElements().get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementNegativeIndex() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 root.insertElement(new XmlElement("child2"), -1);
 }

 @Test(expected = NullPointerException.class)
 public void testInsertElementNullElement() {
 XmlElement root = new XmlElement("root");
 XmlElement elementToInsert = null;
 root.insertElement(elementToInsert, 0);
 }

 @Test
 public void testAppend() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.append(child2);
 assertEquals(2, root.getElements().size());
 assertEquals(child2, root.getElements().get(1));
 }

 @Test
 public void testClone() {
 XmlElement originalElement = new XmlElement("element") {{
 addAttribute("attr1", "value1");
 addAttribute("attr2", "value2");
 addSubElement(new XmlElement("subElement1"));
 addSubElement(new XmlElement("subElement2"));
 }};
 XmlElement clonedElement = (XmlElement)originalElement.clone();
 assertEquals(originalElement.getName(), clonedElement.getName());
 assertEquals(originalElement.getData(), clonedElement.getData());
 assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
 assertEquals(originalElement.getAttributes().get("attr1"), clonedElement.getAttributes().get("attr1"));
 assertEquals(originalElement.getAttributes().get("attr2"), clonedElement.getAttributes().get("attr2"));
 assertEquals(originalElement.getSubElements().size(), clonedElement.getSubElements().size());
 assertEquals(originalElement.getSubElements().get(0).getName(), clonedElement.getSubElements().get(0).getName());
 assertEquals(originalElement.getSubElements().get(1).getName(), clonedElement.getSubElements().get(1).getName());
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneNotSupported() {
 XmlElement originalElement = new XmlElement("element") {{
 addAttribute("attr1", "value1");
 addAttribute("attr2", "value2");
 addSubElement(new XmlElement("subElement1"));
 addSubElement(new XmlElement("subElement2"));
 }};
 try {
 XmlElement clonedElement = (XmlElement)originalElement.clone();
 fail();
 }
 catch (CloneNotSupportedException e) {
 // expected
 }
 }

 @Test
 public void testSetParent() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child = new XmlElement("child");
 child.setParent(parent);
 assertEquals(parent, child.getParent());
 child.removeFromParent();
 assertEquals(null, child.getParent());
 }

 @Test(expected = NullPointerException.class)
 public void testSetParentNullElement() {
 XmlElement child = new XmlElement("child");
 child.setParent(null);
 }

 @Test
 public void testSetData() {
 XmlElement element = new XmlElement("element");
 element.setData("data");
 assertEquals("data", element.getData());
 }

 @Test
 public void testSetName() {
 XmlElement element = new XmlElement("element") {{
 setName("newName");
 }};
 assertEquals("newName", element.getName());
 }
}