package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());

        xmlElement = new XmlElement("testName");
        assertEquals("testName", xmlElement.getName());
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());

        xmlElement = new XmlElement("testName", new Hashtable<String, String>(10));
        assertEquals("testName", xmlElement.getName());
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(10, xmlElement.attributes.size());

        xmlElement = new XmlElement("testName", "testData");
        assertEquals("testName", xmlElement.getName());
        assertEquals("testData", xmlElement.getData());
        assertEquals(0, xmlElement.subElements.size());
        assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        xmlElement.addAttribute("testAttribute2", "testValue2");
        assertEquals("testValue2", xmlElement.getAttribute("testAttribute2"));

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("testValue1", attributes.get("testAttribute1"));
        assertEquals("testValue2", attributes.get("testAttribute2"));

        xmlElement.setAttributes(new Hashtable<String, String>());
        assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals(null, xmlElement.getAttribute("testAttribute2"));

        xmlElement = new XmlElement("testName", new Hashtable<String, String>(10));
        xmlElement.addAttribute("testAttribute1", "testValue1");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals(null, xmlElement.getAttribute("testAttribute2"));

        xmlElement.setAttribute("testAttribute2", "testValue2");
        assertEquals("testValue1", xmlElement.getAttribute("testAttribute1"));
        assertEquals("testValue2", xmlElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");

        Hashtable<String, String> expectedAttributes = new Hashtable<String, String>(2);
        expectedAttributes.put("testAttribute1", "testValue1");
        expectedAttributes.put("testAttribute2", "testValue2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(expectedAttributes.size(), attributes.size());
        assertAttributeValuesEqual(expectedAttributes, attributes);

        xmlElement.setAttributes(new Hashtable<String, String>());
        assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("testName");
        Hashtable<String, String> attributes = new Hashtable<String, String>(2);
        attributes.put("testAttribute1", "testValue1");
        attributes.put("testAttribute2", "testValue2");
        xmlElement.setAttributes(attributes);

        assertEquals(2, xmlElement.attributes.size());
        assertAttributeValuesEqual(attributes, xmlElement.getAttributes());

        xmlElement.setAttributes(new Hashtable<String, String>());
        assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");

        Enumeration<String> enumAttributeNames = xmlElement.getAttributeNames();
        List<String> expectedAttributeNames = new ArrayList<String>(Arrays.asList("testAttribute1", "testAttribute2"));
        List<String> attributeNames = new ArrayList<String>();

        while (enumAttributeNames.hasMoreElements()) {
            String attributeName = enumAttributeNames.nextElement();
            attributeNames.add(attributeName);
        }

        assertEquals(expectedAttributeNames.size(), attributeNames.size());
        listEquals(expectedAttributeNames, attributeNames);

        List<String> emptyAttributeNames = new ArrayList<String>();
        xmlElement.setAttributes(new Hashtable<String, String>());
        assertEquals(emptyAttributeNames.size(), attributeNames.size());
    }

    @Test
    public void testAddElement() {
        XmlElement parentXmlElement = new XmlElement("parentName");
        XmlElement childXmlElement = new XmlElement("childName");

        parentXmlElement.addElement(childXmlElement);
        assertEquals(1, parentXmlElement.subElements.size());
        assertEquals(childXmlElement, parentXmlElement.subElements.get(0));

        childXmlElement.setParent(parentXmlElement);
        assertEquals(parentXmlElement, childXmlElement.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentXmlElement = new XmlElement("parentName");
        XmlElement childXmlElement = new XmlElement("childName");
        parentXmlElement.addElement(childXmlElement);

        XmlElement removedChildXmlElement = parentXmlElement.removeElement(childXmlElement);
        assertEquals(childXmlElement, removedChildXmlElement);
        assertEquals(0, parentXmlElement.subElements.size());

        childXmlElement.setParent(parentXmlElement);
        parentXmlElement.addElement(childXmlElement);
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentXmlElement = new XmlElement("parentName");
        XmlElement childXmlElement = new XmlElement("childName");
        parentXmlElement.addElement(childXmlElement);

        childXmlElement.removeFromParent();
        assertEquals(0, parentXmlElement.subElements.size());
        assertEquals(null, childXmlElement.getParent());

        parentXmlElement.addElement(childXmlElement);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentXmlElement = new XmlElement("parentName");
        XmlElement childXmlElement1 = parentXmlElement.addSubElement("childName1");
        XmlElement childXmlElement2 = parentXmlElement.addSubElement("childName2");

        assertEquals(2, parentXmlElement.subElements.size());
        assertEquals(childXmlElement1, parentXmlElement.subElements.get(0));
        assertEquals(childXmlElement2, parentXmlElement.subElements.get(1));

        assertEquals("childName1", childXmlElement1.getName());
        assertEquals("childName2", childXmlElement2.getName());

        childXmlElement1.setParent(parentXmlElement);
        childXmlElement2.setParent(parentXmlElement);

        assertEquals(parentXmlElement, childXmlElement1.getParent());
        assertEquals(parentXmlElement, childXmlElement2.getParent());
    }

    @Test
    public void testClone() {
        XmlElement originalXmlElement = new XmlElement("originalName");
        originalXmlElement.addAttribute("testAttribute1", "testValue1");
        originalXmlElement.addAttribute("testAttribute2", "testValue2");
        originalXmlElement.addElement(new XmlElement("childName"));

        XmlElement clonedXmlElement = (XmlElement) originalXmlElement.clone();

        assertEquals(originalXmlElement.getName(), clonedXmlElement.getName());
        assertEquals(originalXmlElement.getData(), clonedXmlElement.getData());
        assertEquals(originalXmlElement.attributes.size(), clonedXmlElement.attributes.size());
        assertEquals(originalXmlElement.subElements.size(), clonedXmlElement.subElements.size());

        Hashtable<String, String> originalAttributes = originalXmlElement.getAttributes();
        Hashtable<String, String> clonedAttributes = clonedXmlElement.getAttributes();

        assertAttributeValuesEqual(originalAttributes, clonedAttributes);

        List<XmlElement> originalSubElements = originalXmlElement.getElements();
        List<XmlElement> clonedSubElements = clonedXmlElement.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());
        for (int i = 0; i < originalSubElements.size(); i++) {
            XmlElement originalSubElement = originalSubElements.get(i);
            XmlElement clonedSubElement = clonedSubElements.get(i);
            assertEquals(originalSubElement.getName(), clonedSubElement.getName());
            assertEquals(originalSubElement.getData(), clonedSubElement.getData());
            assertEquals(originalSubElement.attributes.size(), clonedSubElement.attributes.size());
            assertEquals(originalSubElement.subElements.size(), clonedSubElement.subElements.size());
        }
    }

    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyObserversWithNullParent() {
        XmlElement xmlElement = new XmlElement("testName");
        xmlElement.setParent(null);
        xmlElement.notifyObservers();
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement("testName1", "testData1");
        XmlElement xmlElement2 = new XmlElement("testName2", "testData2");
        XmlElement xmlElement3 = new XmlElement("testName1", "testData1");
        XmlElement xmlElement4 = new XmlElement("testName1", "testData2");

        XmlElement xmlElement5 = new XmlElement("testName1", "testData1");
        xmlElement5.setAttributes(new Hashtable<String, String>(10));
        xmlElement5.setSubElements(new Vector<XmlElement>());

        assertTrue(xmlElement1.equals(xmlElement1));
        assertTrue(xmlElement1.equals(xmlElement3));
        assertFalse(xmlElement1.equals(xmlElement2));
        assertFalse(xmlElement1.equals(xmlElement4));
        assertFalse(xmlElement1.equals(xmlElement5));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement("testName1", "testData1");
        XmlElement xmlElement2 = new XmlElement("testName2", "testData2");
        XmlElement xmlElement3 = new XmlElement("testName1", "testData1");
        XmlElement xmlElement4 = new XmlElement("testName1", "testData2");

        int xmlElement1HashCode = xmlElement1.hashCode();
        int xmlElement2HashCode = xmlElement2.hashCode();
        int xmlElement3HashCode = xmlElement3.hashCode();
        int xmlElement4HashCode = xmlElement4.hashCode();

        assertEquals(xmlElement1HashCode, xmlElement3.hashCode());
        assertNotEquals(xmlElement1HashCode, xmlElement2.hashCode());
        assertNotEquals(xmlElement1HashCode, xmlElement4.hashCode());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("testName", "testData");
        xmlElement.addAttribute("testAttribute1", "testValue1");
        xmlElement.addAttribute("testAttribute2", "testValue2");
        xmlElement.addElement(new XmlElement("childName1"));
        xmlElement.addElement(new XmlElement("childName2", "childData1"));

        printNode(xmlElement, "");

        String expectedXml = "testName = 'testData'\n"
                + "testAttribute1:testValue1\n"
                + "testAttribute2:testValue2\n"
                + "childName1\n"
                + "childName2 = 'childData1'\n";

        String actualXml = getOutputFromConsole();

        assertEquals(expectedXml, actualXml);
    }

    private void printNode(XmlElement node, String indent) {
        System.out.println(indent + node.getName());
        String data = node.getData();
        if (data != null && !data.equals("")) {
            System.out.println(indent + node.getName() + " = '" + data + "'");
        }

        Enumeration<String> enumeration = node.getAttributes().keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = node.getAttribute(key);
            System.out.println(indent + key + ":" + value);
        }

        List subs = node.getElements();
        Iterator it = subs.iterator();
        while (it.hasNext()) {
            printNode(it.next(), indent + "    ");
        }
    }

    private void assertAttributeValuesEqual(Hashtable<String, String> expectedAttributes, Hashtable<String, String> actualAttributes) {
        for (String key : expectedAttributes.keySet()) {
            assertEquals(expectedAttributes.get(key), actualAttributes.get(key));
        }
    }

    private void listEquals(List<String> expectedList, List<String> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), actualList.get(i));
        }
    }

    private String getOutputFromConsole() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream consoleOut = System.out;
        PrintStream oldOut = new PrintStream(outputStream);
        System.setOut(oldOut);
        printNode(new XmlElement("testName"), "");
        System.setOut(consoleOut);
        return outputStream.toString();
    }
}