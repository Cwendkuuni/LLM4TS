package com.allenstudio.ir.util;

import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        assertEquals("", xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertFalse(xmlElement.hasAttribute("test"));
    }

    @Test
    public void testXmlElementConstructionWithName() {
        XmlElement xmlElement = new XmlElement("test");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertFalse(xmlElement.hasAttribute("test"));
    }

    @Test
    public void testXmlElementConstructionWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        XmlElement xmlElement = new XmlElement("test", attributes);
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertEquals(1, xmlElement.getAttributes().size());
        assertTrue(xmlElement.hasAttribute("attribute1"));
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testXmlElementConstructionWithNameAndData() {
        XmlElement xmlElement = new XmlElement("test", "data");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertFalse(xmlElement.hasAttribute("test"));
    }

    @Test
    public void testXmlElementAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals(1, xmlElement.getAttributes().size());
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testXmlElementGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testXmlElementGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", null);
        assertEquals(null, xmlElement.getAttribute("attribute1", "defaultValue"));
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1", "defaultValue"));
    }

    @Test
    public void testXmlElementGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertFalse(attributes.isEmpty());
    }

    @Test
    public void testXmlElementSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(1, xmlElement.getAttributes().size());
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testXmlElementGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Enumeration<String> enumeration = xmlElement.getAttributeNames();
        List<String> attributeNames = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            attributeNames.add((String)enumeration.nextElement());
        }
        assertEquals(2, attributeNames.size());
        assertTrue(attributeNames.contains("attribute1"));
        assertTrue(attributeNames.contains("attribute2"));
    }

    @Test
    public void testXmlElementAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testXmlElementRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        XmlElement removedElement = parentElement.removeElement(childElement);
        assertEquals(0, parentElement.count());
        assertNull(removedElement);
    }

    @Test
    public void testXmlElementRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement removedElement = parentElement.removeElement(1);
        assertEquals(1, parentElement.count());
        assertEquals(childElement1, parentElement.getElement(0));
        assertEquals(childElement2, removedElement);
    }

    @Test
    public void testXmlElementRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testXmlElementGetElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement element = parentElement.getElement("child1");
        assertEquals(childElement1, element);
    }

    @Test
    public void testXmlElementGetElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement element = parentElement.getElement(0);
        assertEquals(childElement1, element);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlElementGetElementWithNullPath() {
        XmlElement parentElement = new XmlElement();
        parentElement.getElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlElementGetElementWithEmptyPath() {
        XmlElement parentElement = new XmlElement();
        parentElement.getElement("");
    }

    @Test
    public void testXmlElementAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child/grandchild");
        assertEquals("child/grandchild", childElement.getName());
        assertEquals(parentElement, childElement.getParent());
        assertEquals(1, parentElement.count());
    }

    @Test
    public void testXmlElementAddSubElementWithExistingName() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = parentElement.addSubElement("child1");
        XmlElement childElement2 = parentElement.addSubElement("child1");
        assertEquals(1, parentElement.count());
        assertEquals(childElement1, parentElement.getElement(0));
        assertEquals(childElement1, childElement2.getParent());
    }

    @Test
    public void testXmlElementAddSubElementWithNullPath() {
        XmlElement parentElement = new XmlElement();
        parentElement.addSubElement(null);
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testXmlElementAddSubElementWithEmptyPath() {
        XmlElement parentElement = new XmlElement();
        parentElement.addSubElement("");
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testXmlElementClone() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        XmlElement clonedParentElement = (XmlElement)parentElement.clone();
        assertEquals(1, clonedParentElement.count());
        assertEquals(childElement, clonedParentElement.getElement(0));
    }

    @Test
    public void testXmlElementSetName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("test");
        assertEquals("test", xmlElement.getName());
    }
}