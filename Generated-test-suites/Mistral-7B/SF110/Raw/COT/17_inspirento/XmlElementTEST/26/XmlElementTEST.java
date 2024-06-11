package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

 private XmlElement createXmlElement() {
 return new XmlElement("root");
 }

 @Test
 public void testXmlElementConstructorWithName() {
 XmlElement xmlElement = createXmlElement();
 assertEquals("root", xmlElement.getName());
 }

 @Test
 public void testXmlElementConstructorWithNameAndAttributes() {
 XmlElement xmlElement = new XmlElement("root", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 assertEquals("root", xmlElement.getName());
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 assertEquals("value2", xmlElement.getAttribute("attr2"));
 }

 @Test
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement xmlElement = new XmlElement("root", "data");
 assertEquals("root", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 }

 @Test
 public void testAddAttribute() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 assertEquals("value1", xmlElement.getAttribute("attr1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullName() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullValue() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr", null);
 }

 @Test
 public void testGetAttributeWithNullName() {
 XmlElement xmlElement = createXmlElement();
 assertNull(xmlElement.getAttribute(null));
 }

 @Test
 public void testGetAttributeWithNullDefaultValue() {
 XmlElement xmlElement = new XmlElement("root");
 assertNull(xmlElement.getAttribute(null));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.addAttribute("attr1", "defaultValue");
 assertEquals("defaultValue", xmlElement.getAttribute("attr1", ""));
 }

 @Test
 public void testGetAttributes() {
 XmlElement xmlElement = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 }};
 Hashtable<String, String> attributes = xmlElement.getAttributes();
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 }};
 Hashtable<String, String> newAttributes = new Hashtable<String, String>() {{
 put("attr1", "newValue1");
 put("attr2", "newValue2");
 }};
 xmlElement.setAttributes(newAttributes);
 assertEquals("newValue1", xmlElement.getAttribute("attr1"));
 assertEquals("newValue2", xmlElement.getAttribute("attr2"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetAttributesWithNullAttributes() {
 XmlElement xmlElement = new XmlElement("root");
 xmlElement.setAttributes(null);
 }

 @Test
 public void testAddElement() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 assertTrue(parent.getElements().contains(child));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementWithNullElement() {
 XmlElement parent = createXmlElement();
 XmlElement nullElement = null;
 parent.addElement(nullElement);
 }

 @Test
 public void testRemoveElement() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 XmlElement removedChild = parent.removeElement(child);
 assertSame(child, removedChild);
 assertTrue(parent.getElements().isEmpty());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementWithInvalidIndex() {
 XmlElement parent = createXmlElement();
 parent.addElement(new XmlElement("child1"));
 parent.addElement(new XmlElement("child2"));
 parent.removeElement(10);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parent = createXmlElement();
 parent.addElement(new XmlElement("child1"));
 parent.addElement(new XmlElement("child2"));
 parent.removeAllElements();
 assertTrue(parent.getElements().isEmpty());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 child.removeFromParent();
 assertNull(child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.append(child);
 assertTrue(parent.getElements().contains(child));
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.insertElement(child1, 0);
 parent.insertElement(child2, 1);
 assertEquals(child1, parent.getElements().get(0));
 assertEquals(child2, parent.getElements().get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementWithInvalidIndex() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.insertElement(child, -1);
 }

 @Test
 public void testGetElements() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 List<XmlElement> elements = parent.getElements();
 assertEquals(2, elements.size());
 assertEquals(child1, elements.get(0));
 assertEquals(child2, elements.get(1));
 }

 @Test
 public void testCount() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(2, parent.count());
 }

 @Test(expected = NullPointerException.class)
 public void testIgnoreCaseGetElementWithNullPath() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.ignoreCaseGetElement(null);
 }

 @Test
 public void testGetElementWithEmptyPath() {
 XmlElement xmlElement = createXmlElement();
 XmlElement element = xmlElement.getElement("");
 assertNull(element);
 }

 @Test
 public void testGetElementWithSingleLevelPath() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 XmlElement element = parent.getElement("child");
 assertEquals(child, element);
 }

 @Test
 public void testGetElementWithMultipleLevelPath() {
 XmlElement parent1 = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 parent1.addElement(child1);
 XmlElement parent2 = new XmlElement("parent2");
 child1.addElement(parent2);
 XmlElement child2 = new XmlElement("child2");
 parent2.addElement(child2);
 XmlElement element = parent1.getElement("child1/parent2/child2");
 assertEquals(child2, element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementWithInvalidPath() {
 XmlElement parent = createXmlElement();
 XmlElement element = parent.getElement("invalidPath");
 assertNull(element);
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementWithNullPath() {
 XmlElement parent = createXmlElement();
 parent.addSubElement(null);
 }

 @Test
 public void testAddSubElementWithEmptyPath() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addSubElement(child);
 assertEquals(child, parent.getElements().get(0));
 }

 @Test
 public void testAddSubElementWithSingleLevelPath() {
 XmlElement parent = createXmlElement();
 XmlElement child = new XmlElement("child");
 parent.addSubElement("child");
 assertEquals(child, parent.getElements().get(0));
 }

 @Test
 public void testAddSubElementWithMultipleLevelPath() {
 XmlElement parent = createXmlElement();
 XmlElement child1 = new XmlElement("child1");
 parent.addElement(child1);
 XmlElement child2 = new XmlElement("child2");
 child1.addElement(child2);
 parent.addSubElement("child1/child2");
 assertEquals(child2, parent.getElements().get(1));
 }

 @Test
 public void testClone() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 xmlElement.addElement(new XmlElement("child"));
 XmlElement clonedXmlElement = (XmlElement)xmlElement.clone();
 assertEquals(xmlElement.getName(), clonedXmlElement.getName());
 assertEquals(xmlElement.getAttribute("attr1"), clonedXmlElement.getAttribute("attr1"));
 assertEquals(xmlElement.getAttribute("attr2"), clonedXmlElement.getAttribute("attr2"));
 assertTrue(xmlElement.getElements().size() == clonedXmlElement.getElements().size());
 }

 @Test
 public void testEqualsWithNullObject() {
 XmlElement xmlElement = createXmlElement();
 assertFalse(xmlElement.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClassObject() {
 XmlElement xmlElement = createXmlElement();
 assertFalse(xmlElement.equals(new Object()));
 }

 @Test
 public void testEqualsWithSameObject() {
 XmlElement xmlElement1 = createXmlElement();
 XmlElement xmlElement2 = xmlElement1;
 assertTrue(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithSameNameAndAttributesButDifferentSubElements() {
 XmlElement xmlElement1 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 }};
 XmlElement xmlElement2 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 }};
 XmlElement xmlElement3 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 assertTrue(xmlElement1.equals(xmlElement2));
 assertFalse(xmlElement1.equals(xmlElement3));
 }

 @Test
 public void testEqualsWithSameNameAndSameSubElementsButDifferentAttributes() {
 XmlElement xmlElement1 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 XmlElement xmlElement2 = new XmlElement("root") {{
 putAttribute("attr1", "value3");
 putAttribute("attr2", "value4");
 addElement(new XmlElement("child"));
 }};
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithDifferentNameAndSameSubElements() {
 XmlElement xmlElement1 = new XmlElement("root1") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 XmlElement xmlElement2 = new XmlElement("root2") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testEqualsWithSameNameAndSameSubElementsAndSameAttributes() {
 XmlElement xmlElement1 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 XmlElement xmlElement2 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 assertTrue(xmlElement1.equals(xmlElement2));
 }

 @Test
 public void testHashCode() {
 XmlElement xmlElement = createXmlElement();
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 xmlElement.addElement(new XmlElement("child"));
 int hashCode1 = xmlElement.hashCode();
 XmlElement xmlElement2 = new XmlElement("root") {{
 putAttribute("attr1", "value1");
 putAttribute("attr2", "value2");
 addElement(new XmlElement("child"));
 }};
 int hashCode2 = xmlElement2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}