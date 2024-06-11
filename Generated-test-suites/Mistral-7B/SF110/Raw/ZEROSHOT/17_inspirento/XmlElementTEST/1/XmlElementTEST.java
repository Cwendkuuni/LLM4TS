package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setUp() {
        rootElement = new XmlElement("root");;
    }

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement element = new XmlElement("element");
        Assert.assertEquals("element", element.getName());
        Assert.assertEquals(0, element.count());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement element = new XmlElement("element", attributes);
        Assert.assertEquals("element", element.getName());
        Assert.assertEquals(attributes, element.getAttributes());
        Assert.assertEquals(0, element.count());
    }

    @Test
    public void testXmlElementConstructorWithNameAndData() {
        XmlElement element = new XmlElement("element", "data");
        Assert.assertEquals("element", element.getName());
        Assert.assertEquals("data", element.getData());
        Assert.assertEquals(0, element.count());
    }

    @Test
    public void testAddAttribute() {
        rootElement.addAttribute("attr1", "value1");
        Assert.assertEquals("value1", rootElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        rootElement.addAttribute("attr1", "value1");
        Assert.assertEquals("value1", rootElement.getAttribute("attr1"));
        Assert.assertEquals("defaultValue", rootElement.getAttribute("attr2", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        rootElement.setAttributes(attributes);
        Assert.assertEquals(attributes, rootElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        rootElement.setAttributes(attributes);
        Assert.assertEquals(attributes, rootElement.getAttributes());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("child");;
        rootElement.addElement(childElement);
        Assert.assertEquals(1, rootElement.count());
        Assert.assertEquals(childElement, rootElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement("child");;
        rootElement.addElement(childElement);
        XmlElement removedElement = rootElement.removeElement(0);
        Assert.assertEquals(childElement, removedElement);
        Assert.assertEquals(0, rootElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        rootElement.removeAllElements();
        Assert.assertEquals(0, rootElement.count());
    }

    @Test
    public void testAppend() {
        XmlElement childElement = new XmlElement("child");;
        rootElement.append(childElement);
        Assert.assertEquals(1, rootElement.count());
        Assert.assertEquals(childElement, rootElement.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.insertElement(childElement2, 0);
        Assert.assertEquals(2, rootElement.count());
        Assert.assertEquals(childElement2, rootElement.getElement(0));
        Assert.assertEquals(childElement1, rootElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        List<XmlElement> elements = rootElement.getElements();
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals(childElement1, elements.get(0));
        Assert.assertEquals(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        Assert.assertEquals(2, rootElement.count());
    }

    @Test
    public void testGetElementWithPath() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        XmlElement childElement3 = rootElement.addSubElement("child3/child4");
        Assert.assertEquals(childElement3, rootElement.getElement("child1/child3/child4"));
    }

    @Test
    public void testGetElementWithIndex() {
        XmlElement childElement1 = new XmlElement("child1");;
        XmlElement childElement2 = new XmlElement("child2");;
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        Assert.assertEquals(childElement1, rootElement.getElement(0));
        Assert.assertEquals(childElement2, rootElement.getElement(1));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement childElement = rootElement.addSubElement("child1/child2");
        Assert.assertEquals("child2", childElement.getName());
        Assert.assertEquals(0, childElement.count());
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement childElement = new XmlElement("child");
        rootElement.addSubElement(childElement);
        Assert.assertEquals("child", childElement.getName());
        Assert.assertEquals(0, childElement.count());
        Assert.assertEquals(childElement, rootElement.getElement("child"));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement childElement = rootElement.addSubElement("child", "data");
        Assert.assertEquals("child", childElement.getName());
        Assert.assertEquals("data", childElement.getData());
        Assert.assertEquals(0, childElement.count());
        Assert.assertEquals(childElement, rootElement.getElement("child"));
    }

    @Test
    public void testPrintNode() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        childElement2.addAttribute("attr1", "value1");
        childElement2.addAttribute("attr2", "value2");
        rootElement.addElement(childElement1);
        rootElement.addElement(childElement2);
        rootElement.printNode(0);
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("original");
        XmlElement clonedElement = (XmlElement)originalElement.clone();
        Assert.assertEquals("original", originalElement.getName());
        Assert.assertEquals("original", clonedElement.getName());
        Hashtable<String, String> originalAttributes = originalElement.getAttributes();
        Hashtable<String, String> clonedAttributes = clonedElement.getAttributes();
        Assert.assertEquals(originalAttributes.size(), clonedAttributes.size());
        Assert.assertEquals(originalAttributes, clonedAttributes);
    }
}