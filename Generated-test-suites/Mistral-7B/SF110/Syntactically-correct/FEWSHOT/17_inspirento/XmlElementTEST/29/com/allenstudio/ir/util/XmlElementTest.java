package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructorWithNoArguments() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(XmlElement.class, xmlElement.getClass());
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithNameOnly() {
        XmlElement xmlElement = new XmlElement("name");
        assertEquals("name", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement xmlElement = new XmlElement("name", attributes);
        assertEquals("name", xmlElement.getName());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithNameDataAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement xmlElement = new XmlElement("name", "data", attributes);
        assertEquals("name", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.addAttribute("attr1", "value1");
        Hashtable<String, String> expectedAttributes = new Hashtable<>();
        expectedAttributes.put("attr1", "value1");
        assertEquals(expectedAttributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttr"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.addAttribute("attr1", null);
        assertEquals("defaultValue", xmlElement.getAttribute("attr1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> expectedAttributes = new Hashtable<>();
        expectedAttributes.put("attr1", "value1");
        expectedAttributes.put("attr2", "value2");
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.setAttributes(expectedAttributes);
        assertEquals(expectedAttributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.setAttributes(attributes);
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> expectedAttributeNames = new ArrayList<>();
        expectedAttributeNames.add("attr1");
        expectedAttributeNames.add("attr2");
        assertTrue(Collections.disjoint(expectedAttributeNames, Collections.list(attributeNames)));
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement childXmlElement = new XmlElement("child");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement childXmlElement = new XmlElement("child");
        parentXmlElement.addElement(childXmlElement);
        XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement);
        assertSame(childXmlElement, removedChildXmlElement);
        assertEquals(0, parentXmlElement.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        XmlElement removedChildXmlElement = parentXmlElement.removeElement(1);
        assertSame(child2XmlElement, removedChildXmlElement);
        assertEquals(1, parentXmlElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        parentXmlElement.removeAllElements();
        assertEquals(0, parentXmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        XmlElement child1SubElement = child1XmlElement.getElement("subChild");
        assertNull(child1SubElement);
        XmlElement child2SubElement = child2XmlElement.getElement("subChild");
        assertNull(child2SubElement);
        XmlElement grandChild = child2XmlElement.getElement("grandChild");
        assertNull(grandChild);
        XmlElement child1GrandChild = child1XmlElement.getElement("grandChild");
        assertNull(child1GrandChild);
        XmlElement child1SubChild = child1XmlElement.getElement("subChild");
        XmlElement expectedChild1SubChild = new XmlElement("subChild");
        assertSame(expectedChild1SubChild, child1SubChild);
        XmlElement child2SubChild = child2XmlElement.getElement("subChild");
        XmlElement expectedChild2SubChild = new XmlElement("subChild");
        assertSame(expectedChild2SubChild, child2SubChild);
    }

    @Test
    public void testInsertElement() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        XmlElement newChildXmlElement = new XmlElement("newChild");
        parentXmlElement.insertElement(newChildXmlElement, 1);
        assertEquals(newChildXmlElement, parentXmlElement.getElements().get(1));
        assertEquals(child1XmlElement, parentXmlElement.getElements().get(0));
        assertEquals(child2XmlElement, parentXmlElement.getElements().get(2));
    }

    @Test
    public void testGetElements() {
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        List<XmlElement> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1XmlElement);
        expectedChildren.add(child2XmlElement);
        assertEquals(expectedChildren, parentXmlElement.getElements());
    }

    @Test
    public void testCount() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(0, xmlElement.count());
        XmlElement parentXmlElement = new XmlElement("parent");
        XmlElement child1XmlElement = new XmlElement("child1");
        XmlElement child2XmlElement = new XmlElement("child2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(2, parentXmlElement.count());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        XmlElement originalXmlElement = new XmlElement("name");
        originalXmlElement.addAttribute("attr1", "value1");
        originalXmlElement.addAttribute("attr2", "value2");
        originalXmlElement.setData("data");
        XmlElement clonedXmlElement = (XmlElement) originalXmlElement.clone();
        assertEquals("name", clonedXmlElement.getName());
        assertEquals("data", clonedXmlElement.getData());
        assertEquals(originalXmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(originalXmlElement.subElements.size(), clonedXmlElement.subElements.size());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.setData("Root Element Data");
        XmlElement child1 = new XmlElement("child1");
        child1.setData("Child 1 Data");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        XmlElement child4 = new XmlElement("child4");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        xmlElement.addElement(child3);
        xmlElement.addElement(child4);
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");

        printNode(xmlElement, "");

        String expectedOutput = "root = 'Root Element Data'\n" +
                "attr1:value1\n" +
                "attr2:value2\n" +
                "child1:\n" +
                "child1 = 'Child 1 Data'\n" +
                "child2:\n" +
                "child3:\n" +
                "child4:\n";

        String actualOutput = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.flushBuffer().toString();
        assertEquals(expectedOutput, actualOutput);
    }
}