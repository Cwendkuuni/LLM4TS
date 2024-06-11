package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setUp() {
        rootElement = new XmlElement("root");;
    }

    @Test
    public void testXmlElementCreation() {
        XmlElement element1 = new XmlElement("element1");;
        XmlElement element2 = new XmlElement("element2", "data1");
        XmlElement element3 = new XmlElement("element3", null);

        // Test empty constructor
        assertEquals("root", rootElement.getName());
        assertEquals(0, rootElement.getElements().size());
        assertEquals(0, rootElement.getAttributeNames().hasMoreElements());

        // Test constructor with name
        XmlElement element4 = new XmlElement("element4");
        assertEquals("element4", element4.getName());
        assertEquals(0, element4.getElements().size());
        assertEquals(0, element4.getAttributeNames().hasMoreElements());

        // Test constructor with name and attributes
        XmlElement element5 = new XmlElement("element5", new Hashtable<String, String>() {{
            put("attr1", "value1");
            put("attr2", "value2");
        }});
        assertEquals("element5", element5.getName());
        assertEquals(0, element5.getElements().size());
        assertEquals(2, element5.getAttributeNames().hasMoreElements());
        assertEquals("attr1", element5.getAttributeNames().nextElement());
        assertEquals("value1", element5.getAttribute("attr1"));
        assertEquals("attr2", element5.getAttributeNames().nextElement());
        assertEquals("value2", element5.getAttribute("attr2"));

        // Test constructor with name and data
        XmlElement element6 = new XmlElement("element6", "data2");
        assertEquals("element6", element6.getName());
        assertEquals("data2", element6.getData());
        assertEquals(0, element6.getElements().size());
        assertEquals(0, element6.getAttributeNames().hasMoreElements());
    }

    @Test
    public void testAddAttribute() {
        rootElement.addAttribute("attr1", "value1");
        assertEquals(1, rootElement.getAttributeNames().hasMoreElements());
        assertEquals("attr1", rootElement.getAttributeNames().nextElement());
        assertEquals("value1", rootElement.getAttribute("attr1"));

        rootElement.addAttribute("attr2", null);
        assertEquals(1, rootElement.getAttributeNames().hasMoreElements());
        assertEquals("attr1", rootElement.getAttributeNames().nextElement());
        assertEquals("value1", rootElement.getAttribute("attr1"));
        assertEquals(0, rootElement.getAttributeNames().hasMoreElements());

        rootElement.addAttribute(null, "value3");
        assertEquals(1, rootElement.getAttributeNames().hasMoreElements());
        assertEquals("attr1", rootElement.getAttributeNames().nextElement());
        assertEquals("value1", rootElement.getAttribute("attr1"));
        assertEquals(0, rootElement.getAttributeNames().hasMoreElements());

        rootElement.addAttribute("attr1", "value4");
        assertEquals(1, rootElement.getAttributeNames().hasMoreElements());
        assertEquals("attr1", rootElement.getAttributeNames().nextElement());
        assertEquals("value4", rootElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttribute() {
        rootElement.addAttribute("attr1", "value1");
        assertEquals("value1", rootElement.getAttribute("attr1"));
        assertEquals(null, rootElement.getAttribute(null));
        assertEquals(null, rootElement.getAttribute("nonExistentAttr"));
    }

    @Test
    public void testGetAttributes() {
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");

        Map<String, String> attributes = rootElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("attr1", attributes.keySet().iterator().next());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("attr2", attributes.keySet().iterator().next());
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testSetAttributes() {
        rootElement.setAttributes(new Hashtable<String, String>() {{
            put("attr1", "value1");
            put("attr2", "value2");
        }});
        assertEquals(2, rootElement.getAttributes().size());
        assertEquals("attr1", rootElement.getAttributes().keySet().iterator().next());
        assertEquals("value1", rootElement.getAttributes().get("attr1"));
        assertEquals("attr2", rootElement.getAttributes().keySet().iterator().next());
        assertEquals("value2", rootElement.getAttributes().get("attr2"));

        rootElement.setAttributes(null);
        assertEquals(0, rootElement.getElements().size());
        assertEquals(0, rootElement.getAttributeNames().hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement element1 = new XmlElement("element1");;
        rootElement.addElement(element1);
        assertEquals(1, rootElement.getElements().size());
        assertEquals(element1, rootElement.getElements().get(0));

        XmlElement element2 = new XmlElement("element2");;
        rootElement.addElement(element2);
        assertEquals(2, rootElement.getElements().size());
        assertEquals(element1, rootElement.getElements().get(0));
        assertEquals(element2, rootElement.getElements().get(1));

        XmlElement element3 = new XmlElement("element3", "data3");
        rootElement.addElement(element3);
        assertEquals(3, rootElement.getElements().size());
        assertEquals(element1, rootElement.getElements().get(0));
        assertEquals(element2, rootElement.getElements().get(1));
        assertEquals(element3, rootElement.getElements().get(2));
    }

    @Test
    public void testRemoveElement() {
        XmlElement element1 = new XmlElement("element1");;
        rootElement.addElement(element1);
        assertEquals(1, rootElement.getElements().size());

        XmlElement removedElement = rootElement.removeElement(element1);
        assertEquals(0, rootElement.getElements().size());
        assertEquals(element1, removedElement);

        rootElement.addElement(element1);
        rootElement.addElement(new XmlElement("element2"));

        XmlElement element2 = rootElement.removeElement(1);
        assertEquals(1, rootElement.getElements().size());
        assertEquals(element1, rootElement.getElements().get(0));
        assertEquals(element2, rootElement.getElements().get(0)); // This should not happen, but JUnit allows it

        XmlElement element3 = new XmlElement("element3");;
        rootElement.addElement(element3);
        rootElement.addElement(element1);

        XmlElement removedElement2 = rootElement.removeElement(element1);
        assertEquals(1, rootElement.getElements().size());
        assertEquals(element3, rootElement.getElements().get(0));
        assertEquals(element1, removedElement2);
    }

    @Test
    public void testInsertElement() {
        XmlElement element1 = new XmlElement("element1");;
        rootElement.addElement(element1);
        assertEquals(1, rootElement.getElements().size());

        rootElement.insertElement(new XmlElement("element2"), 0);
        assertEquals(2, rootElement.getElements().size());
        assertEquals(element2, rootElement.getElements().get(0));
        assertEquals(element1, rootElement.getElements().get(1));

        rootElement.insertElement(new XmlElement("element3"), 2);
        assertEquals(3, rootElement.getElements().size());
        assertEquals(element1, rootElement.getElements().get(1));
        assertEquals(element2, rootElement.getElements().get(0));
        assertEquals(element3, rootElement.getElements().get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementIndexOutOfBounds() {
        rootElement.addElement(new XmlElement("element1"));
        rootElement.addElement(new XmlElement("element2"));
        rootElement.addElement(new XmlElement("element3"));

        rootElement.removeElement(rootElement.getElements().size());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement element1 = new XmlElement("element1");;
        rootElement.addElement(element1);
        assertEquals(1, rootElement.getElements().size());

        rootElement.removeAllElements();
        assertEquals(0, rootElement.getElements().size());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");;
        XmlElement element1 = new XmlElement("element1");;
        parent.addElement(element1);

        assertEquals(1, parent.getElements().size());
        assertEquals(element1, parent.getElements().get(0));

        element1.removeFromParent();
        assertEquals(0, parent.getElements().size());
        assertEquals(null, element1.getParent());

        parent.addElement(element1);
        assertEquals(1, parent.getElements().size());
        assertEquals(element1, parent.getElements().get(0));
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");;
        XmlElement child1 = new XmlElement("child1");;
        parent.append(child1);

        assertEquals(1, parent.getElements().size());
        assertEquals(child1, parent.getElements().get(0));
        assertEquals(parent, child1.getParent());

        XmlElement child2 = new XmlElement("child2");;
        parent.append(child2);

        assertEquals(2, parent.getElements().size());
        assertEquals(child1, parent.getElements().get(0));
        assertEquals(child2, parent.getElements().get(1));
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
    }

    @Test
    public void testClone() {
        XmlElement element1 = new XmlElement("element1");;
        XmlElement element1Cloned = (XmlElement)element1.clone();

        assertEquals("element1", element1.getName());
        assertEquals("element1", element1Cloned.getName());

        element1.addAttribute("attr1", "value1");
        assertEquals(0, element1Cloned.getAttributeNames().hasMoreElements());
        assertEquals("value1", element1.getAttribute("attr1"));
        assertEquals(null, element1Cloned.getAttribute("attr1"));

        element1.addElement(new XmlElement("child1"));
        List<XmlElement> children1 = element1.getElements();
        assertEquals(1, children1.size());
        assertEquals("child1", children1.get(0).getName());

        List<XmlElement> children1Cloned = element1Cloned.getElements();
        assertEquals(0, children1Cloned.size());

        element1.setParent(null);
        assertEquals(null, element1.getParent());

        XmlElement element2 = new XmlElement("element2");;
        rootElement.addElement(element2);
        element2.setParent(rootElement);

        XmlElement clonedElement2 = (XmlElement)element2.clone();
        assertEquals("element2", element2.getName());
        assertEquals("element2", clonedElement2.getName());
        assertEquals(rootElement, element2.getParent());
        assertEquals(null, clonedElement2.getParent());

        List<XmlElement> children2 = element2.getElements();
        assertEquals(0, children2.size());

        List<XmlElement> children2Cloned = clonedElement2.getElements();
        assertEquals(0, children2Cloned.size());
    }
}