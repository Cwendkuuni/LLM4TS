package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructorNoArguments() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(XmlElement.class, xmlElement.getClass());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertNull(xmlElement.name);
        assertNull(xmlElement.data);
    }

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("testElement");
        assertEquals("testElement", xmlElement.name);
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertNull(xmlElement.data);
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("testAttribute1", "testValue1");
        attributes.put("testAttribute2", "testValue2");
        XmlElement xmlElement = new XmlElement("testElement", attributes);
        assertEquals("testElement", xmlElement.name);
        assertEquals(attributes, xmlElement.attributes);
        assertTrue(xmlElement.subElements instanceof Vector);
        assertNull(xmlElement.data);
    }

    @Test
    public void testXmlElementConstructorWithNameAndData() {
        XmlElement xmlElement = new XmlElement("testElement", "<testData></testData>");
        assertEquals("testElement", xmlElement.name);
        assertNull(xmlElement.attributes);
        assertEquals("<testData></testData>", xmlElement.data);
        assertTrue(xmlElement.subElements instanceof Vector);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        String attributeName = "testAttribute1";
        String attributeValue = "testValue1";
        xmlElement.addAttribute(attributeName, attributeValue);
        assertEquals(attributeValue, xmlElement.getAttribute(attributeName));
    }

    @Test
    public void testGetAttributeNull() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getAttribute("testAttribute1"));
    }

    @Test
    public void testGetAttributeDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        String attributeName = "testAttribute1";
        String defaultValue = "defaultValue";
        xmlElement.addAttribute(attributeName, null);
        assertEquals(defaultValue, xmlElement.getAttribute(attributeName, defaultValue));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> attributeNamesList = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            attributeNamesList.add((String) attributeNames.nextElement());
        }
        assertEquals(List.of("testAttribute1", "testAttribute2"), attributeNamesList);
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement);
        assertSame(childXmlElement, removedChildXmlElement);
        assertTrue(parentXmlElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        XmlElement removedChildXmlElement = parentXmlElement.removeElement(1);
        assertEquals(child2XmlElement, removedChildXmlElement);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        parentXmlElement.removeAllElements();
        assertTrue(parentXmlElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        childXmlElement.removeFromParent();
        assertNull(childXmlElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.append(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement firstChildXmlElement = new XmlElement("firstChildElement");
        XmlElement secondChildXmlElement = new XmlElement("secondChildElement");
        parentXmlElement.addElement(firstChildXmlElement);
        parentXmlElement.insertElement(secondChildXmlElement, 0);
        assertEquals(secondChildXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testGetElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        List<XmlElement> elements = parentXmlElement.getElements();
        assertEquals(List.of(child1XmlElement, child2XmlElement), elements);
    }

    @Test
    public void testCount() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(2, parentXmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentXmlElement = new XmlElement();
        String testPath = "childElement1/childElement2";
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        child1XmlElement.addElement(child2XmlElement);
        assertEquals(child2XmlElement, parentXmlElement.getElement(testPath));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(child2XmlElement, parentXmlElement.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement();
        String testPath = "newChildElement";
        XmlElement newChildElement = parentXmlElement.addSubElement(testPath);
        assertEquals(newChildElement, parentXmlElement.getElement(testPath));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentXmlElement = new XmlElement();
        String testPath = "newChildElement";
        String data = "newChildData";
        XmlElement newChildElement = parentXmlElement.addSubElement(testPath, data);
        assertEquals(data, newChildElement.getData());
        assertEquals(newChildElement, parentXmlElement.getElement(testPath));
    }

    @Test
    public void testAddSubElementWithExistingPath() {
        XmlElement parentXmlElement = new XmlElement();
        String testPath = "childElement1/childElement2";
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        child1XmlElement.addElement(child2XmlElement);
        XmlElement newChildElement = parentXmlElement.addSubElement(testPath);
        assertEquals(child2XmlElement, newChildElement);
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        xmlElement.setData("<testData></testData>");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        child1.addElement(child2);
        xmlElement.printNode(0);
        String expectedOutput = "testElement:\n"
                + "  testAttribute1:testValue1\n"
                + "  testAttribute2:testValue2\n"
                + "child1:\n"
                + "  child2:\n";
        String actualOutput = System.out.getBuffer().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClone() {
        XmlElement originalXmlElement = new XmlElement();
        XmlElement clonedXmlElement = (XmlElement) originalXmlElement.clone();
        assertEquals(originalXmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());
        assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
        List<XmlElement> originalSubElements = originalXmlElement.getElements();
        List<XmlElement> clonedSubElements = clonedXmlElement.getElements();
        assertEquals(originalSubElements.size(), clonedSubElements.size());
        for (int i = 0; i < originalSubElements.size(); i++) {
            XmlElement originalSubElement = originalSubElements.get(i);
            XmlElement clonedSubElement = clonedSubElements.get(i);
            assertEquals(originalSubElement.getName(), clonedSubElement.getName());
            assertEquals(originalSubElement.getData(), clonedSubElement.getData());
            assertEquals(originalSubElement.getAttributes(), clonedSubElement.getAttributes());
            assertEquals(originalSubElement.getParent(), clonedSubElement.getParent());
            List<XmlElement> originalSubSubElements = originalSubElement.getElements();
            List<XmlElement> clonedSubSubElements = clonedSubElement.getElements();
            assertEquals(originalSubSubElements.size(), clonedSubSubElements.size());
            for (int j = 0; j < originalSubSubElements.size(); j++) {
                XmlElement originalSubSubElement = originalSubSubElements.get(j);
                XmlElement clonedSubSubElement = clonedSubSubElements.get(j);
                assertEquals(originalSubSubElement.getName(), clonedSubSubElement.getName());
                assertEquals(originalSubSubElement.getData(), clonedSubSubElement.getData());
                assertEquals(originalSubSubElement.getAttributes(), clonedSubSubElement.getAttributes());
                assertEquals(originalSubSubElement.getParent(), clonedSubSubElement.getParent());
            }
        }
    }

    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("testElement");
        assertEquals("testElement", xmlElement.getName());
    }

}