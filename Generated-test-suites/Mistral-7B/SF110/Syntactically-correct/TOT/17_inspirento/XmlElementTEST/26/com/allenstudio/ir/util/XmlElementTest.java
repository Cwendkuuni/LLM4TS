package com.allenstudio.ir.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class XmlElementTest {

    private XmlElement xmlElement;
    private XmlElement subElement;
    private XmlElement parentElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        xmlElement = new XmlElement("root");
        subElement = new XmlElement("sub");
        parentElement = new XmlElement("parent");
    }

    @Test
    public void testXmlElementCreation() {
        XmlElement element = new XmlElement();
        assertNotNull(element);

        XmlElement nameElement = new XmlElement("name");
        assertNotNull(nameElement);

        XmlElement nameAndAttributesElement = new XmlElement("name", new Hashtable<String, String>() {{
            put("attribute1", "value1");
        }});
        assertNotNull(nameAndAttributesElement);

        XmlElement nameAndDataElement = new XmlElement("name", "data");
        assertNotNull(nameAndDataElement);
    }

    @Test
    public void testAddAttribute() {
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("attribute2", null);
        assertNull(xmlElement.getAttribute("attribute2"));

        xmlElement.addAttribute("attribute1", null);
        assertNull(xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("attribute1", "value2");
        assertEquals("value2", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute(null, "defaultValue");
        assertEquals("defaultValue", xmlElement.getAttribute(null));

        xmlElement.addAttribute("attribute1", null);
        assertNull(xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttributes() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");

        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));

        attributes = new Hashtable<String, String>();
        xmlElement.setAttributes(attributes);
        assertEquals(0, xmlElement.getAttributes().size());
    }

    @Test
    public void testAttributeNames() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");

        Enumeration<String> names = xmlElement.getAttributeNames();
        verify(xmlElement, times(2)).getAttribute(anyString());

        assertEquals("attribute1", names.nextElement());
        assertEquals("attribute2", names.nextElement());
        assertNull(names.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        xmlElement.addElement(subElement);
        assertEquals(subElement, xmlElement.removeElement(xmlElement.getElements().size() - 1));

        xmlElement.addElement(parentElement);
        assertEquals(parentElement, xmlElement.getParent());
        assertNull(parentElement.getParent());
    }

    @Test
    public void testRemoveElement() {
        xmlElement.addElement(subElement);
        xmlElement.removeElement(subElement);
        assertFalse(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.addElement(subElement);
        xmlElement.addElement(subElement);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        xmlElement.addElement(subElement);
        subElement.setParent(xmlElement);
        xmlElement.removeFromParent();
        assertNull(subElement.getParent());
    }

    @Test
    public void testAppend() {
        xmlElement.append(subElement);
        assertEquals(subElement, xmlElement.getElements().get(0));
    }

    @Test
    public void testInsertElement() {
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        xmlElement.insertElement(subElement1, 0);
        assertEquals(subElement1, xmlElement.getElements().get(0));
        assertEquals(subElement2, xmlElement.getElements().get(1));
    }

    @Test
    public void testGetElements() {
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        List<XmlElement> elements = xmlElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(subElement1, elements.get(0));
        assertEquals(subElement2, elements.get(1));

        elements = new ArrayList<XmlElement>();
        xmlElement.setElements(elements);
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testCount() {
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        assertEquals(2, xmlElement.count());

        xmlElement.removeElement(subElement1);
        assertEquals(1, xmlElement.count());

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        XmlElement element = xmlElement.getElement("sub/subElement2");
        assertEquals(subElement2, element);

        element = xmlElement.getElement("nonExistingElement");
        assertNull(element);

        element = xmlElement.getElement("/");
        assertEquals(xmlElement, element);
    }

    @Test
    public void testGetElementByIndex() {
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        XmlElement element = xmlElement.getElement(0);
        assertEquals(subElement1, element);

        element = xmlElement.getElement(1);
        assertEquals(subElement2, element);

        element = xmlElement.getElement(2);
        assertNull(element);
    }

    @Test
    public void testAddSubElement() {
        xmlElement.addSubElement("sub/subElement1");
        assertEquals(1, xmlElement.getElements().size());

        XmlElement subElement = xmlElement.getElement("sub/subElement1");
        assertEquals("subElement1", subElement.getName());
        assertNull(subElement.getData());
        assertTrue(subElement.getAttributes().isEmpty());

        xmlElement.addSubElement("sub/subElement2", "data");
        assertEquals(2, xmlElement.getElements().size());

        subElement = xmlElement.getElement("sub/subElement2");
        assertEquals("subElement2", subElement.getName());
        assertEquals("data", subElement.getData());
        assertTrue(subElement.getAttributes().isEmpty());
    }

    @Test
    public void testClone() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addSubElement("subElement1");
        xmlElement.addSubElement("subElement2");

        XmlElement clonedElement = (XmlElement) xmlElement.clone();

        assertEquals(xmlElement.getName(), clonedElement.getName());
        assertEquals(xmlElement.getData(), clonedElement.getData());
        assertEquals(xmlElement.getAttributes().size(), clonedElement.getAttributes().size());
        assertEquals(xmlElement.getElements().size(), clonedElement.getElements().size());

        for (XmlElement element : xmlElement.getElements()) {
            XmlElement clonedElement = (XmlElement) element.clone();
            assertEquals(element.getName(), clonedElement.getName());
            assertEquals(element.getData(), clonedElement.getData());
            assertEquals(element.getAttributes().size(), clonedElement.getAttributes().size());
        }
    }

    @Test
    public void testSetName() {
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }

    @Test
    public void testNotifyObservers() {
        Mockito.spy(XmlObserver.class);
        xmlElement.addObserver(mock(XmlObserver.class));
        xmlElement.notifyObservers();
        verify(mock(XmlObserver.class), times(1)).update(xmlElement);
    }

    @Test
    public void testEquals() {
        XmlElement other = new XmlElement("name");
        assertFalse(xmlElement.equals(other));

        XmlElement otherWithSameAttributes = new XmlElement("name") {{
            addAttribute("attribute1", "value1");
            addAttribute("attribute2", "value2");
        }};
        assertTrue(xmlElement.equals(otherWithSameAttributes));

        XmlElement otherWithSameData = new XmlElement("name") {{
            setData("data");
        }};
        assertTrue(xmlElement.equals(otherWithSameData));

        XmlElement otherWithSameNameAndData = new XmlElement("name") {{
            setData("data");
            setName("name");
        }};
        assertTrue(xmlElement.equals(otherWithSameNameAndData));

        XmlElement otherWithSameNameAndAttributesAndSubElements = new XmlElement("name") {{
            setName("name");
            setData("data");
            Hashtable<String, String> attributes = new Hashtable<String, String>();
            attributes.put("attribute1", "attributeValue1");
            attributes.put("attribute2", "attributeValue2");
            setAttributes(attributes);
            addElement(subElement1);
            addElement(subElement2);
        }};
        assertTrue(xmlElement.equals(otherWithSameNameAndAttributesAndSubElements));
    }

    @Test
    public void testHashCode() {
        XmlElement other = new XmlElement("name");
        int xmlElementHashCode = xmlElement.hashCode();
        int otherHashCode = other.hashCode();
        assertNotEquals(xmlElementHashCode, otherHashCode);

        XmlElement otherWithSameAttributes = new XmlElement("name") {{
            addAttribute("attribute1", "value1");
            addAttribute("attribute2", "value2");
        }};
        int sameAttributesHashCode = xmlElement.hashCode();
        int otherWithSameAttributesHashCode = otherWithSameAttributes.hashCode();
        assertEquals(sameAttributesHashCode, otherWithSameAttributesHashCode);

        XmlElement otherWithSameData = new XmlElement("name") {{
            setData("data");
        }};
        int sameDataHashCode = xmlElement.hashCode();
        int otherWithSameDataHashCode = otherWithSameData.hashCode();
        assertEquals(sameDataHashCode, otherWithSameDataHashCode);

        XmlElement otherWithSameNameAndData = new XmlElement("name") {{
            setData("data");
            setName("name");
        }};
        int sameNameAndDataHashCode = xmlElement.hashCode();
        int otherWithSameNameAndDataHashCode = otherWithSameNameAndData.hashCode();
        assertEquals(sameNameAndDataHashCode, otherWithSameNameAndDataHashCode);

        XmlElement otherWithSameNameAndAttributesAndSubElements = new XmlElement("name") {{
            setName("name");
            setData("data");
            Hashtable<String, String> attributes = new Hashtable<String, String>();
            attributes.put("attribute1", "attributeValue1");
            attributes.put("attribute2", "attributeValue2");
            setAttributes(attributes);
            addElement(subElement1);
            addElement(subElement2);
        }};
        int sameNameAndAttributesAndSubElementsHashCode = xmlElement.hashCode();
        int otherWithSameNameAndAttributesAndSubElementsHashCode = otherWithSameNameAndAttributesAndSubElements.hashCode();
        assertEquals(sameNameAndAttributesAndSubElementsHashCode, otherWithSameNameAndAttributesAndSubElementsHashCode);
    }
}