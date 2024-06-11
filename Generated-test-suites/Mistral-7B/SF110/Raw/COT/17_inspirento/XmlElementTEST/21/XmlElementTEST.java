package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor1() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(new Hashtable<String, String>(10), xmlElement.getAttributes());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.name);
        assertNull(xmlElement.data);
        assertNull(xmlElement.parent);
    }

    @Test
    public void testXmlElementConstructor2() {
        XmlElement xmlElement = new XmlElement("testName");
        assertEquals("testName", xmlElement.getName());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.data);
        assertNull(xmlElement.parent);
    }

    @Test
    public void testXmlElementConstructor3() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        assertEquals("testName", xmlElement.getName());
        assertEquals(attributes, xmlElement.getAttributes());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.data);
        assertNull(xmlElement.parent);
    }

    @Test
    public void testXmlElementConstructor4() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        assertEquals("testName", xmlElement.getName());
        assertNull(xmlElement.data);
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.parent);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("testKey1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testKey1"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "testValue1");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("testKey1", null);
    }

    @Test
    public void testGetAttribute() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        assertEquals("testValue1", xmlElement.getAttribute("testKey1"));
        assertNull(xmlElement.getAttribute("invalidKey"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        assertEquals("testValue1", xmlElement.getAttribute("testKey1"));
        assertEquals("defaultValue", xmlElement.getAttribute("invalidKey", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        Iterator<String> iterator = xmlElement.getAttributeNames();
        List<String> expectedKeys = new ArrayList<String>();
        expectedKeys.add("testKey1");
        expectedKeys.add("testKey2");
        while (iterator.hasNext()) {
            expectedKeys.remove(iterator.next());
        }
        assertTrue(expectedKeys.isEmpty());
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement = new XmlElement("testChildName");
        assertTrue(parentXmlElement.addElement(childXmlElement));
        assertEquals(1, parentXmlElement.count());
        assertEquals(childXmlElement, parentXmlElement.subElements.get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement = new XmlElement("testChildName");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(1, parentXmlElement.count());
        assertTrue(parentXmlElement.removeElement(childXmlElement));
        assertEquals(0, parentXmlElement.count());
        assertNull(parentXmlElement.subElements.get(0));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        assertEquals(2, parentXmlElement.count());
        assertTrue(parentXmlElement.removeElement(1));
        assertEquals(1, parentXmlElement.count());
        assertEquals(childXmlElement2, parentXmlElement.subElements.get(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        assertEquals(2, parentXmlElement.count());
        parentXmlElement.removeAllElements();
        assertEquals(0, parentXmlElement.count());
        assertTrue(parentXmlElement.subElements.isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement = new XmlElement("testChildName");
        parentXmlElement.addElement(childXmlElement);

        assertEquals(1, parentXmlElement.count());
        childXmlElement.removeFromParent();
        assertEquals(0, parentXmlElement.count());
        assertNull(parentXmlElement.subElements.get(0));
    }

    @Test
    public void testAppend() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.append(childXmlElement1);
        parentXmlElement.append(childXmlElement2);

        assertEquals(2, parentXmlElement.count());
        assertEquals(childXmlElement1, parentXmlElement.subElements.get(0));
        assertEquals(childXmlElement2, parentXmlElement.subElements.get(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.insertElement(childXmlElement1, 0);
        parentXmlElement.insertElement(childXmlElement2, 1);

        assertEquals(2, parentXmlElement.count());
        assertEquals(childXmlElement2, parentXmlElement.subElements.get(0));
        assertEquals(childXmlElement1, parentXmlElement.subElements.get(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        List<XmlElement> elements = parentXmlElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childXmlElement1, elements.get(0));
        assertEquals(childXmlElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        assertEquals(2, parentXmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        assertEquals(childXmlElement1, parentXmlElement.getElement("testParentName/testChildName1"));
        assertEquals(childXmlElement2, parentXmlElement.getElement("testParentName/testChildName2"));
        assertNull(parentXmlElement.getElement("testParentName/invalidChildName"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parentXmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = new XmlElement("testChildName1");
        XmlElement childXmlElement2 = new XmlElement("testChildName2");
        parentXmlElement.addElement(childXmlElement1);
        parentXmlElement.addElement(childXmlElement2);

        assertEquals(childXmlElement1, parentXmlElement.getElement(0));
        assertEquals(childXmlElement2, parentXmlElement.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement xmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = xmlElement.addSubElement("testChildName1");
        XmlElement childXmlElement2 = xmlElement.addSubElement("testChildName2");
        XmlElement childXmlElement3 = xmlElement.addSubElement("testChildName3");

        assertEquals("testChildName1", childXmlElement1.getName());
        assertEquals("testChildName2", childXmlElement2.getName());
        assertEquals("testChildName3", childXmlElement3.getName());
        assertEquals(xmlElement, childXmlElement1.getParent());
        assertEquals(xmlElement, childXmlElement2.getParent());
        assertEquals(xmlElement, childXmlElement3.getParent());
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement xmlElement = new XmlElement("testParentName");
        XmlElement childXmlElement1 = xmlElement.addSubElement("testChildName1");
        XmlElement childXmlElement2 = xmlElement.addSubElement("testChildName2");
        XmlElement childXmlElement3 = xmlElement.addSubElement("testChildName3");
        XmlElement grandChildXmlElement = xmlElement.addSubElement("testChildName1/testChildName3");

        assertEquals("testChildName3", grandChildXmlElement.getName());
        assertEquals(childXmlElement1, grandChildXmlElement.getParent());
        assertEquals(childXmlElement1, grandChildXmlElement.getParent().getParent());
        assertEquals(xmlElement, grandChildXmlElement.getParent().getParent());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals("testName", clonedXmlElement.getName());
        assertEquals("testData", clonedXmlElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(xmlElement.subElements.size(), clonedXmlElement.subElements.size());
        for (int i = 0; i < xmlElement.subElements.size(); ++i) {
            assertEquals(xmlElement.subElements.get(i).clone(), clonedXmlElement.subElements.get(i).clone());
        }
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        xmlElement.addSubElement("testChildName1");
        xmlElement.addSubElement("testChildName2");

        XmlElement childXmlElement1 = xmlElement.subElements.get(0);
        XmlElement childXmlElement2 = xmlElement.subElements.get(1);

        XmlElement printNodeXmlElement = new XmlElement();
        XmlElement.printNode(xmlElement, "");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("testName").append(" = 'testData'\n");
        expectedOutput.append("testAttribute1:testValue1\n");
        expectedOutput.append("testAttribute2:testValue2\n");
        expectedOutput.append("testChildName1\n");
        expectedOutput.append("testChildName2\n");

        assertEquals(expectedOutput.toString(), printNodeXmlElement.getData());
    }
}