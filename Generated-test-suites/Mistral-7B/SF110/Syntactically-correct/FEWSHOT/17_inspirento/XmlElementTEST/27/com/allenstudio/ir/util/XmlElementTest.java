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

        xmlElement = new XmlElement("TestElement", new Hashtable<String, String>());
        assertEquals("TestElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());

        xmlElement = new XmlElement("TestElement", "TestData");
        assertEquals("TestElement", xmlElement.getName());
        assertEquals("TestData", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals("TestValue2", xmlElement.getAttribute("TestAttribute2"));
        assertEquals(2, xmlElement.getAttributes().size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals(null, xmlElement.getAttribute("NonExistentAttribute"));

        xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.setData("TestData");
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals("TestData", xmlElement.getData());
        assertEquals(null, xmlElement.getAttribute("NonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals("TestValue1", attributes.get("TestAttribute1"));
        assertEquals("TestValue2", attributes.get("TestAttribute2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("TestAttribute2", "TestValue2");
        xmlElement.setAttributes(attributes);
        assertEquals("TestValue1", xmlElement.getAttribute("TestAttribute1"));
        assertEquals("TestValue2", xmlElement.getAttribute("TestAttribute2"));
        assertEquals(2, xmlElement.getAttributes().size());
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        String attributeName = attributeNames.nextElement();
        assertEquals("TestAttribute1", attributeName);
        attributeName = attributeNames.nextElement();
        assertEquals("TestAttribute2", attributeName);
        assertFalse(attributeNames.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        parentXmlElement.addElement(childXmlElement1);
        assertEquals(1, parentXmlElement.getSubElements().size());
        assertEquals(childXmlElement1, parentXmlElement.getSubElements().get(0));

        XmlElement childXmlElement2 = new XmlElement("ChildElement2");
        parentXmlElement.addElement(childXmlElement2);
        assertEquals(2, parentXmlElement.getSubElements().size());
        assertEquals(childXmlElement1, parentXmlElement.getSubElements().get(0));
        assertEquals(childXmlElement2, parentXmlElement.getSubElements().get(1));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        parentXmlElement.addElement(childXmlElement1);
        assertEquals(1, parentXmlElement.getSubElements().size());
        XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement1);
        assertEquals(0, parentXmlElement.getSubElements().size());
        assertNull(removedChildXmlElement);

        childXmlElement1.setParent(parentXmlElement);
        parentXmlElement.addElement(childXmlElement1);
        assertEquals(1, parentXmlElement.getSubElements().size());
        removedChildXmlElement = parentXmlElement.removeElement(0);
        assertEquals(0, parentXmlElement.getSubElements().size());
        assertEquals(childXmlElement1, removedChildXmlElement);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        parentXmlElement.addElement(childXmlElement1);
        assertEquals(1, parentXmlElement.getSubElements().size());
        parentXmlElement.removeAllElements();
        assertEquals(0, parentXmlElement.getSubElements().size());
    }

    @Test
    public void testSetAndGetParent() {
        XmlElement childXmlElement = new XmlElement("ChildElement");
        XmlElement parentXmlElement = new XmlElement();
        childXmlElement.setParent(parentXmlElement);
        assertEquals(null, childXmlElement.getParent());
        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getSubElements().get(0));
        assertEquals(parentXmlElement, childXmlElement.getParent());

        XmlElement grandParentXmlElement = new XmlElement();
        parentXmlElement.setParent(grandParentXmlElement);
        assertEquals(grandParentXmlElement, childXmlElement.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        XmlElement childXmlElement2 = new XmlElement("ChildElement2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);
        List<XmlElement> elements = parentXmlElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childXmlElement1, elements.get(0));
        assertEquals(childXmlElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        XmlElement childXmlElement2 = new XmlElement("ChildElement2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);
        assertEquals(2, parentXmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("ChildElement1");
        XmlElement childXmlElement2 = new XmlElement("ChildElement2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);
        assertEquals(childXmlElement1, parentXmlElement.getElement("ChildElement1"));
        assertEquals(childXmlElement2, parentXmlElement.getElement("ChildElement2"));
        assertEquals(null, parentXmlElement.getElement("NonExistentElement"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement1 = parentXmlElement.addSubElement("ChildElement1");
        assertEquals("ChildElement1", childXmlElement1.getName());
        assertEquals(null, childXmlElement1.getData());
        assertEquals(parentXmlElement, childXmlElement1.getParent());

        parentXmlElement.addSubElement("ChildElement2", "TestData");
        XmlElement childXmlElement2 = parentXmlElement.getSubElements().get(1);
        assertEquals("ChildElement2", childXmlElement2.getName());
        assertEquals("TestData", childXmlElement2.getData());
        assertEquals(parentXmlElement, childXmlElement2.getParent());

        parentXmlElement.addSubElement("ChildElement3", new XmlElement("GrandChildElement"));
        XmlElement childXmlElement3 = parentXmlElement.getSubElements().get(2);
        assertEquals("ChildElement3", childXmlElement3.getName());
        assertNull(childXmlElement3.getData());
        assertEquals(parentXmlElement, childXmlElement3.getParent());
        assertEquals(1, childXmlElement3.getSubElements().size());
        XmlElement grandChildXmlElement = childXmlElement3.getSubElements().get(0);
        assertEquals("GrandChildElement", grandChildXmlElement.getName());
        assertNull(grandChildXmlElement.getData());
        assertEquals(childXmlElement3, grandChildXmlElement.getParent());
    }

    @Test
    public void testClone() {
        XmlElement originalXmlElement = new XmlElement();
        originalXmlElement.addAttribute("TestAttribute1", "TestValue1");
        originalXmlElement.addAttribute("TestAttribute2", "TestValue2");
        originalXmlElement.addElement(new XmlElement("ChildElement1"));
        originalXmlElement.addElement(new XmlElement("ChildElement2"));

        XmlElement clonedXmlElement = (XmlElement) originalXmlElement.clone();

        assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
        assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());
        assertEquals(originalXmlElement.getAttributes().size(), clonedXmlElement.getAttributes().size());
        assertEquals(originalXmlElement.getSubElements().size(), clonedXmlElement.getSubElements().size());

        for (XmlElement attribute : originalXmlElement.getAttributes()) {
            XmlElement clonedAttribute = clonedXmlElement.getAttributes().get(attribute.getName());
            assertEquals(attribute.getData(), clonedAttribute.getData());
        }

        for (XmlElement child : originalXmlElement.getSubElements()) {
            XmlElement clonedChild = clonedXmlElement.getSubElements().get(clonedXmlElement.getSubElements().indexOf(child));
            assertEquals(child.getName(), clonedChild.getName());
            assertEquals(child.getData(), clonedChild.getData());
            assertEquals(child.getAttributes().size(), clonedChild.getAttributes().size());
            assertEquals(child.getSubElements().size(), clonedChild.getSubElements().size());
        }
    }
}