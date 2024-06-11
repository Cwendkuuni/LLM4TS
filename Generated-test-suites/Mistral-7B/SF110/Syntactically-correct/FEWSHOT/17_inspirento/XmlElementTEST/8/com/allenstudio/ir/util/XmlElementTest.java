package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementBasicConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement instanceof Observable);
        assertTrue(xmlElement instanceof Cloneable);
    }

    @Test
    public void testXmlElementConstructionWithName() {
        XmlElement xmlElement = new XmlElement("elementName");
        assertNotNull(xmlElement);
        assertEquals("elementName", xmlElement.getName());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement instanceof Observable);
        assertTrue(xmlElement instanceof Cloneable);
    }

    @Test
    public void testXmlElementConstructionWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        XmlElement xmlElement = new XmlElement("elementName", attributes);
        assertNotNull(xmlElement);
        assertEquals("elementName", xmlElement.getName());
        assertEquals(0, xmlElement.count());
        assertEquals(attributes, xmlElement.getAttributes());
        assertTrue(xmlElement instanceof Observable);
        assertTrue(xmlElement instanceof Cloneable);
    }

    @Test
    public void testXmlElementConstructionWithNameAndData() {
        XmlElement xmlElement = new XmlElement("elementName", "elementData");
        assertNotNull(xmlElement);
        assertEquals("elementName", xmlElement.getName());
        assertEquals("elementData", xmlElement.getData());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement instanceof Observable);
        assertTrue(xmlElement instanceof Cloneable);
    }

    @Test
    public void testXmlElementConstructionWithNameAndDataAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        XmlElement xmlElement = new XmlElement("elementName", "elementData", attributes);
        assertNotNull(xmlElement);
        assertEquals("elementName", xmlElement.getName());
        assertEquals("elementData", xmlElement.getData());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement instanceof Observable);
        assertTrue(xmlElement instanceof Cloneable);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        xmlElement.addAttribute("attribute2", "value2");
        attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        xmlElement.addAttribute("attribute2", "value2");
        assertEquals(null, xmlElement.getAttribute("attribute2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1", "defaultValue"));
        xmlElement.addAttribute("attribute2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attribute2", "defaultValue"));
        assertEquals("defaultValue", xmlElement.getAttribute("nonExistentAttribute", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> attributeNamesList = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            attributeNamesList.add(name);
        }
        assertEquals(Arrays.asList("attribute1", "attribute2"), attributeNamesList);
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        XmlElement removedElement = parentElement.removeElement(childElement1);
        assertEquals(1, parentElement.count());
        assertEquals(childElement2, parentElement.getElement(0));
        assertEquals(childElement1, removedElement);
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        XmlElement removedElement = parentElement.removeElement(0);
        assertEquals(1, parentElement.count());
        assertEquals(childElement2, parentElement.getElement(0));
        assertEquals(childElement1, removedElement);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        childElement.removeFromParent();
        assertEquals(0, parentElement.count());
        assertEquals(null, childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.append(childElement);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.insertElement(childElement2, 0);
        assertEquals(2, parentElement.count());
        assertEquals(childElement2, parentElement.getElement(0));
        assertEquals(childElement1, parentElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
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
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        childElement1.addElement(childElement2);
        assertEquals(childElement2, parentElement.getElement("parentElement/childElement1/childElement2"));
        assertEquals(null, parentElement.getElement("parentElement/childElement1/childElement3"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(childElement1, parentElement.getElement(0));
        assertEquals(childElement2, parentElement.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("childElement");
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("childElement", "childData");
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals("childData", childElement.getData());
    }

    @Test
    public void testAddSubElementWithNameAndAttributes() {
        XmlElement parentElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        XmlElement childElement = parentElement.addSubElement("childElement", attributes);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals(attributes, childElement.getAttributes());
    }

    @Test
    public void testPrintNode() {
        XmlElement rootElement = new XmlElement("rootElement");
        XmlElement childElement1 = new XmlElement("childElement1", "value1");
        childElement1.addAttribute("attribute1", "value1");
        childElement1.addAttribute("attribute2", "value2");
        rootElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement("childElement2");
        childElement2.addAttribute("attribute1", "value3");
        childElement2.addAttribute("attribute2", "value4");
        rootElement.addElement(childElement2);
        XmlElement grandChildElement = new XmlElement("grandChildElement", "grandChildValue");
        childElement1.addElement(grandChildElement);
        XmlElement.printNode(rootElement, "");
        String expectedOutput = ""
                + "rootElement\n"
                + "childElement1 = 'value1'\n"
                + "attribute1:value1\n"
                + "attribute2:value2\n"
                + "grandChildElement\n";
        // Use a custom way to compare strings, as the System.out.println() does not allow direct comparison
        String actualOutput = new String(System.out.getBuffer().array(), 0, System.out.getBuffer().length()).trim();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement();
        originalElement.addAttribute("attribute1", "value1");
        originalElement.addAttribute("attribute2", "value2");
        originalElement.addSubElement("subElement1", "subElementData1");
        XmlElement clonedElement = (XmlElement) originalElement.clone();
        assertEquals(originalElement, clonedElement);
        assertNotSame(originalElement.attributes, clonedElement.getAttributes());
        assertNotSame(originalElement.subElements, clonedElement.getElements());
        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(originalElement.getElements(), clonedElement.getElements());
    }

    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("oldName");
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertFalse(xmlElement1.equals(xmlElement2));
        XmlElement xmlElement3 = new XmlElement();
        xmlElement3.setAttributes(xmlElement1.getAttributes());
        xmlElement3.setData(xmlElement1.getData());
        xmlElement3.setName(xmlElement1.getName());
        xmlElement3.setSubElements(xmlElement1.getElements());
        assertTrue(xmlElement1.equals(xmlElement3));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        int hashcode1 = xmlElement1.hashCode();
        XmlElement xmlElement3 = new XmlElement();
        xmlElement3.setAttributes(xmlElement1.getAttributes());
        xmlElement3.setData(xmlElement1.getData());
        xmlElement3.setName(xmlElement1.getName());
        xmlElement3.setSubElements(xmlElement1.getElements());
        int hashcode2 = xmlElement3.hashCode();
        assertEquals(hashcode1, hashcode2);
    }
}