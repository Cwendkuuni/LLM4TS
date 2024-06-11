package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        XmlElement xmlElementWithName = new XmlElement("name");
        assertNotNull(xmlElementWithName);
        XmlElement xmlElementWithNameAndAttributes = new XmlElement("name", new Hashtable<String, String>());
        assertNotNull(xmlElementWithNameAndAttributes);
        XmlElement xmlElementWithNameAndData = new XmlElement("name", "data");
        assertNotNull(xmlElementWithNameAndData);
        XmlElement xmlElementWithNameAndAttributesAndData = new XmlElement("name", new Hashtable<String, String>(5), "data");
        assertNotNull(xmlElementWithNameAndAttributesAndData);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        xmlElement.addAttribute("attributeName2", "attributeValue2");
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
        xmlElement.addAttribute("attributeName", "attributeValue3");
        assertEquals("attributeValue3", xmlElement.getAttribute("attributeName"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttributeName"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        xmlElement.addAttribute("attributeName2", "attributeValue2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("attributeValue1", attributes.get("attributeName1"));
        assertEquals("attributeValue2", attributes.get("attributeName2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>(5);
        attributes.put("attributeName1", "attributeValue1");
        attributes.put("attributeName2", "attributeValue2");
        xmlElement.setAttributes(attributes);
        assertEquals("attributeValue1", xmlElement.getAttribute("attributeName1"));
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        xmlElement.addAttribute("attributeName2", "attributeValue2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        String attributeName = attributeNames.nextElement();
        assertEquals("attributeName1", attributeName);
        attributeName = attributeNames.nextElement();
        assertEquals("attributeName2", attributeName);
        assertNull(attributeNames.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("child");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
        assertEquals(1, parentXmlElement.count());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);
        parentXmlElement.removeElement(childXmlElement1);
        assertEquals(1, parentXmlElement.count());
        assertEquals(childXmlElement2, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("child");
        parentXmlElement.addElement(childXmlElement);
        childXmlElement.removeFromParent();
        assertEquals(0, parentXmlElement.count());
    }

    @Test
    public void testAppend() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        parentXmlElement.append(childXmlElement1);
        parentXmlElement.append(childXmlElement2);
        assertEquals(2, parentXmlElement.count());
        assertEquals(childXmlElement1, parentXmlElement.getElements().get(0));
        assertEquals(childXmlElement2, parentXmlElement.getElements().get(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.insertElement(childXmlElement2, 0);
        assertEquals(2, parentXmlElement.count());
        assertEquals(childXmlElement2, parentXmlElement.getElements().get(0));
        assertEquals(childXmlElement1, parentXmlElement.getElements().get(1));
    }

    @Test
    public void testGetElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        xmlElement.addElement(childXmlElement1);
        xmlElement.addElement(childXmlElement2);
        List<XmlElement> elements = xmlElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childXmlElement1, elements.get(0));
        assertEquals(childXmlElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        xmlElement.addElement(childXmlElement1);
        xmlElement.addElement(childXmlElement2);
        assertEquals(2, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("child1");
        XmlElement childXmlElement2 = new XmlElement("child2");
        xmlElement.addElement(childXmlElement1);
        xmlElement.addElement(childXmlElement2);
        XmlElement childXmlElement3 = xmlElement.getElement("child1/child2");
        assertEquals(childXmlElement2, childXmlElement3);
        XmlElement childXmlElement4 = xmlElement.getElement("nonExistentPath");
        assertNull(childXmlElement4);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = parentXmlElement.addSubElement("child");
        assertEquals("child", childXmlElement.getName());
        assertEquals(0, parentXmlElement.count());
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = parentXmlElement.addSubElement("child1");
        XmlElement child2XmlElement = child1XmlElement.addSubElement("child2");
        assertEquals("child2", child2XmlElement.getName());
        assertEquals(1, parentXmlElement.count());
        assertEquals("child1", parentXmlElement.getName());
        assertEquals(2, parentXmlElement.countRecursive());
        assertEquals("child2", parentXmlElement.getElement("child1/child2").getName());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.addAttribute("attributeName", "attributeValue");
        xmlElement.addAttribute("anotherAttributeName", "anotherAttributeValue");
        xmlElement.setData("data");
        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        assertEquals(xmlElement.getAttributes().size(), clonedXmlElement.getAttributes().size());
        assertEquals(xmlElement.getData(), clonedXmlElement.getData());
        assertEquals(xmlElement.count(), clonedXmlElement.count());
        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("root");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        XmlElement child3XmlElement = new XmlElement("child3");
        xmlElement.addElement(child1XmlElement);
        child1XmlElement.addElement(child2XmlElement);
        child1XmlElement.addElement(child3XmlElement);
        child2XmlElement.addAttribute("attributeName1", "attributeValue1");
        child2XmlElement.addAttribute("attributeName2", "attributeValue2");
        child3XmlElement.setData("data");
        assertEquals(3, xmlElement.count());
        assertEquals("root", xmlElement.getName());
        xmlElement.printNode(System.out);
    }
}