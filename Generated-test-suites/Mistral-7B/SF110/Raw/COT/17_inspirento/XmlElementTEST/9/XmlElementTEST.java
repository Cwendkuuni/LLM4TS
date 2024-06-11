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
 public void testXmlElementWithNoArguments() {
 XmlElement xmlElement = new XmlElement();
 assertEquals("XmlElement with no arguments should have default name and empty attributes and subElements", "", xmlElement.getName());
 assertEquals("XmlElement with no arguments should have empty attributes", null, xmlElement.getAttributes());
 assertEquals("XmlElement with no arguments should have empty subElements", 0, xmlElement.count());
 }

 @Test
 public void testXmlElementWithNameOnly() {
 XmlElement xmlElement = new XmlElement("name");
 assertEquals("XmlElement with name only should have the given name", "name", xmlElement.getName());
 assertEquals("XmlElement with name only should have empty attributes", null, xmlElement.getAttributes());
 assertEquals("XmlElement with name only should have empty subElements", 0, xmlElement.count());
 }

 @Test
 public void testXmlElementWithNameAndAttributes() {
 XmlElement xmlElement = new XmlElement("name", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 assertEquals("XmlElement with name and attributes should have the given name and attributes", "name", xmlElement.getName());
 assertEquals("XmlElement with name and attributes should have the given attributes", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }}, xmlElement.getAttributes());
 assertEquals("XmlElement with name and attributes should have empty subElements", 0, xmlElement.count());
 }

 @Test
 public void testXmlElementWithNameDataAndNoAttributes() {
 XmlElement xmlElement = new XmlElement("name", "data");
 assertEquals("XmlElement with name, data and no attributes should have the given name and data", "name", xmlElement.getName());
 assertEquals("XmlElement with name, data and no attributes should have empty attributes", null, xmlElement.getAttributes());
 assertEquals("XmlElement with name, data and no attributes should have the given data and empty subElements", "data", xmlElement.getData());
 assertEquals("XmlElement with name, data and no attributes should have empty subElements", 0, xmlElement.count());
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullName() {
 root.addAttribute(null, "value");
 fail("Should throw NullPointerException when trying to add attribute with null name");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullValue() {
 root.addAttribute("name", null);
 fail("Should throw NullPointerException when trying to add attribute with null value");
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attr1", "value1");
 assertEquals("XmlElement should have the added attribute", "value1", root.getAttribute("attr1"));
 }

 @Test
 public void testGetAttributeWithNullName() {
 assertEquals("XmlElement should return null when trying to get attribute with null name", null, root.getAttribute(null));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("attr1", "value1");
 assertEquals("XmlElement should return the specified default value when trying to get attribute with null name or non-existing attribute", "defaultValue", root.getAttribute("attr2", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 assertEquals("XmlElement should return the correct attributes", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }}, root.getAttributes());
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }};
 root.setAttributes(attributes);
 assertEquals("XmlElement should have the set attributes", attributes, root.getAttributes());
 }

 @Test
 public void testAddElementWithNullElement() {
 root.addElement(null);
 fail("Should throw NullPointerException when trying to add null element");
 }

 @Test
 public void testAddElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 assertEquals("XmlElement should have the added child element", child, root.getElements().get(0));
 }

 @Test
 public void testRemoveElementWithNullElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 root.removeElement(child);
 assertEquals("XmlElement should not have the removed child element", null, root.getElements().get(0));
 }

 @Test
 public void testRemoveElementWithIndexOutOfBounds() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 root.removeElement(10);
 assertEquals("XmlElement should not have removed element at index out of bounds", 2, root.count());
 }

 @Test
 public void testRemoveAllElements() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 root.removeAllElements();
 assertEquals("XmlElement should not have any elements after removing all elements", 0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.removeFromParent();
 assertEquals("XmlElement should not be a child of its parent after removing from parent", null, child.getParent());
 assertEquals("Parent XmlElement should not have the removed child element", 0, root.count());
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("child");
 root.append(child);
 assertEquals("XmlElement should have the appended child element", child, root.getElements().get(0));
 }

 @Test
 public void testInsertElementWithNullIndex() {
 XmlElement child = new XmlElement("child");
 root.insertElement(child, -1);
 assertEquals("XmlElement should not have inserted element at negative index", 0, root.count());
 }

 @Test
 public void testInsertElementWithIndexOutOfBounds() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.insertElement(new XmlElement("child3"), 5);
 assertEquals("XmlElement should not have inserted element at index out of bounds", 2, root.count());
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 root.insertElement(new XmlElement("child3"), 1);
 assertEquals("XmlElement should have the inserted child element at the given index", "child3", root.getElements().get(1).getName());
 }

 @Test
 public void testGetElements() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 List<XmlElement> elements = root.getElements();
 assertEquals("XmlElement should return the correct list of elements", 2, elements.size());
 assertEquals("XmlElement should return the first element", "child1", elements.get(0).getName());
 assertEquals("XmlElement should return the second element", "child2", elements.get(1).getName());
 }

 @Test
 public void testCount() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 assertEquals("XmlElement should return the correct number of elements", 2, root.count());
 }

 @Test
 public void testElementWithEmptyPath() {
 assertEquals("XmlElement should return itself when path is empty", root, root.getElement(""));
 }

 @Test
 public void testElementWithSingleName() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 assertEquals("XmlElement should return the child element with the given name", child1, root.getElement("child1"));
 }

 @Test
 public void testElementWithMultipleNames() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals("XmlElement should return the child element with the given path", child2, root.getElement("child1/child2"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementWithNullPath() {
 root.addSubElement(null);
 fail("Should throw NullPointerException when trying to add sub-element with null path");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithEmptyPath() {
 root.addSubElement("");
 fail("Should throw IllegalArgumentException when trying to add sub-element with empty path");
 }

 @Test
 public void testAddSubElementWithPath() {
 root.addSubElement("child1");
 assertEquals("XmlElement should have the added sub-element with the given path", "child1", root.subElements.get(0).getName());
 }

 @Test
 public void testAddSubElementWithPathAndData() {
 root.addSubElement("child1", "data");
 assertEquals("XmlElement should have the added sub-element with the given path and data", "data", root.subElements.get(0).getData());
 }

 @Test
 public void testAddSubElementWithPathAndAttributes() {
 root.addSubElement("child1", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 assertEquals("XmlElement should have the added sub-element with the given path, data and attributes", "value1", root.subElements.get(0).getAttribute("attr1"));
 }

 @Test
 public void testClone() {
 XmlElement xmlElement = new XmlElement("name");
 xmlElement.addAttribute("attr1", "value1");
 xmlElement.addAttribute("attr2", "value2");
 xmlElement.addElement(new XmlElement("child1"));
 assertEquals("XmlElement should clone correctly", xmlElement, xmlElement.clone());
 }
}