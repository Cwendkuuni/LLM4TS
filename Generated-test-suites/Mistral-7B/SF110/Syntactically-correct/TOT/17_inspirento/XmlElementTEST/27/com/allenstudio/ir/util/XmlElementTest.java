package com.allenstudio.ir.util;

import junit.framework.Assert;
import junit.framework.TestCase;
import java.util.*;

public class XmlElementTest extends TestCase {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root"); ;
 }

 // Test constructor with no arguments
 public void testXmlElementDefaultConstructor() {
 XmlElement xmlElement = new XmlElement();
 assertEquals("", xmlElement.getData());
 assertNull(xmlElement.getName());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 // Test constructor with name argument
 public void testXmlElementConstructorWithName() {
 XmlElement xmlElement = new XmlElement("elementName"); ;
 assertEquals("elementName", xmlElement.getName());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 // Test constructor with name and attributes argument
 public void testXmlElementConstructorWithNameAndAttributes() {
 Map<String, String> attributes = new HashMap<>();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 XmlElement xmlElement = new XmlElement("elementName", attributes);
 assertEquals("elementName", xmlElement.getName());
 assertEquals("value1", xmlElement.getAttribute("attribute1"));
 assertEquals("value2", xmlElement.getAttribute("attribute2"));
 }

 // Test constructor with name and data argument
 public void testXmlElementConstructorWithNameAndData() {
 XmlElement xmlElement = new XmlElement("elementName", "data"); ;
 assertEquals("elementName", xmlElement.getName());
 assertEquals("data", xmlElement.getData());
 assertNotNull(xmlElement.getAttributes());
 assertFalse(xmlElement.hasSubElements());
 }

 // Test addAttribute method
 public void testAddAttribute() {
 root.addAttribute("attribute1", "value1");
 assertEquals("value1", root.getAttribute("attribute1"));
 }

 // Test getAttribute method
 public void testGetAttribute() {
 root.addAttribute("attribute1", "value1");
 assertEquals("value1", root.getAttribute("attribute1"));
 }

 // Test getAttribute with default value
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("attribute1", null);
 assertEquals("defaultValue", root.getAttribute("attribute1", "defaultValue"));
 }

 // Test getAttributes method
 public void testGetAttributes() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 Map<String, String> attributes = root.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 // Test setAttributes method
 public void testSetAttributes() {
 Map<String, String> attributes = new HashMap<>();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 root.setAttributes(attributes);
 assertEquals("value1", root.getAttribute("attribute1"));
 assertEquals("value2", root.getAttribute("attribute2"));
 }

 // Test getAttributeNames method
 public void testGetAttributeNames() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 Enumeration enumeration = root.getAttributeNames();
 String attributeName = "";
 while (enumeration.hasMoreElements()) {
 attributeName = (String)enumeration.nextElement();
 assertEquals("attribute1" , attributeName);
 assertEquals("attribute2" , attributeName);
 }
 }

 // Test addElement method
 public void testAddElement() {
 XmlElement child = new XmlElement("childElement"); ;
 root.addElement(child);
 assertTrue(root.hasSubElements());
 assertEquals(1, root.getElements().size());
 assertEquals(child, root.getElement(0));
 }

 // Test removeElement method with existing element
 public void testRemoveElementWithExistingElement() {
 XmlElement child = new XmlElement("childElement"); ;
 root.addElement(child);
 root.removeElement(child);
 assertFalse(root.hasSubElements());
 }

 // Test removeElement method with non-existing element
 public void testRemoveElementWithNonExistingElement() {
 XmlElement child = new XmlElement("childElement"); ;
 root.removeElement(child);
 assertFalse(root.hasSubElements());
 }

 // Test removeAllElements method
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("childElement1"); ;
 XmlElement child2 = new XmlElement("childElement2"); ;
 root.addElement(child1);
 root.addElement(child2);
 root.removeAllElements();
 assertFalse(root.hasSubElements());
 }

 // Test removeFromParent method
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("childElement") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child);
 child.removeFromParent();
 assertFalse(root.hasSubElements());
 }

 // Test append method
 public void testAppend() {
 XmlElement child = new XmlElement("childElement") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.append(child);
 assertTrue(root.hasSubElements());
 assertEquals(1, root.getElements().size());
 assertEquals(child, root.getElement(0));
 }

 // Test insertElement method
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.insertElement(child1, 0);
 root.insertElement(child2, 1);
 assertTrue(root.hasSubElements());
 assertEquals(2, root.getElements().size());
 assertEquals(child1, root.getElement(0));
 assertEquals(child2, root.getElement(1));
 }

 // Test getElements method
 public void testGetElements() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 root.addElement(child2);
 List elements = root.getElements();
 assertEquals(2, elements.size());
 }

 // Test count method
 public void testCount() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 }

 // Test getElement method with simple path
 public void testGetElementWithSimplePath() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(child1, root.getElement("childElement1"));
 }

 // Test getElement method with complex path
 public void testGetElementWithComplexPath() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child3 = new XmlElement("childElement3") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 child1.addElement(child2);
 child2.addElement(child3);
 assertEquals(child3, root.getElement("childElement1/childElement2/childElement3"));
 }

 // Test addSubElement method with simple path
 public void testAddSubElementWithSimplePath() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 XmlElement child2 = root.addSubElement("childElement2");
 assertEquals("childElement2", child2.getName());
 }

 // Test addSubElement method with complex path
 public void testAddSubElementWithComplexPath() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 child1.addElement(child2);
 XmlElement child3 = root.addSubElement("childElement1/childElement2/childElement3");
 assertEquals("childElement3", child3.getName());
 }

 // Test addSubElement method with existing sub element
 public void testAddSubElementWithExistingSubElement() {
 XmlElement child1 = new XmlElement("childElement1") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 XmlElement child2 = new XmlElement("childElement2") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 root.addElement(child1);
 child1.addElement(child2);
 try {
 root.addSubElement("childElement1/childElement2/childElement3");
 fail("Expected IllegalStateException");
 }
 catch (IllegalStateException e) {
 // Expected
 }
 }

 // Test clone method
 public void testClone() {
 XmlElement original = new XmlElement("originalElement") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 };
 original.addAttribute("attribute1", "value1");
 original.addAttribute("attribute2", "value2");
 original.addElement(new XmlElement("childElement") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 this.parent = parent;
 }
 });
 XmlElement cloned = (XmlElement)original.clone();
 assertEquals("originalElement", original.getName());
 assertEquals("originalElement", cloned.getName());
 assertEquals("value1", original.getAttribute("attribute1"));
 assertEquals("value1", cloned.getAttribute("attribute1"));
 assertEquals("value2", original.getAttribute("attribute2"));
 assertEquals("value2", cloned.getAttribute("attribute2"));
 assertEquals(1, original.getElements().size());
 assertEquals(1, cloned.getElements().size());
 assertEquals(original.getElement(0), cloned.getElement(0));
 }
}