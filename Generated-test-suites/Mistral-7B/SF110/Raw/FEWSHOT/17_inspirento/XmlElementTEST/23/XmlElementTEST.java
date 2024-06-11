package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
    }

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("testName");
        assertEquals("testName", xmlElement.getName());
        assertNull(xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        XmlElement xmlElement = new XmlElement("testName", attributes);
        assertEquals("testName", xmlElement.getName());
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertNull(xmlElement.getData());
        assertEquals(attributes, xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
    }

    @Test
    public void testXmlElementConstructorWithNameAndData() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        assertEquals("testName", xmlElement.getName());
        assertEquals("testData", xmlElement.getData());
        assertNull(xmlElement.getAttributes());
        assertNull(xmlElement.getParent());
        assertEquals(0, xmlElement.count());
        assertTrue(xmlElement.subElements instanceof Vector);
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals(null, xmlElement.getAttribute("attr2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1", "defaultValue"));
        assertEquals("defaultValue", xmlElement.getAttribute("attr2", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes1 = new Hashtable<>();
        attributes1.put("attr1", "value1");

        Hashtable<String, String> attributes2 = new Hashtable<>();
        attributes2.put("attr2", "value2");

        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(attributes1);
        xmlElement.setAttributes(attributes2);
        assertEquals(attributes2, xmlElement.getAttributes());
    }

    @Test
    public void testAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(attributes);

        String[] attributeNames = xmlElement.getAttributeNames().toArray(new String[0]);
        assertEquals(1, attributeNames.length);
        assertEquals("attr1", attributeNames[0]);
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(1, parentXmlElement.count());
        assertEquals(childXmlElement, parentXmlElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(1, parentXmlElement.count());
        XmlElement removedElement = parentXmlElement.removeElement(childXmlElement);
        assertEquals(0, parentXmlElement.count());
        assertEquals(childXmlElement, removedElement);
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(2, parentXmlElement.count());
        XmlElement removedElement = parentXmlElement.removeElement(1);
        assertEquals("childElement2", removedElement.getName());
        assertEquals(1, parentXmlElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(2, parentXmlElement.count());
        parentXmlElement.removeAllElements();
        assertEquals(0, parentXmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(1, parentXmlElement.count());
        childXmlElement.removeFromParent();
        assertEquals(0, parentXmlElement.count());
        assertNull(childXmlElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentXmlElement1 = new XmlElement();
        XmlElement childXmlElement1 = new XmlElement("childElement1");
        parentXmlElement1.addElement(childXmlElement1);
        assertEquals(1, parentXmlElement1.count());

        XmlElement parentXmlElement2 = new XmlElement();
        XmlElement childXmlElement2 = new XmlElement("childElement2");
        parentXmlElement2.append(childXmlElement1);
        assertEquals(1, parentXmlElement2.count());
        assertEquals(childXmlElement1, parentXmlElement2.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.insertElement(child2XmlElement, 0);
        assertEquals(2, parentXmlElement.count());
        assertEquals(child2XmlElement, parentXmlElement.getElement(0));
    }

    @Test
    public void testGetElements() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        List<XmlElement> elements = parentXmlElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1XmlElement, elements.get(0));
        assertEquals(child2XmlElement, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(2, parentXmlElement.count());
    }

    @Test
    public void testElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement("childElement");
        parentXmlElement.addElement(childXmlElement);
        assertEquals(childXmlElement, parentXmlElement.getElement("childElement"));
        assertNull(parentXmlElement.getElement("nonexistentElement"));
    }

    @Test
    public void testElementByIndex() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = new XmlElement("childElement1");
        XmlElement child2XmlElement = new XmlElement("childElement2");
        parentXmlElement.addElement(child1XmlElement);
        parentXmlElement.addElement(child2XmlElement);
        assertEquals(child1XmlElement, parentXmlElement.getElement(0));
        assertEquals(child2XmlElement, parentXmlElement.getElement(1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement child1XmlElement = parentXmlElement.addSubElement("childElement1");
        XmlElement child2XmlElement = parentXmlElement.addSubElement("childElement2");
        assertEquals(2, parentXmlElement.count());
        assertEquals(child1XmlElement, parentXmlElement.getElement("childElement1"));
        assertEquals(child2XmlElement, parentXmlElement.getElement("childElement2"));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = parentXmlElement.addSubElement("childElement", "childData");
        assertEquals(1, parentXmlElement.count());
        assertEquals(childXmlElement, parentXmlElement.getElement("childElement"));
        assertEquals("childData", childXmlElement.getData());
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertFalse(xmlElement1.equals(xmlElement2));

        XmlElement xmlElement3 = new XmlElement();
        xmlElement3.addAttribute("attr1", "value1");
        xmlElement3.addAttribute("attr2", "value2");
        xmlElement3.setData("data");
        xmlElement3.setName("name");
        XmlElement xmlElement4 = new XmlElement();
        xmlElement4.addAttribute("attr1", "value1");
        xmlElement4.addAttribute("attr2", "value2");
        xmlElement4.setData("data");
        xmlElement4.setName("name");
        xmlElement4.setSubElements(xmlElement3.getSubElements());
        assertTrue(xmlElement3.equals(xmlElement4));

        XmlElement xmlElement5 = new XmlElement();
        xmlElement5.addAttribute("attr1", "value1");
        xmlElement5.addAttribute("attr2", "value3");
        xmlElement5.setData("data");
        xmlElement5.setName("name");
        assertFalse(xmlElement3.equals(xmlElement5));
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        xmlElement.setData("data");
        xmlElement.setName("name");
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedElement.getName());
        assertEquals(xmlElement.getData(), clonedElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(xmlElement.count(), clonedElement.count());
        for (int i = 0; i < xmlElement.count(); i++) {
            XmlElement clonedChild = clonedElement.getElements().get(i).clone();
            XmlElement originalChild = xmlElement.getElements().get(i).clone();
            assertEquals(originalChild.getName(), clonedChild.getName());
            assertEquals(originalChild.getData(), clonedChild.getData());
            assertEquals(originalChild.getAttributes(), clonedChild.getAttributes());
        }
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        xmlElement.setData("data");
        xmlElement.setName("name");

        XmlElement child1XmlElement = new XmlElement();
        child1XmlElement.addAttribute("childAttr1", "childValue1");
        child1XmlElement.setData("childData");
        child1XmlElement.setName("child1");

        XmlElement child2XmlElement = new XmlElement();
        child2XmlElement.addAttribute("childAttr2", "childValue2");
        child2XmlElement.setData("childData");
        child2XmlElement.setName("child2");

        xmlElement.addElement(child1XmlElement);
        xmlElement.addElement(child2XmlElement);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        XmlElement.printNode(xmlElement, "");
        printWriter.close();

        String expectedOutput =
                "name\n" +
                "attr1:value1\n" +
                "attr2:value2\n" +
                "child1\n" +
                "childAttr1:childValue1\n" +
                "childData\n" +
                "child2\n" +
                "childAttr2:childValue2\n" +
                "childData\n";

        String actualOutput = stringWriter.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}