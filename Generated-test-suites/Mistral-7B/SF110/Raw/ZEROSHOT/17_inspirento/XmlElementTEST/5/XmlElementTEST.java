import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setup() {
 root = new XmlElement("root");;
 }

 @Test
 public void testConstructor() {
 XmlElement xmlElement = new XmlElement();
 assertEquals("", xmlElement.getName());
 assertEquals(0, xmlElement.count());

 xmlElement = new XmlElement("elementName");
 assertEquals("elementName", xmlElement.getName());
 assertEquals(0, xmlElement.count());
 }

 @Test
 public void testAddAttribute() {
 XmlElement xmlElement = new XmlElement("elementName");
 xmlElement.addAttribute("attributeName1", "attributeValue1");
 xmlElement.addAttribute("attributeName2", "attributeValue2");

 Map<String, String> attributes = xmlElement.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("attributeValue1", attributes.get("attributeName1"));
 assertEquals("attributeValue2", attributes.get("attributeName2"));
 }

 @Test
 public void testGetAttribute() {
 XmlElement xmlElement = new XmlElement("elementName");
 xmlElement.addAttribute("attributeName", "attributeValue");

 assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
 assertEquals(null, xmlElement.getAttribute("nonExistentAttributeName"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement xmlElement = new XmlElement("elementName");
 xmlElement.addAttribute("attributeName", "attributeValue");

 assertEquals("attributeValue", xmlElement.getAttribute("attributeName", "defaultValue"));
 assertEquals("defaultValue", xmlElement.getAttribute("nonExistentAttributeName", "defaultValue"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement xmlElement = new XmlElement("elementName");
 Map<String, String> attributes = new HashMap<>();
 attributes.put("attributeName1", "attributeValue1");
 attributes.put("attributeName2", "attributeValue2");
 xmlElement.setAttributes(attributes);

 assertEquals(2, xmlElement.countAttributes());
 assertEquals("attributeValue1", xmlElement.getAttribute("attributeName1"));
 assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement xmlElement = new XmlElement("elementName");
 xmlElement.addAttribute("attributeName1", "attributeValue1");
 xmlElement.addAttribute("attributeName2", "attributeValue2");

 List<String> attributeNames = new ArrayList<>(xmlElement.getAttributeNames());
 assertEquals(2, attributeNames.size());
 assertEquals("attributeName1", attributeNames.get(0));
 assertEquals("attributeName2", attributeNames.get(1));
 }

 @Test
 public void testAddElement() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");

 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);

 assertEquals(2, parentElement.count());
 assertTrue(parentElement.contains(childElement1));
 assertTrue(parentElement.contains(childElement2));
 }

 @Test
 public void testRemoveElement() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");

 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 parentElement.removeElement(childElement1);

 assertEquals(1, parentElement.count());
 assertFalse(parentElement.contains(childElement1));
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");

 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 parentElement.removeAllElements();

 assertEquals(0, parentElement.count());
 }

 @Test
 public void testAddSubElement() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement = new XmlElement("childElement");

 parentElement.addSubElement(childElement);
 assertEquals(1, parentElement.count());
 assertEquals(childElement, parentElement.getElement("childElement"));
 }

 @Test
 public void testAddSubElementWithPath() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 XmlElement childElement3 = new XmlElement("childElement3");

 parentElement.addElement(childElement1);
 childElement1.addElement(childElement2);
 childElement2.addElement(childElement3);

 parentElement.addSubElement("childElement1/childElement2/childElement3");
 assertEquals(1, parentElement.count());
 assertEquals(childElement3, parentElement.getElement("childElement1/childElement2/childElement3"));
 }

 @Test
 public void testClone() {
 XmlElement parentElement = new XmlElement("parentElement");
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");

 parentElement.addElement(childElement1);
 parentElement.addElement(childElement2);
 XmlElement clonedParentElement = (XmlElement)parentElement.clone();

 assertEquals(parentElement.count(), clonedParentElement.count());
 for (XmlElement child : parentElement.getElements()) {
 assertEquals(child, clonedParentElement.getElement(child.getName()));
 }
 }

 @Test
 public void testEquals() {
 XmlElement xmlElement1 = new XmlElement("elementName");
 XmlElement xmlElement2 = new XmlElement("elementName");
 XmlElement xmlElement3 = new XmlElement("differentName");

 xmlElement1.addAttribute("attributeName", "attributeValue");
 xmlElement2.addAttribute("attributeName", "attributeValue");
 xmlElement1.addElement(new XmlElement("childElement"));
 xmlElement2.addElement(new XmlElement("childElement"));

 assertEquals(xmlElement1, xmlElement1);
 assertEquals(xmlElement1, xmlElement1);
 assertNotSame(xmlElement1, xmlElement2);
 assertNotSame(xmlElement1, xmlElement3);
 }

 @Test
 public void testHashCode() {
 XmlElement xmlElement1 = new XmlElement("elementName");
 XmlElement xmlElement2 = new XmlElement("elementName");
 XmlElement xmlElement3 = new XmlElement("differentName");

 xmlElement1.addAttribute("attributeName", "attributeValue");
 xmlElement1.addElement(new XmlElement("childElement"));
 xmlElement2.addAttribute("attributeName", "attributeValue");
 xmlElement2.addElement(new XmlElement("childElement"));

 int hashCode1 = xmlElement1.hashCode();
 int hashCode2 = xmlElement2.hashCode();
 int hashCode3 = xmlElement3.hashCode();

 assertEquals(hashCode1, hashCode1);
 assertEquals(hashCode1, xmlElement1.hashCode());
 assertEquals(hashCode2, hashCode2);
 assertEquals(hashCode2, xmlElement2.hashCode());
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode2, hashCode3);
 }
}