import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root"); ;
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attribute1", "value1");
 assertEquals("value1", element.getAttribute("attribute1"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement element = new XmlElement("element");;
 assertEquals("", element.getAttribute("nonexistentAttribute", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 Hashtable<String, String> attributes = element.getAttributes();
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 @Test
 public void testSetAttributes() {
 XmlElement element = new XmlElement("element");;
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("attribute1", "value1");
 put("attribute2", "value2");
 }};
 element.setAttributes(attributes);
 assertEquals("value1", element.getAttribute("attribute1"));
 assertEquals("value2", element.getAttribute("attribute2"));
 }

 @Test
 public void testGetAttributeNames() {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 List<String> attributeNames = new ArrayList<String>(Arrays.asList(element.getAttributeNames()));
 assertTrue(attributeNames.contains("attribute1"));
 assertTrue(attributeNames.contains("attribute2"));
 }

 @Test
 public void testAddElement() {
 XmlElement parent = new XmlElement("parent"); ;
 XmlElement child1 = new XmlElement("child1"); ;
 XmlElement child2 = new XmlElement("child2"); ;
 parent.addElement(child1);
 parent.addElement(child2);
 List<XmlElement> elements = parent.getElements();
 assertEquals(2, elements.size());
 assertTrue(elements.contains(child1));
 assertTrue(elements.contains(child2));
 }

 @Test
 public void testRemoveElement() {
 XmlElement parent = new XmlElement("parent"); ;
 XmlElement child1 = new XmlElement("child1"); ;
 XmlElement child2 = new XmlElement("child2") ;
 parent.addElement(child1);
 parent.addElement(child2);
 parent.removeElement(child1);
 List<XmlElement> elements = parent.getElements();
 assertEquals(1, elements.size());
 assertFalse(elements.contains(child1));
 assertFalse(child1.getParent() instanceof XmlElement);
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement parent = new XmlElement("parent") ;
 XmlElement child1 = new XmlElement("child1") ;
 XmlElement child2 = new XmlElement("child2") ;
 parent.addElement(child1);
 parent.addElement(child2);
 parent.removeAllElements();
 List<XmlElement> elements = parent.getElements();
 assertEquals(0, elements.size());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement parent = new XmlElement("parent") ;
 XmlElement child = new XmlElement("child") ;
 parent.addElement(child);
 child.removeFromParent();
 List<XmlElement> elements = parent.getElements();
 assertEquals(0, elements.size());
 assertFalse(child.getParent() instanceof XmlElement);
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original") ;
 XmlElement clone = original.clone();
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 List<XmlElement> originalElements = original.getElements();
 List<XmlElement> cloneElements = clone.getElements();
 assertEquals(originalElements.size(), cloneElements.size());
 for (XmlElement element : originalElements) {
 XmlElement clonedElement = clone.getElement(element.getPath());
 assertEquals(element.getName(), clonedElement.getName());
 assertEquals(element.getData(), clonedElement.getData());
 List<XmlElement> clonedElementSubElements = clonedElement.getElements();
 List<XmlElement> elementSubElements = element.getElements();
 assertEquals(elementSubElements.size(), clonedElementSubElements.size());
 for (XmlElement subElement : elementSubElements) {
 XmlElement clonedSubElement = clonedElement.getElement(subElement.getPath());
 assertEquals(subElement.getName(), clonedSubElement.getName());
 assertEquals(subElement.getData(), clonedSubElement.getData());
 }
 }
 }

 @Test
 public void testAddSubElement() {
 XmlElement parent = new XmlElement("parent") ;
 XmlElement child1 = parent.addSubElement("child1");
 XmlElement child2 = parent.addSubElement("child2/child3") ;
 List<XmlElement> elements = parent.getElements();
 assertEquals(2, elements.size());
 XmlElement child1FromParent = parent.getElement("child1");
 XmlElement child2FromParent = parent.getElement("child2/child3");
 assertEquals(child1, child1FromParent);
 assertEquals(child2, child2FromParent);
 }

 @Test
 public void testAddSubElementWithNullPath() {
 XmlElement parent = new XmlElement("parent") ;
 try {
 parent.addSubElement(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAddSubElementWithEmptyPath() {
 XmlElement parent = new XmlElement("parent") ;
 try {
 parent.addSubElement("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAddSubElementWithExistingPath() {
 XmlElement parent = new XmlElement("parent") ;
 XmlElement child1 = new XmlElement("child1");
 parent.addElement(child1);
 try {
 parent.addSubElement(child1.getPath() + "/child2");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAddSubElementWithDuplicateName() {
 XmlElement parent = new XmlElement("parent") ;
 XmlElement child1 = new XmlElement("child1");
 parent.addElement(child1);
 try {
 parent.addSubElement("child1");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testToString() {
 XmlElement element = new XmlElement("element") ;
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 XmlElement child1 = new XmlElement("child1") ;
 XmlElement child2 = new XmlElement("child2") ;
 element.addElement(child1);
 element.addElement(child2);
 StringBuilder expected = new StringBuilder();
 expected.append("element")
 .append(" attribute1:value1")
 .append(" attribute2:value2")
 .append(" child1")
 .append(" child2")
 .append("\n");
 StringBuilder actual = new StringBuilder();
 XmlElement.printNode(element, "");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element1");
 XmlElement element3 = new XmlElement("element2");
 XmlElement element4 = new XmlElement("element1");
 HashSet<XmlElement> set = new HashSet<XmlElement>() {{
 add(element1);
 add(element2);
 add(element3);
 add(element4);
 }};
 assertTrue(element1.equals(element1));
 assertTrue(element1.equals(element2));
 assertFalse(element1.equals(element3));
 assertFalse(element1.equals(element4));
 assertFalse(element1.equals(new Object()));
 assertFalse(element1.equals(null));
 }

 @Test
 public void testHashCode() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element1");
 XmlElement element3 = new XmlElement("element2");
 XmlElement element4 = new XmlElement("element1");
 List<XmlElement> elements1 = new ArrayList<XmlElement>() {{
 add(element1);
 add(new XmlElement("child"));
 }};
 List<XmlElement> elements2 = new ArrayList<XmlElement>() {{
 add(element2);
 add(new XmlElement("child"));
 }};
 assertEquals(elements1.hashCode(), elements2.hashCode());
 List<XmlElement> elements3 = new ArrayList<XmlElement>() {{
 add(element3);
 add(new XmlElement("child"));
 }};
 assertNotEquals(elements1.hashCode(), elements3.hashCode());
 assertEquals(element1.hashCode(), element4.hashCode());
 }
}