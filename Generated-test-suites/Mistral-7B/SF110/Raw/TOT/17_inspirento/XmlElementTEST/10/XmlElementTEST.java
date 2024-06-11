package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root"); ;
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 element.addAttribute("attr1", "value1");
 assertEquals("value1", root.getElement("element").getAttribute("attr1"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attr1", "value1");
 assertEquals("value1", root.getAttribute("attr1"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("attr1", null);
 assertEquals("defaultValue", root.getAttribute("attr1", "defaultValue"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }};
 root.setAttributes(attributes);
 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 }

 @Test
 public void testGetAttributeNames() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 Enumeration<String> enumeration = root.getAttributeNames();
 List<String> attributesNames = new ArrayList<>();
 while (enumeration.hasMoreElements()) {
 attributesNames.add((String)enumeration.nextElement());
 }
 assertEquals(Arrays.asList("attr1", "attr2"), attributesNames);
 }

 @Test
 public void testAddElement() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2");;
 root.addElement(element1);
 root.addElement(element2);
 assertEquals(2, root.count());
 assertEquals("element1", root.getElement(0).getName());
 assertEquals("element2", root.getElement(1).getName());
 }

 @Test
 public void testRemoveElement() {
 XmlElement element = new XmlElement("element");;
 root.addElement(element);
 root.removeElement(element);
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2");;
 root.addElement(element1);
 root.addElement(element2);
 root.removeElement(1);
 assertEquals("element1", root.getElement(0).getName());
 assertEquals(1, root.count());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2");;
 root.addElement(element1);
 root.addElement(element2);
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child = new XmlElement("child");;
 parent.addElement(child);
 child.removeFromParent();
 assertEquals(0, parent.count());
 }

 @Test
 public void testAddSubElement() {
 XmlElement parent = root;
 for (String path : new String[]{"child1", "child2", "child3"}) {
 parent = parent.addSubElement(path);
 }
 assertEquals("child3", parent.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("name", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementNullElement() {
 root.addElement(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementIndexOutOfBounds() {
 root.removeElement(100);
 }

 @Test(expected = NullPointerException.class)
 public void testGetElementNullPath() {
 root.getElement(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetElementEmptyPath() {
 root.getElement("");
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 XmlElement clone = original.clone();
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 assertFalse(original.equals(clone));
 }

}