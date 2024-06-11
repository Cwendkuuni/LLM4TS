import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setup() {
 root = new XmlElement("root");;
 }

 @Test
 public void testConstructorWithName() {
 XmlElement element = new XmlElement("elementName");
 assertEquals("elementName", element.getName());
 }

 @Test
 public void testConstructorWithNameAndAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 XmlElement element = new XmlElement("elementName", attributes);
 assertEquals("elementName", element.getName());
 assertEquals("value1", element.getAttribute("attribute1"));
 assertEquals("value2", element.getAttribute("attribute2"));
 }

 @Test
 public void testConstructorWithNameAndData() {
 XmlElement element = new XmlElement("elementName", "data");
 assertEquals("elementName", element.getName());
 assertEquals("data", element.getData());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attribute1", "value1");
 assertEquals("value1", root.getAttribute("attribute1"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "value1");
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("attribute1", null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetAttributeNullName() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttributeDefaultValue() {
 root.addAttribute("attribute1", "defaultValue");
 assertEquals("defaultValue", root.getAttribute("attribute1", "defaultValue2"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> newAttributes = new Hashtable<String, String>(Arrays.asList("attribute1", "value1").toArray(new String[0]), Arrays.asList("attribute2", "value2").toArray(new String[0]));
 root.setAttributes(newAttributes);
 assertEquals("value1", root.getAttribute("attribute1"));
 assertEquals("value2", root.getAttribute("attribute2"));
 }

 @Test
 public void testAttributeNames() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 Enumeration<String> attributeNames = root.getAttributeNames();
 assertEquals("attribute1", attributeNames.nextElement());
 assertEquals("attribute2", attributeNames.nextElement());
 }

 @Test
 public void testAddElement() {
 XmlElement childElement = new XmlElement("childElement");
 root.addElement(childElement);
 assertEquals(1, root.count());
 assertEquals(childElement, root.getElement(0));
 }

 @Test
 public void testAddElementNullElement() {
 XmlElement nullElement = null;
 root.addElement(nullElement);
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveElement() {
 root.addElement(new XmlElement("childElement1"));
 root.addElement(new XmlElement("childElement2"));
 XmlElement childElementToRemove = root.getElement(0);
 root.removeElement(childElementToRemove);
 assertEquals(1, root.count());
 assertNotEquals(childElementToRemove, root.getElement(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 root.removeElement(10);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testRemoveAllElements() {
 root.addElement(new XmlElement("childElement1"));
 root.addElement(new XmlElement("childElement2"));
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement childElement = new XmlElement("childElement");
 root.addElement(childElement);
 childElement.removeFromParent();
 assertEquals(0, root.count());
 }

 @Test
 public void testGetElementPath() {
 root.addElement(new XmlElement("child1"));
 root.getElement("child1").addElement(new XmlElement("child2"));
 XmlElement result = root.getElement("child1/child2");
 assertEquals("child2", result.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementInvalidPath() {
 root.getElementById("child1/nonExistentChild");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAddSubElement() {
 XmlElement childElement = root.addSubElement("childElement");
 assertEquals("childElement", childElement.getName());
 assertEquals(1, root.count());
 }

 @Test
 public void testAddSubElementNullName() {
 try {
 root.addSubElement(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 //expected
 }
 }

 @Test
 public void testAddSubElementNullData() {
 try {
 root.addSubElement("", "");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testAddSubElementWithData() {
 XmlElement childElement = root.addSubElement("childElement", "data");
 assertEquals("childElement", childElement.getName());
 assertEquals("data", childElement.getData());
 assertEquals(1, root.count());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 original.addAttribute("attribute1", "value1");
 original.addAttribute("attribute2", "value2");
 original.addElement(new XmlElement("child1"));
 XmlElement clone = (XmlElement)original.clone();
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 assertEquals(original.count(), clone.count());
 assertEquals(original.getElement(0).getName(), clone.getElement(0).getName());
 assertEquals(original.getAttribute("attribute1"), clone.getAttribute("attribute1"));
 assertEquals(original.getAttribute("attribute2"), clone.getAttribute("attribute2"));
 }

 @Test
 public void testSetParent() {
 XmlElement parent = new XmlElement("parent");
 XmlElement child = new XmlElement("child");
 child.setParent(parent);
 assertEquals(parent, child.getParent());
 }

 @Test
 public void testNotifyObservers() {
 XmlElement observer = new XmlElement() {
 @Override
 public void update(XmlElement observable) {
 assertEquals(root, observable);
 }
 };
 root.addObserver(observer);
 root.setName("newName");
 observer.update(root);
 }
}