package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("test");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        XmlElement xmlElement = new XmlElement("test", attributes);
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testXmlElementConstructorWithNameData() {
        XmlElement xmlElement = new XmlElement("test", "testData");
        assertNotNull(xmlElement);
        assertEquals("test", xmlElement.getName());
        assertEquals("testData", xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", null);
    }

    @Test
    public void testGetAttributeNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals(null, xmlElement.getAttribute(null));
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttributeDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", null);
        xmlElement.addAttribute("attribute1", "defaultValue");
        assertEquals("defaultValue", xmlElement.getAttribute("attribute1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        XmlElement xmlElement = new XmlElement("test", attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attribute1", "value1");
        XmlElement xmlElement = new XmlElement("test", attributes);
        Set<String> attributeNames = xmlElement.getAttributeNames();
        assertEquals(1, attributeNames.size());
        assertTrue(attributeNames.contains("attribute1"));
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.count());
        assertEquals(parent, child.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementNullChild() {
        XmlElement parent = new XmlElement();
        parent.addElement(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        parent.removeElement(child);
        assertEquals(0, parent.count());
        assertNull(child.getParent());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementIndexOutOfBounds() {
        XmlElement parent = new XmlElement();
        parent.removeElement(1);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(1, parent.count());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement firstChild = new XmlElement("firstChild");
        XmlElement secondChild = new XmlElement("secondChild");
        parent.addElement(firstChild);
        parent.insertElement(secondChild, 0);
        assertEquals(2, parent.count());
        assertEquals(parent, secondChild.getParent());
        assertEquals(secondChild, parent.getElement(0));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
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
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testGetElementByName() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertNull(parent.getElement("nonexistent"));
    }

    @Test
    public void testGetElementByPath() {
        XmlElement root = new XmlElement("root");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        root.addElement(child1);
        root.addElement(child2);
        child1.addElement(child3);
        assertEquals(child3, root.getElement("child1/child3"));
        assertEquals(child1, root.getElement("child1"));
        assertEquals(child2, root.getElement("child2"));
        assertNull(root.getElement("nonexistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementInvalidPath() {
        XmlElement root = new XmlElement("root");
        root.addSubElement("root/child1/child2/child3/child4");
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child", "data");
        parent.addSubElement(child);
        assertEquals(1, parent.count());
        assertEquals("data", parent.getElement(0).getData());
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        original.addAttribute("attribute1", "value1");
        original.addAttribute("attribute2", "value2");
        original.addElement(new XmlElement("child1"));
        original.addElement(new XmlElement("child2"));
        XmlElement cloned = (XmlElement)original.clone();
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getData(), cloned.getData());
        assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
        assertEquals(original.getAttributes().get("attribute1"), cloned.getAttributes().get("attribute1"));
        assertEquals(original.getAttributes().get("attribute2"), cloned.getAttributes().get("attribute2"));
        assertEquals(original.count(), cloned.count());
        assertEquals(original.getElement(0), cloned.getElement(0));
        assertEquals(original.getElement(1), cloned.getElement(1));
    }
}