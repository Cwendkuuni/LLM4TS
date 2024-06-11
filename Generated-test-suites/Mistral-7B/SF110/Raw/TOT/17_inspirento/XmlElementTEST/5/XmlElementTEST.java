package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setUp() {
        rootElement = new XmlElement("root");;
    }

    @Test
    public void testConstructorWithName() {
        XmlElement element = new XmlElement("element");
        assertEquals("element", element.getName());
        assertNull(element.getData());
        assertNotNull(element.getAttributes());
        assertTrue(element.getAttributes().isEmpty());
        assertFalse(element.hasSubElements());
    }

    @Test
    public void testAddAttribute() {
        rootElement.addAttribute("test", "testValue");
        assertEquals("testValue", rootElement.getAttribute("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullName() {
        rootElement.addAttribute(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullValue() {
        rootElement.addAttribute("test", null);
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        rootElement.addAttribute("test", "testValue");
        assertEquals("testValue", rootElement.getAttribute("test", "defaultValue"));
    }

    @Test
    public void testGetAttributeWithNullValue() {
        rootElement.addAttribute("test", "testValue");
        assertNull(rootElement.getAttribute("nonexistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        rootElement.addAttribute("test1", "testValue1");
        rootElement.addAttribute("test2", "testValue2");
        Map<String, String> attributes = rootElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("testValue1", attributes.get("test1"));
        assertEquals("testValue2", attributes.get("test2"));
    }

    @Test
    public void testSetAttributes() {
        rootElement.setAttributes(new Hashtable<String, String>() {{
            put("test1", "testValue1");
            put("test2", "testValue2");
        }});
        Map<String, String> attributes = rootElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("testValue1", attributes.get("test1"));
        assertEquals("testValue2", attributes.get("test2"));
    }

    @Test
    public void testGetAttributeNames() {
        rootElement.addAttribute("test1", "testValue1");
        rootElement.addAttribute("test2", "testValue2");
        List<String> attributeNames = new ArrayList<>(rootElement.getAttributeNames());
        assertEquals(2, attributeNames.size());
        assertTrue(attributeNames.contains("test1"));
        assertTrue(attributeNames.contains("test2"));
    }

    @Test
    public void testAddElementWithNullElement() {
        try {
            rootElement.addElement(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("child");
        rootElement.addElement(childElement);
        assertEquals(1, rootElement.count());
        XmlElement element = rootElement.getElement(0);
        assertEquals("child", element.getName());
        assertNull(element.getData());
        assertTrue(element.hasSubElements());
        List<XmlElement> childElements = element.getElements();
        assertEquals(1, childElements.size());
        assertEquals("child", childElements.get(0).getName());
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement("child");
        rootElement.addElement(childElement);
        rootElement.removeElement(childElement);
        assertEquals(0, rootElement.count());
    }

    @Test
    public void testRemoveElementWithNullElement() {
        try {
            rootElement.removeElement(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement childElement = new XmlElement("child1");
        rootElement.addElement(childElement);
        XmlElement childElement2 = new XmlElement("child2");
        rootElement.addElement(childElement2);
        rootElement.removeAllElements();
        assertEquals(0, rootElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(1, parent.count());
        assertEquals("child", parent.getElement(0).getName());
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.insertElement(child1, 0);
        parent.insertElement(child2, 1);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals("child1", elements.get(0).getName());
        assertEquals("child2", elements.get(1).getName());
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
        assertEquals("child1", elements.get(0).getName());
        assertEquals("child2", elements.get(1).getName());
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
    public void testClone() {
        XmlElement element = new XmlElement("element") {{
            addAttribute("test", "testValue");
            addSubElement("subElement1");
            addSubElement("subElement2");
        }};
        XmlElement clonedElement = (XmlElement)element.clone();
        assertEquals("element", clonedElement.getName());
        assertEquals("testValue", clonedElement.getAttribute("test"));
        assertEquals(2, clonedElement.count());
        XmlElement subElement1 = clonedElement.getElement(0);
        assertEquals("subElement1", subElement1.getName());
        assertNull(subElement1.getData());
        XmlElement subElement2 = clonedElement.getElement(1);
        assertEquals("subElement2", subElement2.getName());
        assertNull(subElement2.getData());
    }
}