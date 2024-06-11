package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setup() {
        root = new XmlElement("root");;
    }

    @Test
    public void testXmlElementCreation() {
        XmlElement element1 = new XmlElement("element1");;
        XmlElement element2 = new XmlElement("element2", "data1");
        XmlElement element3 = new XmlElement("element3", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});

        Assert.assertEquals("element1", element1.getName());
        Assert.assertNull(element1.getData());
        Assert.assertNull(element1.getAttributes());

        Assert.assertEquals("element2", element2.getName());
        Assert.assertEquals("data1", element2.getData());
        Assert.assertNotNull(element2.getAttributes());
        Assert.assertEquals(2, element2.getAttributes().size());
        Assert.assertEquals("value1", element2.getAttributes().get("attribute1"));
        Assert.assertEquals("value2", element2.getAttributes().get("attribute2"));
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("test", "testValue");
        Assert.assertEquals("testValue", root.getAttribute("test"));
        Assert.assertEquals(1, root.getAttributes().size());
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        root.addAttribute("test", null);
        Assert.assertEquals(null, root.getAttribute("test"));
        Assert.assertEquals("defaultValue", root.getAttribute("test", "defaultValue"));
        Assert.assertEquals(1, root.getAttributes().size());
    }

    @Test
    public void testGetAttributes() {
        root.addAttribute("test1", "testValue1");
        root.addAttribute("test2", "testValue2");
        Map<String, String> attributes = root.getAttributes();
        Assert.assertEquals("testValue1", attributes.get("test1"));
        Assert.assertEquals("testValue2", attributes.get("test2"));
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");
        root.setAttributes(attributes);
        Assert.assertEquals(2, root.getAttributes().size());
        Assert.assertEquals("testValue1", root.getAttribute("test1"));
        Assert.assertEquals("testValue2", root.getAttribute("test2"));
    }

    @Test
    public void testGetAttributeNames() {
        root.addAttribute("test1", "testValue1");
        root.addAttribute("test2", "testValue2");
        Enumeration enumeration = root.getAttributeNames();
        Assert.assertFalse(enumeration.hasMoreElements());
        root.setAttributes(new Hashtable<String, String>() {{
            put("test1", "testValue1");
            put("test2", "testValue2");
        }});
        Assert.assertTrue(enumeration.hasMoreElements());
        Assert.assertEquals("test1", enumeration.nextElement());
        Assert.assertEquals("test2", enumeration.nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement child1 = new XmlElement("child1");;
        root.addElement(child1);
        Assert.assertEquals(1, root.getElements().size());
        Assert.assertEquals(child1, root.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement child1 = new XmlElement("child1");;
        root.addElement(child1);
        root.removeElement(child1);
        Assert.assertEquals(0, root.getElements().size());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;
        root.addElement(child1);
        root.addElement(child2);
        root.removeAllElements();
        Assert.assertEquals(0, root.getElements().size());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child1 = new XmlElement("child1");;
        root.addElement(child1);
        child1.removeFromParent();
        Assert.assertEquals(0, root.getElements().size());
    }

    @Test
    public void testAppend() {
        XmlElement child1 = new XmlElement("child1");;
        root.append(child1);
        Assert.assertEquals(1, root.getElements().size());
        Assert.assertEquals(child1, root.getElements().get(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");;
        root.insertElement(child1, 0);
        Assert.assertEquals(1, root.getElements().size());
        Assert.assertEquals(child1, root.getElements().get(0));
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");;
        XmlElement cloned = (XmlElement)original.clone();
        Assert.assertNotEquals(original, cloned);
        Assert.assertEquals(original.getName(), cloned.getName());
        Assert.assertEquals(original.getData(), cloned.getData());
        Assert.assertEquals(original.getAttributes(), cloned.getAttributes());
        Assert.assertEquals(original.getElements(), cloned.getElements());
    }

    @Test
    public void testGetElement() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;
        root.addElement(child1);
        root.addElement(child2);
        XmlElement element = root.getElement("child1/child2");
        Assert.assertNull(element);
        element = root.getElement("child1");
        Assert.assertEquals(child1, element);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetElementWithInvalidPath() {
        XmlElement child1 = new XmlElement("child1");;
        root.addElement(child1);
        root.getElement("child2");
    }

    @Test
    public void testAddSubElement() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;
        root.addSubElement(child1);
        root.addSubElement(child2);
        Assert.assertEquals(2, root.getElements().size());
        Assert.assertEquals(child1, root.getElement("child1"));
        Assert.assertEquals(child2, root.getElement("child1/child2"));
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2", "data");;
        root.addSubElement(child1);
        root.addSubElement(child2);
        Assert.assertEquals(2, root.getElements().size());
        Assert.assertEquals(child1, root.getElement("child1"));
        Assert.assertEquals(child2, root.getElement("child1/child2"));
        Assert.assertEquals("data", child2.getData());
    }

    @Test
    public void testAddSubElementWithAttributes() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");
        child2.addAttribute("test1", "testValue1");
        child2.addAttribute("test2", "testValue2");
        root.addSubElement(child1);
        root.addSubElement(child2);
        Assert.assertEquals(2, root.getElements().size());
        Assert.assertEquals(child1, root.getElement("child1"));
        Assert.assertEquals(child2, root.getElement("child1/child2"));
        Assert.assertEquals(2, child2.getAttributes().size());
        Assert.assertEquals("testValue1", child2.getAttribute("test1"));
        Assert.assertEquals("testValue2", child2.getAttribute("test2"));
    }

    @Test
    public void testXmlElementPrinting() {
        XmlElement child1 = new XmlElement("child1");;
        child1.addAttribute("test1", "testValue1");
        child1.addAttribute("test2", "testValue2");
        XmlElement child2 = new XmlElement("child2");;
        child2.addAttribute("test3", "testValue3");
        child2.addAttribute("test4", "testValue4");
        root.addElement(child1);
        root.addElement(child2);
        XmlElement.printNode(root, "");
    }
}