package com.allenstudio.ir.util.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("testElement");
        assertNotNull(xmlElementWithName);
        assertEquals("testElement", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("testElement", new Hashtable<String, String>() {{
            put("testAttribute1", "testValue1");
            put("testAttribute2", "testValue2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        assertEquals("testElement", xmlElementWithNameAndAttributes.getName());
        assertEquals("testValue1", xmlElementWithNameAndAttributes.getAttribute("testAttribute1"));
        assertEquals("testValue2", xmlElementWithNameAndAttributes.getAttribute("testAttribute2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("testElement", "testData");
        assertNotNull(xmlElementWithNameAndData);
        assertEquals("testElement", xmlElementWithNameAndData.getName());
        assertEquals("testData", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            addAttribute("testAttribute1", "testValue1");
        }};
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals(null, xmlElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            addAttribute("testAttribute1", null);
        }};
        assertEquals("", xmlElement.getAttribute("testAttribute1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            addAttribute("testAttribute1", "testValue1");
            addAttribute("testAttribute2", "testValue2");
        }};
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("testValue1", attributes.get("testAttribute1"));
        assertEquals("testValue2", attributes.get("testAttribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            addAttribute("testAttribute1", "testValue1");
        }};
        Hashtable<String, String> newAttributes = new Hashtable<String, String>() {{
            put("testAttribute1", "newTestValue1");
            put("testAttribute2", "newTestValue2");
        }};
        xmlElement.setAttributes(newAttributes);
        assertEquals("newTestValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("newTestValue2", xmlElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            addAttribute("testAttribute1", "testValue1");
            addAttribute("testAttribute2", "testValue2");
        }};
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        assertEquals("testAttribute1", attributeNames.nextElement());
        assertEquals("testAttribute2", attributeNames.nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertTrue(parentElement.removeElement(childElement));
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement removedChild = parentElement.removeElement(1);
        assertEquals(childElement2, removedChild);
        assertEquals(1, parentElement.count());
        assertEquals(childElement1, parentElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement("parentElement") {{
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};
        XmlElement childElement = parentElement.getElement(0);
        childElement.removeFromParent();
        assertEquals(1, parentElement.count());
        assertNull(parentElement.getElement(0));
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement("parentElement") {{
            addElement(new XmlElement("childElement1"));
        }};
        XmlElement childElement = new XmlElement("childElement2");
        parentElement.append(childElement);
        assertEquals(2, parentElement.count());
        assertEquals(childElement, parentElement.getElement(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement("parentElement") {{
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};
        XmlElement childElement = new XmlElement("childElement3");
        parentElement.insertElement(childElement, 0);
        assertEquals(3, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement("parentElement") {{
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};
        List<XmlElement> elements = parentElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(parentElement.getElement(0), elements.get(0));
        assertEquals(parentElement.getElement(1), elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement("parentElement") {{
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};
        assertEquals(2, parentElement.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithNullPath() {
        XmlElement parentElement = new XmlElement("parentElement");
        parentElement.addSubElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithEmptyName() {
        XmlElement parentElement = new XmlElement("parentElement");
        parentElement.addSubElement("");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementWithInvalidIndex() {
        XmlElement parentElement = new XmlElement("parentElement");
        parentElement.addElement(new XmlElement("childElement1"));
        parentElement.addElement(new XmlElement("childElement2"));
        parentElement.getElement(10);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintNodeWithNullNode() {
        XmlElement.printNode(null, "");
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        XmlElement original = new XmlElement("originalElement") {{
            addAttribute("testAttribute1", "testValue1");
            addAttribute("testAttribute2", "testValue2");
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};

        XmlElement cloned = (XmlElement)original.clone();
        assertEquals(original, cloned);

        // Verify that the attributes and elements are cloned separately
        cloned.addAttribute("testAttribute3", "testValue3");
        assertFalse(original.getAttributeNames().hasMoreElements());
        assertFalse(original.getElements().isEmpty());
        assertEquals(2, original.count());

        cloned.removeElement(cloned.getElement(0));
        assertEquals(1, cloned.count());
        assertFalse(original.getAttributeNames().hasMoreElements());
        assertEquals(2, original.count());
    }
}