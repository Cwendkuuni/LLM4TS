package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());
        assertEquals(null, xmlElement.name);
        assertEquals(null, xmlElement.data);

        xmlElement = new XmlElement("root");
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());
        assertEquals("root", xmlElement.name);
        assertEquals(null, xmlElement.data);

        xmlElement = new XmlElement("root", new Hashtable<String, String>());
        assertEquals(0, xmlElement.subElements.size());
        assertEquals("root", xmlElement.name);
        assertNotNull(xmlElement.attributes);
        assertEquals(0, xmlElement.attributes.size());
        assertEquals(null, xmlElement.data);

        xmlElement = new XmlElement("root", "data");
        assertEquals(0, xmlElement.subElements.size());
        assertEquals("root", xmlElement.name);
        assertEquals("data", xmlElement.data);
        assertNotNull(xmlElement.attributes);
        assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getAttribute("name"));

        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
        assertEquals(1, xmlElement.attributes.size());

        xmlElement.addAttribute("name2", "value2");
        assertEquals("value", xmlElement.getAttribute("name"));
        assertEquals("value2", xmlElement.getAttribute("name2"));
        assertEquals(2, xmlElement.attributes.size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getAttribute("name"));

        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
        assertEquals(null, xmlElement.getAttribute("nonExistentName"));

        xmlElement.addAttribute("name2", "value2");
        assertEquals("value", xmlElement.getAttribute("name"));
        assertEquals("value2", xmlElement.getAttribute("name2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");

        xmlElement.setAttributes(attributes);
        assertEquals(2, xmlElement.attributes.size());
        assertEquals("value1", xmlElement.getAttribute("name1"));
        assertEquals("value2", xmlElement.getAttribute("name2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");

        xmlElement.setAttributes(attributes);
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        String name;
        while (attributeNames.hasMoreElements()) {
            name = attributeNames.nextElement();
            assertEquals(name, "name1") || assertEquals(name, "name2");
        }
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");

        parentElement.addElement(childElement);
        assertEquals(1, parentElement.subElements.size());
        assertEquals(childElement, parentElement.subElements.get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);

        parentElement.removeElement(childElement);
        assertEquals(0, parentElement.subElements.size());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        parentElement.removeAllElements();
        assertEquals(0, parentElement.subElements.size());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);

        childElement.removeFromParent();
        assertEquals(0, parentElement.subElements.size());
        assertEquals(null, childElement.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
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
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement element = parentElement.getElement("child1");
        assertEquals(childElement1, element);

        element = parentElement.getElement("child3");
        assertNull(element);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();

        XmlElement element1 = parentElement.addSubElement("child1");
        assertEquals("child1", element1.getName());
        assertEquals(null, element1.getData());
        assertEquals(parentElement, element1.getParent());

        XmlElement element2 = parentElement.addSubElement("child2");
        assertEquals("child2", element2.getName());
        assertEquals(null, element2.getData());
        assertEquals(parentElement, element2.getParent());

        XmlElement element3 = parentElement.addSubElement("child3", "data");
        assertEquals("child3", element3.getName());
        assertEquals("data", element3.getData());
        assertEquals(parentElement, element3.getParent());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("root", "data");
        xmlElement.addAttribute("name", "value");
        xmlElement.addAttribute("name2", "value2");

        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);

        xmlElement.setParent(null);
        XmlElement.printNode(xmlElement, "");

        String expectedOutput =
                "root = 'data'\n" +
                "name:value\n" +
                "name2:value2\n" +
                "child1\n" +
                "child2\n";

        String actualOutput = System.out.getOutputStream().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClone() {
        XmlElement originalXmlElement = new XmlElement("root", "data");
        originalXmlElement.addAttribute("name", "value");
        originalXmlElement.addAttribute("name2", "value2");

        XmlElement clonedXmlElement = (XmlElement)originalXmlElement.clone();

        assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
        assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());
        assertEquals(originalXmlElement.attributes.size(), clonedXmlElement.attributes.size());
        for (Map.Entry<String, String> entry : originalXmlElement.attributes.entrySet()) {
            assertEquals(entry.getKey(), clonedXmlElement.getAttribute(entry.getKey()));
        }

        List<XmlElement> originalSubElements = originalXmlElement.getElements();
        List<XmlElement> clonedSubElements = clonedXmlElement.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());
        for (XmlElement element : originalSubElements) {
            XmlElement clonedElement = (XmlElement)clonedXmlElement.getElements().get(clonedSubElements.indexOf(element));
            assertEquals(element.getName(), clonedElement.getName());
            assertEquals(element.getData(), clonedElement.getData());
            assertEquals(element.attributes.size(), clonedElement.attributes.size());
            for (Map.Entry<String, String> entry : element.attributes.entrySet()) {
                assertEquals(entry.getKey(), clonedElement.getAttribute(entry.getKey()));
            }
        }
    }
}