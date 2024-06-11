import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstructor1() {
 XmlElement element1 = new XmlElement();
 assertNotNull(element1);
 }

 @Test
 public void testXmlElementConstructor2() {
 XmlElement element2 = new XmlElement("elementName");
 assertNotNull(element2);
 assertEquals("elementName", element2.getName());
 }

 @Test
 public void testXmlElementConstructor3() {
 XmlElement element3 = new XmlElement("elementName", new Hashtable<>());
 assertNotNull(element3);
 assertEquals("elementName", element3.getName());
 assertNotNull(element3.getAttributes());
 }

 @Test
 public void testXmlElementConstructor4() {
 XmlElement element4 = new XmlElement("elementName", "data");
 assertNotNull(element4);
 assertEquals("elementName", element4.getName());
 assertEquals("data", element4.getData());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attributeName1", "attributeValue1");
 root.addAttribute("attributeName2", "attributeValue2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("attributeValue1", attributes.get("attributeName1"));
 assertEquals("attributeValue2", attributes.get("attributeName2"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attributeName1", "attributeValue1");
 assertEquals("attributeValue1", root.getAttribute("attributeName1"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 root.addAttribute("attributeName1", "attributeValue1");
 assertEquals("attributeValue1", root.getAttribute("attributeName1", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attributeName1", "attributeValue1");
 root.addAttribute("attributeName2", "attributeValue2");
 assertSame(root.getAttributes(), root.getAttributes());
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attributeName1", "attributeValue1");
 root.setAttributes(attributes);
 assertEquals(attributes, root.getAttributes());
 }

 @Test
 public void testAttributeNames() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attributeName1", "attributeValue1");
 attributes.put("attributeName2", "attributeValue2");
 root.setAttributes(attributes);
 Enumeration enumeration = root.getAttributeNames();
 List<String> attributeNames = new ArrayList<>();
 while (enumeration.hasMoreElements()) {
 attributeNames.add((String) enumeration.nextElement());
 }
 assertEquals(2, attributeNames.size());
 assertEquals("attributeName1", attributeNames.get(0));
 assertEquals("attributeName2", attributeNames.get(1));
 }

 @Test
 public void testAddElement() {
 XmlElement childElement1 = new XmlElement("childElement1");
 root.addElement(childElement1);
 List<XmlElement> elements = root.getElements();
 assertEquals(1, elements.size());
 assertSame(childElement1, elements.get(0));
 }

 @Test
 public void testRemoveElement() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.addElement(childElement1);
 root.addElement(childElement2);
 List<XmlElement> elementsBeforeRemoval = root.getElements();
 assertEquals(2, elementsBeforeRemoval.size());
 root.removeElement(childElement1);
 List<XmlElement> elementsAfterRemoval = root.getElements();
 assertEquals(1, elementsAfterRemoval.size());
 assertNull(elementsAfterRemoval.get(0));
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement childElement1 = new XmlElement("childElement1");
 root.addElement(childElement1);
 childElement1.removeFromParent();
 assertNull(childElement1.getParent());
 }

 @Test
 public void testAppend() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.append(childElement1);
 List<XmlElement> elementsBeforeAppend = root.getElements();
 assertEquals(1, elementsBeforeAppend.size());
 root.append(childElement2);
 List<XmlElement> elementsAfterAppend = root.getElements();
 assertEquals(2, elementsAfterAppend.size());
 assertSame(childElement1, elementsAfterAppend.get(0));
 assertSame(childElement2, elementsAfterAppend.get(1));
 }

 @Test
 public void testInsertElement() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.insertElement(childElement2, 0);
 List<XmlElement> elementsBeforeInsert = root.getElements();
 assertEquals(1, elementsBeforeInsert.size());
 List<XmlElement> elementsAfterInsert = root.getElements();
 assertEquals(2, elementsAfterInsert.size());
 assertSame(childElement2, elementsAfterInsert.get(0));
 assertSame(childElement1, elementsAfterInsert.get(1));
 }

 @Test
 public void testGetElement() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.addElement(childElement1);
 root.addElement(childElement2);
 assertEquals(childElement1, root.getElement("childElement1"));
 assertNull(root.getElement("childElement3"));
 }

 @Test
 public void testGetElementWithPath() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement2 = new XmlElement("childElement2");
 root.addElement(childElement1);
 childElement1.addElement("childElement3");
 assertEquals(childElement1.getElement("childElement3"), root.getElement("childElement1/childElement3"));
 }

 @Test
 public void testAddSubElement() {
 XmlElement childElement1 = new XmlElement("childElement1");
 XmlElement childElement3 = new XmlElement("childElement3");
 root.addSubElement(childElement1, "childElement2", "data");
 List<XmlElement> elementsBeforeAdd = root.getElements();
 assertEquals(1, elementsBeforeAdd.size());
 List<XmlElement> elementsAfterAdd = root.getElements();
 assertEquals(2, elementsAfterAdd.size());
 assertSame(childElement1, elementsAfterAdd.get(0));
 assertSame(childElement3, elementsAfterAdd.get(1));
 assertEquals("data", childElement3.getData());
 }

 @Test
 public void testClone() {
 XmlElement originalElement = new XmlElement("originalElement") {
 @Override
 public String toString() {
 return "OriginalElement";
 }
 };
 XmlElement clonedElement = (XmlElement)originalElement.clone();
 assertEquals("OriginalElement", originalElement.toString());
 assertEquals("OriginalElement", clonedElement.toString());
 }

 @Test
 public void testEquals() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element1");
 XmlElement element3 = new XmlElement("element2");
 assertEquals(element1, element1);
 assertEquals(element1, element1);
 assertFalse(element1.equals(null));
 assertFalse(element1.equals(element2));
 assertFalse(element1.equals(element3));
 }

 @Test
 public void testHashCode() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 assertEquals(element1.hashCode(), element1.hashCode());
 assertNotEquals(element1.hashCode(), element2.hashCode());
 }
}