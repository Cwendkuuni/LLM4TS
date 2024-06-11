package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
 XmlElement e1 = new XmlElement("element1");
 XmlElement e2 = new XmlElement("element2", "data1");
 XmlElement e3 = new XmlElement("element3", new Hashtable<String, String>() {{
 put("attribute1", "value1");
 put("attribute2", "value2");
 }});

 assertEquals("element1", e1.getName());
 assertEquals("", e1.getData());
 assertEquals(0, e1.getAttributes().size());
 assertEquals("element2", e2.getName());
 assertEquals("data1", e2.getData());
 assertEquals(2, e2.getAttributes().size());
 assertEquals("attribute1", e3.getAttributes().keyAt(0));
 assertEquals("value1", e3.getAttributes().valueAt(0));
 assertEquals("attribute2", e3.getAttributes().keyAt(1));
 assertEquals("value2", e3.getAttributes().valueAt(1));
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("test1", "value1");
 root.addAttribute("test2", null);

 assertEquals("value1", root.getAttribute("test1"));
 assertEquals(null, root.getAttribute("test2"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("test1", "value1");
 root.addAttribute("test2", "value2");
 root.setAttributes(new Hashtable<String, String>() {{
 put("test3", "value3");
 put("test4", null);
 }});

 assertEquals("value1", root.getAttribute("test1"));
 assertEquals("value2", root.getAttribute("test2"));
 assertEquals("value3", root.getAttribute("test3"));
 assertEquals(null, root.getAttribute("test4"));
 assertEquals(null, root.getAttribute("nonexistent"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("test1", "value1");
 root.addAttribute("test2", "value2");
 root.setAttributes(new Hashtable<String, String>() {{
 put("test3", "value3");
 put("test4", null);
 put("test5", "value5");
 }});

 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals(5, attributes.size());
 assertEquals("test1", attributes.keyAt(0));
 assertEquals("value1", attributes.valueAt(0));
 assertEquals("test2", attributes.keyAt(1));
 assertEquals("value2", attributes.valueAt(1));
 assertEquals("test3", attributes.keyAt(2));
 assertEquals("value3", attributes.valueAt(2));
 assertEquals("test5", attributes.keyAt(4));
 assertEquals("value5", attributes.valueAt(4));
 }

 @Test
 public void testSetAttributes() {
 root.setAttributes(new Hashtable<String, String>() {{
 put("test1", "value1");
 put("test2", "value2");
 }});
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("test3", "value3");
 root.setAttributes(attributes);

 assertEquals(2, root.getAttributes().size());
 assertEquals("value1", root.getAttribute("test1"));
 assertEquals("value2", root.getAttribute("test2"));
 }

 @Test
 public void testGetAttributeNames() {
 root.addAttribute("test1", "value1");
 root.addAttribute("test2", "value2");
 root.setAttributes(new Hashtable<String, String>() {{
 put("test3", "value3");
 put("test4", null);
 put("test5", "value5");
 }});
 List<String> names = new ArrayList<String>(root.getAttributeNames());

 assertEquals(5, names.size());
 assertEquals("test1", names.get(0));
 assertEquals("test2", names.get(1));
 assertEquals("test3", names.get(2));
 assertEquals("test4", names.get(3));
 assertEquals("test5", names.get(4));
 }

 @Test
 public void testAddElement() {
 XmlElement e1 = root.addElement(new XmlElement("child1"));
 XmlElement e2 = root.addElement(new XmlElement("child2"));
 XmlElement e3 = new XmlElement("child3");
 root.addElement(e3);

 assertEquals(3, root.getElements().size());
 assertEquals(root, e1.getParent());
 assertEquals(root, e2.getParent());
 assertEquals(null, e3.getParent());
 }

 @Test
 public void testRemoveElement() {
 XmlElement e1 = root.addElement(new XmlElement("child1"));
 XmlElement e2 = root.addElement(new XmlElement("child2"));
 root.removeElement(e1);

 assertEquals(1, root.getElements().size());
 assertNotEquals(root, e1.getParent());
 assertEquals(root, e2.getParent());
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement e1 = root.addElement(new XmlElement("child1"));
 XmlElement e2 = root.addElement(new XmlElement("child2"));
 root.removeElement(1);

 assertEquals(1, root.getElements().size());
 assertNotEquals(root, e1.getParent());
 assertEquals(root, e2.getParent());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement e1 = root.addElement(new XmlElement("child1"));
 XmlElement e2 = root.addElement(new XmlElement("child2"));
 root.removeAllElements();

 assertEquals(0, root.getElements().size());
 assertNull(e1.getParent());
 assertNull(e2.getParent());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement e1 = new XmlElement("child1");
 root.addElement(e1);
 e1.removeFromParent();

 assertEquals(0, root.getElements().size());
 assertNull(e1.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 parent.append(e1);

 assertEquals(1, parent.getElements().size());
 assertEquals(parent, e1.getParent());
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 XmlElement e2 = new XmlElement("child2");
 parent.insertElement(e1, 0);
 parent.insertElement(e2, 1);

 assertEquals(2, parent.getElements().size());
 assertEquals(parent, e1.getParent());
 assertEquals(parent, e2.getParent());
 assertEquals(e2, e1.getNextSibling());
 assertEquals(null, e1.getPreviousSibling());
 }

 @Test
 public void testGetElements() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 XmlElement e2 = new XmlElement("child2");
 parent.addElement(e1);
 parent.addElement(e2);

 assertEquals(2, parent.getElements().size());
 }

 @Test
 public void testCount() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 XmlElement e2 = new XmlElement("child2");
 parent.addElement(e1);
 parent.addElement(e2);

 assertEquals(2, parent.count());
 }

 @Test
 public void testGetElement() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 XmlElement e2 = new XmlElement("child2");
 parent.addElement(e1);
 parent.addElement(e2);

 assertEquals(e1, parent.getElement("child1"));
 assertEquals(e2, parent.getElement("child2"));
 }

 @Test
 public void testGetElementByPath() {
 XmlElement parent = new XmlElement("parent");
 XmlElement e1 = new XmlElement("child1");
 XmlElement e2 = new XmlElement("child2");
 XmlElement e3 = new XmlElement("child3");
 parent.addElement(e1);
 e1.addElement(e2);
 e1.addElement(e3);

 assertEquals(e3, parent.getElement("child1/child1/child3"));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 original.addAttribute("attribute1", "value1");
 original.addAttribute("attribute2", "value2");
 original.addElement(new XmlElement("child1"));
 original.addElement(new XmlElement("child2"));

 XmlElement cloned = (XmlElement)original.clone();

 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getAttribute("attribute1"), cloned.getAttribute("attribute1"));
 assertEquals(original.getAttribute("attribute2"), cloned.getAttribute("attribute2"));
 assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
 assertEquals(original.getElements().size(), cloned.getElements().size());
 }
}