package com.allenstudio.ir.util.test;

import org.junit.Assert;
import org.junit.Test;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testXmlElementEmptyConstructor() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNotNull(xmlElement);
        Assert.assertEquals(0, xmlElement.subElements.size());
        Assert.assertEquals(0, xmlElement.attributes.size());
        Assert.assertNull(xmlElement.name);
        Assert.assertNull(xmlElement.data);
    }

    @Test
    public void testXmlElementStringNameConstructor() {
        XmlElement xmlElement = new XmlElement("TestElement");
        Assert.assertNotNull(xmlElement);
        Assert.assertEquals("TestElement", xmlElement.name);
        Assert.assertEquals(0, xmlElement.subElements.size());
        Assert.assertEquals(0, xmlElement.attributes.size());
        Assert.assertNull(xmlElement.data);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlElementNullNameConstructor() {
        new XmlElement(null);
    }

    @Test
    public void testXmlElementEmptyStringNameConstructor() {
        XmlElement xmlElement = new XmlElement("");
        Assert.assertNotNull(xmlElement);
        Assert.assertEquals("", xmlElement.name);
        Assert.assertEquals(0, xmlElement.subElements.size());
        Assert.assertEquals(0, xmlElement.attributes.size());
        Assert.assertNull(xmlElement.data);
    }

    @Test
    public void testAddAttributeNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "value");
        Assert.assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testAddAttributeNullValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", null);
        Assert.assertEquals(0, xmlElement.attributes.size());
    }

    @Test
    public void testAddElementNullXmlElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = null;
        Assert.assertFalse(parentXmlElement.addElement(childXmlElement));
        Assert.assertEquals(0, parentXmlElement.subElements.size());
    }

    @Test
    public void testAddElementAlreadyAddedXmlElement() {
        XmlElement parentXmlElement = new XmlElement();
        XmlElement childXmlElement = new XmlElement();
        parentXmlElement.addElement(childXmlElement);
        Assert.assertFalse(parentXmlElement.addElement(childXmlElement));
        Assert.assertEquals(1, parentXmlElement.subElements.size());
    }

    @Test
    public void testGetElementEmptyPath() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNull(xmlElement.getElement(""));
    }

    @Test
    public void testGetElementNullXmlElement() {
        XmlElement xmlElement = null;
        Assert.assertNull(xmlElement.getElement("path"));
    }

    @Test
    public void testGetElementInvalidPath() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("childElement"));
        Assert.assertNull(xmlElement.getElement("invalidPath"));
    }

    @Test
    public void testGetElementInvalidIndex() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addElement(new XmlElement("childElement1"));
        xmlElement.addElement(new XmlElement("childElement2"));
        Assert.assertNull(xmlElement.getElement("childElement", 3));
    }

    @Test
    public void testGetElementValidPath() {
        XmlElement xmlElement = new XmlElement("rootElement");
        xmlElement.addElement(new XmlElement("childElement1"));
        xmlElement.addElement(new XmlElement("childElement2"));
        XmlElement childElement3 = xmlElement.addElement(new XmlElement("childElement3"));
        Assert.assertEquals(childElement3, xmlElement.getElement("rootElement/childElement1/childElement3"));
    }
}