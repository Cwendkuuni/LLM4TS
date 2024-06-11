package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementCreation() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("testElement");
        assertNotNull(xmlElementWithName);
        assertEquals("testElement", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("testElement", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        assertEquals("testElement", xmlElementWithNameAndAttributes.getName());
        assertEquals("value1", xmlElementWithNameAndAttributes.getAttribute("attribute1"));
        assertEquals("value2", xmlElementWithNameAndAttributes.getAttribute("attribute2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("testElement", "data");
        assertNotNull(xmlElementWithNameAndData);
        assertEquals("testElement", xmlElementWithNameAndData.getName());
        assertEquals("data", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement");
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }};
        xmlElement.setAttributes(attributes);
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals("value2", xmlElement.getAttribute("attribute2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        String attributeName;
        while (attributeNames.hasMoreElements()) {
            attributeName = attributeNames.nextElement();
            assertEquals("attribute1", attributeName);
            assertEquals("attribute2", attributeName);
        }
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertTrue(parentElement.removeElement(childElement));
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        XmlElement removedElement = parentElement.removeElement(1);
        assertEquals(childElement2, removedElement);
        assertEquals(1, parentElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        childElement.setParent(parentElement);
        assertEquals(1, parentElement.count());
        childElement.removeFromParent();
        assertEquals(0, parentElement.count());
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        assertTrue(parentElement.append(childElement));
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement existingChildElement = new XmlElement("existingChildElement");
        XmlElement newChildElement = new XmlElement("newChildElement");
        parentElement.addElement(existingChildElement);
        parentElement.insertElement(newChildElement, 0);
        assertEquals(2, parentElement.count());
        assertEquals(newChildElement, parentElement.getElement(0));
        assertEquals(existingChildElement, parentElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement("parentElement");
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
    public void testGetElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement element = parentElement.getElement("childElement1");
        assertEquals(childElement1, element);
        assertNull(parentElement.getElement("nonExistentChildElement"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement element = parentElement.getElement(0);
        assertEquals(childElement1, element);
        assertEquals(childElement2, parentElement.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = parentElement.addSubElement("childElement");
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithAttributes() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = parentElement.addSubElement("childElement", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals("value1", childElement.getAttribute("attribute1"));
        assertEquals("value2", childElement.getAttribute("attribute2"));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = parentElement.addSubElement("childElement", "data");
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals("data", childElement.getData());
    }

    @Test
    public void testClone() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement clonedParentElement = (XmlElement) parentElement.clone();
        List<XmlElement> elements = clonedParentElement.getElements();
        assertEquals(parentElement.count(), elements.size());
        for (XmlElement element : elements) {
            XmlElement clonedElement = (XmlElement) element.clone();
            assertEquals(element.getName(), clonedElement.getName());
            assertEquals(element.getData(), clonedElement.getData());
            assertEquals(element.getAttributes().size(), clonedElement.getAttributes().size());
            for (Map.Entry<String, String> entry : element.getAttributes().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                assertEquals(key, clonedElement.getAttribute(key));
                assertEquals(value, clonedElement.getAttribute(key));
            }
        }
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("root");
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2", "data");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.printNode(System.out);
    }
}