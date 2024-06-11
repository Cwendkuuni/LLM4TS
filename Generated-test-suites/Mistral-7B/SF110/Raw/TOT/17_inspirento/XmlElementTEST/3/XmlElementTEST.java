package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

    private XmlElement root;
    private XmlElement element1;
    private XmlElement element2;
    private XmlElement element3;

    @Before
    public void setUp() {
        root = new XmlElement("root"); ;
        element1 = new XmlElement("element1");
        element2 = new XmlElement("element2", "data2");
        element3 = new XmlElement("element3", "data3");
    }

    @Test
    public void testXmlElementCreation() {
        // Test default constructor
        XmlElement emptyElement = new XmlElement();
        assertNull(emptyElement.getName());
        assertTrue(emptyElement.getSubElements().isEmpty());
        assertFalse(emptyElement.hasAttributes());

        // Test constructors with name and attributes
        XmlElement elementWithNameAndAttributes = new XmlElement("name");
        assertEquals("name", elementWithNameAndAttributes.getName());
        assertTrue(elementWithNameAndAttributes.getSubElements().isEmpty());
        assertFalse(elementWithNameAndAttributes.hasAttributes());

        // Test constructor with name, data and attributes
        XmlElement elementWithNameDataAndAttributes = new XmlElement("name", "data");
        assertEquals("name", elementWithNameDataAndAttributes.getName());
        assertEquals("data", elementWithNameDataAndAttributes.getData());
        assertTrue(elementWithNameDataAndAttributes.getSubElements().isEmpty());
        assertFalse(elementWithNameDataAndAttributes.hasAttributes());

        // Test constructor with name and attributes
        XmlElement elementWithNameAndAttributes2 = new XmlElement("name", new Hashtable<String, String>() {{
            put("attr1", "value1");
            put("attr2", "value2");
        }});
        assertEquals("name", elementWithNameAndAttributes2.getName());
        assertTrue(elementWithNameAndAttributes2.getSubElements().isEmpty());
        assertTrue(elementWithNameAndAttributes2.hasAttributes());
        assertEquals("value1", elementWithNameAndAttributes2.getAttribute("attr1"));
        assertEquals("value2", elementWithNameAndAttributes2.getAttribute("attr2"));

        // Test constructor with name, data
        XmlElement elementWithNameData = new XmlElement("name", "data");
        assertEquals("name", elementWithNameData.getName());
        assertEquals("data", elementWithNameData.getData());
        assertTrue(elementWithNameData.getSubElements().isEmpty());
        assertFalse(elementWithNameData.hasAttributes());

        // Test constructor with name only
        XmlElement elementWithName = new XmlElement("name");
        assertEquals("name", elementWithName.getName());
        assertTrue(elementWithName.getSubElements().isEmpty());
        assertFalse(elementWithName.hasAttributes());

        // Test cloning
        XmlElement clonedElement = (XmlElement) root.clone();
        assertEquals(root, clonedElement.getParent());
        assertEquals(root.getName(), clonedElement.getName());
        assertEquals(root.getData(), clonedElement.getData());
        assertEquals(root.getAttributes(), clonedElement.getAttributes());
        assertEquals(root.getSubElements(), clonedElement.getSubElements());
    }

    @Test
    public void testAddingElements() {
        root.addElement(element1);
        root.addElement(element2);
        root.addElement(element3);

        List<XmlElement> subElements = root.getElements();
        assertEquals(3, subElements.size());
        assertEquals(element1, subElements.get(0));
        assertEquals(element2, subElements.get(1));
        assertEquals(element3, subElements.get(2));

        XmlElement newElement = new XmlElement("newElement");
        root.addElement(newElement);

        subElements = root.getElements();
        assertEquals(4, subElements.size());
        assertEquals(newElement, subElements.get(3));

        // Test adding an existing element
        root.addElement(element2);
        subElements = root.getElements();
        assertEquals(4, subElements.size());
        assertEquals(element2, subElements.get(1));
    }

    @Test
    public void testAddingAttributes() {
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        Hashtable<String, String> attributes = element1.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testGettingAttributes() {
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        assertEquals("value1", element1.getAttribute("attr1"));
        assertEquals("value2", element1.getAttribute("attr2"));
        assertEquals(null, element1.getAttribute("nonExistentAttribute"));

        String defaultValue = "default";
        assertEquals("value1", element1.getAttribute("attr1", defaultValue));
        assertEquals("default", element1.getAttribute("nonExistentAttribute", defaultValue));
    }

    @Test
    public void testGettingEmptyAttributes() {
        XmlElement emptyElement = new XmlElement();
        assertEquals(0, emptyElement.getAttributes().size());
    }

    @Test
    public void testSettingAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>(2);
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        element1.setAttributes(attributes);

        assertEquals(2, element1.getAttributes().size());
        assertEquals("value1", element1.getAttribute("attr1"));
        assertEquals("value2", element1.getAttribute("attr2"));

        element1.setAttributes(new Hashtable<String, String>());
        assertEquals(0, element1.getAttributes().size());
    }

    @Test
    public void testGettingAttributeNames() {
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        Enumeration<String> names = element1.getAttributeNames();
        List<String> namesList = new ArrayList<String>();
        while (names.hasMoreElements()) {
            namesList.add((String) names.nextElement());
        }

        assertEquals(2, namesList.size());
        assertEquals("attr1", namesList.get(0));
        assertEquals("attr2", namesList.get(1));
    }

    @Test
    public void testRemovingElement() {
        root.addElement(element1);
        root.addElement(element2);

        List<XmlElement> subElements = root.getElements();
        assertEquals(2, subElements.size());

        root.removeElement(element1);
        subElements = root.getElements();
        assertEquals(1, subElements.size());
        assertNull(root.getElements().get(0).getParent());
    }

    @Test
    public void testRemovingElementByIndex() {
        root.addElement(element1);
        root.addElement(element2);

        List<XmlElement> subElements = root.getElements();
        assertEquals(2, subElements.size());

        XmlElement removedElement = root.removeElement(0);
        assertEquals(element1, removedElement);
        subElements = root.getElements();
        assertEquals(1, subElements.size());
        assertNull(root.getElements().get(0).getParent());
    }

    @Test
    public void testRemovingAllElements() {
        root.addElement(element1);
        root.addElement(element2);

        List<XmlElement> subElements = root.getElements();
        assertEquals(2, subElements.size());

        root.removeAllElements();
        subElements = root.getElements();
        assertEquals(0, subElements.size());
    }

    @Test
    public void testRemovingFromParent() {
        root.addElement(element1);
        element1.setParent(root);

        root.removeFromParent();
        assertEquals(null, element1.getParent());
    }

    @Test
    public void testGetElementByIndex() {
        root.addElement(element1);
        root.addElement(element2);

        XmlElement elementByIndex = root.getElement(1);
        assertEquals(element2, elementByIndex);
    }

    @Test
    public void testGetElementByPath() {
        root.addElement(element1);
        element1.addElement(element2);
        element2.addElement(element3);

        XmlElement elementByPath = root.getElement("element1/element2/element3");
        assertEquals(element3, elementByPath);

        XmlElement elementByPathNull = root.getElement("nonexistentPath");
        assertNull(elementByPathNull);
    }

    @Test
    public void testAddingSubElementByPath() {
        root.addElement(element1);

        XmlElement elementByPath = root.addSubElement("element1/element4");
        assertEquals("element4", elementByPath.getName());
        assertEquals(0, elementByPath.getSubElements().size());
        assertEquals(element1, elementByPath.getParent());

        XmlElement elementByPath2 = root.addSubElement("element1/element2/element5");
        assertEquals("element5", elementByPath2.getName());
        assertEquals(0, elementByPath2.getSubElements().size());
        assertEquals(element1, elementByPath2.getParent());
        assertEquals(element1, elementByPath2.getParent().getElements().get(0));
    }
}