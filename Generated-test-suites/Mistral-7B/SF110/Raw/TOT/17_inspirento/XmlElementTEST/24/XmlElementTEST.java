package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class XmlElementTest {

    @Test
    public void testXmlElementCreation() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("name");
        Assert.assertEquals("name", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("name", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        Assert.assertEquals("name", xmlElementWithNameAndAttributes.getName());
        Assert.assertEquals("value1", xmlElementWithNameAndAttributes.getAttribute("attribute1"));
        Assert.assertEquals("value2", xmlElementWithNameAndAttributes.getAttribute("attribute2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("name", "data");
        Assert.assertEquals("name", xmlElementWithNameAndData.getName());
        Assert.assertEquals("data", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNull(xmlElement.getAttribute("attribute"));

        xmlElement.addAttribute("attribute", "value");
        Assert.assertEquals("value", xmlElement.getAttribute("attribute"));

        xmlElement.addAttribute("attribute", "newValue");
        Assert.assertEquals("newValue", xmlElement.getAttribute("attribute"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute", "value");

        Assert.assertEquals("value", xmlElement.getAttribute("attribute"));
        Assert.assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        Assert.assertEquals("value1", attributes.get("attribute1"));
        Assert.assertEquals("value2", attributes.get("attribute2"));
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }};
        xmlElement.setAttributes(attributes);

        Assert.assertEquals("value1", xmlElement.getAttribute("attribute1"));
        Assert.assertEquals("value2", xmlElement.getAttribute("attribute2"));
        Assert.assertEquals(2, xmlElement.getAttributes().size());
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");

        parentElement.addSubElement(childElement);
        Assert.assertEquals(1, parentElement.getElements().size());
        Assert.assertEquals(childElement, parentElement.getElement(0));

        XmlElement subElementWithAttributes = new XmlElement("subElement");
        subElementWithAttributes.addAttribute("attribute1", "value1");
        subElementWithAttributes.addAttribute("attribute2", "value2");

        parentElement.addSubElement(subElementWithAttributes);
        Assert.assertEquals(2, parentElement.getElements().size());
        Assert.assertEquals(childElement, parentElement.getElement(0));
        Assert.assertEquals(subElementWithAttributes, parentElement.getElement(1));

        XmlElement subElementWithData = new XmlElement("subElement", "data");
        parentElement.addSubElement(subElementWithData);
        Assert.assertEquals(3, parentElement.getElements().size());
        Assert.assertEquals(childElement, parentElement.getElement(0));
        Assert.assertEquals(subElementWithAttributes, parentElement.getElement(1));
        Assert.assertEquals(subElementWithData, parentElement.getElement(2));
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");

        parentElement.addSubElement(childElement1);
        parentElement.addSubElement(childElement2);

        XmlElement resultElement = parentElement.getElement("child1");
        Assert.assertEquals(childElement1, resultElement);

        resultElement = parentElement.getElement("child2");
        Assert.assertEquals(childElement2, resultElement);

        resultElement = parentElement.getElement("child3");
        Assert.assertNull(resultElement);

        resultElement = parentElement.getElement("child1/child");
        Assert.assertNull(resultElement);
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.addSubElement(new XmlElement("subElement1", "data1"));
        xmlElement.addSubElement(new XmlElement("subElement2", "data2"));

        XmlElement clonedElement = (XmlElement)xmlElement.clone();

        Assert.assertEquals(xmlElement.getName(), clonedElement.getName());
        Assert.assertEquals(xmlElement.getData(), clonedElement.getData());

        Assert.assertEquals(xmlElement.getAttributes().size(), clonedElement.getAttributes().size());
        Assert.assertEquals(xmlElement.getAttributes().get("attribute1"), clonedElement.getAttributes().get("attribute1"));
        Assert.assertEquals(xmlElement.getAttributes().get("attribute2"), clonedElement.getAttributes().get("attribute2"));

        Assert.assertEquals(xmlElement.getElements().size(), clonedElement.getElements().size());
        for (int i = 0; i < xmlElement.getElements().size(); i++) {
            XmlElement originalElement = xmlElement.getElements().get(i);
            XmlElement clonedElement2 = clonedElement.getElements().get(i);
            Assert.assertEquals(originalElement.getName(), clonedElement2.getName());
            Assert.assertEquals(originalElement.getData(), clonedElement2.getData());
            Assert.assertEquals(originalElement.getAttributes().size(), clonedElement2.getAttributes().size());
            Assert.assertEquals(originalElement.getAttributes().get("attribute1"), clonedElement2.getAttributes().get("attribute1"));
            Assert.assertEquals(originalElement.getAttributes().get("attribute2"), clonedElement2.getAttributes().get("attribute2"));
        }
    }
}