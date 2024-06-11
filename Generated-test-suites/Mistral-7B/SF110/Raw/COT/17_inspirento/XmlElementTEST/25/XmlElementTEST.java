package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("test");
        assertEquals("test", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("test", new Hashtable<String, String>(){{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertEquals("test", xmlElementWithNameAndAttributes.getName());
        assertEquals("value1", xmlElementWithNameAndAttributes.getAttribute("attribute1"));
        assertEquals("value2", xmlElementWithNameAndAttributes.getAttribute("attribute2"));

        XmlElement xmlElementWithNameData = new XmlElement("test", "data");
        assertEquals("test", xmlElementWithNameData.getName());
        assertEquals("data", xmlElementWithNameData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("attribute2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attribute2"));

        xmlElement.addAttribute("attribute1", "value3");
        assertEquals("value3", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("attribute2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attribute2"));

        xmlElement.addAttribute("attribute1", "value3");
        assertEquals("value3", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("nonexistentAttribute", null);
        assertNull(xmlElement.getAttribute("nonexistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        xmlElement.setAttributes(attributes);

        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals("value2", xmlElement.getAttribute("attribute2"));

        xmlElement.addAttribute("attribute3", "value3");
        Hashtable<String, String> updatedAttributes = xmlElement.getAttributes();
        assertEquals("value3", updatedAttributes.get("attribute3"));
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");

        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> attributeNameList = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            attributeNameList.add((String) attributeNames.nextElement());
        }
        assertTrue(attributeNameList.contains("attribute1"));
        assertTrue(attributeNameList.contains("attribute2"));
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("child");

        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("child");
        parentXmlElement.addElement(childXmlElement);

        XmlElement removedChild = parentXmlElement.removeElement(childXmlElement);
        assertEquals(childXmlElement, removedChild);
        assertNull(parentXmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);

        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveFromParentNullParent() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.removeFromParent();
    }

    @Test
    public void testGetElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parentXmlElement.addElement(child1);
        parentXmlElement.addElement(child2);

        XmlElement elementFound = parentXmlElement.getElement("child1");
        assertEquals(child1, elementFound);

        XmlElement elementNotFound = parentXmlElement.getElement("nonexistentChild");
        assertNull(elementNotFound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetElementInvalidPath() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.getElement("");
    }

    @Test
    public void testGetElementWithSubElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement grandChild1 = new XmlElement("grandchild1");
        XmlElement grandChild2 = new XmlElement("grandchild2");
        parentXmlElement.addElement(child1);
        child1.addElement(grandChild1);
        child1.addElement(grandChild2);

        XmlElement elementFound = parentXmlElement.getElement("child1/grandchild1");
        assertEquals(grandChild1, elementFound);

        XmlElement elementNotFound = parentXmlElement.getElement("child1/nonexistentGrandchild");
        assertNull(elementNotFound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementInvalidPath() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addSubElement("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addSubElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementEmptyName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addSubElement("");
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parentXmlElement.addElement(child1);

        XmlElement child2 = new XmlElement("child2");
        parentXmlElement.addSubElement(child2);
        assertEquals(child2, parentXmlElement.getElements().get(child1.getElements().size()));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parentXmlElement.addElement(child1);

        XmlElement child2 = new XmlElement("child2", "data");
        parentXmlElement.addSubElement(child2);
        assertEquals("data", parentXmlElement.getElements().get(child1.getElements().size()).getData());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.addElement(new XmlElement("child"));

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        assertEquals(xmlElement.getData(), clonedXmlElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(xmlElement.getElements().size(), clonedXmlElement.getElements().size());

        for (int i = 0; i < xmlElement.getElements().size(); i++) {
            XmlElement child1 = xmlElement.getElements().get(i);
            XmlElement child2 = clonedXmlElement.getElements().get(i);
            assertEquals(child1.getName(), child2.getName());
            assertEquals(child1.getData(), child2.getData());
            assertEquals(child1.getAttributes(), child2.getAttributes());
        }
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        XmlElement xmlElement3 = new XmlElement();
        XmlElement xmlElement4 = xmlElement1;

        assertFalse(xmlElement1.equals(xmlElement2));
        assertFalse(xmlElement1.equals(xmlElement3));
        assertTrue(xmlElement1.equals(xmlElement4));

        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement2.addAttribute("attribute1", "value1");
        assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.setData("data1");
        xmlElement2.setData("data2");
        assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.addElement(new XmlElement("child1"));
        xmlElement2.addElement(new XmlElement("child1"));
        assertTrue(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        XmlElement xmlElement3 = new XmlElement();
        XmlElement xmlElement4 = xmlElement1;

        int hashCode1 = xmlElement1.hashCode();
        int hashCode2 = xmlElement2.hashCode();
        int hashCode3 = xmlElement3.hashCode();
        int hashCode4 = xmlElement1.hashCode();

        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement2.addAttribute("attribute1", "value1");
        assertEquals(hashCode1, xmlElement1.hashCode());
        assertEquals(hashCode2, xmlElement2.hashCode());

        xmlElement1.setData("data1");
        xmlElement2.setData("data2");
        assertNotEquals(hashCode1, xmlElement2.hashCode());

        xmlElement1.addElement(new XmlElement("child1"));
        xmlElement2.addElement(new XmlElement("child1"));
        assertEquals(hashCode1, xmlElement1.hashCode());
        assertEquals(hashCode2, xmlElement2.hashCode());

        xmlElement1 = new XmlElement("name1");
        xmlElement2 = new XmlElement("name2");
        assertNotEquals(hashCode1, xmlElement2.hashCode());
    }
}