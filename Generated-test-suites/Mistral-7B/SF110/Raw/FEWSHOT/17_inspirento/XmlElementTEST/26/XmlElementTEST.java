package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        XmlElement xmlElementWithName = new XmlElement("elementName");
        assertNotNull(xmlElementWithName);
        XmlElement xmlElementWithNameAndAttributes = new XmlElement("elementName", new Hashtable<String, String>() {{
            put("attributeName1", "attributeValue1");
            put("attributeName2", "attributeValue2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        XmlElement xmlElementWithNameAndData = new XmlElement("elementName", "elementData");
        assertNotNull(xmlElementWithNameAndData);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        assertEquals("attributeValue1", xmlElement.getAttribute("attributeName1"));
        xmlElement.addAttribute("attributeName2", "attributeValue2");
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        assertEquals("attributeValue1", xmlElement.getAttribute("attributeName1"));
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
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("attributeName1", "attributeValue1");
            put("attributeName2", "attributeValue2");
        }};
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
        String attributeName;
        while (attributeNames.hasMoreElements()) {
            attributeName = attributeNames.nextElement();
            assertEquals(attributeName, "attributeName1" || attributeName.equals("attributeName2"));
        }
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElementName");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElementName2");
        parentElement.addElement(childElement2);
        parentElement.removeElement(childElement1);
        assertEquals(1, parentElement.count());
        assertEquals(childElement2, parentElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElementName2");
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElementName2");
        parentElement.addElement(childElement2);
        List<XmlElement> elements = parentElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childElement1, elements.get(0));
        assertEquals(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElementName2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElementName2");
        parentElement.addElement(childElement2);
        assertEquals(childElement1, parentElement.getElement("childElementName1"));
        assertEquals(null, parentElement.getElement("nonExistentElementName"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement subElement1 = parentElement.addSubElement("subElement1/subElement2");
        assertEquals("subElement2", subElement1.getName());
        XmlElement subElement2 = parentElement.addSubElement("subElement3");
        assertEquals("subElement3", subElement2.getName());
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentElement = new XmlElement();
        XmlElement subElement = parentElement.addSubElement("subElementName", "subElementData");
        assertEquals("subElementData", subElement.getData());
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement();
        originalElement.addAttribute("attributeName1", "attributeValue1");
        originalElement.addAttribute("attributeName2", "attributeValue2");
        originalElement.addElement(new XmlElement("childElementName1"));
        originalElement.addElement(new XmlElement("childElementName2"));
        XmlElement clonedElement = (XmlElement)originalElement.clone();
        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
        assertEquals(originalElement.count(), clonedElement.count());
        for (int i = 0; i < originalElement.count(); ++i) {
            XmlElement childElementOriginal = originalElement.getElement(i);
            XmlElement childElementCloned = clonedElement.getElement(i);
            assertEquals(childElementOriginal.getName(), childElementCloned.getName());
            assertEquals(childElementOriginal.getData(), childElementCloned.getData());
            assertEquals(childElementOriginal.getAttributes().size(), childElementCloned.getAttributes().size());
        }
    }
}