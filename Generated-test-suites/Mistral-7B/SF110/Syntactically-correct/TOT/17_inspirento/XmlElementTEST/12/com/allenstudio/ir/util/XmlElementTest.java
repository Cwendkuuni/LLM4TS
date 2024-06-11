package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

	@Test
	public void testXmlElementCreation() {
		XmlElement xmlElement = new XmlElement();
		assertNotNull(xmlElement);

		XmlElement xmlElementWithName = new XmlElement("root");
		assertNotNull(xmlElementWithName);
		assertEquals("root", xmlElementWithName.getName());

		XmlElement xmlElementWithNameAndAttributes = new XmlElement("root", new Hashtable<String, String>() {{
			put("id", "1");
			put("class", "element");
		}});
		assertNotNull(xmlElementWithNameAndAttributes);
		assertEquals("root", xmlElementWithNameAndAttributes.getName());
		assertEquals("1", xmlElementWithNameAndAttributes.getAttribute("id"));
		assertEquals("element", xmlElementWithNameAndAttributes.getAttribute("class"));

		XmlElement xmlElementWithNameAndData = new XmlElement("root", "data");
		assertNotNull(xmlElementWithNameAndData);
		assertEquals("root", xmlElementWithNameAndData.getName());
		assertEquals("data", xmlElementWithNameAndData.getData());
	}

	@Test
	public void testAddAttribute() {
		XmlElement xmlElement = new XmlElement();
		xmlElement.addAttribute("id", "1");
		xmlElement.addAttribute("class", "element");

		assertEquals("1", xmlElement.getAttribute("id"));
		assertEquals("element", xmlElement.getAttribute("class"));
		assertEquals(null, xmlElement.getAttribute("name"));
	}

	@Test
	public void testGetAttribute() {
		XmlElement xmlElement = new XmlElement();
		xmlElement.addAttribute("id", "1");
		xmlElement.addAttribute("class", "element");

		assertEquals("1", xmlElement.getAttribute("id"));
		assertEquals("element", xmlElement.getAttribute("class"));
		assertEquals(null, xmlElement.getAttribute("name"));
		assertEquals("1", xmlElement.getAttribute("id", "defaultValue"));

		xmlElement.addAttribute("name", "test");
		assertEquals("test", xmlElement.getAttribute("name"));
	}

	@Test
	public void testGetAttributes() {
		XmlElement xmlElement = new XmlElement();
		xmlElement.addAttribute("id", "1");
		xmlElement.addAttribute("class", "element");
		xmlElement.addAttribute("name", "test");

		Hashtable<String, String> attributes = xmlElement.getAttributes();
		assertEquals("1", attributes.get("id"));
		assertEquals("element", attributes.get("class"));
		assertEquals("test", attributes.get("name"));
	}

	@Test
	public void testSetAttributes() {
		XmlElement xmlElement = new XmlElement();
		Hashtable<String, String> attributes = new Hashtable<String, String>() {{
			put("id", "1");
			put("class", "element");
		}};
		xmlElement.setAttributes(attributes);

		assertEquals("1", xmlElement.getAttribute("id"));
		assertEquals("element", xmlElement.getAttribute("class"));
		assertEquals(null, xmlElement.getAttribute("name"));
	}

	@Test
	public void testGetAttributeNames() {
		XmlElement xmlElement = new XmlElement();
		xmlElement.addAttribute("id", "1");
		xmlElement.addAttribute("class", "element");
		xmlElement.addAttribute("name", "test");

		Enumeration<String> attributeNames = xmlElement.getAttributeNames();
		ArrayList<String> attributeNamesList = new ArrayList<>();
		while (attributeNames.hasMoreElements()) {
			attributeNamesList.add((String) attributeNames.nextElement());
		}

		assertEquals(3, attributeNamesList.size());
		assertTrue(attributeNamesList.contains("id"));
		assertTrue(attributeNamesList.contains("class"));
		assertTrue(attributeNamesList.contains("name"));
	}

	@Test
	public void testAddElement() {
		XmlElement parent = new XmlElement();
		XmlElement child = new XmlElement("child");
		parent.addElement(child);

		assertEquals(1, parent.count());
		assertEquals(child, parent.getElement(0));
		assertEquals(parent, child.getParent());
	}

	@Test
	public void testRemoveElement() {
		XmlElement parent = new XmlElement();
		XmlElement child = new XmlElement("child");
		parent.addElement(child);

		parent.removeElement(child);
		assertEquals(0, parent.count());
		assertNull(parent.getElement(0));
		assertNull(child.getParent());
	}

	@Test
	public void testRemoveAllElements() {
		XmlElement parent = new XmlElement();
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		parent.addElement(child1);
		parent.addElement(child2);

		parent.removeAllElements();
		assertEquals(0, parent.count());
		assertNull(parent.getElement(0));
		assertNull(child1.getParent());
		assertNull(child2.getParent());
	}

	@Test
	public void testRemoveFromParent() {
		XmlElement parent = new XmlElement();
		XmlElement child = new XmlElement("child");
		parent.addElement(child);

		child.removeFromParent();
		assertEquals(0, parent.count());
		assertNull(parent.getElement(0));
		assertNull(child.getParent());
	}

	@Test
	public void testClone() {
		XmlElement parent = new XmlElement();
		XmlElement child = new XmlElement("child");
		parent.addElement(child);

		XmlElement clonedParent = (XmlElement) parent.clone();
		XmlElement clonedChild = (XmlElement) child.clone();

		assertEquals(1, clonedParent.count());
		assertEquals(clonedChild, clonedParent.getElement(0));
		assertEquals(clonedParent, clonedChild.getParent());
	}

	@Test(expected = CloneNotSupportedException.class)
	public void testCloneException() {
		XmlElement xmlElement = new XmlElement();
		xmlElement.clone();
	}

	@Test
	public void testEquals() {
		XmlElement xmlElement1 = new XmlElement();
		XmlElement xmlElement2 = new XmlElement();
		XmlElement xmlElement3 = new XmlElement();
		xmlElement1.addAttribute("id", "1");
		xmlElement1.addAttribute("class", "element");
		xmlElement1.addAttribute("name", "test");
		xmlElement1.addElement(new XmlElement("child1"));
		xmlElement1.addElement(new XmlElement("child2"));
		xmlElement2.setAttributes(xmlElement1.getAttributes());
		xmlElement2.subElements = new Vector<>(xmlElement1.subElements);

		assertTrue(xmlElement1.equals(xmlElement2));
		assertTrue(xmlElement1.hashCode() == xmlElement2.hashCode());

		XmlElement xmlElement4 = new XmlElement();
		xmlElement4.addAttribute("id", "2");
		xmlElement4.addAttribute("class", "element");
		xmlElement4.addAttribute("name", "test");
		xmlElement4.addElement(new XmlElement("child1"));
		xmlElement4.addElement(new XmlElement("child2"));

		assertFalse(xmlElement1.equals(xmlElement4));
		assertFalse(xmlElement1.hashCode() == xmlElement4.hashCode());

		XmlElement xmlElement5 = new XmlElement();
		assertFalse(xmlElement1.equals(xmlElement5));
		assertFalse(xmlElement1.hashCode() == xmlElement5.hashCode());

		XmlElement xmlElement6 = new XmlElement();
		xmlElement6.addAttribute("id", null);
		xmlElement6.addAttribute("class", null);
		xmlElement6.addAttribute("name", null);
		xmlElement6.addElement(new XmlElement("child1"));
		xmlElement6.addElement(new XmlElement("child2"));

		assertFalse(xmlElement1.equals(xmlElement6));
		assertFalse(xmlElement1.hashCode() == xmlElement6.hashCode());
	}
}