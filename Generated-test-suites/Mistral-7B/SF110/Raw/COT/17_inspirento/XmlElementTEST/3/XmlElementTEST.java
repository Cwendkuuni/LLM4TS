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
 public void testXmlElementConstructor() {
 XmlElement element = new XmlElement("element");
 assertEquals("element", element.getName());
 assertEquals(10, element.attributes.capacity());
 assertEquals(0, element.attributes.size());
 assertEquals(0, element.subElements.size());
 }

 @Test
 public void testXmlElementAddAttribute() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddAttributeNullName() {
 root.addAttribute(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddAttributeNullValue() {
 root.addAttribute("test", null);
 }

 @Test
 public void testXmlElementAddSubElement() {
 XmlElement subElement = new XmlElement("subElement");
 root.addSubElement(subElement);
 XmlElement actual = root.getElement("subElement");
 assertEquals(subElement, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementAddSubElementNullName() {
 root.addSubElement(null);
 }

 @Test
 public void testXmlElementGetElements() {
 root.addSubElement("testSubElement1", "testValue1");
 root.addSubElement("testSubElement2", "testValue2");
 List<XmlElement> elements = root.getElements();
 assertEquals(2, elements.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testXmlElementRemoveElementOutOfBounds() {
 root.addSubElement("testSubElement1", "testValue1");
 root.addSubElement("testSubElement2", "testValue2");
 root.removeElement(3);
 }

 @Test
 public void testXmlElementRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.removeFromParent();
 assertNull(child.getParent());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlElementRemoveFromParentNullParent() {
 child.removeFromParent(null);
 }

 @Test
 public void testXmlElementPrintNode() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2") {
 @Override
 public void printNode(String indent) {
 super.printNode(indent);
 System.out.println("Custom message");
 }
 };
 element1.addAttribute("test1", "testValue1");
 element1.addSubElement("subElement1", "subValue1");
 element2.addAttribute("test2", "testValue2");
 element2.addSubElement("subElement2", "subValue2");
 root.addElement(element1);
 root.addElement(element2);
 XmlElement.printNode(root, "");
 }

 @Test
 public void testXmlElementClone() {
 XmlElement original = new XmlElement("original");
 original.addSubElement("testSubElement1", "testValue1");
 XmlElement clone = (XmlElement)original.clone();
 assertEquals(original, clone);
 }

}