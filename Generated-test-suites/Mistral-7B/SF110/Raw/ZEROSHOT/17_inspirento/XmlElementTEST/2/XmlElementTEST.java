import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement();
 XmlElement element2 = new XmlElement("elementName");
 XmlElement element3 = new XmlElement("elementName", new Hashtable<String, String>());
 XmlElement element4 = new XmlElement("elementName", "data");

 assertEquals("", element1.getData());
 assertEquals("elementName", element2.getName());
 assertEquals(10, element3.getAttributes().capacity());
 assertEquals("data", element4.getData());
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement();
 element.addAttribute("attributeName", "attributeValue");
 assertEquals("attributeValue", element.getAttribute("attributeName"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 XmlElement element = new XmlElement();
 element.addAttribute(null, "attributeValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 XmlElement element = new XmlElement();
 element.addAttribute("attributeName", null);
 }

 @Test
 public void testGetAttribute() {
 XmlElement element = new XmlElement();
 element.addAttribute("attributeName", "attributeValue");
 assertEquals("attributeValue", element.getAttribute("attributeName"));
 assertEquals(null, element.getAttribute("nonExistentAttributeName"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement element = new XmlElement();
 assertEquals("defaultValue", element.getAttribute("nonExistentAttributeName", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement element = new XmlElement();
 element.addAttribute("attributeName1", "attributeValue1");
 element.addAttribute("attributeName2", "attributeValue2");
 Hashtable<String, String> attributes = element.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("attributeValue1", attributes.get("attributeName1"));
 assertEquals("attributeValue2", attributes.get("attributeName2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement element = new XmlElement();
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("attributeName1", "attributeValue1");
 attributes.put("attributeName2", "attributeValue2");
 element.setAttributes(attributes);
 assertEquals(2, element.getAttributes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testSetAttributesNullAttributes() {
 XmlElement element = new XmlElement();
 element.setAttributes(null);
 }

 @Test
 public void testAttributesEnumeration() {
 XmlElement element = new XmlElement();
 element.addAttribute("attributeName1", "attributeValue1");
 element.addAttribute("attributeName2", "attributeValue2");
 String[] attributes = element.getAttributeNames().toArray(new String[0]);
 assertEquals(2, attributes.length);
 assertEquals("attributeName1", attributes[0]);
 assertEquals("attributeName2", attributes[1]);
 }

 @Test
 public void testAddElement() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 assertTrue(parent.getElements().contains(child));
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullChild() {
 XmlElement parent = new XmlElement();
 try {
 parent.addElement(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testRemoveElement() {
 XmlElement parent = new XmlElement();
 XmlElement child = new XmlElement("child");
 parent.addElement(child);
 parent.removeElement(child);
 assertFalse(parent.getElements().contains(child));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementInvalidIndex() {
 XmlElement parent = new XmlElement();
 parent.addElement(new XmlElement("child1"));
 parent.addElement(new XmlElement("child2"));
 parent.removeElement(10);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parent = new XmlElement();
 parent.addElement(new XmlElement("child1"));
 parent.addElement(new XmlElement("child2"));
 parent.removeAllElements();
 assertTrue(parent.getElements().isEmpty());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 XmlElement parent = new XmlElement();
 parent.addElement(child);
 child.removeFromParent();
 assertFalse(parent.getElements().contains(child));
 assertNull(child.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.append(child1);
 parent.append(child2);
 assertTrue(parent.getElements().containsAll(Arrays.asList(child1, child2)));
 }

 @Test
 public void testInsertElement() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.insertElement(child1, 0);
 parent.insertElement(child2, 1);
 assertTrue(parent.getElements().containsAll(Arrays.asList(child1, child2)));
 }

 @Test
 public void testGetElements() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 List<XmlElement> elements = parent.getElements();
 assertEquals(2, elements.size());
 assertTrue(elements.contains(child1));
 assertTrue(elements.contains(child2));
 }

 @Test
 public void testCount() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(2, parent.count());
 }

 @Test
 public void testGetElement() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(child1, parent.getElement("child1"));
 assertEquals(child2, parent.getElement("child2"));
 assertEquals(null, parent.getElement("nonExistentChild"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetElementInvalidPath() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 parent.addElement(child1);
 assertEquals(null, parent.getElement("nonExistentPath"));
 }

 @Test
 public void testGetElementByIndex() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 parent.addElement(child1);
 parent.addElement(child2);
 assertEquals(child1, parent.getElement(0));
 assertEquals(child2, parent.getElement(1));
 }

 @Test
 public void testAddSubElement() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = parent.addSubElement("child1");
 XmlElement child2 = parent.addSubElement("child2/grandchild");
 XmlElement child3 = parent.addSubElement("child3");
 assertEquals(3, parent.count());
 assertEquals("child1", child1.getName());
 assertEquals("grandchild", child2.getName());
 assertEquals("child3", child3.getName());
 }

 @Test
 public void testAddSubElementWithExistingChild() {
 XmlElement parent = new XmlElement();
 XmlElement child1 = parent.addSubElement("child1");
 XmlElement child2 = parent.addSubElement("child1/grandchild");
 assertEquals(2, parent.count());
 XmlElement newChild = parent.addSubElement("child1/grandchild");
 assertEquals(3, parent.count());
 assertEquals("grandchild", newChild.getName());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("root");
 XmlElement cloned = (XmlElement)original.clone();
 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getData(), cloned.getData());
 assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
 assertEquals(original.getElements().size(), cloned.getElements().size());
 }

 @Test
 public void testEquals() {
 XmlElement xmlElement1 = new XmlElement("root");
 XmlElement xmlElement2 = new XmlElement("root");
 XmlElement xmlElement3 = new XmlElement("root1");
 assertTrue(xmlElement1.equals(xmlElement1));
 assertTrue(xmlElement1.equals(xmlElement2));
 assertFalse(xmlElement1.equals(xmlElement3));
 }

 @Test
 public void testHashCode() {
 XmlElement xmlElement1 = new XmlElement("root");
 XmlElement xmlElement2 = new XmlElement("root");
 XmlElement xmlElement3 = new XmlElement("root1");
 int hashcode1 = xmlElement1.hashCode();
 int hashcode2 = xmlElement2.hashCode();
 int hashcode3 = xmlElement3.hashCode();
 assertEquals(hashcode1, hashcode2);
 assertNotEquals(hashcode1, hashcode3);
 }
}