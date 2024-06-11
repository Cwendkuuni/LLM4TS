package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

import java.util.*;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("testElement");
        assertEquals("testElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("testElement", new Hashtable<String, String>(10));
        assertEquals("testElement", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertEquals(new Hashtable<String, String>(10), xmlElement.getAttributes());
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("testElement", "testData");
        assertEquals("testElement", xmlElement.getName());
        assertEquals("testData", xmlElement.getData());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.subElements.isEmpty());
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testAddAttribute() {
        final XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute", "testValue");
        assertEquals("testValue", xmlElement.getAttribute("testAttribute"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        final XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        final XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute", null);
    }

    @Test
    public void testAddElement() {
        final XmlElement parent = new XmlElement("parentElement");
        final XmlElement child1 = new XmlElement("childElement1");
        final XmlElement child2 = new XmlElement("childElement2");

        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(2, parent.count());
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementNullElement() {
        final XmlElement parent = new XmlElement("parentElement");
        parent.addElement(null);
    }

    @Test
    public void testGetElement() {
        final XmlElement parent = new XmlElement("parentElement");
        final XmlElement child1 = new XmlElement("childElement1");
        final XmlElement child2 = new XmlElement("childElement2");

        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(child1, parent.getElement("childElement1"));
        assertEquals(child2, parent.getElement("childElement2"));
        assertNull(parent.getElement("nonExistentElement"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetElementInvalidPath() {
        final XmlElement parent = new XmlElement("parentElement");
        parent.addElement(new XmlElement("childElement"));

        assertNull(parent.getElement(""));
        assertNull(parent.getElement(null));
        assertNull(parent.getElement("childElement"));
        assertNull(parent.getElement("childElement."));
        assertNull(parent.getElement("childElement.."));
        assertNull(parent.getElement("childElement/"));
        assertNull(parent.getElement("childElement/childElement"));
        assertNull(parent.getElement("childElement/childElement."));
        assertNull(parent.getElement("childElement/childElement.."));
        assertNull(parent.getElement("childElement/childElement/"));
        assertNull(parent.getElement("childElement/childElement/childElement"));
        assertNull(parent.getElement("/childElement"));
        assertNull(parent.getElement("/childElement/"));
        assertNull(parent.getElement("/childElement/childElement"));
        assertNull(parent.getElement("childElement/childElement/childElement/"));
        assertNull(parent.getElement("childElement/childElement/childElement/childElement"));
    }

    @Test
    public void testGetElementValidPath() {
        final XmlElement parent = new XmlElement("parentElement");
        final XmlElement child1 = new XmlElement("childElement1");
        final XmlElement child2 = new XmlElement("childElement2");
        final XmlElement grandChild1 = new XmlElement("grandChild1");

        parent.addElement(child1);
        parent.addElement(child2);
        child1.addElement(grandChild1);

        assertEquals(grandChild1, parent.getElement("parentElement/childElement1/grandChild1"));
        assertEquals(child1, parent.getElement("parentElement/childElement1"));
        assertEquals(child2, parent.getElement("parentElement/childElement2"));
    }

    @Test
    public void testAddSubElement() {
        final XmlElement parent = new XmlElement("parentElement");
        final XmlElement child1 = parent.addSubElement("childElement1");
        final XmlElement child2 = parent.addSubElement("childElement2");

        assertEquals("childElement1", child1.getName());
        assertEquals("childElement2", child2.getName());
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementInvalidPath() {
        final XmlElement parent = new XmlElement("parentElement");
        parent.addSubElement("invalidPath");
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubElementNullName() {
        final XmlElement parent = new XmlElement("parentElement");
        parent.addSubElement(null);
    }

    @Test
    public void testPrintNode() {
        final XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        xmlElement.setData("testData");

        final XmlElement child1 = new XmlElement("childElement1");
        child1.addAttribute("childAttribute1", "childValue1");
        child1.addAttribute("childAttribute2", "childValue2");
        child1.setData("childData");

        xmlElement.addElement(child1);

        XmlElement.printNode(xmlElement, "");

        assertEquals("testElement = 'testData'\n" +
                "testAttribute1:testValue1\n" +
                "testAttribute2:testValue2\n" +
                "childElement1\n" +
                "childElement1 = 'childData'\n" +
                "childAttribute1:childValue1\n" +
                "childAttribute2:childValue2", XmlElementTest.getPrintNodeOutput(xmlElement));
    }

    private static String getPrintNodeOutput(XmlElement node) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        XmlElement.printNode(node, "");
        System.setOut(System.out);
        return byteArrayOutputStream.toString();
    }

    @Test
    public void testClone() {
        final XmlElement originalXmlElement = new XmlElement("testElement");
        originalXmlElement.addAttribute("testAttribute1", "testValue1");
        originalXmlElement.addAttribute("testAttribute2", "testValue2");
        originalXmlElement.setData("testData");

        final XmlElement clonedXmlElement = (XmlElement)originalXmlElement.clone();

        assertEquals(originalXmlElement, clonedXmlElement);
        assertNotSame(originalXmlElement, clonedXmlElement);

        assertEquals("testElement", clonedXmlElement.getName());
        assertEquals("testData", clonedXmlElement.getData());
        assertEquals(originalXmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(originalXmlElement.subElements, clonedXmlElement.subElements);

        final XmlElement child1 = new XmlElement("childElement1");
        child1.addAttribute("childAttribute1", "childValue1");
        child1.addAttribute("childAttribute2", "childValue2");
        child1.setData("childData");

        originalXmlElement.addElement(child1);

        assertEquals(1, originalXmlElement.count());
        assertEquals(child1, originalXmlElement.getElement(0));

        assertEquals(1, clonedXmlElement.count());
        assertEquals(child1, clonedXmlElement.getElement(0));
    }
}