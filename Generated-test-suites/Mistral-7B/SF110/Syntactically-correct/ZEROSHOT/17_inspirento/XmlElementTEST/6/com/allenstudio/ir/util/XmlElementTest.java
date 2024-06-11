import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {

 private XmlElement rootElement;

 @Before
 public void setup() {
 rootElement = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2", new Hashtable<String, String>());
 XmlElement element3 = new XmlElement("element3", "data");

 assertEquals("element1", element1.getName());
 assertEquals(0, element1.count());
 assertEquals(null, element1.getData());
 assertEquals(0, element1.size());

 assertEquals("element2", element2.getName());
 assertEquals(0, element2.count());
 assertEquals(null, element2.getData());
 assertEquals(0, element2.size());

 assertEquals("element3", element3.getName());
 assertEquals(0, element3.count());
 assertEquals("data", element3.getData());
 assertEquals(0, element3.size());
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", "value1");
 assertEquals("value1", element.getAttribute("attr1"));
 }

 @Test
 public void testGetAttribute() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", "value1");
 assertEquals("value1", element.getAttribute("attr1"));
 assertEquals(null, element.getAttribute("attr2"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", "value1");
 element.addAttribute("attr2", "value2");
 Hashtable<String, String> attributes = element.getAttributes();
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 assertEquals(2, attributes.size());
 }

 @Test
 public void testSetAttributes() {
 XmlElement element = new XmlElement("element");
 Hashtable<String, String> attributes = new Hashtable<String, String>(2);
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 element.setAttributes(attributes);
 assertEquals("value1", element.getAttribute("attr1"));
 assertEquals("value2", element.getAttribute("attr2"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", "value1");
 element.addAttribute("attr2", "value2");
 Enumeration<String> attributeNames = element.getAttributeNames();
 List<String> attributeNamesList = new ArrayList<>();
 while (attributeNames.hasMoreElements()) {
 attributeNamesList.add((String) attributeNames.nextElement());
 }
 assertEquals(2, attributeNamesList.size());
 assertEquals("attr1", attributeNamesList.get(0));
 assertEquals("attr2", attributeNamesList.get(1));
 }

 @Test
 public void testAddElement() {
 XmlElement parentElement = new XmlElement("parent");
 XmlElement childElement1 = new XmlElement("child1");
 XmlElement childElement2 = new XmlElement("child2");
 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 assertEquals(2, parentElement.count());
 assertTrue(parentElement.contains(childElement1));
 assertTrue(parentElement.contains(childElement2));
 }

 @Test
 public void testRemoveElement() {
 XmlElement parentElement = new XmlElement("parent");
 XmlElement childElement1 = new XmlElement("child1");
 XmlElement childElement2 = new XmlElement("child2");
 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 parentElement.removeElement(childElement1);
 assertEquals(1, parentElement.count());
 assertFalse(parentElement.contains(childElement1));
 assertTrue(parentElement.contains(childElement2));
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parentElement = new XmlElement("parent");
 XmlElement childElement1 = new XmlElement("child1");
 XmlElement childElement2 = new XmlElement("child2");
 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 parentElement.removeAllElements();
 assertEquals(0, parentElement.count());
 }

 @Test
 public void testClone() {
 XmlElement originalElement = new XmlElement("original");
 XmlElement clonedElement = null;
 try {
 clonedElement = (XmlElement) originalElement.clone();
 } catch (CloneNotSupportedException e) {
 e.printStackTrace();
 }
 assertEquals(originalElement.getName(), clonedElement.getName());
 assertEquals(originalElement.getData(), clonedElement.getData());
 assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
 List<XmlElement> originalSubElements = originalElement.getElements();
 List<XmlElement> clonedSubElements = clonedElement.getElements();
 assertEquals(originalSubElements.size(), clonedSubElements.size());
 for (int i = 0; i < originalSubElements.size(); i++) {
 assertEquals(originalSubElements.get(i).getName(), clonedSubElements.get(i).getName());
 }
 }

 @Test
 public void testPrintNode() {
 XmlElement element = new XmlElement("element");
 element.addAttribute("attr1", "value1");
 element.addAttribute("attr2", "value2");
 element.addSubElement("subElement1");
 element.addSubElement("subElement2", "data");
 XmlElement subElement1 = new XmlElement("subElement1");
 XmlElement subElement2 = new XmlElement("subElement2", "data2");
 element.addElement(subElement1);
 element.addElement(subElement2);
 assertEquals(5, element.count());
 XmlElement.printNode(element, "");
 }
}