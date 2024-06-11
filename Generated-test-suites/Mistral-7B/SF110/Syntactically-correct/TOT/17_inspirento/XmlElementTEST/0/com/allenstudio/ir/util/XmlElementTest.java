package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

XmlElement root;
XmlElement element1;
XmlElement element2;
XmlElement element3;

@Before
public void setUp() {
 root = new XmlElement("root");;
 element1 = new XmlElement("element1");
 element2 = new XmlElement("element2", "data2");
 element3 = new XmlElement("element3", "data3");
 root.addElement(element1);
 root.addElement(element2);
 root.addElement(element3);
}

@Test
public void testAddAttribute() {
 element1.addAttribute("attribute1", "value1");
 assertEquals("value1", element1.getAttribute("attribute1"));
}

@Test
public void testGetAttributeNull() {
 assertNull(element1.getAttribute("nonExistentAttribute"));
}

@Test
public void testGetAttributeDefaultValue() {
 element1.addAttribute("attribute1", null);
 assertEquals("", element1.getAttribute("attribute1"));
}

@Test
public void testGetAttributes() {
 Hashtable<String, String> attributes = element1.getAttributes();
 assertEquals(1, attributes.size());
 assertEquals("value1", attributes.get("attribute1"));
}

@Test
public void testSetAttributes() {
 Hashtable<String, String> newAttributes = new Hashtable<>();
 newAttributes.put("newAttribute", "newValue");
 element1.setAttributes(newAttributes);
 assertEquals("newValue", element1.getAttribute("newAttribute"));
}

@Test
public void testAttributeNames() {
 List<String> attributeNames = new ArrayList<>();
 for (String name : element1.getAttributeNames()) {
 attributeNames.add(name);
 }
 assertEquals(1, attributeNames.size());
 assertEquals("attribute1", attributeNames.get(0));
}

@Test
public void testAddElement() {
 XmlElement newElement = new XmlElement("newElement");
 root.addElement(newElement);
 assertTrue(root.getElements().contains(newElement));
}

@Test
public void testRemoveElement() {
 XmlElement removeElement = root.getElement("root/element1");
 root.removeElement(removeElement);
 assertFalse(root.getElements().contains(removeElement));
}

@Test
public void testRemoveFromParent() {
 XmlElement removeElement = element1;
 element1.removeFromParent();
 assertNull(removeElement.getParent());
}

@Test
public void testGetElement() {
 XmlElement element = root.getElement("root/element2");
 assertEquals(element2, element);
}

@Test(expected = NullPointerException.class)
public void testGetElementNullPath() {
 root.getElement(null);
}

@Test(expected = IllegalArgumentException.class)
public void testGetElementEmptyPath() {
 root.getElement("");
}

@Test(expected = IllegalArgumentException.class)
public void testGetElementInvalidPath() {
 root.getElement("root/nonExistentElement");
}

@Test
public void testAddSubElement() {
 XmlElement newElement = new XmlElement("newElement");
 root.addSubElement("newElement");
 assertTrue(root.getElements().contains(newElement));
}

@Test
public void testClone() {
 XmlElement cloned = (XmlElement) root.clone();
 assertEquals(root.getName(), cloned.getName());
 assertEquals(root.getAttributes().size(), cloned.getAttributes().size());
 assertEquals(root.getElements().size(), cloned.getElements().size());
}
}