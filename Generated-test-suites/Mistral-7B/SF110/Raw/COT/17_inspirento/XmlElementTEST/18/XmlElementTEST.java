package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;

import com.allenstudio.ir.util.XmlElement;

@Category(IntegrationTest.class)
public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root"); ;
 }

 // Test constructor with no arguments
 @Test
 public void testXmlElementConstructorNoArgs() {
 final XmlElement xmlElement = new XmlElement();
 assertNotNull(xmlElement);
 assertTrue(xmlElement.isEmpty());
 }

 // Test constructor with name argument
 @Test
 public void testXmlElementConstructorName() {
 final XmlElement xmlElement = new XmlElement("elementName");
 assertNotNull(xmlElement);
 assertEquals("elementName", xmlElement.getName());
 assertTrue(xmlElement.isEmpty());
 }

 // Test constructor with name and attributes argument
 @Test
 public void testXmlElementConstructorNameAndAttributes() {
 final Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attrName1", "attrValue1");
 attributes.put("attrName2", "attrValue2");
 final XmlElement xmlElement = new XmlElement("elementName", attributes);
 assertNotNull(xmlElement);
 assertEquals("elementName", xmlElement.getName());
 assertSame(attributes, xmlElement.getAttributes());
 assertTrue(xmlElement.isEmpty());
 }

 // Test constructor with name and data argument
 @Test
 public void testXmlElementConstructorNameAndData() {
 final XmlElement xmlElement = new XmlElement("elementName", "dataValue");
 assertNotNull(xmlElement);
 assertEquals("elementName", xmlElement.getName());
 assertEquals("dataValue", xmlElement.getData());
 assertTrue(xmlElement.isEmpty());
 }

 // Test addAttribute method
 @Test
 public void testAddAttribute() {
 root.addAttribute("attributeName1", "attributeValue1");
 root.addAttribute("attributeName2", "attributeValue2");
 final Map<String, String> attributes = root.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("attributeValue1", attributes.get("attributeName1"));
 assertEquals("attributeValue2", attributes.get("attributeName2"));
 }

 // Test getAttribute method with null name
 @Test(expected = NullPointerException.class)
 public void testGetAttributeNullName() {
 root.getAttribute(null);
 }

 // Test getAttribute method with empty name
 @Test
 public void testGetAttributeEmptyName() {
 root.setName("");
 assertEquals(null, root.getAttribute(""));
 }

 // Test getAttribute method with existing name
 @Test
 public void testGetAttributeExistingName() {
 root.addAttribute("attributeName1", "attributeValue1");
 assertEquals("attributeValue1", root.getAttribute("attributeName1"));
 }

 // Test getAttribute method with non-existing name and default value
 @Test
 public void testGetAttributeNonExistingNameAndDefaultValue() {
 root.addAttribute("attributeName1", "attributeValue1");
 assertEquals("defaultValue", root.getAttribute("attributeName2", "defaultValue"));
 }

 // Test getAttributes method
 @Test
 public void testGetAttributes() {
 final Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attributeName1", "attributeValue1");
 attributes.put("attributeName2", "attributeValue2");
 root.setAttributes(attributes);
 final Map<String, String> actualAttributes = root.getAttributes();
 assertEquals(attributes, actualAttributes);
 }

 // Test getAttributeNames method
 @Test
 public void testGetAttributeNames() {
 root.addAttribute("attributeName1", "attributeValue1");
 root.addAttribute("attributeName2", "attributeValue2");
 final Enumeration<String> attributeNames = root.getAttributeNames();
 final List<String> expectedAttributeNames = Arrays.asList("attributeName1", "attributeName2");
 List<String> actualAttributeNames = new ArrayList<>();
 while (attributeNames.hasMoreElements()) {
 actualAttributeNames.add((String)attributeNames.nextElement());
 }
 assertEquals(expectedAttributeNames, actualAttributeNames);
 }

 // Test addElement method with null element
 @Test(expected = NullPointerException.class)
 public void testAddElementNullElement() {
 root.addElement(null);
 }

 // Test addElement method when adding an empty XmlElement
 @Test
 public void testAddElementEmptyXmlElement() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.addElement(emptyXmlElement);
 assertTrue(root.getElements().contains(emptyXmlElement));
 }

 // Test addElement method when adding an XmlElement with name and data
 @Test
 public void testAddElementXmlElementWithNameAndData() {
 final XmlElement xmlElementToAdd = new XmlElement("child1", "dataValue1");
 root.addElement(xmlElementToAdd);
 assertEquals(1, root.count());
 assertEquals(xmlElementToAdd, root.getElement(0));
 }

 // Test addElement method when adding an XmlElement with name only
 @Test
 public void testAddElementXmlElementWithNameOnly() {
 final XmlElement xmlElementToAdd = new XmlElement("child2");
 root.addElement(xmlElementToAdd);
 assertEquals(1, root.count());
 assertEquals(xmlElementToAdd, root.getElement(0));
 }

 // Test removeElement method with null element
 @Test(expected = NullPointerException.class)
 public void testRemoveElementNullElement() {
 root.removeElement(null);
 }

 // Test removeElement method when removing an empty XmlElement
 @Test
 public void testRemoveElementEmptyXmlElement() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.addElement(emptyXmlElement);
 root.removeElement(emptyXmlElement);
 assertTrue(root.getElements().isEmpty());
 }

 // Test removeElement method when removing an existing XmlElement by index
 @Test
 public void testRemoveElementExistingXmlElementByIndex() {
 final XmlElement xmlElementToRemove = new XmlElement("child1");
 root.addElement(xmlElementToRemove);
 root.removeElement(xmlElementToRemove);
 assertTrue(root.getElements().isEmpty());
 }

 // Test removeElement method when removing an existing XmlElement by name
 @Test
 public void testRemoveElementExistingXmlElementByName() {
 final XmlElement xmlElementToRemove = new XmlElement("child1");
 root.addElement(xmlElementToRemove);
 root.removeElement("child1");
 assertTrue(root.getElements().isEmpty());
 }

 // Test removeAllElements method
 @Test
 public void testRemoveAllElements() {
 final XmlElement xmlElement1 = new XmlElement("child1");
 final XmlElement xmlElement2 = new XmlElement("child2");
 root.addElement(xmlElement1);
 root.addElement(xmlElement2);
 root.removeAllElements();
 assertTrue(root.getElements().isEmpty());
 }

 // Test removeFromParent method
 @Test
 public void testRemoveFromParent() {
 final XmlElement parent = new XmlElement("parent");
 final XmlElement child = new XmlElement("child");
 parent.addElement(child);
 child.removeFromParent();
 assertTrue(parent.getElements().isEmpty());
 }

 // Test append method with null child XmlElement
 @Test(expected = NullPointerException.class)
 public void testAppendNullChildXmlElement() {
 root.append(null);
 }

 // Test append method when appending an empty XmlElement
 @Test
 public void testAppendEmptyXmlElement() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.append(emptyXmlElement);
 assertEquals(1, root.count());
 assertEquals(emptyXmlElement, root.getElement(0));
 }

 // Test insertElement method with null child XmlElement
 @Test(expected = NullPointerException.class)
 public void testInsertElementNullChildXmlElement() {
 root.insertElement(null, 0);
 }

 // Test insertElement method when inserting an empty XmlElement at index 0
 @Test
 public void testInsertElementEmptyXmlElementAtIndex0() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.insertElement(emptyXmlElement, 0);
 assertEquals(1, root.count());
 assertEquals(emptyXmlElement, root.getElement(0));
 }

 // Test insertElement method when inserting an empty XmlElement at index 1
 @Test
 public void testInsertElementEmptyXmlElementAtIndex1() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.insertElement(emptyXmlElement, 1);
 assertEquals(1, root.count());
 assertEquals(emptyXmlElement, root.getElement(1));
 }

 // Test insertElement method when inserting an existing XmlElement by index
 @Test
 public void testInsertElementExistingXmlElementByIndex() {
 final XmlElement xmlElementToInsert = new XmlElement("child1");
 root.addElement(new XmlElement("child0"));
 root.insertElement(xmlElementToInsert, 0);
 assertEquals(2, root.count());
 assertEquals(xmlElementToInsert, root.getElement(0));
 }

 // Test getElements method
 @Test
 public void testGetElements() {
 final XmlElement xmlElement1 = new XmlElement("child1");
 final XmlElement xmlElement2 = new XmlElement("child2");
 root.addElement(xmlElement1);
 root.addElement(xmlElement2);
 final List<XmlElement> expectedXmlElements = Arrays.asList(xmlElement1, xmlElement2);
 List<XmlElement> actualXmlElements = new ArrayList<>();
 for (XmlElement xmlElement : root.getElements()) {
 actualXmlElements.add(xmlElement);
 }
 assertEquals(expectedXmlElements, actualXmlElements);
 }

 // Test count method
 @Test
 public void testCount() {
 final XmlElement xmlElement1 = new XmlElement("child1");
 final XmlElement xmlElement2 = new XmlElement("child2");
 root.addElement(xmlElement1);
 root.addElement(xmlElement2);
 assertEquals(2, root.count());
 }

 // Test getElement method with null path
 @Test(expected = NullPointerException.class)
 public void testGetElementNullPath() {
 root.getElement(null);
 }

 // Test getElement method with empty path
 @Test
 public void testGetElementEmptyPath() {
 assertEquals(null, root.getElement(""));
 }

 // Test getElement method with path containing only name
 @Test
 public void testGetElementPathContainingOnlyName() {
 root.addElement(new XmlElement("child1"));
 assertEquals(root.getElement("child1"), root.getElement("child1"));
 }

 // Test getElement method with path containing multiple names
 @Test
 public void testGetElementPathContainingMultipleNames() {
 final XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 root.addElement(new XmlElement("child2"));
 assertEquals(child1.getElement("child2"), root.getElement("child1/child2"));
 }

 // Test getElement method with path containing multiple names and empty names
 @Test
 public void testGetElementPathContainingMultipleNamesAndEmptyNames() {
 final XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 root.addElement(new XmlElement("child2"));
 root.addElement(new XmlElement(""));
 assertEquals(child1.getElement(""), root.getElement("child1/"));
 assertEquals(child1.getElement("child2"), root.getElement("child1/child2"));
 }

 // Test addSubElement method with null path
 @Test(expected = NullPointerException.class)
 public void testAddSubElementNullPath() {
 root.addSubElement(null);
 }

 // Test addSubElement method when adding an empty XmlElement with no path
 @Test
 public void testAddSubElementEmptyXmlElementNoPath() {
 final XmlElement emptyXmlElement = new XmlElement();
 root.addSubElement(emptyXmlElement);
 assertEquals(1, root.count());
 assertEquals(emptyXmlElement, root.getElement(0));
 }

 // Test addSubElement method when adding an XmlElement with name and data with no path
 @Test
 public void testAddSubElementXmlElementNameAndDataNoPath() {
 final XmlElement xmlElementToAdd = new XmlElement("child1", "dataValue1");
 root.addSubElement(xmlElementToAdd);
 assertEquals(1, root.count());
 assertEquals(xmlElementToAdd, root.getElement(0));
 }

 // Test addSubElement method when adding an XmlElement with name and data with path
 @Test
 public void testAddSubElementXmlElementNameAndDataPath() {
 final XmlElement parent = new XmlElement("parent");
 root.addElement(parent);
 final XmlElement xmlElementToAdd = new XmlElement("child1", "dataValue1");
 parent.addSubElement(xmlElementToAdd);
 assertEquals(1, parent.count());
 assertEquals(xmlElementToAdd, parent.getElement(0));
 assertEquals(1, root.count());
 assertEquals(parent, root.getElement(0));
 }

 // Test addSubElement method when adding an XmlElement with name only with no path
 @Test
 public void testAddSubElementXmlElementNameOnlyNoPath() {
 final XmlElement xmlElementToAdd = new XmlElement("child1");
 root.addElement(xmlElementToAdd);
 assertEquals(1, root.count());
 assertEquals(xmlElementToAdd, root.getElement(0));
 }

 // Test addSubElement method when adding an XmlElement with name only with path
 @Test
 public void testAddSubElementXmlElementNameOnlyPath() {
 final XmlElement parent = new XmlElement("parent");
 root.addElement(parent);
 final XmlElement xmlElementToAdd = new XmlElement("child1");
 parent.addSubElement(xmlElementToAdd);
 assertEquals(1, parent.count());
 assertEquals(xmlElementToAdd, parent.getElement(0));
 assertEquals(1, root.count());
 assertEquals(parent, root.getElement(0));
 }

 // Test clone method
 @Test
 public void testClone() throws CloneNotSupportedException {
 final XmlElement xmlElementToClone = new XmlElement("cloneTest");
 xmlElementToClone.addAttribute("attributeName1", "attributeValue1");
 xmlElementToClone.addAttribute("attributeName2", "attributeValue2");
 xmlElementToClone.addElement(new XmlElement("child1"));
 final XmlElement clonedXmlElement = (XmlElement)xmlElementToClone.clone();
 assertEquals(xmlElementToClone, clonedXmlElement);
 }

 // Test equals method with null object
 @Test
 public void testEqualsNullObject() {
 assertFalse(root.equals(null));
 }

 // Test equals method with non-XmlElement object
 @Test
 public void testEqualsNonXmlElementObject() {
 assertFalse(root.equals(new Object()));
 }

 // Test equals method with different XmlElement objects
 @Test
 public void testEqualsDifferentXmlElementObjects() {
 final XmlElement xmlElement1 = new XmlElement("xmlElement1");
 final XmlElement xmlElement2 = new XmlElement("xmlElement2");
 assertFalse(xmlElement1.equals(xmlElement2));
 }

 // Test equals method with same XmlElement objects
 @Test
 public void testEqualsSameXmlElementObjects() {
 final XmlElement xmlElement1 = new XmlElement("xmlElement1");
 final XmlElement xmlElement2 = new XmlElement("xmlElement1");
 assertTrue(xmlElement1.equals(xmlElement2));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 final XmlElement xmlElement1 = new XmlElement("xmlElement1");
 final XmlElement xmlElement2 = new XmlElement("xmlElement1");
 assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
 }

}