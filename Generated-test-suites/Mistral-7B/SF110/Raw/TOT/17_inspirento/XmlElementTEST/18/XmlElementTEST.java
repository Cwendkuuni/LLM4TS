package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import java.util.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root"); {};
 }

 @Test
 public void testConstruction() {
 XmlElement element1 = new XmlElement();
 XmlElement element2 = new XmlElement("name1");
 XmlElement element3 = new XmlElement("name1", new Hashtable<String, String>());
 XmlElement element4 = new XmlElement("name1", "data1");

 assertEquals("root", root.getName());
 assertNull(root.getParent());
 assertNotNull(root.getAttributes());
 assertNotNull(root.getSubElements());
 assertEquals(0, root.getSubElements().size());

 assertEquals("", element1.getData());
 assertNull(element1.getName());
 assertNull(element1.getParent());
 assertNotNull(element1.getAttributes());
 assertNotNull(element1.getSubElements());
 assertEquals(0, element1.getSubElements().size());

 assertEquals("name1", element2.getName());
 assertNull(element2.getParent());
 assertNotNull(element2.getAttributes());
 assertNotNull(element2.getSubElements());
 assertEquals(0, element2.getSubElements().size());

 assertEquals("name1", element3.getName());
 assertNull(element3.getParent());
 assertSame(Collections.emptyMap(), element3.getAttributes());
 assertNotNull(element3.getSubElements());
 assertEquals(0, element3.getSubElements().size());

 assertEquals("name1", element4.getName());
 assertNull(element4.getParent());
 assertSame("data1", element4.getData());
 assertNotNull(element4.getAttributes());
 assertNotNull(element4.getSubElements());
 assertEquals(0, element4.getSubElements().size());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals(2, root.getAttributes().size());
 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("value2", root.getAttribute("attr2"));
 assertEquals("", root.getAttribute("nonexistent"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals("value1", root.getAttribute("attr1"));
 assertEquals("defaultValue", root.getAttribute("nonexistent", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
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
 public void testAttributeNames() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 assertEquals(2, root.getAttributeNames().size());
 assertTrue(root.getAttributeNames().hasMoreElements());
 assertEquals("attr1", root.getAttributeNames().nextElement());
 assertTrue(root.getAttributeNames().hasMoreElements());
 assertEquals("attr2", root.getAttributeNames().nextElement());
 assertFalse(root.getAttributeNames().hasMoreElements());
 }

 @Test
 public void testAddElement() {
 XmlElement child = new XmlElement("child"); {};
 root.addElement(child);

 assertEquals(1, root.getSubElements().size());
 assertSame(child, root.getSubElements().get(0));
 assertEquals(root, child.getParent());
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child") {};
 root.addElement(child);

 assertEquals(1, root.getSubElements().size());
 XmlElement removedChild = root.removeElement(child);

 assertEquals(0, root.getSubElements().size());
 assertSame(child, removedChild);
 }

 @Test
 public void testRemoveElementByIndex() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.getSubElements().size());
 XmlElement removedChild = root.removeElement(1);

 assertEquals(1, root.getSubElements().size());
 assertSame(child2, removedChild);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.getSubElements().size());
 root.removeAllElements();

 assertEquals(0, root.getSubElements().size());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child") {};
 root.addElement(child);

 assertEquals(1, root.getSubElements().size());
 assertEquals(root, child.getParent());
 child.removeFromParent();

 assertEquals(0, root.getSubElements().size());
 assertNull(child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("child") {};
 root.append(child);

 assertEquals(1, root.getSubElements().size());
 assertSame(child, root.getSubElements().get(0));
 assertEquals(root, child.getParent());
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 root.addElement(child1);

 assertEquals(1, root.getSubElements().size());
 root.insertElement(child2, 0);

 assertEquals(2, root.getSubElements().size());
 assertSame(child2, root.getSubElements().get(0));
 assertSame(child1, root.getSubElements().get(1));
 }

 @Test
 public void testGetElements() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.getSubElements().size());
 List elements = root.getElements();

 assertEquals(2, elements.size());
 assertSame(child1, elements.get(0));
 assertSame(child2, elements.get(1));
 }

 @Test
 public void testCount() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.count());
 }

 @Test
 public void testElement() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 XmlElement child3 = new XmlElement("child3") {};
 root.addElement(child1);
 root.addElement(child2);
 root.addElement(child3);

 assertEquals(child1, root.getElement("child1"));
 assertEquals(child2, root.getElement("child2"));
 assertEquals(child3, root.getElement("child3"));
 assertEquals(null, root.getElement("nonexistent"));
 }

 @Test
 public void testGetElementByPath() {
 XmlElement child1 = new XmlElement("child1") {};
 XmlElement child2 = new XmlElement("child2") {};
 XmlElement child3 = new XmlElement("child3") {};
 XmlElement child4 = new XmlElement("child4") {};
 XmlElement child5 = new XmlElement("child5") {};
 root.addElement(child1);
 child1.addElement(child2);
 child2.addElement(child3);
 child3.addElement(child4);
 child4.addElement(child5);

 assertEquals(child5, root.getElement("child1/child2/child3/child4/child5"));
 assertEquals(null, root.getElement("nonexistent"));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original") {};
 XmlElement cloned = (XmlElement)original.clone();

 assertEquals("original", original.getName());
 assertEquals("original", cloned.getName());
 assertNotSame(original.getAttributes(), cloned.getAttributes());
 assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
 assertNotSame(original.getSubElements(), cloned.getSubElements());
 assertEquals(original.getSubElements().size(), cloned.getSubElements().size());
 }

 @Test
 public void testEquals() {
 XmlElement element1 = new XmlElement("element1") {};
 XmlElement element2 = new XmlElement("element1") {};
 XmlElement element3 = new XmlElement("element2") {};

 assertTrue(element1.equals(element1));
 assertTrue(element1.equals(element2));
 assertFalse(element1.equals(element3));
 }

 @Test
 public void testHashCode() {
 XmlElement element1 = new XmlElement("element1") {};
 XmlElement element2 = new XmlElement("element1") {};
 XmlElement element3 = new XmlElement("element2") {};

 int hashCode1 = element1.hashCode();
 int hashCode2 = element2.hashCode();
 int hashCode3 = element3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}