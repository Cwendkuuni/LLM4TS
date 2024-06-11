package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2", "data1");
 XmlElement element3 = new XmlElement("element3", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});

 assertEquals("element1", element1.getName());
 assertEquals("", element1.getData());
 assertEquals(0, element1.count());

 assertEquals("element2", element2.getName());
 assertEquals("data1", element2.getData());
 assertEquals(0, element2.count());

 assertEquals("element3", element3.getName());
 assertEquals("", element3.getData());
 assertEquals(0, element3.count());

 assertNotNull(element3.getAttributes());
 assertEquals("value1", element3.getAttribute("attr1"));
 assertEquals("value2", element3.getAttribute("attr2"));
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameOrValue() {
 root.addAttribute(null, "value1");
 root.addAttribute("attr1", null);
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 assertEquals("", root.getAttribute("nonExistentAttribute"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
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

 Enumeration<String> names = root.getAttributeNames();
 String name;
 while (names.hasMoreElements()) {
 name = names.nextElement();
 assertEquals(name, "attr1");
 assertEquals(name, "attr2");
 }
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
 public void testNullXmlElement() {
 root.addElement(null);
 }

 @Test
 public void testRemoveElement() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));

 XmlElement child1 = root.removeElement(0);
 XmlElement child2 = root.removeElement(1);

 assertEquals(0, root.count());
 assertEquals(child1, child1);
 assertEquals(child2, child2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));

 root.removeElement(2);
 }

 @Test
 public void testRemoveAllElements() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));

 root.removeAllElements();

 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 child.setParent(parent);

 parent.removeFromParent();
 assertEquals(null, child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);

 parent.append(child2);

 assertEquals(2, parent.count());
 assertEquals(child1, parent.getElement(0));
 assertEquals(child2, parent.getElement(1));
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);

 parent.insertElement(child2, 0);

 assertEquals(2, parent.count());
 assertEquals(child2, parent.getElement(0));
 assertEquals(child1, parent.getElement(1));
 }

 @Test
 public void testGetElements() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);

 List<XmlElement> elements = parent.getElements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XmlElement original = new XmlElement("original");
 XmlElement cloned = (XmlElement)original.clone();

 assertEquals(original, original);
 assertEquals(cloned, cloned);
 assertEquals(original, cloned);
 }

 @Test
 public void testSetName() {
 XmlElement e = new XmlElement("name1");
 e.setName("name2");
 assertEquals("name2", e.getName());
 }
}