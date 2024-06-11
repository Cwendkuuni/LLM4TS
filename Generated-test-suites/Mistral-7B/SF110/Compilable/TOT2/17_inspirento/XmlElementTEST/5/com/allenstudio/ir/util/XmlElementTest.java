package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

public class XmlElementTest {
    // Test for XML element constructor with no arguments
    @Test
    public void testXmlElementDefaultConstructor() {
        XmlElement xmlElement = new XmlElement();
        // Basic functionality test
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.subElements.isEmpty());
        // Edge cases and exception handling tests may be added here
    }

    // Test for XML element constructor with a single String argument
    @Test
    public void testXmlElementWithStringArgumentConstructor() {
        XmlElement xmlElement = new XmlElement("testElement");
        // Basic functionality test
        assertEquals("testElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.subElements.isEmpty());
        // Edge cases and exception handling tests may be added here
    }

    // Test for adding an attribute
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        // Basic functionality test
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        // Edge cases and exception handling tests may be added here
    }

    // Test for removing an XML element
    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        parentElement.removeElement(childElement);
        // Basic functionality test
        assertFalse(parentElement.subElements.contains(childElement));
        // Edge cases and exception handling tests may be added here
    }

    // Test for getElement method
    @Test
    public void testGetElement() {
        XmlElement root = new XmlElement("root");
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        root.addElement(element1);
        root.addElement(element2);
        // Basic functionality test
        assertEquals(element1, root.getElement("element1"));
        // Edge cases and exception handling tests may be added here
    }

    // Test for getElement method with invalid path
    @Test(expected = IllegalArgumentException.class)
    public void testGetElementWithInvalidPath() {
        XmlElement root = new XmlElement("root");
        root.getElement("invalidPath");
    }

    // Test for addSubElement method
    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addSubElement("child1");
        parent.addSubElement("child2");
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        // Edge cases and exception handling tests may be added here
    }

    // Test for addSubElement method with empty path
    @Test
    public void testAddSubElementWithEmptyPath() {
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement("");
        assertEquals(Collections.emptyList(), parent.getElements());
    }

    // Test for adding a sub-element with the same name as an existing one
    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithSameName() {
        XmlElement parent = new XmlElement("parent");
        XmlElement existingChild = new XmlElement("child");
        parent.addElement(existingChild);
        parent.addSubElement("child");
    }
}