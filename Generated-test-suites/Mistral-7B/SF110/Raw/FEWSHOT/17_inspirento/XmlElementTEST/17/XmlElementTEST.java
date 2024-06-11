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
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("test");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals("", xmlElement.getData());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        XmlElement xmlElement = new XmlElement("test", attributes);
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals("", xmlElement.getData());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementConstructorWithNameAndData() {
        XmlElement xmlElement = new XmlElement("test", "data");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals("value1", xmlElement.getAttribute("attr1", "defaultValue"));
        assertEquals(1, xmlElement.getAttributes().size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertNull(xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("test");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("test");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertTrue(parent.removeElement(child));
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        XmlElement removedChild = parent.removeElement(1);
        assertEquals(child2, removedChild);
        assertEquals(1, parent.count());
        assertEquals(child1, parent.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
        assertEquals(0, parent.count());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.append(child));
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement(0));
        assertEquals(child1, parent.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testgetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertNull(parent.getElement("nonExistentChild"));
    }

    @Test
    public void testgetElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1");
        assertEquals("child1", child.getName());
        assertEquals(0, child.count());
        parent.setData("parentData");
        child.setData("childData");
        parent.printNode("");
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1", "childData");
        assertEquals("child1", child.getName());
        assertEquals("childData", child.getData());
        assertEquals(0, child.count());
        parent.setData("parentData");
        parent.printNode("");
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "value1");
        original.addAttribute("attr2", "value2");
        original.addElement("child1");
        XmlElement cloned = (XmlElement)original.clone();
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
        assertNotSame(original.getAttributes(), cloned.getAttributes());
        assertNotSame(original.subElements, cloned.subElements);
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getData(), cloned.getData());
        assertEquals(original.getAttributes(), cloned.getAttributes());
        assertEquals(original.subElements.size(), cloned.subElements.size());
        for (XmlElement child : original.getElements()) {
            XmlElement clonedChild = cloned.addSubElement((XmlElement)child.clone());
            assertEquals(child, clonedChild);
            assertNotSame(child, clonedChild);
        }
    }

    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }
}