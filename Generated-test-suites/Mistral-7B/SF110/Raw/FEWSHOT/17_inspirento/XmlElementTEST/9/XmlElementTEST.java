package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertEquals(Collections.emptyMap(), xmlElement.getAttributes());
        assertEquals(Collections.emptyList(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertNull(xmlElement.getData());

        xmlElement = new XmlElement("name");
        assertEquals("name", xmlElement.getName());
        assertEquals(Collections.emptyMap(), xmlElement.getAttributes());
        assertEquals(Collections.emptyList(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertNull(xmlElement.getData());

        xmlElement = new XmlElement("name", new Hashtable<String, String>(10));
        assertEquals("name", xmlElement.getName());
        assertNotEquals(Collections.emptyMap(), xmlElement.getAttributes());
        assertEquals(Collections.emptyList(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertNull(xmlElement.getData());

        xmlElement = new XmlElement("name", "data");
        assertEquals("name", xmlElement.getName());
        assertEquals(Collections.emptyMap(), xmlElement.getAttributes());
        assertEquals(Collections.emptyList(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", "value");
        assertNotEquals(Collections.emptyMap(), xmlElement.getAttributes());
        assertEquals("value", xmlElement.getAttribute("name"));

        xmlElement.addAttribute("name2", "value2");
        assertNotEquals(1, xmlElement.getAttributes().size());
        assertEquals("value2", xmlElement.getAttribute("name2"));
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
        assertEquals(null, xmlElement.getAttribute("name2"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        assertEquals(new Hashtable<String, String>() {{
            put("name1", "value1");
            put("name2", "value2");
        }}, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("name1", "value1");
            put("name2", "value2");
        }};
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        List<String> attributeNames = new ArrayList<String>(xmlElement.getAttributeNames());
        assertEquals(Arrays.asList("name1", "name2"), attributeNames);
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        parent.removeElement(child);
        assertEquals(0, parent.count());
        assertEquals(null, parent.getElement(0));
        assertEquals(null, child.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(Arrays.asList(child1, child2), elements);
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
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertEquals(null, parent.getElement("nonExistentChild"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1");
        assertEquals("child1", child.getName());
        assertEquals(parent, child.getParent());
        assertEquals(0, parent.count());
        assertEquals(child, parent.getElement("child1"));

        child = parent.addSubElement("child2/grandchild");
        assertEquals("grandchild", child.getName());
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement("child2/grandchild"));
        assertEquals(child, parent.getElement("child2"));
        assertEquals(0, child.count());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        xmlElement.addElement(new XmlElement("child"));
        xmlElement.setData("data");

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(xmlElement.getElements(), clonedXmlElement.getElements());
        assertEquals(xmlElement.getData(), clonedXmlElement.getData());

        xmlElement.addAttribute("name3", "value3");
        assertNotEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());

        XmlElement child = xmlElement.getElements().get(0);
        XmlElement clonedChild = (XmlElement) clonedXmlElement.getElements().get(0).clone();
        assertEquals(child.getName(), clonedChild.getName());
        assertEquals(child.getAttributes(), clonedChild.getAttributes());
        assertEquals(child.getElements(), clonedChild.getElements());
        assertEquals(child.getData(), clonedChild.getData());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("name1", "value1");
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        child1.addAttribute("name2", "value2");
        xmlElement.setData("data");

        Util.printNode(xmlElement, "");

        String expectedOutput = "root\n" +
                "name1:value1\n" +
                "child1\n" +
                " name2:value2\n";

        String actualOutput = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}