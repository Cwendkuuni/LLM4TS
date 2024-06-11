package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());

        xmlElement = new XmlElement("TestElement");
        assertEquals("TestElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());

        xmlElement = new XmlElement("TestElement", new Hashtable<String, String>(5));
        assertEquals("TestElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());

        xmlElement = new XmlElement("TestElement", "Test Data");
        assertEquals("TestElement", xmlElement.getName());
        assertEquals("Test Data", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));

        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals("TestValue2", xmlElement.getAttribute("TestAttribute2"));

        xmlElement.addAttribute("TestAttribute1", "TestValue3");
        assertEquals("TestValue3", xmlElement.getAttribute("TestAttribute1"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("TestElement");
        Hashtable<String, String> attributes = new Hashtable<String, String>(5);
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("TestAttribute2", "TestValue2");
        xmlElement.setAttributes(attributes);

        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals("TestValue2", xmlElement.getAttribute("TestAttribute2"));
        assertTrue(xmlElement.getAttributes().size() == 2);
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("TestElement");
        Hashtable<String, String> attributes = new Hashtable<String, String>(5);
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("TestAttribute2", "TestValue2");
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("TestElement");
        Hashtable<String, String> attributes = new Hashtable<String, String>(5);
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("TestAttribute2", "TestValue2");
        xmlElement.setAttributes(attributes);

        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        String attributeName;
        while (attributeNames.hasMoreElements()) {
            attributeName = attributeNames.nextElement();
            assertEquals("TestAttribute1", attributeName);
            assertEquals("TestAttribute2", attributeName);
        }
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement("Parent");
        XmlElement childXmlElement = new XmlElement("Child");

        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getSubElements().get(0));
        assertEquals(childXmlElement, childXmlElement.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement("Parent");
        XmlElement childXmlElement = new XmlElement("Child");
        parentXmlElement.addElement(childXmlElement);

        XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement);
        assertEquals(childXmlElement, removedChildXmlElement);
        assertTrue(parentXmlElement.getSubElements().isEmpty());
        assertNull(childXmlElement.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement("Parent");
        XmlElement child1XmlElement = new XmlElement("Child1");
        XmlElement child2XmlElement = new XmlElement("Child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);

        parentXmlElement.removeAllElements();
        assertTrue(parentXmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        xmlElement.setData("Test Data");

        List<XmlElement> subXmlElements = new Vector<>();
        XmlElement child1XmlElement = new XmlElement("Child1");
        XmlElement child2XmlElement = new XmlElement("Child2");
        subXmlElements.add(child1XmlElement);
        subXmlElements.add(child2XmlElement);
        xmlElement.setSubElements(subXmlElements);

        XmlElement rootXmlElement = new XmlElement("RootElement");
        rootXmlElement.addElement(xmlElement);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        XmlElement.printNode(xmlElement, "");
        printWriter.flush();
        String output = stringWriter.toString();

        String expectedOutput = "RootElement\n" +
                "TestElement = 'Test Data'\n" +
                "TestElement:TestAttribute1:TestValue1\n" +
                "TestElement:TestAttribute2:TestValue2\n" +
                "Child1\n" +
                "Child2";

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        xmlElement.setData("Test Data");

        List<XmlElement> subXmlElements = new Vector<>();
        XmlElement child1XmlElement = new XmlElement("Child1");
        XmlElement child2XmlElement = new XmlElement("Child2");
        subXmlElements.add(child1XmlElement);
        subXmlElements.add(child2XmlElement);
        xmlElement.setSubElements(subXmlElements);

        XmlElement clonedXmlElement = (XmlElement)xmlElement.clone();

        assertEquals("TestElement", clonedXmlElement.getName());
        assertEquals("Test Data", clonedXmlElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(xmlElement.getSubElements(), clonedXmlElement.getSubElements());
    }

    @Test
    public void testGetElement() {
        XmlElement xmlElement = new XmlElement("Parent");
        XmlElement child1XmlElement = new XmlElement("Child1");
        XmlElement child2XmlElement = new XmlElement("Child2");
        xmlElement.addElement(child1XmlElement);
        xmlElement.addElement(child2XmlElement);

        assertEquals(child1XmlElement, xmlElement.getElement("Child1"));
        assertEquals(child2XmlElement, xmlElement.getElement("Child2"));
        assertNull(xmlElement.getElement("NonExistentChild"));
    }

    @Test
    public void testGetElementByPath() {
        XmlElement xmlElement = new XmlElement("Parent");
        XmlElement child1XmlElement = new XmlElement("Child1");
        XmlElement child2XmlElement = new XmlElement("Child2");
        XmlElement child3XmlElement = new XmlElement("Child3");
        xmlElement.addElement(child1XmlElement);
        child1XmlElement.addElement(child2XmlElement);
        child2XmlElement.addElement(child3XmlElement);

        assertEquals(child3XmlElement, xmlElement.getElement("Parent/Child1/Child2/Child3"));
        assertNull(xmlElement.getElement("Parent/NonExistentChild"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement xmlElement = new XmlElement("TestElement");
        XmlElement subElement = xmlElement.addSubElement("SubElement");
        assertEquals(subElement, xmlElement.getSubElements().get(0));
        assertEquals("SubElement", subElement.getName());
        assertNull(subElement.getData());
        assertTrue(subElement.getSubElements().isEmpty());
        assertNull(subElement.getParent());
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement xmlElement = new XmlElement("TestElement");
        XmlElement subElement = xmlElement.addSubElement("SubElement", "Test Data");
        assertEquals(subElement, xmlElement.getSubElements().get(0));
        assertEquals("SubElement", subElement.getName());
        assertEquals("Test Data", subElement.getData());
        assertTrue(subElement.getSubElements().isEmpty());
        assertNull(subElement.getParent());
    }
}