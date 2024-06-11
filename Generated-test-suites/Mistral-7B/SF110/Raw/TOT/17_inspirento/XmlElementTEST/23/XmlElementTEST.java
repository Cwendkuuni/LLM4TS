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
 XmlElement empty = new XmlElement();
 XmlElement withName = new XmlElement("test");
 XmlElement withNameAndAttributes = new XmlElement("test", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 XmlElement withNameAndData = new XmlElement("test", "data");

 assertEquals("XmlElement", root.getClass().getName());
 assertEquals("", root.getData());
 assertNull(root.getParent());
 assertNotNull(root.getAttributes());
 assertFalse(root.hasSubElements());

 assertEquals("", empty.getName());
 assertNull(empty.getData());
 assertNull(empty.getParent());
 assertNotNull(empty.getAttributes());
 assertFalse(empty.hasSubElements());

 assertEquals("test", withName.getName());
 assertNull(withName.getData());
 assertNull(withName.getParent());
 assertNotNull(withName.getAttributes());
 assertFalse(withName.hasSubElements());

 assertEquals("test", withNameAndAttributes.getName());
 assertNull(withNameAndAttributes.getData());
 assertNull(withNameAndAttributes.getParent());
 assertNotNull(withNameAndAttributes.getAttributes());
 assertTrue(withNameAndAttributes.hasSubElements());

 assertEquals("test", withNameAndData.getName());
 assertNull(withNameAndData.getAttributes());
 assertEquals("data", withNameAndData.getData());
 assertNull(withNameAndData.getParent());
 assertFalse(withNameAndData.hasSubElements());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 Map<String, String> attributes = root.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 assertEquals("value1", root.getAttribute("key1"));
 assertEquals("value2", root.getAttribute("key2"));
 assertEquals("", root.getAttribute("nonExistentKey"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 assertEquals("value1", root.getAttribute("key1"));
 assertEquals("defaultValue", root.getAttribute("nonExistentKey", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 assertTrue(root.getAttributes() instanceof Hashtable);
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 root.setAttributes(attributes);

 assertEquals(2, root.getAttributes().size());
 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 }

 @Test
 public void testgetAttributeNames() {
 root.addAttribute("key1", "value1");
 root.addAttribute("key2", "value2");

 assertTrue(root.getAttributeNames().hasMoreElements());
 assertEquals("key1", root.getAttributeNames().nextElement());
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNull() {
 root.addElement(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testAddElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);

 assertEquals(1, root.getSubElements().size());
 assertEquals("child", root.getSubElements().get(0).getName());
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 root.removeElement(child);

 assertEquals(0, root.getSubElements().size());
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.removeElement(1);

 assertEquals(1, root.getSubElements().size());
 assertEquals("child1", root.getSubElements().get(0).getName());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.removeAllElements();

 assertEquals(0, root.getSubElements().size());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.setParent(null);

 assertEquals(0, root.getSubElements().size());
 assertEquals(null, child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("child");
 root.append(child);

 assertEquals(1, root.getSubElements().size());
 assertEquals("child", root.getSubElements().get(0).getName());
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.insertElement(child2, 0);

 assertEquals(2, root.getSubElements().size());
 assertEquals("child2", root.getSubElements().get(0).getName());
 assertEquals("child1", root.getSubElements().get(1).getName());
 }

 @Test
 public void testGetElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.getSubElements().size());
 assertEquals(child1, root.getElements().get(0));
 assertEquals(child2, root.getElements().get(1));
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
 public void testgetElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2/child3");
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(child2, root.getElement("child2/child3"));
 assertEquals(null, root.getElement("nonExistentPath"));
 }

 @Test
 public void testAddSubElement() {
 XmlElement child1 = new XmlElement("child1");
 root.addSubElement(child1);

 assertEquals(1, root.getSubElements().size());
 assertEquals("child1", root.getSubElements().get(0).getName());
 }

 @Test
 public void testAddSubElementWithData() {
 XmlElement child1 = new XmlElement("child1", "data1");
 root.addSubElement(child1);

 assertEquals(1, root.getSubElements().size());
 assertEquals("child1", root.getSubElements().get(0).getName());
 assertEquals("data1", root.getSubElements().get(0).getData());
 }

 @Test
 public void testAddSubElementWithPath() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addSubElement("child1");
 root.addSubElement("child1/child2");

 assertEquals(2, root.getSubElements().size());
 assertEquals("child1", root.getSubElements().get(0).getName());
 assertEquals("child2", root.getSubElements().get(1).getName());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XmlElement original = new XmlElement("test");
 original.addAttribute("attr1", "value1");
 original.addAttribute("attr2", "value2");
 original.setData("data");
 XmlElement cloned = (XmlElement)original.clone();

 assertEquals("test", cloned.getName());
 assertEquals("data", cloned.getData());
 assertEquals("value1", cloned.getAttribute("attr1"));
 assertEquals("value2", cloned.getAttribute("attr2"));
 assertTrue(cloned.getAttributes() instanceof Hashtable);
 assertTrue(cloned.getSubElements() instanceof Vector);
 }
}