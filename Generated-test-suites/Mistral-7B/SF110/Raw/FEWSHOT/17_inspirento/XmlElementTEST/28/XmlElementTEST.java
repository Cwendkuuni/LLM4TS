package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("test");
        assertNotNull(xmlElementWithName);
        assertEquals("test", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("test", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        assertEquals("test", xmlElementWithNameAndAttributes.getName());
        assertEquals("value1", xmlElementWithNameAndAttributes.getAttribute("attribute1"));
        assertEquals("value2", xmlElementWithNameAndAttributes.getAttribute("attribute2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("test", "test data");
        assertNotNull(xmlElementWithNameAndData);
        assertEquals("test", xmlElementWithNameAndData.getName());
        assertEquals("test data", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "test value");
        assertEquals("test value", xmlElement.getAttribute("test"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttribute("test", "test value");
        assertEquals("test value", xmlElement.getAttribute("test"));
        assertEquals(null, xmlElement.getAttribute("nonexistent"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertEquals(2, xmlElement.getAttributes().size());
        assertEquals("value1", xmlElement.getAttributes().get("attribute1"));
        assertEquals("value2", xmlElement.getAttributes().get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }};
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testgetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        Enumeration<String> names = xmlElement.getAttributeNames();
        assertEquals(2, names.size());
        assertTrue(names.hasMoreElements());
        assertEquals("attribute1", names.nextElement());
        assertTrue(names.hasMoreElements());
        assertEquals("attribute2", names.nextElement());
        assertFalse(names.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.getElements().size());
        assertEquals(child, parent.getElements().get(0));
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.getElements().size());
        parent.removeElement(child);
        assertEquals(0, parent.getElements().size());
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.getElements().size());
        parent.removeAllElements();
        assertEquals(0, parent.getElements().size());
        assertNull(child1.getParent());
        assertNull(child2.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(1, parent.getElements().size());
        assertEquals(child, parent.getElements().get(0));
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(2, parent.getElements().size());
        assertEquals(child2, parent.getElements().get(0));
        assertEquals(child1, parent.getElements().get(1));
        assertEquals(parent, child2.getParent());
        assertEquals(parent, child1.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testgetElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        XmlElement child3 = parent.getElement("child1/child3");
        assertNull(child3);
        child3 = parent.getElement("child1");
        assertEquals(child1, child3);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child1/child2");
        XmlElement child3 = parent.addSubElement("child1/child2/child3");
        assertEquals(3, parent.count());
        assertEquals(child3, parent.getElement("child1/child2/child3"));
    }

    @Test
    public void testAddSubElementWithAttributes() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child1/child2", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement("child1/child2"));
        assertEquals("value1", child2.getAttribute("attribute1"));
        assertEquals("value2", child2.getAttribute("attribute2"));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child1/child2", null, "data");
        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement("child1/child2"));
        assertEquals("data", child2.getData());
    }

    @Test
    public void testClone() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child1/child2");
        XmlElement clonedParent = (XmlElement) parent.clone();
        assertEquals(parent, clonedParent);
        assertEquals(parent.hashCode(), clonedParent.hashCode());
        assertEquals(2, clonedParent.count());
        XmlElement clonedChild1 = clonedParent.getElement("child1");
        XmlElement clonedChild2 = clonedParent.getElement("child1/child2");
        assertEquals(child1, clonedChild1);
        assertEquals(child2, clonedChild2);
        assertNotSame(parent, clonedChild1);
        assertNotSame(parent, clonedChild2);
        assertEquals(clonedParent, clonedChild1.getParent());
        assertEquals(clonedParent, clonedChild2.getParent());
    }

    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("root");
        XmlElement element1 = root.addSubElement("element1");
        XmlElement element2 = root.addSubElement("element1/element2", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        XmlElement element3 = root.addSubElement("element1/element3", null, "data3");
        root.printNode(0);
        String expectedOutput = "root\n" +
                "element1 = 'data3'\n" +
                "element1:attribute1:value1\n" +
                "element1:attribute2:value2\n";
        String actualOutput = System.out.getOutputStream().toString();
        assertEquals(expectedOutput, actualOutput);
    }
}