package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setup() {
        this.rootElement = new XmlElement("root");
    }

    @Test
    public void testConstructorDefault() {
        XmlElement element = new XmlElement();
        assertNull(element.getName());
        assertNull(element.getData());
        assertNotNull(element.getAttributes());
        assertNotNull(element.subElements);
        assertTrue(element.subElements instanceof Vector);
        assertTrue(element.attributes instanceof Hashtable);
        assertEquals(0, element.subElements.size());
        assertEquals(0, element.attributes.size());
    }

    @Test
    public void testConstructorWithName() {
        XmlElement element = new XmlElement("testElement");
        assertEquals("testElement", element.getName());
        assertNull(element.getData());
        assertNotNull(element.getAttributes());
        assertNotNull(element.subElements);
        assertTrue(element.subElements instanceof Vector);
        assertTrue(element.attributes instanceof Hashtable);
        assertEquals(0, element.subElements.size());
        assertEquals(0, element.attributes.size());
    }

    @Test
    public void testConstructorWithNameAndAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        XmlElement element = new XmlElement("testElement", attributes);
        assertEquals("testElement", element.getName());
        assertNotNull(element.getData());
        assertEquals(2, element.attributes.size());
        assertSame("testValue1", element.getAttribute("testKey1"));
        assertSame("testValue2", element.getAttribute("testKey2"));
        assertNull(element.subElements);
    }

    @Test
    public void testConstructorWithNameAndData() {
        XmlElement element = new XmlElement("testElement", "testData");
        assertEquals("testElement", element.getName());
        assertSame("testData", element.getData());
        assertNotNull(element.getAttributes());
        assertNotNull(element.subElements);
        assertTrue(element.subElements instanceof Vector);
        assertTrue(element.attributes instanceof Hashtable);
        assertEquals(0, element.subElements.size());
        assertEquals(0, element.attributes.size());
    }

    @Test
    public void testAddAttribute() {
        this.rootElement.addAttribute("testKey", "testValue");
        assertEquals("testValue", this.rootElement.getAttribute("testKey"));
    }

    @Test
    public void testGetAttribute() {
        this.rootElement.setAttribute("testKey", "testValue");
        assertEquals("testValue", this.rootElement.getAttribute("testKey"));
        assertEquals(null, this.rootElement.getAttribute("nonExistentKey"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        this.rootElement.setName("root");
        this.rootElement.setAttribute("testKey", "testValue");
        assertEquals("testValue", this.rootElement.getAttribute("testKey", "defaultValue"));
        assertEquals("defaultValue", this.rootElement.getAttribute("nonExistentKey", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        this.rootElement.setAttributes(attributes);

        Map<String, String> result = this.rootElement.getAttributes();
        assertEquals(2, result.size());
        assertSame("testValue1", result.get("testKey1"));
        assertSame("testValue2", result.get("testKey2"));
    }

    @Test
    public void testSetAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        this.rootElement.setAttributes(attributes);

        Map<String, String> result = this.rootElement.getAttributes();
        assertEquals(2, result.size());
        assertSame("testValue1", result.get("testKey1"));
        assertSame("testValue2", result.get("testKey2"));
    }

    @Test
    public void testGetAttributeNames() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        this.rootElement.setAttributes(attributes);

        Enumeration<String> attributeNames = this.rootElement.getAttributeNames();
        ArrayList<String> names = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            names.add((String) attributeNames.nextElement());
        }

        ArrayList<String> expectedNames = new ArrayList<>();
        expectedNames.add("testKey1");
        expectedNames.add("testKey2");

        assertEquals(expectedNames, names);
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("testChildElement");
        this.rootElement.addElement(childElement);

        XmlElement retrievedChildElement = this.rootElement.getElement("testChildElement");
        assertEquals(childElement, retrievedChildElement);
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement("testChildElement");
        this.rootElement.addElement(childElement);

        XmlElement retrievedChildElement = this.rootElement.removeElement(childElement);
        assertSame(childElement, retrievedChildElement);
        assertFalse(this.rootElement.hasElement("testChildElement"));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement childElement1 = new XmlElement("testChildElement1");
        this.rootElement.addElement(childElement1);

        XmlElement childElement2 = new XmlElement("testChildElement2");
        this.rootElement.addElement(childElement2);

        XmlElement retrievedChildElement = this.rootElement.removeElement(1);
        assertEquals(childElement2, retrievedChildElement);
        assertFalse(this.rootElement.hasElement("testChildElement2"));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement childElement1 = new XmlElement("testChildElement1");
        this.rootElement.addElement(childElement1);

        XmlElement childElement2 = new XmlElement("testChildElement2");
        this.rootElement.addElement(childElement2);

        this.rootElement.removeAllElements();
        assertTrue(this.rootElement.subElements.isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement childElement = new XmlElement("testChildElement");
        this.rootElement.addElement(childElement);
        childElement.removeFromParent();
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement childElement = new XmlElement("testChildElement");
        XmlElement parentElement = new XmlElement("testParentElement");

        parentElement.append(childElement);
        assertEquals(childElement, parentElement.getLastChild());
    }

    @Test
    public void testInsertElement() {
        XmlElement childElement1 = new XmlElement("testChildElement1");
        XmlElement childElement2 = new XmlElement("testChildElement2");
        XmlElement parentElement = new XmlElement("testParentElement");

        parentElement.insertElement(childElement1, 0);
        parentElement.insertElement(childElement2, 1);

        assertEquals(childElement2, parentElement.getElement(1));
        assertEquals(childElement1, parentElement.getElement(0));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement("testParentElement");
        XmlElement childElement1 = new XmlElement("testChildElement1");
        XmlElement childElement2 = new XmlElement("testChildElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        List<XmlElement> elements = parentElement.getElements();
        assertEquals(2, elements.size());
        assertSame(childElement1, elements.get(0));
        assertSame(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement("testParentElement");
        XmlElement childElement1 = new XmlElement("testChildElement1");
        XmlElement childElement2 = new XmlElement("testChildElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement("testParentElement");
        XmlElement childElement1 = new XmlElement("testChildElement1");
        XmlElement childElement2 = new XmlElement("testChildElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement retrievedChildElement1 = parentElement.getElement("testChildElement1");
        assertEquals(childElement1, retrievedChildElement1);

        XmlElement retrievedChildElement2 = parentElement.getElement("testChildElement2");
        assertEquals(childElement2, retrievedChildElement2);

        assertNull(parentElement.getElement("nonExistentChildElement"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement("testParentElement");
        XmlElement childElement = parentElement.addSubElement("testChildElement1");

        assertEquals("testChildElement1", childElement.getName());
        assertEquals(parentElement, childElement.getParent());
        assertTrue(parentElement.hasElement("testChildElement1"));
    }

    @Test
    public void testAddSubElementWithExistingParent() {
        XmlElement parentElement1 = new XmlElement("testParentElement1");
        XmlElement parentElement2 = new XmlElement("testParentElement2");

        parentElement1.addElement(parentElement2);
        XmlElement childElement = parentElement1.addSubElement("testChildElement1");

        assertEquals("testChildElement1", childElement.getName());
        assertEquals(parentElement1, childElement.getParent());
        assertTrue(parentElement1.hasElement("testParentElement2"));
        assertTrue(parentElement1.hasElement("testChildElement1"));
    }

    @Test
    public void testAddSubElementWithExistingChild() {
        XmlElement parentElement = new XmlElement("testParentElement");
        XmlElement childElement1 = new XmlElement("testChildElement1");
        XmlElement childElement2 = new XmlElement("testChildElement2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement newChildElement = parentElement.addSubElement("testChildElement1");

        assertEquals("testChildElement1", newChildElement.getName());
        assertEquals(parentElement, newChildElement.getParent());
        assertTrue(parentElement.hasElement("testChildElement1"));
        assertSame(childElement1, newChildElement);
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("testElement");
        originalElement.setAttribute("testKey", "testValue");
        originalElement.addSubElement("testChildElement");

        XmlElement clonedElement = (XmlElement) originalElement.clone();

        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertSame(originalElement.getAttributes(), clonedElement.getAttributes());
        assertSame(originalElement.subElements, clonedElement.subElements);

        for (XmlElement child : originalElement.getElements()) {
            XmlElement clonedChild = clonedElement.getElement(child.getName());
            assertEquals(child.getName(), clonedChild.getName());
            assertEquals(child.getData(), clonedChild.getData());
            assertSame(child.getAttributes(), clonedChild.getAttributes());
            assertSame(child.subElements, clonedChild.subElements);
        }
    }

    @Test
    public void testSetName() {
        XmlElement element = new XmlElement("testElement");
        element.setName("newName");
        assertEquals("newName", element.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement rootElement = new XmlElement("root");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2") {{
            setAttribute("testKey", "testValue");
        }};
        XmlElement child3 = new XmlElement("child3") {{
            setAttribute("testKey1", "testValue1");
            setAttribute("testKey2", "testValue2");
            addSubElement("grandChild1");
            addSubElement("grandChild2");
        }};

        rootElement.addElement(child1);
        rootElement.addElement(child2);
        rootElement.addElement(child3);

        printNode(rootElement, "");
    }

    @After
    public void teardown() {
        this.rootElement = null;
    }
}