package com.allenstudio.ir.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "testValue");
        assertEquals("testValue", xmlElement.getAttribute("test"));
    }

    @Test
    public void testAddAttributeWithNullNameOrValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "testValue");
        assertNull(xmlElement.getAttribute(null));
    }

    @Test
    public void testAddAttributeWithSameAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "testValue");
        xmlElement.addAttribute("test", "newTestValue");
        assertEquals("testValue", xmlElement.getAttribute("test"));
    }

    @Test
    public void testAddAttributeWithEmptyNameOrValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("", "testValue");
        assertEquals("testValue", xmlElement.getAttribute(""));
        xmlElement.addAttribute("test", "");
        assertEquals("", xmlElement.getAttribute("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullValueInConstructor() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "testValue");
    }

    @Test
    public void testAddAttributeWithEmptyNameOrValueInConstructor() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("", "testValue");
        xmlElement.addAttribute("test", "");
        assertEquals("", xmlElement.getAttribute(""));
        assertEquals("testValue", xmlElement.getAttribute("test"));
    }

    @Test
    public void testAddAttributeWhenCapacityIsReached() {
        XmlElement xmlElement = new XmlElement();
        for (int i = 0; i < XmlElement.DEFAULT_CAPACITY; i++) {
            xmlElement.addAttribute("test" + i, "testValue" + i);
        }
        try {
            xmlElement.addAttribute("test" + XmlElement.DEFAULT_CAPACITY, "testValue" + XmlElement.DEFAULT_CAPACITY);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementWithNullElement() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(null);
    }

    @Test
    public void testAddElementWithExistingElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement existingChild = parent.getElement("child");
        assertEquals(existingChild, parent.addElement("child"));
    }

    @Test
    public void testAddElementWhenCapacityIsReached() {
        XmlElement xmlElement = new XmlElement();
        for (int i = 0; i < XmlElement.DEFAULT_CAPACITY; i++) {
            xmlElement.addElement(new XmlElement("element" + i));
        }
        try {
            xmlElement.addElement(new XmlElement("element" + XmlElement.DEFAULT_CAPACITY));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetElementWithNullOrEmptyPath() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getElement(null));
        assertNull(xmlElement.getElement(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetElementWithInvalidIndex() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        xmlElement.addElement(new XmlElement("child2"));
        xmlElement.addElement(new XmlElement("child3"));
        xmlElement.getElement(3);
    }

    @Test
    public void testGetElementWithNonExistentElement() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        xmlElement.addElement(new XmlElement("child2"));
        assertNull(xmlElement.getElement("nonExistentChild"));
    }

    @Test
    public void testRemoveElementWithNullElement() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        assertNull(xmlElement.removeElement(null));
    }

    @Test
    public void testRemoveElementWithNonExistentElement() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        assertNull(xmlElement.removeElement("nonExistentChild"));
    }

    @Test
    public void testRemoveElementWithAnIndexOutOfBounds() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        xmlElement.addElement(new XmlElement("child2"));
        xmlElement.removeElement(3);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("child1"));
        xmlElement.addElement(new XmlElement("child2"));
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testSetParentWithNullParent() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setParent(null);
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testSetParentWithExistingParent() {
        XmlElement parent = new XmlElement();
        XmlElement xmlElement = new XmlElement();
        parent.addElement(xmlElement);
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testSetParentWhenParentIsAlreadySet() {
        XmlElement xmlElement = new XmlElement();
        XmlElement parent = new XmlElement();
        xmlElement.setParent(parent);
        assertThrows(IllegalStateException.class, () -> xmlElement.setParent(parent));
    }

    @Test
    public void testAddSubElementWithNullOrEmptyPath() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.addSubElement(null));
        assertNull(xmlElement.addSubElement(""));
    }

    @Test
    public void testAddSubElementWithExistingElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement existingElement = new XmlElement("existingElement");
        xmlElement.addElement(existingElement);
        XmlElement newElement = xmlElement.addSubElement("newElement");
        assertEquals(newElement, xmlElement.getElement("existingElement/newElement"));
    }

    @Test
    public void testAddSubElementWhenCapacityIsReached() {
        XmlElement xmlElement = new XmlElement();
        for (int i = 0; i < XmlElement.DEFAULT_CAPACITY; i++) {
            xmlElement.addSubElement("element" + i);
        }
        try {
            xmlElement.addSubElement("element" + XmlElement.DEFAULT_CAPACITY);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.addElement(new XmlElement("child1"));
        xmlElement.addElement(new XmlElement("child2"));

        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedElement.getName());
        assertEquals(xmlElement.getData(), clonedElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(xmlElement.getSubElements(), clonedElement.getSubElements());
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        XmlElement xmlElement1 = new XmlElement("testElement1");
        XmlElement xmlElement2 = new XmlElement("testElement2");
        assertNotEquals(xmlElement1, xmlElement2);
    }

    @Test
    public void testEqualsWithTheSameObject() {
        XmlElement xmlElement = new XmlElement("testElement");
        XmlElement sameXmlElement = xmlElement;
        assertEquals(xmlElement, sameXmlElement);
    }

    @Test
    public void testEqualsWithNullObject() {
        XmlElement xmlElement = new XmlElement("testElement");
        assertNotEquals(xmlElement, null);
    }
}