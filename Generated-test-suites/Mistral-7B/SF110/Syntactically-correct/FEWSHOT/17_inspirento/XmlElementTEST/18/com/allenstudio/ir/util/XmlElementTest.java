package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testXmlElementCreation() {
        XmlElement element = new XmlElement();
        assertNull(element.getName());
        assertNull(element.getData());
        assertNull(element.getAttributes());
        assertTrue(element.subElements instanceof Vector);
        assertTrue(element.attributes instanceof Hashtable);

        XmlElement element1 = new XmlElement("name1");
        assertEquals("name1", element1.getName());
        assertNull(element1.getData());
        assertTrue(element1.getAttributes() instanceof Hashtable);
        assertTrue(element1.subElements instanceof Vector);

        XmlElement element2 = new XmlElement("name1", "data1");
        assertEquals("name1", element2.getName());
        assertEquals("data1", element2.getData());
        assertTrue(element2.getAttributes() instanceof Hashtable);
        assertTrue(element2.subElements instanceof Vector);

        XmlElement element3 = new XmlElement("name1", null);
        assertEquals("name1", element3.getName());
        assertNull(element3.getData());
        assertTrue(element3.getAttributes() instanceof Hashtable);
        assertTrue(element3.subElements instanceof Vector);
    }

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("name1");
        element.addAttribute("attr1", "value1");
        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals("name1", element.getName());
        assertEquals("value1", element.getAttributes().get("attr1"));
        assertEquals(1, element.getAttributes().size());

        element.addAttribute("attr1", "value2");
        assertEquals("value2", element.getAttribute("attr1"));
        assertEquals(1, element.getAttributes().size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("name1");
        element.addAttribute("attr1", "value1");
        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals(null, element.getAttribute("attr2"));
        assertEquals("name1", element.getName());
    }

    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement("name1");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Hashtable attributes = element.getAttributes();
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement("name1");
        Hashtable attributes = new Hashtable();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        element.setAttributes(attributes);
        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals("value2", element.getAttribute("attr2"));
        assertEquals(2, element.getAttributes().size());
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
        assertEquals(child, parent.getElement("child"));
        assertEquals(1, parent.count());
        assertEquals("parent", child.getParent().getName());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertTrue(parent.removeElement(child));
        assertNull(parent.getElement("child"));
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertNull(parent.getElements());
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
        assertNull(parent.getElement("child"));
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.append(child1);
        parent.append(child2);
        assertEquals(child1, parent.getElements().get(0));
        assertEquals(child2, parent.getElements().get(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(child2, parent.getElements().get(0));
        assertEquals(child1, parent.getElements().get(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
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
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        XmlElement element = parent.getElement("child1");
        assertEquals(child1, element);
        element = parent.getElement("child3");
        assertNull(element);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2");
        XmlElement child3 = parent.addSubElement("child2/grandchild");
        assertEquals(3, parent.count());
        assertEquals("child1", child1.getName());
        assertEquals("child2", child2.getName());
        assertEquals("grandchild", child3.getName());
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
        assertEquals(parent, child3.getParent());
    }

    @Test
    public void testClone() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        XmlElement clonedParent = (XmlElement)parent.clone();
        assertEquals(child1.getName(), clonedParent.getElement("child1").getName());
        assertEquals(child2.getName(), clonedParent.getElement("child2").getName());
        assertEquals(2, clonedParent.count());
    }

    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("root");
        XmlElement element1 = new XmlElement("element1", "value1");
        XmlElement element2 = new XmlElement("element2", "value2");
        XmlElement element3 = new XmlElement("element3");
        element3.addAttribute("attr1", "value1");
        root.addElement(element1);
        root.addElement(element2);
        root.addElement(element3);
        XmlElement element4 = new XmlElement("element4", "value4");
        element4.addAttribute("attr2", "value2");
        element3.addElement(element4);

        XmlElement.printNode(root, "");
    }
}