package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;

import com.allenstudio.ir.util.XmlElement;

@Category(IntegrationTest.class)
public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement("TestElement");
        assertNotNull(xmlElement);
        assertEquals("TestElement", xmlElement.getName());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testXmlElementAddAttribute() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute", "TestValue");
        assertEquals("TestValue", xmlElement.getAttribute("TestAttribute"));
    }

    @Test(expected = NullPointerException.class)
    public void testXmlElementAddAttributeNullName() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute(null, "TestValue");
    }

    @Test(expected = NullPointerException.class)
    public void testXmlElementAddAttributeNullValue() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute", null);
    }

    @Test
    public void testXmlElementAddSubElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement = new XmlElement("ChildElement");
        parentElement.addSubElement(childElement);
        assertEquals(1, parentElement.count());
        assertEquals("ChildElement", parentElement.getElement(0).getName());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlElementAddSubElementNullName() {
        XmlElement parentElement = new XmlElement("ParentElement");
        parentElement.addSubElement(null);
    }

    @Test
    public void testXmlElementGetElement() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        parentElement.addSubElement(childElement1);
        parentElement.addSubElement("ChildElement3", "TestData");
        parentElement.addSubElement("ChildElement4/ChildElement5", "TestData2");

        XmlElement childElement1Retrieved = parentElement.getElement("ChildElement1");
        assertEquals("ChildElement1", childElement1Retrieved.getName());
        assertEquals(0, childElement1Retrieved.count());

        XmlElement childElement3Retrieved = parentElement.getElement("ChildElement3");
        assertEquals("ChildElement3", childElement3Retrieved.getName());
        assertEquals("TestData", childElement3Retrieved.getData());
        assertEquals(0, childElement3Retrieved.count());

        XmlElement childElement4Child5Retrieved = parentElement.getElement("ChildElement4/ChildElement5");
        assertEquals("ChildElement5", childElement4Child5Retrieved.getName());
        assertEquals(0, childElement4Child5Retrieved.count());
        assertEquals("TestData2", childElement4Child5Retrieved.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlElementGetElementInvalidPath() {
        XmlElement parentElement = new XmlElement("ParentElement");
        parentElement.getElement("InvalidPath");
    }

    @Test
    public void testXmlElementRemoveFromParent() {
        XmlElement parentElement = new XmlElement("ParentElement");
        XmlElement childElement = new XmlElement("ChildElement");
        parentElement.addSubElement(childElement);
        assertEquals(1, parentElement.count());
        parentElement.removeFromParent();
        assertEquals(0, parentElement.count());
        assertNull(childElement.getParent());
    }

    @Test
    public void testXmlElementEquals() {
        XmlElement xmlElement1 = new XmlElement("TestElement");
        XmlElement xmlElement2 = new XmlElement("TestElement");
        XmlElement xmlElement3 = new XmlElement("TestElement2");
        XmlElement xmlElement4 = xmlElement1;

        assertEquals(xmlElement1, xmlElement4);
        assertEquals(xmlElement1.hashCode(), xmlElement4.hashCode());

        assertEquals(xmlElement1, xmlElement2);
        assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());

        assertNotEquals(xmlElement1, xmlElement3);
        assertNotEquals(xmlElement1.hashCode(), xmlElement3.hashCode());
    }

    @Test
    public void testXmlElementClone() {
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("TestAttribute1", "TestValue1");
        xmlElement.addAttribute("TestAttribute2", "TestValue2");
        xmlElement.addSubElement("ChildElement1", "TestData1");
        xmlElement.addSubElement("ChildElement2", "TestData2");

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();

        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        assertEquals(xmlElement.getAttribute("TestAttribute1"), clonedXmlElement.getAttribute("TestAttribute1"));
        assertEquals(xmlElement.getAttribute("TestAttribute2"), clonedXmlElement.getAttribute("TestAttribute2"));
        assertEquals(xmlElement.getElements().size(), clonedXmlElement.getElements().size());

        assertEquals(xmlElement.getElement(0).getName(), clonedXmlElement.getElement(0).getName());
        assertEquals(xmlElement.getElement(0).getData(), clonedXmlElement.getElement(0).getData());
        assertEquals(xmlElement.getElement(1).getName(), clonedXmlElement.getElement(1).getName());
        assertEquals(xmlElement.getElement(1).getData(), clonedXmlElement.getElement(1).getData());
    }

    @Test
    public void testXmlElementNotifyObservers() {
        XmlElement xmlElement = new XmlElement("TestElement");
        List<XmlElementObserver> observers = new ArrayList<>();
        xmlElement.addObserver(new XmlElementObserver() {
            @Override
            public void update(XmlElement xmlElement) {
                // Do something
            }
        });
        xmlElement.addObserver(new XmlElementObserver() {
            @Override
            public void update(XmlElement xmlElement) {
                // Do something else
            }
        });

        xmlElement.setData("TestData");
        xmlElement.notifyObservers();
    }
}

class XmlElementObserver implements Observer {
    @Override
    public void update(XmlElement xmlElement) {
        // Implement observer logic here
    }
}