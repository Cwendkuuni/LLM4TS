package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    @Test
    public void testXmlElementConstruction() {
        XmlElement element1 = new XmlElement("element1");;;
        XmlElement element2 = new XmlElement("element2", "data1");;
        XmlElement element3 = new XmlElement("element3", new Hashtable<String, String>(2));;

        Assert.assertEquals("element1", element1.getName());
        Assert.assertEquals(Collections.emptyMap(), element1.getAttributes());
        Assert.assertEquals(Collections.emptyList(), element1.getSubElements());

        Assert.assertEquals("element2", element2.getName());
        Assert.assertEquals("data1", element2.getData());
        Assert.assertEquals(Collections.emptyMap(), element2.getAttributes());
        Assert.assertEquals(Collections.emptyList(), element2.getSubElements());

        Assert.assertEquals("element3", element3.getName());
        Assert.assertEquals(new Hashtable<String, String>(2), element3.getAttributes());
        Assert.assertEquals(Collections.emptyList(), element3.getSubElements());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attribute1", "value1");
        root.addAttribute("attribute2", null);

        Assert.assertEquals("value1", root.getAttribute("attribute1"));
        Assert.assertEquals(null, root.getAttribute("attribute2"));

        Set<String> keys = new HashSet<String>(root.getAttributeNames());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("attribute1")), keys);
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attribute1", "value1");
        root.addAttribute("attribute2", "value2");

        Assert.assertEquals("value1", root.getAttribute("attribute1"));
        Assert.assertEquals("value2", root.getAttribute("attribute2"));
        Assert.assertEquals("value1", root.getAttribute("attribute1", "defaultValue"));
        Assert.assertEquals("value2", root.getAttribute("attribute2", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        root.addAttribute("attribute1", "value1");
        root.addAttribute("attribute2", "value2");

        Assert.assertEquals(new Hashtable<String, String>(2), root.getAttributes());
        Assert.assertEquals(new Hashtable<String, String>(Collections.singletonMap("attribute1", "value1")), root.getAttributes(false));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>(2);
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        root.setAttributes(attributes);

        Assert.assertEquals(attributes, root.getAttributes());
    }

    @Test
    public void testSubElementAddition() {
        XmlElement subElement1 = new XmlElement("subElement1");;
        XmlElement subElement2 = new XmlElement("subElement2", "data2");;

        root.addElement(subElement1);
        root.addElement(subElement2);

        Assert.assertEquals(2, root.count());
        Assert.assertEquals(subElement1, root.getElement(0));
        Assert.assertEquals(subElement2, root.getElement(1));
        Assert.assertEquals(subElement1, root.getElement("subElement1"));
        Assert.assertEquals(subElement2, root.getElement("subElement2"));

        Assert.assertNull(root.getElement("nonExistentElement"));
    }

    @Test
    public void testSubElementRemoval() {
        XmlElement subElement1 = new XmlElement("subElement1");;
        XmlElement subElement2 = new XmlElement("subElement2");;

        root.addElement(subElement1);
        root.addElement(subElement2);

        root.removeElement(subElement1);

        Assert.assertEquals(1, root.count());
        Assert.assertNull(root.getElement("subElement1"));
    }

    @Test
    public void testSubElementRemovalByIndex() {
        XmlElement subElement1 = new XmlElement("subElement1");;
        XmlElement subElement2 = new XmlElement("subElement2");;

        root.addElement(subElement1);
        root.addElement(subElement2);

        root.removeElement(1);

        Assert.assertEquals(1, root.count());
        Assert.assertEquals(subElement1, root.getElement(0));
        Assert.assertNull(root.getElement(1));
    }

    @Test
    public void testSubElementRemovalAll() {
        XmlElement subElement1 = new XmlElement("subElement1");;
        XmlElement subElement2 = new XmlElement("subElement2");;

        root.addElement(subElement1);
        root.addElement(subElement2);

        root.removeAllElements();

        Assert.assertEquals(0, root.count());
    }

    @Test
    public void testSubElementInsertion() {
        XmlElement subElement1 = new XmlElement("subElement1");;
        XmlElement subElement2 = new XmlElement("subElement2");;

        root.addElement(subElement1);
        root.insertElement(subElement2, 0);

        Assert.assertEquals(2, root.count());
        Assert.assertEquals(subElement2, root.getElement(0));
        Assert.assertEquals(subElement1, root.getElement(1));
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");;
        XmlElement clone = original.clone();

        original.addAttribute("attribute1", "value1");
        original.addElement(new XmlElement("child1"));

        Assert.assertEquals(original, clone);
        Assert.assertEquals(original.getAttributes(), clone.getAttributes());
        Assert.assertEquals(original.getSubElements(), clone.getSubElements());

        original.addAttribute("attribute2", "value2");
        original.getSubElements().get(0).setName("child2");

        Assert.assertNotEquals(original, clone);
        Assert.assertEquals(original.getAttributes(), clone.getAttributes());
        Assert.assertNotEquals(original.getSubElements(), clone.getSubElements());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        root.addAttribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        root.addAttribute("name", null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubElementNullName() {
        root.addSubElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementNegativeIndex() {
        root.addSubElement(-1);
    }
}