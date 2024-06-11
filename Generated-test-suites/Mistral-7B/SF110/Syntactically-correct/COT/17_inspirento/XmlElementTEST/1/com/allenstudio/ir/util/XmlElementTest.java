package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("name");
        assertNotNull(xmlElementWithName);
        assertEquals("name", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("name", new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        assertNotNull(xmlElementWithNameAndAttributes);
        assertEquals("name", xmlElementWithNameAndAttributes.getName());
        assertEquals(2, xmlElementWithNameAndAttributes.getAttributes().size());

        XmlElement xmlElementWithNameAndData = new XmlElement("name", "data");
        assertNotNull(xmlElementWithNameAndData);
        assertEquals("name", xmlElementWithNameAndData.getName());
        assertEquals("data", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", null);
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute", "defaultValue"));
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(new Hashtable<String, String>() {{
            put("attribute1", "value1");
            put("attribute2", "value2");
        }});
        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test(expected = NullPointerException.class)
    public void testSetAttributesNullMap() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(null);
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(1, parentElement.getElements().size());
        assertEquals(parentElement, childElement.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementNullChild() {
        XmlElement parentElement = new XmlElement();
        assertFalse(parentElement.addElement(null));
        assertEquals(0, parentElement.getElements().size());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        assertTrue(parentElement.removeElement(childElement));
        assertEquals(0, parentElement.getElements().size());
        assertNull(childElement.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveElementNullChild() {
        XmlElement parentElement = new XmlElement();
        assertFalse(parentElement.removeElement(null));
        assertEquals(0, parentElement.getElements().size());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement removedElement = parentElement.removeElement(1);
        assertEquals(childElement2, removedElement);
        assertEquals(1, parentElement.getElements().size());
        assertEquals(parentElement, childElement1.getParent());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementByIndexOutOfBounds() {
        XmlElement parentElement = new XmlElement();
        parentElement.addElement(new XmlElement("child1"));
        parentElement.removeElement(0);
        parentElement.removeElement(0);
        assertThrows(IndexOutOfBoundsException.class, () -> parentElement.removeElement(0));
        assertEquals(0, parentElement.getElements().size());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        assertEquals(0, parentElement.getElements().size());
        assertNull(childElement1.getParent());
        assertNull(childElement2.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertEquals(0, parentElement.getElements().size());
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.append(childElement);
        assertEquals(1, parentElement.getElements().size());
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement existingChildElement = new XmlElement("existingChild");
        XmlElement newChildElement = new XmlElement("newChild");
        parentElement.addElement(existingChildElement);
        parentElement.insertElement(newChildElement, 0);
        assertEquals(2, parentElement.getElements().size());
        assertEquals(parentElement, newChildElement.getParent());
        assertEquals(newChildElement, parentElement.getElement(0));
        assertEquals(existingChildElement, parentElement.getElement(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInsertElementIndexOutOfBounds() {
        XmlElement parentElement = new XmlElement();
        XmlElement existingChildElement = new XmlElement("existingChild");
        parentElement.addElement(existingChildElement);
        parentElement.insertElement(new XmlElement("newChild"), parentElement.getElements().size() + 1);
        assertEquals(1, parentElement.getElements().size());
        assertThrows(IndexOutOfBoundsException.class, () -> parentElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        List<XmlElement> elements = parentElement.getElements();
        assertEquals(2, elements.size());
        assertEquals(childElement1, elements.get(0));
        assertEquals(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
    }

    @Test
    public void testgetElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        XmlElement retrievedElement = parentElement.getElement("child1");
        assertEquals(childElement1, retrievedElement);
        XmlElement retrievedElementByIndex = parentElement.getElement(0);
        assertEquals(childElement1, retrievedElementByIndex);

        XmlElement notExistentElement = parentElement.getElement("nonExistentElement");
        assertNull(notExistentElement);
        XmlElement notExistentElementByIndex = parentElement.getElement(3);
        assertNull(notExistentElementByIndex);
    }

    @Test
    public void testgetElementByPath() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        childElement1.addElement(new XmlElement("grandChild1"));
        childElement1.addElement(new XmlElement("grandChild2"));
        parentElement.addElement(childElement2);
        XmlElement retrievedElement = parentElement.getElement("child1/grandChild1");
        assertEquals(parentElement.getElement("child1").getElement("grandChild1"), retrievedElement);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child");
        assertEquals(1, parentElement.getElements().size());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals("child", childElement.getName());
        assertEquals(0, childElement.getElements().size());
        assertEquals(null, childElement.getData());
        assertEquals(null, childElement.getParent());

        parentElement.addSubElement("child/grandchild");
        XmlElement grandChildElement = parentElement.getElement("child/grandchild");
        assertEquals(1, parentElement.getElements().size());
        assertEquals(grandChildElement, parentElement.getElement(0));
        assertEquals("child/grandchild", grandChildElement.getName());
        assertEquals(null, grandChildElement.getData());
        assertEquals(parentElement, grandChildElement.getParent());
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child", "data");
        assertEquals(1, parentElement.getElements().size());
        assertEquals(childElement, parentElement.getElement(0));
        assertEquals("child", childElement.getName());
        assertEquals("data", childElement.getData());
        assertEquals(null, childElement.getParent());

        parentElement.addSubElement("child/grandchild");
        XmlElement grandChildElement = parentElement.getElement("child/grandchild");
        assertEquals(1, parentElement.getElements().size());
        assertEquals(grandChildElement, parentElement.getElement(0));
        assertEquals("child/grandchild", grandChildElement.getName());
        assertEquals(null, grandChildElement.getData());
        assertEquals(parentElement, grandChildElement.getParent());
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("original");
        originalElement.addAttribute("attribute1", "value1");
        originalElement.addAttribute("attribute2", "value2");
        originalElement.addElement("child1");
        originalElement.addElement("child2");
        XmlElement clonedElement = (XmlElement) originalElement.clone();
        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
        assertEquals(originalElement.getAttributes().get("attribute1"), clonedElement.getAttributes().get("attribute1"));
        assertEquals(originalElement.getAttributes().get("attribute2"), clonedElement.getAttributes().get("attribute2"));

        List<XmlElement> originalChildren = originalElement.getElements();
        List<XmlElement> clonedChildren = clonedElement.getElements();
        assertEquals(originalChildren.size(), clonedChildren.size());
        for (XmlElement originalChild : originalChildren) {
            XmlElement clonedChild = clonedElement.getElement(clonedChildren.indexOf(originalChild.clone()));
            assertEquals(originalChild.getName(), clonedChild.getName());
            assertEquals(originalChild.getData(), clonedChild.getData());
            assertEquals(originalChild.getAttributes().size(), clonedChild.getAttributes().size());
            assertEquals(originalChild.getAttributes().get("attribute1"), clonedChild.getAttributes().get("attribute1"));
            assertEquals(originalChild.getAttributes().get("attribute2"), clonedChild.getAttributes().get("attribute2"));
        }
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.setName("name1");
        xmlElement2.setName("name1");
        xmlElement1.setAttributes(new Hashtable<String, String>() {{
            put("attribute1", "value1");
        }});
        xmlElement2.setAttributes(new Hashtable<String, String>() {{
            put("attribute1", "value1");
        }});
        XmlElement xmlElement3 = new XmlElement();
        xmlElement3.setName("name1");
        xmlElement3.setAttributes(new Hashtable<String, String>() {{
            put("attribute2", "value2");
        }});
        assertFalse(xmlElement1.equals(xmlElement3));

        xmlElement1.addElement(new XmlElement("child1"));
        xmlElement2.addElement(new XmlElement("child1"));
        assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.addElement(new XmlElement("child2"));
        assertFalse(xmlElement1.equals(xmlElement2));

        XmlElement xmlElement4 = new XmlElement();
        xmlElement4.setName("name1");
        xmlElement4.setAttributes(xmlElement1.getAttributes());
        xmlElement4.addElements(xmlElement1.getElements());
        assertTrue(xmlElement1.equals(xmlElement4));
    }
}