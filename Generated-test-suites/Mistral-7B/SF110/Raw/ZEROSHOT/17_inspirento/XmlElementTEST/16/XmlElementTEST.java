package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

    @Test
    public void testXmlElementCreation() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("ElementName");
        Assert.assertEquals("ElementName", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("ElementName", new Hashtable<String, String>() {{
            put("Attribute1", "Value1");
            put("Attribute2", "Value2");
        }});
        Assert.assertEquals("ElementName", xmlElementWithNameAndAttributes.getName());
        Assert.assertEquals("Value1", xmlElementWithNameAndAttributes.getAttribute("Attribute1"));
        Assert.assertEquals("Value2", xmlElementWithNameAndAttributes.getAttribute("Attribute2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("ElementName", "Data");
        Assert.assertEquals("ElementName", xmlElementWithNameAndData.getName());
        Assert.assertEquals("Data", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("ElementName");
        xmlElement.addAttribute("Attribute1", "Value1");
        Assert.assertEquals("Value1", xmlElement.getAttribute("Attribute1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("ElementName");
        xmlElement.addAttribute("Attribute1", "Value1");
        Assert.assertEquals("Value1", xmlElement.getAttribute("Attribute1"));

        XmlElement xmlElementWithDefaultValue = new XmlElement("ElementName");
        Assert.assertEquals("", xmlElementWithDefaultValue.getAttribute("Attribute1"));
        xmlElementWithDefaultValue.addAttribute("Attribute1", "DefaultValue");
        Assert.assertEquals("DefaultValue", xmlElementWithDefaultValue.getAttribute("Attribute1"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("ElementName");
        xmlElement.addAttribute("Attribute1", "Value1");
        xmlElement.addAttribute("Attribute2", "Value2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        Assert.assertEquals("Value1", attributes.get("Attribute1"));
        Assert.assertEquals("Value2", attributes.get("Attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("ElementName");
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("Attribute1", "Value1");
            put("Attribute2", "Value2");
        }};
        xmlElement.setAttributes(attributes);

        Assert.assertEquals("Value1", xmlElement.getAttribute("Attribute1"));
        Assert.assertEquals("Value2", xmlElement.getAttribute("Attribute2"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("ElementName");
        xmlElement.addAttribute("Attribute1", "Value1");
        xmlElement.addAttribute("Attribute2", "Value2");

        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        Assert.assertEquals("Attribute1", attributeNames.nextElement());
        Assert.assertEquals("Attribute2", attributeNames.nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement = new XmlElement("ChildElement");
        parentElement.addElement(childElement);

        Assert.assertNotNull(parentElement.getElement("ChildElement"));
        Assert.assertSame(childElement, parentElement.getElement("ChildElement"));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeElement(childElement1);

        Assert.assertNull(parentElement.getElement("ChildElement1"));
        Assert.assertNotNull(parentElement.getElement("ChildElement2"));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        List<XmlElement> elements = parentElement.getElements();
        Assert.assertEquals(2, elements.size());
        Assert.assertSame(childElement1, elements.get(0));
        Assert.assertSame(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        Assert.assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement childElement1Result = parentElement.getElement("ChildElement1");
        Assert.assertSame(childElement1, childElement1Result);

        XmlElement childElement3Result = parentElement.getElement("ChildElement3");
        Assert.assertNull(childElement3Result);
    }

    @Test
    public void testGetElementWithPath() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        XmlElement childElement3 = new XmlElement("ChildElement3");
        parentElement.addElement(childElement1);
        childElement1.addElement(childElement2);
        childElement2.addElement(childElement3);

        XmlElement childElement3Result = parentElement.getElement("ChildElement1/ChildElement2/ChildElement3");
        Assert.assertSame(childElement3, childElement3Result);

        XmlElement childElement4Result = parentElement.getElement("ChildElement1/ChildElement4");
        Assert.assertNull(childElement4Result);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement = parentElement.addSubElement("ChildElement");
        Assert.assertSame(childElement, parentElement.getElement("ChildElement"));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement = parentElement.addSubElement("ChildElement1/ChildElement2");
        Assert.assertNull(parentElement.getElement("ChildElement1"));
        Assert.assertNull(parentElement.getElement("ChildElement1/ChildElement2"));
        Assert.assertSame(childElement, parentElement.getElement("ChildElement1/ChildElement2"));
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement("ElementName") {{
            addAttribute("Attribute1", "Value1");
            addAttribute("Attribute2", "Value2");
            addElement(new XmlElement("ChildElement1") {{
                addAttribute("ChildAttribute1", "ChildValue1");
                addElement(new XmlElement("GrandChildElement") {{
                    setData("GrandChildData");
                }});
            }});
            setData("Data");
        }};

        StringBuilder output = new StringBuilder();
        XmlElement.printNode(xmlElement, "");
        String expectedOutput = "" +
                "ElementName = 'Data'\n" +
                "Attribute1: Value1\n" +
                "Attribute2: Value2\n" +
                "ChildElement1\n" +
                "    ChildAttribute1: ChildValue1\n" +
                "    GrandChildElement\n";
        Assert.assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("ElementName") {{
            addAttribute("Attribute1", "Value1");
            addAttribute("Attribute2", "Value2");
            addElement(new XmlElement("ChildElement") {{
                setName("ChildElementName");
                setData("ChildData");
            }});
        }};

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();

        Assert.assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        Assert.assertEquals(xmlElement.getAttribute("Attribute1"), clonedXmlElement.getAttribute("Attribute1"));
        Assert.assertEquals(xmlElement.getAttribute("Attribute2"), clonedXmlElement.getAttribute("Attribute2"));
        Assert.assertEquals(xmlElement.getElements().size(), clonedXmlElement.getElements().size());

        XmlElement childElement = xmlElement.getElement("ChildElement");
        XmlElement clonedChildElement = clonedXmlElement.getElement("ChildElement");

        Assert.assertSame(childElement.getName(), clonedChildElement.getName());
        Assert.assertEquals(childElement.getData(), clonedChildElement.getData());
    }
}