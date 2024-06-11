package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstructorWithName() {
 XmlElement element = new XmlElement("elementName");
 assertEquals("elementName", element.getName());
 }

 @Test
 public void testXmlElementConstructorWithNameAndAttributes() {
 XmlElement element = new XmlElement("elementName", new Hashtable<String, String>() {{
 put("attributeName1", "attributeValue1");
 put("attributeName2", "attributeValue2");
 }});
 assertEquals("elementName", element.getName());
 assertEquals("attributeValue1", element.getAttribute("attributeName1"));
 assertEquals("attributeValue2", element.getAttribute("attributeName2"));
 }

 @Test
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement element = new XmlElement("elementName", "data");
 assertEquals("elementName", element.getName());
 assertEquals("data", element.getData());
 }

 @Test
 public void testAddAttributeWithValidNameAndValue() {
 root.addAttribute("attributeName", "attributeValue");
 assertEquals("attributeValue", root.getAttribute("attributeName"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeWithNullNameOrValue() {
 root.addAttribute(null, "attributeValue");
 root.addAttribute("attributeName", null);
 }

 @Test
 public void testGetAttributeWithValidName() {
 root.addAttribute("attributeName", "attributeValue");
 assertEquals("attributeValue", root.getAttribute("attributeName"));
 }

 @Test
 public void testGetAttributeWithInvalidNameOrDefaultValue() {
 root.addAttribute("attributeName", "attributeValue");
 assertEquals("attributeValue", root.getAttribute("attributeName", "defaultValue"));
 assertEquals("defaultValue", root.getAttribute("defaultName", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attributeName1", "attributeValue1");
 root.addAttribute("attributeName2", "attributeValue2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("attributeValue1", attributes.get("attributeName1"));
 assertEquals("attributeValue2", attributes.get("attributeName2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("attributeName1", "attributeValue1");
 put("attributeName2", "attributeValue2");
 }};
 root.setAttributes(attributes);
 assertEquals("attributeValue1", root.getAttribute("attributeName1"));
 assertEquals("attributeValue2", root.getAttribute("attributeName2"));
 }

 @Test
 public void testAddElementWithValidXmlElement() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(child);
 assertEquals(child, root.getElements().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementWithNullXmlElement() {
 XmlElement nullElement = null;
 root.addElement(nullElement);
 }

 @Test
 public void testRemoveElementWithValidXmlElement() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(child);
 assertEquals(child, root.getElements().get(0));
 root.removeElement(child);
 assertNull(root.getElements().get(0));
 }

 @Test
 public void testRemoveElementWithInvalidIndex() {
 root.addElement(new XmlElement("childElement1"));
 root.addElement(new XmlElement("childElement2"));
 assertFalse(root.removeElement(3));
 }

 @Test
 public void testRemoveAllElements() {
 root.addElement(new XmlElement("childElement1"));
 root.addElement(new XmlElement("childElement2"));
 root.removeAllElements();
 assertTrue(root.getElements().isEmpty());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(child);
 child.removeFromParent();
 assertFalse(root.getElements().contains(child));
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("childElement");
 root.append(child);
 assertEquals(child, root.getElements().get(root.getElements().size() - 1));
 }

 @Test
 public void testInsertElementWithValidXmlElementAndIndex() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(new XmlElement("childElement1"));
 root.insertElement(child, 0);
 assertEquals(child, root.getElements().get(0));
 }

 @Test
 public void testInsertElementWithValidXmlElementAndLastIndex() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(new XmlElement("childElement1"));
 root.insertElement(child, root.getElements().size());
 assertEquals(child, root.getElements().get(root.getElements().size() - 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementWithInvalidIndex() {
 XmlElement child = new XmlElement("childElement");
 root.addElement(new XmlElement("childElement1"));
 root.insertElement(child, 2);
 }

 @Test
 public void testGetElements() {
 XmlElement child1 = new XmlElement("childElement1");
 XmlElement child2 = new XmlElement("childElement2");
 root.addElement(child1);
 root.addElement(child2);
 List<XmlElement> elements = root.getElements();
 assertEquals(child1, elements.get(0));
 assertEquals(child2, elements.get(1));
 }

 @Test
 public void testCount() {
 XmlElement child1 = new XmlElement("childElement1");
 XmlElement child2 = new XmlElement("childElement2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 }

 @Test
 public void testGetElementWithValidPath() {
 XmlElement child1 = new XmlElement("childElement1");
 XmlElement child2 = new XmlElement("childElement2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(child2, root.getElement("childElement2"));
 }

 @Test
 public void testGetElementWithInvalidPath() {
 assertEquals(null, root.getElement("invalidPath"));
 }

 @Test
 public void testAddSubElementWithValidNameAndData() {
 XmlElement child = new XmlElement("childElement");
 root.addSubElement("childElement", "data");
 assertEquals("data", root.getElement("childElement").getData());
 }

 @Test
 public void testAddSubElementWithValidPathAndData() {
 XmlElement child = new XmlElement("childElement");
 root.addSubElement("childElement1/childElement", "data");
 assertEquals("data", root.getElement("childElement1/childElement").getData());
 }

 @Test
 public void testAddSubElementWithValidPathAndXmlElement() {
 XmlElement child = new XmlElement("childElement");
 root.addSubElement("childElement1/childElement", child);
 assertEquals(child, root.getElement("childElement1/childElement"));
 }

 @Test
 public void testAddSubElementWithInvalidPath() {
 assertFalse(root.addSubElement("invalidPath", null));
 }

 @Test
 public void testEqualsWithSameObjects() {
 XmlElement element1 = new XmlElement("elementName");
 XmlElement element2 = new XmlElement("elementName");
 assertTrue(element1.equals(element2));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 XmlElement element1 = new XmlElement("elementName1");
 XmlElement element2 = new XmlElement("elementName2");
 assertFalse(element1.equals(element2));
 }

 @Test
 public void testEqualsWithNullObject() {
 XmlElement element = new XmlElement("elementName");
 assertFalse(element.equals(null));
 }

 @Test
 public void testHashCodeWithSameObjects() {
 XmlElement element1 = new XmlElement("elementName");
 XmlElement element2 = new XmlElement("elementName");
 assertEquals(element1.hashCode(), element2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentObjects() {
 XmlElement element1 = new XmlElement("elementName1");
 XmlElement element2 = new XmlElement("elementName2");
 assertNotEquals(element1.hashCode(), element2.hashCode());
 }

 @Test
 public void testPrintNodeWithEmptyXmlElement() {
 XmlElement element = new XmlElement();
 StringBuilder output = new StringBuilder();
 XmlElement.printNode(element, "");
 assertEquals("", output.toString());
 }

 @Test
 public void testPrintNodeWithXmlElementContainingAttributesAndChildren() {
 XmlElement element = new XmlElement("elementName");
 element.addAttribute("attributeName", "attributeValue");
 element.addSubElement("childElement1");
 element.addSubElement("childElement2");
 StringBuilder output = new StringBuilder();
 XmlElement.printNode(element, "");
 String expectedOutput = "elementName:\nchildElement1:\nchildElement2:\n";
 assertEquals(expectedOutput, output.toString());
 }
}