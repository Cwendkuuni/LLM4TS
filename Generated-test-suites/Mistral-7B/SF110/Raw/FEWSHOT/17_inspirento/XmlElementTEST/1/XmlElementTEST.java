package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("testElement");
        assertNotNull(xmlElementWithName);
        assertEquals("testElement", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndData = new XmlElement("testElement", "testData");
        assertNotNull(xmlElementWithNameAndData);
        assertEquals("testElement", xmlElementWithNameAndData.getName());
        assertEquals("testData", xmlElementWithNameAndData.getData());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("testElement", new Hashtable<String, String>() {{
            put("testAttribute1", "testValue1");
            put("testAttribute2", "testValue2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        assertEquals("testElement", xmlElementWithNameAndAttributes.getName());
        assertEquals("testValue1", xmlElementWithNameAndAttributes.getAttribute("testAttribute1"));
        assertEquals("testValue2", xmlElementWithNameAndAttributes.getAttribute("testAttribute2"));
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));

        xmlElement.addAttribute("testAttribute2", "testValue2");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("testValue2", xmlElement.getAttribute("testAttribute2"));

        xmlElement.addAttribute("testAttribute1", "updatedTestValue1");
        assertEquals("updatedTestValue1", xmlElement.getAttribute("testAttribute1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("defaultValue", xmlElement.getAttribute("nonExistentAttribute", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("testValue1", attributes.get("testAttribute1"));
        assertEquals("testValue2", attributes.get("testAttribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("testElement");
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("testAttribute1", "updatedTestValue1");
            put("testAttribute2", "updatedTestValue2");
        }};
        xmlElement.setAttributes(attributes);
        assertEquals("updatedTestValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("updatedTestValue2", xmlElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("testElement");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");

        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> attributeNamesList = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            attributeNamesList.add((String) attributeNames.nextElement());
        }
        assertEquals(2, attributeNamesList.size());
        assertTrue(attributeNamesList.contains("testAttribute1"));
        assertTrue(attributeNamesList.contains("testAttribute2"));
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertEquals(childElement, parentElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        parentElement.removeElement(childElement);
        assertEquals(0, parentElement.count());
        assertNull(parentElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        assertEquals(2, parentElement.count());
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
        assertNull(parentElement.getElement(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.append(childElement1);
        parentElement.append(childElement2);
        assertEquals(2, parentElement.count());
        assertEquals(childElement1, parentElement.getElement(0));
        assertEquals(childElement2, parentElement.getElement(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");

        parentElement.insertElement(childElement1, 0);
        parentElement.insertElement(childElement2, 1);
        assertEquals(2, parentElement.count());
        assertEquals(childElement1, parentElement.getElement(0));
        assertEquals(childElement2, parentElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        XmlElement childElement3 = new XmlElement("childElement3");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.addElement(childElement3);

        List<XmlElement> elements = parentElement.getElements();
        assertEquals(3, elements.size());
        assertTrue(elements.contains(childElement1));
        assertTrue(elements.contains(childElement2));
        assertTrue(elements.contains(childElement3));
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("testElement") {{
            setName("testElement");
            setData("testData");
            addAttribute("testAttribute1", "testValue1");
            addAttribute("testAttribute2", "testValue2");
            addElement(new XmlElement("childElement1"));
            addElement(new XmlElement("childElement2"));
        }};

        XmlElement clonedElement = (XmlElement) originalElement.clone();

        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
        assertEquals(originalElement.count(), clonedElement.count());

        for (int i = 0; i < originalElement.count(); ++i) {
            XmlElement originalChild = originalElement.getElement(i);
            XmlElement clonedChild = clonedElement.getElement(i);
            assertEquals(originalChild.getName(), clonedChild.getName());
            assertEquals(originalChild.getData(), clonedChild.getData());
            assertEquals(originalChild.getAttributes().size(), clonedChild.getAttributes().size());
            assertEquals(originalChild.count(), clonedChild.count());
        }
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("testElement") {{
            setName("testElement");
            setData("testData");
            addAttribute("testAttribute1", "testValue1");
            addAttribute("testAttribute2", "testValue2");
            addElement(new XmlElement("childElement1") {{
                setName("childElement1");
                setData("childData1");
                addAttribute("testAttribute3", "testValue3");
            }});
            addElement(new XmlElement("childElement2") {{
                setName("childElement2");
                setData("childData2");
                addAttribute("testAttribute4", "testValue4");
            }});
        }};

        String expectedOutput = "testElement:\n" +
                " testAttribute1:testValue1\n" +
                " testAttribute2:testValue2\n" +
                "childElement1 = 'childData1'\n" +
                " childElement1:\n" +
                "  testAttribute3:testValue3\n";

        String output = "";
        XmlElement.printNode(xmlElement, "");
        assertEquals(expectedOutput, output);
    }
}