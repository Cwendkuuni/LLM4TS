package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

private XmlElement rootElement;

@Before
public void setUp() {
this.rootElement = new XmlElement("root");;
}

@Test
public void testXmlElementCreation() {
XmlElement element1 = new XmlElement("element1");
XmlElement element2 = new XmlElement("element2", "data1");
XmlElement element3 = new XmlElement("element3", "data2", new Hashtable<String, String>() {{
put("attribute1", "value1");
put("attribute2", "value2");
}});

assertEquals("element1", element1.getName());
assertEquals("data1", element2.getData());
assertEquals("value1", element3.getAttribute("attribute1"));
assertEquals("value2", element3.getAttribute("attribute2", "defaultValue"));
assertTrue(element3.getAttributes().containsKey("attribute1"));
assertTrue(element3.getAttributes().containsKey("attribute2"));
}

@Test
public void testAddAttribute() {
XmlElement element = new XmlElement("element");
element.addAttribute("attribute1", "value1");
element.addAttribute("attribute2", "value2");

assertTrue(element.getAttributes().containsKey("attribute1"));
assertTrue(element.getAttributes().containsKey("attribute2"));
assertEquals("value1", element.getAttribute("attribute1"));
assertEquals("value2", element.getAttribute("attribute2", "defaultValue"));
}

@Test
public void testGetAttribute() {
XmlElement element = new XmlElement("element");
element.addAttribute("attribute1", "value1");
element.setData("data1");

assertEquals("value1", element.getAttribute("attribute1"));
assertEquals("data1", element.getData());
assertEquals("data1", element.getAttribute("data", "defaultValue"));
}

@Test
public void testSetAttributes() {
XmlElement element = new XmlElement("element");
Hashtable<String, String> attributes = new Hashtable<>();
attributes.put("attribute1", "value1");
attributes.put("attribute2", "value2");
element.setAttributes(attributes);

assertTrue(element.getAttributes().containsKey("attribute1"));
assertTrue(element.getAttributes().containsKey("attribute2"));
assertEquals("value1", element.getAttribute("attribute1"));
assertEquals("value2", element.getAttribute("attribute2", "defaultValue"));
}

@Test
public void testAddElement() {
XmlElement parentElement = this.rootElement;
XmlElement childElement1 = new XmlElement("child1");
XmlElement childElement2 = new XmlElement("child2");

parentElement.addElement(childElement1);
parentElement.addElement(childElement2);

assertTrue(parentElement.getElements().contains(childElement1));
assertTrue(parentElement.getElements().contains(childElement2));
assertEquals(childElement1, parentElement.getElement("child1"));
assertEquals(childElement2, parentElement.getElement("child2"));
}

@Test
public void testAddSubElement() {
XmlElement parentElement = this.rootElement;
XmlElement childElement1 = parentElement.addSubElement("child1");
XmlElement childElement2 = parentElement.addSubElement("child2", "data1");
XmlElement childElement3 = parentElement.addSubElement("child3", "data2", new Hashtable<String, String>() {{
put("attribute1", "value1");
put("attribute2", "value2");
}});

assertTrue(parentElement.getElements().contains(childElement1));
assertTrue(parentElement.getElements().contains(childElement2));
assertTrue(parentElement.getElements().contains(childElement3));
assertEquals(childElement1, parentElement.getElement("child1"));
assertEquals(childElement2, parentElement.getElement("child2"));
assertEquals(childElement3, parentElement.getElement("child3"));
}

@Test
public void testRemoveElement() {
XmlElement parentElement = this.rootElement;
XmlElement childElement1 = new XmlElement("child1");
XmlElement childElement2 = new XmlElement("child2");
parentElement.addElement(childElement1);
parentElement.addElement(childElement2);

assertEquals(2, parentElement.getElements().size());
parentElement.removeElement(childElement1);

assertEquals(1, parentElement.getElements().size());
assertFalse(parentElement.getElements().contains(childElement1));
assertTrue(parentElement.getElements().contains(childElement2));
}

@Test(expected = NullPointerException.class)
public void testNullParameterException() {
XmlElement element = new XmlElement(null);
}

@Test(expected = NullPointerException.class)
public void testNullAttributeNameException() {
XmlElement element = new XmlElement("element");
element.addAttribute(null, "value");
}

@Test(expected = NullPointerException.class)
public void testNullAttributeValueException() {
XmlElement element = new XmlElement("element");
element.addAttribute("attribute", null);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testInvalidIndexException() {
final List<XmlElement> elements = new ArrayList<>();
elements.add(new XmlElement("element1"));
elements.add(new XmlElement("element2"));

try {
elements.get(100);
}
catch (IndexOutOfBoundsException e) {
assertEquals("Index: 100, Size: 2", e.getMessage());
}
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidPathException() {
XmlElement parentElement = this.rootElement;
parentElement.addElement(new XmlElement("child1"));
parentElement.addElement(new XmlElement("child2"));

try {
parentElement.getElement("child2child1");
}
catch (IllegalArgumentException e) {
assertEquals("Invalid path: child2child1", e.getMessage());
}
}
}