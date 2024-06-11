import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.allenstudio.ir.util.XmlElement;
import com.allenstudio.ir.util.XmlElementEmpty;

public class XmlElementTest {

 private XmlElement root;
 private XmlElement element1;
 private XmlElement element2;
 private XmlElement element3;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 element1 = new XmlElement("element1", "data1");
 element2 = new XmlElement("element2");
 element3 = new XmlElement("element3", "data3");
 root.addElement(element1);
 root.addElement(element2);
 root.addElement(element3);
 }

 @Test
 public void testAddAttribute() {
 XmlElement testElement = new XmlElement("test"); ;
 testElement.addAttribute("attribute1", "value1");
 assertEquals("value1", testElement.getAttribute("attribute1"));
 }

 @Test
 public void testGetAttribute() {
 assertEquals(null, root.getAttribute("nonExistentAttribute"));
 assertEquals("data1", element1.getAttribute("data"));
 }

 @Test
 public void testGetAttributeWithDefault() {
 XmlElement testElement = new XmlElement("test");;
 assertEquals("", testElement.getAttribute("nonExistentAttribute", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 Hashtable<String, String> expectedAttributes = new Hashtable<>();
 expectedAttributes.put("attribute1", "value1");
 expectedAttributes.put("attribute2", "value2");
 assertEquals(expectedAttributes, element1.getAttributes());
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<>();
 attributes.put("attribute1", "newValue1");
 root.setAttributes(attributes);
 assertEquals(attributes, root.getAttributes());
 }

 @Test
 public void testGetAttributeNames() {
 Set<String> expectedAttributeNames = new HashSet<>();
 expectedAttributeNames.add("attribute1");
 expectedAttributeNames.add("attribute2");
 assertEquals(expectedAttributeNames, element1.getAttributeNames());
 }

 @Test
 public void testAddElement() {
 XmlElement testElement = new XmlElement("test"); ;
 root.addElement(testElement);
 assertTrue(root.getElements().contains(testElement));
 }

 @Test
 public void testRemoveElement() {
 assertTrue(root.getElements().contains(element1));
 root.removeElement(element1);
 assertFalse(root.getElements().contains(element1));
 }

 @Test
 public void testRemoveAllElements() {
 assertEquals(3, root.count());
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement testElement = new XmlElement("test");;
 root.addElement(testElement);
 testElement.removeFromParent();
 assertFalse(root.getElements().contains(testElement));
 }

 @Test
 public void testClone() {
 XmlElement clonedElement = root.clone();
 assertEquals(root.getName(), clonedElement.getName());
 assertEquals(root.getAttributes(), clonedElement.getAttributes());
 assertEquals(root.getData(), clonedElement.getData());
 assertEquals(root.getElements(), clonedElement.getElements());
 }

 @Test
 public void testAppend() {
 XmlElement testElement = new XmlElement("test");;
 root.append(testElement);
 assertTrue(root.getElements().contains(testElement));
 }

 @Test
 public void testInsertElement() {
 XmlElement testElement = new XmlElement("test");;
 root.insertElement(testElement, 0);
 assertTrue(root.getElements().get(0) == testElement);
 }

 @Test
 public void testGetElement() {
 XmlElement expectedElement = element2;
 assertEquals(expectedElement, root.getElement("element2"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetElementNullPath() {
 root.getElement(null);
 }

 @Test
 public void testAddSubElement() {
 XmlElement testElement = new XmlElement("test");;
 XmlElement subElement = root.addSubElement("element4/element5");
 assertEquals("element5", subElement.getName());
 assertEquals(root, subElement.getParent());
 }

 @Test
 public void testAddSubElementWithExistingName() {
 XmlElement testElement1 = new XmlElement("test1");;
 XmlElement testElement2 = new XmlElement("test1");;
 root.addSubElement("test1");
 root.addSubElement(testElement1);
 root.addSubElement(testElement2);
 assertEquals(2, root.getElements().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementEmptyName() {
 root.addSubElement("");
 }

 @Test
 public void testPrintNode() {
 XmlElement printTest = new XmlElement("printTest");;
 printTest.addAttribute("attribute1", "value1");
 printTest.addSubElement("subElement1/subElement2");
 printTest.addSubElement("subElement3");
 printTest.addSubElement("subElement3/subElement4");
 XmlElementEmpty emptyTest = new XmlElementEmpty();
 printNode(printTest, "");
 printNode(emptyTest, "");
 }
}