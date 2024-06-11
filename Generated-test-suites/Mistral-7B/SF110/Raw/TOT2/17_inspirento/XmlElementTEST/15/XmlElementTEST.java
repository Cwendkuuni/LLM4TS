package com.allenstudio.ir.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setup() {
        root = new XmlElement("root");
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attribute1", "value1");
        String attributeValue = root.getAttribute("attribute1");
        assertEquals("value1", attributeValue);
        Object oldValue = root.addAttribute("attribute1", "value2");
        assertEquals("value1", oldValue);
        assertEquals("value2", root.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attribute1", "value1");
        assertEquals("value1", root.getAttribute("attribute1"));
        assertEquals("defaultValue", root.getAttribute("attribute2"));
        assertNull(root.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testAddElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        XmlElement addedChild = root.addElement("child1");
        assertEquals(child1, addedChild);
        assertEquals(2, root.count());
        XmlElement existingChild = root.getElement("child1");
        assertEquals(existingChild, addedChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithSameName() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        XmlElement addedChild = root.addElement("child");
        // The test should fail with an IllegalArgumentException
    }

    @Test
    public void testRemoveElement() {
        root.addElement(new XmlElement("child1"));
        root.addElement(new XmlElement("child2"));
        XmlElement childToRemove = root.getElement("child1");
        root.removeElement(childToRemove);
        assertEquals(1, root.count());
        assertNull(root.getElement("child1"));
    }

    @Test
    public void testGetElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        root.addElement(element1);
        root.addElement(element2);
        List<XmlElement> elements = root.getElements();
        assertEquals(2, elements.size());
        assertEquals(element1, elements.get(0));
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testGetElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        root.addElement(element1);
        root.addElement(element2);
        XmlElement foundElement = root.getElement("element1");
        assertEquals(element1, foundElement);
        XmlElement notFoundElement = root.getElement("nonExistentElement");
        assertNull(notFoundElement);
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        XmlElement cloned = (XmlElement) original.clone();
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getData(), cloned.getData());
        Hashtable<String, String> originalAttributes = original.getAttributes();
        Hashtable<String, String> clonedAttributes = cloned.getAttributes();
        assertEquals(originalAttributes.size(), clonedAttributes.size());
        for (String key : originalAttributes.keySet()) {
            assertEquals(originalAttributes.get(key), clonedAttributes.get(key));
        }
        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> clonedSubElements = cloned.getElements();
        assertEquals(originalSubElements.size(), clonedSubElements.size());
        for (XmlElement subElement : originalSubElements) {
            XmlElement foundSubElement = null;
            for (XmlElement clonedSubElement : clonedSubElements) {
                if (clonedSubElement.getName().equals(subElement.getName())) {
                    foundSubElement = clonedSubElement;
                    break;
                }
            }
            assertEquals(subElement, foundSubElement);
        }
    }
}