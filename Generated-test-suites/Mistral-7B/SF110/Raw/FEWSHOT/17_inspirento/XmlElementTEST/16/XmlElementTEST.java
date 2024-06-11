package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());
        assertEquals("", xmlElement.data);
        assertEquals(null, xmlElement.name);

        XmlElement xmlElementWithName = new XmlElement("TestElement");
        assertNotNull(xmlElementWithName);
        assertEquals("TestElement", xmlElementWithName.name);
        assertEquals(0, xmlElementWithName.subElements.size());
        assertEquals(0, xmlElementWithName.attributes.size());
        assertEquals("", xmlElementWithName.data);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeKey", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeKey"));
        assertEquals(1, xmlElement.attributes.size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeKey", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeKey"));
        assertEquals(null, xmlElement.getAttribute("nonExistingAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeKey1", "attributeValue1");
        xmlElement.addAttribute("attributeKey2", "attributeValue2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("attributeValue1", attributes.get("attributeKey1"));
        assertEquals("attributeValue2", attributes.get("attributeKey2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attributeKey1", "attributeValue1");
        attributes.put("attributeKey2", "attributeValue2");
        xmlElement.setAttributes(attributes);

        assertEquals(2, xmlElement.attributes.size());
        assertEquals("attributeValue1", xmlElement.getAttribute("attributeKey1"));
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeKey2"));
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");

        parentElement.addElement(childElement);
        assertEquals(1, parentElement.subElements.size());
        assertEquals(childElement, parentElement.subElements.get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);

        parentElement.removeElement(childElement);
        assertEquals(0, parentElement.subElements.size());
        assertNull(parentElement.getElements().get(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);

        childElement.removeFromParent();
        assertEquals(0, parentElement.subElements.size());
        assertNull(parentElement.getElements().get(0));
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.append(childElement1);
        parentElement.append(childElement2);

        assertEquals(2, parentElement.subElements.size());
        assertEquals(childElement1, parentElement.subElements.get(0));
        assertEquals(childElement2, parentElement.subElements.get(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.insertElement(childElement1, 0);
        parentElement.insertElement(childElement2, 1);

        assertEquals(2, parentElement.subElements.size());
        assertEquals(childElement1, parentElement.subElements.get(0));
        assertEquals(childElement2, parentElement.subElements.get(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        List<XmlElement> elements = parentElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childElement1, elements.get(0));
        assertEquals(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        assertEquals(2, parentElement.count());
    }

    @Test
    public void testElementWithPath() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement elementWithPath = parentElement.getElement("childElement2");
        assertEquals(childElement2, elementWithPath);

        elementWithPath = parentElement.getElement("childElement3");
        assertNull(elementWithPath);
    }

    @Test
    public void testElementWithPathAndSubElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement subElement = childElement1.getElement("subElement");
        assertNull(subElement);

        childElement1.addSubElement("subElement", "subElementData");
        subElement = childElement1.getElement("subElement");
        assertEquals("subElementData", subElement.getData());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeKey", "attributeValue");
        xmlElement.addElement(new XmlElement("childElement", "childData"));

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.attributes, clonedXmlElement.attributes);
        assertEquals(xmlElement.subElements, clonedXmlElement.subElements);

        XmlElement childElement = xmlElement.getElements().get(0);
        XmlElement clonedChildElement = clonedXmlElement.getElements().get(0);
        assertEquals(childElement, clonedChildElement);
    }
}