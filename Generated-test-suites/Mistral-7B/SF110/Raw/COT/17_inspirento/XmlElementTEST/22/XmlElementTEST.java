package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    // Test constructor with no arguments
    @Test
    public void testXmlElementConstructorNoArgs() {
        XmlElement xmlElement = new XmlElement();
        assertEquals("", xmlElement.getData());
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertTrue(xmlElement.getSubElements() instanceof Vector);
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    // Test constructor with name argument
    @Test
    public void testXmlElementConstructorName() {
        XmlElement xmlElement = new XmlElement("name");;
        assertEquals("name", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertTrue(xmlElement.getSubElements() instanceof Vector);
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    // Test constructor with name and attributes argument
    @Test
    public void testXmlElementConstructorNameAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "val1");

        XmlElement xmlElement = new XmlElement("name", attributes);
        assertEquals("name", xmlElement.getName());
        assertEquals("val1", xmlElement.getAttribute("attr1"));
        assertNull(xmlElement.getData());
        assertSame(attributes, xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertTrue(xmlElement.getSubElements() instanceof Vector);
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    // Test constructor with name and data argument
    @Test
    public void testXmlElementConstructorNameData() {
        XmlElement xmlElement = new XmlElement("name", "data");
        assertEquals("name", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertTrue(xmlElement.getSubElements() instanceof Vector);
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    // Test addAttribute method
    @Test
    public void testAddAttribute() {
        root.addAttribute("attr1", "val1");
        assertEquals("val1", root.getAttribute("attr1"));
    }

    // Test getAttribute method
    @Test
    public void testGetAttribute() {
        root.addAttribute("attr1", "val1");
        assertEquals("val1", root.getAttribute("attr1"));
        assertEquals(null, root.getAttribute("attr2"));
    }

    // Test getAttribute method with default value
    @Test
    public void testGetAttributeWithDefaultValue() {
        root.addAttribute("attr1", "val1");
        assertEquals("val1", root.getAttribute("attr1"));
        assertEquals("defaultVal", root.getAttribute("attr2", "defaultVal"));
    }

    // Test getAttributes method
    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "val1");
        root.setAttributes(attributes);
        assertEquals(attributes, root.getAttributes());
    }

    // Test setAttributes method
    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "val1");
        root.setAttributes(attributes);
        assertEquals(attributes, root.getAttributes());
    }

    // Test getAttributeNames method
    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");
        root.setAttributes(attributes);
        List<String> names = new ArrayList<>(attributes.keySet());
        Collections.sort(names);
        List<String> namesFromXmlElement = new ArrayList<>(root.getAttributeNames());
        Collections.sort(namesFromXmlElement);
        assertEquals(names, namesFromXmlElement);
    }

    // Test addElement method
    @Test
    public void testAddElement() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        assertTrue(root.getSubElements().contains(child));
        assertSame(root, child.getParent());
    }

    // Test removeElement method
    @Test
    public void testRemoveElement() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        root.removeElement(child);
        assertFalse(root.getSubElements().contains(child));
        assertNull(child.getParent());
    }

    // Test removeElement method by index
    @Test
    public void testRemoveElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        root.removeElement(1);
        assertFalse(root.getSubElements().contains(child2));
        assertNull(child2.getParent());
    }

    // Test removeAllElements method
    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        root.removeAllElements();
        assertTrue(root.getSubElements().isEmpty());
        assertNull(child1.getParent());
        assertNull(child2.getParent());
    }

    // Test removeFromParent method
    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        child.removeFromParent();
        assertFalse(root.getSubElements().contains(child));
        assertNull(child.getParent());
    }

    // Test append method
    @Test
    public void testAppend() {
        XmlElement child = new XmlElement("child");
        root.append(child);
        assertTrue(root.getSubElements().contains(child));
        assertSame(root, child.getParent());
    }

    // Test insertElement method
    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.insertElement(child2, 0);
        assertTrue(root.getSubElements().get(0) == child2);
        assertTrue(root.getSubElements().get(1) == child1);
        assertSame(root, child2.getParent());
        assertSame(root, child1.getParent());
    }

    // Test getElements method
    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        List<XmlElement> elements = root.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(child1));
        assertTrue(elements.contains(child2));
    }

    // Test count method
    @Test
    public void testCount() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(2, root.count());
    }

    // Test getElement method with simple path
    @Test
    public void testGetElementWithSimplePath() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        XmlElement result = root.getElement("child1");
        assertSame(child1, result);
    }

    // Test getElement method with complex path
    @Test
    public void testGetElementWithComplexPath() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        root.addElement(child1);
        child1.addElement(child2);
        child2.addElement(child3);
        XmlElement result = root.getElement("child1/child2/child3");
        assertSame(child3, result);
    }

    // Test getElement method when element not found
    @Test(expected = NullPointerException.class)
    public void testGetElementWhenElementNotFound() {
        root.getElement("nonexistent");
    }

    // Test getElement method with empty path
    @Test(expected = IllegalArgumentException.class)
    public void testGetElementWithEmptyPath() {
        root.getElement("");
    }

    // Test addSubElement method with simple path
    @Test
    public void testAddSubElementWithSimplePath() {
        XmlElement child1 = new XmlElement("child1");
        root.addElement(child1);
        XmlElement child2 = root.addSubElement("child2");
        assertEquals("child2", child2.getName());
        assertTrue(root.getSubElements().contains(child2));
        assertSame(child1, child2.getParent());
    }

    // Test addSubElement method with complex path
    @Test
    public void testAddSubElementWithComplexPath() {
        XmlElement child1 = new XmlElement("child1");
        root.addElement(child1);
        XmlElement child2 = root.getElement("child1");
        XmlElement child3 = child2.addSubElement("child3");
        assertEquals("child3", child3.getName());
        assertTrue(root.getSubElements().contains(child3));
        assertSame(child2, child3.getParent());
    }

    // Test addSubElement method when element already exists
    @Test
    public void testAddSubElementWhenElementAlreadyExists() {
        XmlElement child1 = new XmlElement("child1");
        root.addElement(child1);
        XmlElement child2 = root.getElement("child1/child2");
        root.addSubElement("child1/child2");
        assertSame(child2, root.getElement("child1/child2"));
    }

    // Test addSubElement method with empty path
    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithEmptyPath() {
        root.addSubElement("");
    }

    // Test addSubElement method with invalid path
    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithInvalidPath() {
        root.addSubElement("invalidPath");
    }

    // Test clone method
    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "val1");
        original.addElement(new XmlElement("child1"));
        XmlElement cloned = (XmlElement)original.clone();
        assertEquals("original", original.getName());
        assertEquals("original", cloned.getName());
        assertEquals("val1", original.getAttribute("attr1"));
        assertEquals("val1", cloned.getAttribute("attr1"));
        assertFalse(original.getSubElements().equals(cloned.getSubElements()));
        assertTrue(original.getSubElements().size() == cloned.getSubElements().size());
        for (int i = 0; i < original.getSubElements().size(); i++) {
            XmlElement oChild = original.getSubElements().get(i);
            XmlElement cChild = cloned.getSubElements().get(i);
            assertEquals(oChild.getName(), cChild.getName());
            assertEquals(oChild.getData(), cChild.getData());
            assertEquals(oChild.getAttributes(), cChild.getAttributes());
            assertFalse(oChild.getSubElements().equals(cChild.getSubElements()));
            assertTrue(oChild.getSubElements().size() == cChild.getSubElements().size());
        }
    }
}