package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {
    XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement("root");
    }

    @Test
    public void testXmlElementConstructorWithName() {
        XmlElement xmlElement = new XmlElement("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testXmlElementConstructorWithNameAndAttributes() {
        XmlElement xmlElement = new XmlElement("name", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertEquals("name", xmlElement.getName());
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals("value2", xmlElement.getAttribute("attribute2"));
    }

    @Test
    public void testXmlElementConstructorWithNameAndData() {
        XmlElement xmlElement = new XmlElement("name", "data");
        assertEquals("name", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullName() {
        xmlElement.addAttribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeWithNullValue() {
        xmlElement.addAttribute("name", null);
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        xmlElement.addAttribute("attribute1", null);
        assertEquals("defaultValue", xmlElement.getAttribute("attribute1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        Map<String, String> attributes = new HashMap<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }};
        xmlElement.setAttributes(attributes);
        Map<String, String> attributes2 = xmlElement.getAttributes();
        assertEquals("value1", attributes2.get("attribute1"));
        assertEquals("value2", attributes2.get("attribute2"));
    }

    @Test(expected = NullPointerException.class)
    public void testSetAttributesWithNullMap() {
        xmlElement.setAttributes(null);
    }

    @Test
    public void testGetAttributeNames() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Enumeration<String> enumeration = xmlElement.getAttributeNames();
        String name;
        while (enumeration.hasMoreElements()) {
            name = enumeration.nextElement();
            assertEquals("attribute1", name.equals("attribute1") ? name : "!" + name + "!");
            assertEquals("attribute2", name.equals("attribute2") ? name : "!" + name + "!");
        }
    }

    @Test
    public void testAddElement() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);
        assertEquals(1, xmlElement.count());
        assertEquals(child, xmlElement.getElement(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementWithNullElement() {
        xmlElement.addElement(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);
        xmlElement.removeElement(child);
        assertEquals(0, xmlElement.count());
        assertNull(xmlElement.getElement(0));
    }

    @Test
    public void testRemoveElementWithNullElement() {
        xmlElement.removeElement(null);
        assertEquals(0, xmlElement.count());
        assertNull(xmlElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
        assertNull(xmlElement.getElement(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement(0));
        assertEquals(child1, parent.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testGetElementWithNullPath() {
        assertNull(xmlElement.getElement(null));
    }

    @Test
    public void testGetElementWithEmptyPath() {
        assertNull(xmlElement.getElement(""));
    }

    @Test
    public void testGetElementWithValidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        parent.addElement(child1);
        parent.addElement(child2);
        child1.addElement(child3);
        assertEquals(child3, parent.getElement("child1/child3"));
    }

    @Test
    public void testGetElementWithInvalidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        assertNull(parent.getElement("child1/child2"));
    }

    @Test
    public void testAddSubElementWithNullPath() {
        assertNull(xmlElement.getElement((String)null));
    }

    @Test
    public void testAddSubElementWithEmptyPath() {
        xmlElement.addSubElement("");
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testAddSubElementWithValidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        XmlElement child2 = new XmlElement("child2");
        parent.addSubElement("child1/child2");
        assertEquals(1, parent.count());
        assertEquals(child2, parent.getElement("child1/child2"));
    }

    @Test
    public void testAddSubElementWithExistingElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        child1.addElement(child2);
        XmlElement child3 = new XmlElement("child3");
        parent.addSubElement("child1/child3");
        assertEquals(2, parent.count());
        assertEquals(child3, parent.getElement("child1/child3"));
    }

    @Test
    public void testAddSubElementWithInvalidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        assertNull(parent.getElement("child1/child2"));
        try {
            parent.addSubElement("child1/child2");
            fail("Exception expected");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testAddSubElementWithCircularPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        child1.addElement(parent);
        try {
            parent.addSubElement("child1/parent");
            fail("Exception expected");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCloningWithNullAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(null);
        try {
            XmlElement clone = (XmlElement)xmlElement.clone();
            fail("Exception expected");
        } catch (CloneNotSupportedException e) {
            // Expected
        }
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloning() throws CloneNotSupportedException {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.addElement(new XmlElement("child1"));
        XmlElement clone = (XmlElement)xmlElement.clone();

        assertEquals(xmlElement.getName(), clone.getName());
        assertEquals(xmlElement.getData(), clone.getData());
        assertEquals(xmlElement.getAttributes(), clone.getAttributes());
        assertEquals(xmlElement.count(), clone.count());
        assertEquals(xmlElement.getElement(0), clone.getElement(0));
    }

    @Test
    public void testSettingName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testSettingNameWithEmptyString() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("");
        assertEquals("", xmlElement.getName());
    }

    @Test
    public void testSettingNameWithNullString() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName(null);
        assertNull(xmlElement.getName());
    }
}