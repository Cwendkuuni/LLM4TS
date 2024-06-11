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
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("root");
        assertNotNull(xmlElement);
        assertEquals("root", xmlElement.getName());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("root", new Hashtable<String, String>(10));
        assertNotNull(xmlElement);
        assertEquals("root", xmlElement.getName());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("root", "data");
        assertNotNull(xmlElement);
        assertEquals("root", xmlElement.getName());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.getElements().isEmpty());
        assertEquals("data", xmlElement.getData());
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        xmlElement.addAttribute("attr2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attr2"));
        assertEquals(2, xmlElement.getAttributes().size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals("value2", xmlElement.getAttribute("attr2"));
        assertEquals(null, xmlElement.getAttribute("attr3"));
        assertEquals("value1", xmlElement.getAttribute("attr1", "defaultValue"));
        assertEquals("defaultValue", xmlElement.getAttribute("attr3", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(2, xmlElement.getAttributes().size());
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals("value2", xmlElement.getAttribute("attr2"));
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> attributeNamesList = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            attributeNamesList.add((String) attributeNames.nextElement());
        }
        assertEquals(2, attributeNamesList.size());
        assertEquals("attr1", attributeNamesList.get(0));
        assertEquals("attr2", attributeNamesList.get(1));
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.getElements().size());
        assertEquals(parent, child.getParent());
        assertEquals("child", child.getName());
        assertFalse(child.getAttributes().isEmpty());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        parent.removeElement(child);
        assertEquals(0, parent.getElements().size());
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertEquals(0, parent.getElements().size());
        assertEquals(null, child1.getParent());
        assertEquals(null, child2.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.getElements().size());
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(1, parent.getElements().size());
        assertEquals(parent, child.getParent());
        assertEquals("child", child.getName());
        assertFalse(child.getAttributes().isEmpty());
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(2, parent.getElements().size());
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
        assertEquals("child2", child2.getName());
        assertEquals("child1", child1.getName());
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
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
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
        assertNull(parent.getElement("nonexistent"));
    }

    @Test
    public void testGetElementByPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        parent.addElement(child1);
        child1.addElement(child2);
        child1.addElement(child3);
        assertEquals(child2, parent.getElement("child1/child2"));
        assertEquals(child3, parent.getElement("child1/child3"));
        assertNull(parent.getElement("nonexistent"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1/child2");
        assertEquals("child2", child.getName());
        assertEquals(parent, child.getParent());
        assertFalse(child.getAttributes().isEmpty());
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1", "data");
        assertEquals("child1", child.getName());
        assertEquals("data", child.getData());
        assertEquals(parent, child.getParent());
        assertFalse(child.getAttributes().isEmpty());
    }

    @Test
    public void testAddSubElementWithAttributes() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1", "data", "attr1", "value1", "attr2", "value2");
        assertEquals("child1", child.getName());
        assertEquals("data", child.getData());
        assertEquals("value1", child.getAttribute("attr1"));
        assertEquals("value2", child.getAttribute("attr2"));
        assertEquals(parent, child.getParent());
        assertFalse(child.getAttributes().isEmpty());
    }

    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("root");
        XmlElement child1 = root.addSubElement("child1", "data1", "attr1", "value1");
        XmlElement child2 = root.addSubElement("child2");
        XmlElement child3 = child1.addSubElement("grandchild1", "data2", "attr2", "value2");

        XmlElement.printNode(root, "");
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("root")
                .addAttribute("attr1", "value1")
                .addAttribute("attr2", "value2")
                .addSubElement("child1")
                .addSubElement("child2");
        XmlElement cloned = (XmlElement) original.clone();
        assertEquals(original.getData(), cloned.getData());
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
        assertEquals(original.getAttributes().get("attr1"), cloned.getAttributes().get("attr1"));
        assertEquals(original.getAttributes().get("attr2"), cloned.getAttributes().get("attr2"));
        assertEquals(original.getElements().size(), cloned.getElements().size());
        assertEquals(original.getElements().get(0).getData(), cloned.getElements().get(0).getData());
        assertEquals(original.getElements().get(0).getName(), cloned.getElements().get(0).getName());
        assertEquals(original.getElements().get(1).getData(), cloned.getElements().get(1).getData());
        assertEquals(original.getElements().get(1).getName(), cloned.getElements().get(1).getName());
    }
}