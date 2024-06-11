package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        assertEquals("", xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertNull(xmlElement.getName());
        assertEquals(10, xmlElement.attributes.capacity());
    }

    @Test
    public void testXmlElementWithName() {
        XmlElement xmlElement = new XmlElement("testName");
        assertNotNull(xmlElement);
        assertEquals("testName", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertEquals(10, xmlElement.attributes.capacity());
    }

    @Test
    public void testXmlElementWithNameAndAttributes() {
        XmlElement xmlElement = new XmlElement("testName", new Hashtable<String, String>() {{
            put("testAttribute1", "testValue1");
            put("testAttribute2", "testValue2");
        }});
        assertNotNull(xmlElement);
        assertEquals("testName", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(2, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("testValue2", xmlElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testXmlElementWithNameData() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        assertNotNull(xmlElement);
        assertEquals("testName", xmlElement.getName());
        assertEquals("testData", xmlElement.getData());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
        assertTrue(xmlElement.attributes instanceof Hashtable);
        assertEquals(10, xmlElement.attributes.capacity());
    }

    @Test
    public void testXmlElementMethods() {
        XmlElement parentElement = new XmlElement("parent");
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        assertEquals(2, parentElement.count());
        assertTrue(parentElement.getElements() instanceof List);

        XmlElement retrievedChildElement1 = parentElement.getElement(0);
        assertEquals(childElement1, retrievedChildElement1);

        XmlElement retrievedChildElement2 = parentElement.getElement(1);
        assertEquals(childElement2, retrievedChildElement2);

        parentElement.removeElement(childElement1);
        assertEquals(1, parentElement.count());

        XmlElement retrievedChildElement1AfterRemoval = parentElement.getElement(0);
        assertEquals(childElement2, retrievedChildElement1AfterRemoval);

        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());

        parentElement.removeFromParent();
        assertNull(parentElement.getParent());

        XmlElement newChildElement = parentElement.addSubElement("testChild");
        assertEquals(1, parentElement.count());
        assertEquals("testChild", newChildElement.getName());
        assertNull(newChildElement.getData());
        assertNull(newChildElement.getParent());
    }

    @Test
    public void testXmlElementClone() {
        XmlElement originalXmlElement = new XmlElement("testClone");
        originalXmlElement.addAttribute("testAttribute1", "testValue1");
        originalXmlElement.addAttribute("testAttribute2", "testValue2");
        originalXmlElement.addSubElement("testChild1");
        originalXmlElement.addSubElement("testChild2");

        XmlElement clonedXmlElement = (XmlElement) originalXmlElement.clone();

        assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
        assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());

        assertEquals(originalXmlElement.getAttributes().size(), clonedXmlElement.getAttributes().size());
        for (Map.Entry<String, String> entry : originalXmlElement.getAttributes().entrySet()) {
            String value = clonedXmlElement.getAttribute(entry.getKey());
            assertEquals(entry.getValue(), value);
        }

        List children = originalXmlElement.getElements();
        List clonedChildren = clonedXmlElement.getElements();
        for (int i = 0; i < children.size(); i++) {
            XmlElement child = (XmlElement) children.get(i);
            XmlElement clonedChild = (XmlElement) clonedChildren.get(i);

            assertEquals(child.getName(), clonedChild.getName());
            assertEquals(child.getData(), clonedChild.getData());

            assertEquals(child.getAttributes().size(), clonedChild.getAttributes().size());
            for (Map.Entry<String, String> entry : child.getAttributes().entrySet()) {
                String value = clonedChild.getAttribute(entry.getKey());
                assertEquals(entry.getValue(), value);
            }

            List childElements = child.getElements();
            List clonedChildElements = clonedChild.getElements();
            for (int j = 0; j < childElements.size(); j++) {
                XmlElement childElement = (XmlElement) childElements.get(j);
                XmlElement clonedChildElement = (XmlElement) clonedChildElements.get(j);

                assertEquals(childElement.getName(), clonedChildElement.getName());
                assertEquals(childElement.getData(), clonedChildElement.getData());

                assertEquals(childElement.getAttributes().size(), clonedChildElement.getAttributes().size());
                for (Map.Entry<String, String> entry : childElement.getAttributes().entrySet()) {
                    String value = clonedChildElement.getAttribute(entry.getKey());
                    assertEquals(entry.getValue(), value);
                }
            }
        }
    }

    @Test
    public void testXmlElementEquals() {
        XmlElement xmlElement1 = new XmlElement("test1");
        XmlElement xmlElement2 = new XmlElement("test2");
        XmlElement xmlElement3 = new XmlElement("test1");

        assertFalse(xmlElement1.equals(xmlElement2));
        assertFalse(xmlElement1.equals(null));
        assertFalse(xmlElement1.equals("testString"));

        assertTrue(xmlElement1.equals(xmlElement3));

        xmlElement3.addAttribute("testAttribute1", "testValue1");
        xmlElement3.addSubElement("testChild1");

        assertFalse(xmlElement1.equals(xmlElement3));
    }

    @Test
    public void testXmlElementHashCode() {
        XmlElement xmlElement1 = new XmlElement("test1");
        XmlElement xmlElement2 = new XmlElement("test2");
        XmlElement xmlElement3 = new XmlElement("test1");

        int hashCode1 = xmlElement1.hashCode();
        int hashCode2 = xmlElement2.hashCode();
        int hashCode3 = xmlElement3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);

        xmlElement3.addAttribute("testAttribute1", "testValue1");
        xmlElement3.addSubElement("testChild1");

        int hashCode4 = xmlElement3.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testXmlElementPrintNode() {
        XmlElement xmlElement = new XmlElement("testPrintNode");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addSubElement("testChild1");
        xmlElement.addSubElement("testChild2");

        XmlElement testChild1 = xmlElement.getElement("testChild1");
        testChild1.addAttribute("testChild1Attribute1", "testChild1Value1");
        testChild1.addSubElement("testChild1Child1");

        XmlElement testChild1Child1 = testChild1.getElement("testChild1Child1");
        testChild1Child1.addAttribute("testChild1Child1Attribute1", "testChild1Child1Value1");

        XmlElement.printNode(xmlElement, "");
    }
}