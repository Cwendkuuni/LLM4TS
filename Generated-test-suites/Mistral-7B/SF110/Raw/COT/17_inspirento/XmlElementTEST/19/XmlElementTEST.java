package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("test");
        assertEquals("test", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        String key1 = "attribute1";
        String value1 = "value1";
        XmlElement xmlElement = new XmlElement("test", new Hashtable<String, String>() {{
            put(key1, value1);
        }});
        assertEquals("test", xmlElement.getName());
        assertEquals(value1, xmlElement.getAttribute(key1));
        assertNull(xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().containsKey(key1));
        assertTrue(xmlElement.getAttributes().containsValue(value1));
        assertNotNull(xmlElement.getSubElements());
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertTrue(xmlElement.getAttributes().containsKey("attribute1"));
        assertTrue(xmlElement.getAttributes().containsValue("value1"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullName() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addAttribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullValue() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addAttribute("attribute1", null);
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
        assertTrue(parent.getSubElements().contains(child));
        assertEquals(child, parent.getSubElements().get(0));
    }

    @Test
    public void testAddElementWithExistingChild() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        assertFalse(parent.addElement(child2));
        assertTrue(parent.getSubElements().contains(child1));
        assertTrue(parent.getSubElements().contains(child2));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertTrue(parent.getSubElements().contains(child));
        assertEquals(child, parent.getSubElements().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubElementWithNullName() {
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithEmptyName() {
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement("");
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addSubElement("child1");
        parent.addSubElement("child1", "child2");
        assertTrue(parent.getSubElements().contains(child1));
        assertTrue(child1.getSubElements().contains(child2));
        assertEquals(child2, child1.getSubElements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithInvalidPath() {
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement("child1/child2");
    }

    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addSubElement(child1);
        parent.addSubElement(child1, child2);
        assertEquals(child2, parent.getElement("child1/child2"));
        assertEquals(child2, parent.getElement("child1", child2));
    }

    @Test
    public void testGetElementWithNullPath() {
        XmlElement parent = new XmlElement("parent");
        assertEquals(null, parent.getElement(null));
    }

    @Test
    public void testGetElementWithInvalidPath() {
        XmlElement parent = new XmlElement("parent");
        assertEquals(null, parent.getElement("child1/child2"));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addSubElement(child1);
        parent.addSubElement(child2);
        assertTrue(parent.removeElement(child2));
        assertFalse(parent.getSubElements().contains(child2));
        assertEquals(child1, parent.getSubElements().get(0));
    }

    @Test
    public void testRemoveElementWithNullChild() {
        XmlElement parent = new XmlElement("parent");
        assertFalse(parent.removeElement(null));
    }

    @Test
    public void testRemoveElementWithNonExistentChild() {
        XmlElement parent = new XmlElement("parent");
        assertFalse(parent.removeElement(new XmlElement("nonExistentChild")));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveFromParentWithNullParent() {
        XmlElement child = new XmlElement("child");
        assertNull(child.removeFromParent());
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("parent");
        XmlElement clone = original.clone();
        assertEquals(original, clone);
        assertFalse(original == clone);
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getData(), clone.getData());
        assertEquals(original.getAttributes(), clone.getAttributes());
        assertEquals(original.getSubElements().size(), clone.getSubElements().size());
        for (XmlElement element : original.getSubElements()) {
            XmlElement clonedElement = clone.getSubElements().get(clone.getSubElements().indexOf(element));
            assertEquals(element, clonedElement);
            assertFalse(element == clonedElement);
        }
    }
}