import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testAddAttribute() {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attr1", "value1");
 assertEquals("value1", element.getAttribute("attr1"));
 }

 @Test
 public void testGetAttributeWithDefaultValue() {
 XmlElement element = new XmlElement("element");;
 assertEquals("", element.getAttribute("attr1", "defaultValue"));
 element.addAttribute("attr1", "value1");
 assertEquals("value1", element.getAttribute("attr1", "defaultValue"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 XmlElement element = new XmlElement("element");;
 element.setAttributes(attributes);
 assertEquals("value1", element.getAttribute("attr1"));
 assertEquals("value2", element.getAttribute("attr2"));
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 assertTrue(root.getElements().contains(child1));
 assertTrue(root.getElements().contains(child2));
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child");;
 root.addElement(child);
 assertEquals(1, root.count());
 root.removeElement(child);
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testAppend() {
 XmlElement child = new XmlElement("child");;
 root.append(child);
 assertEquals(1, root.count());
 assertTrue(root.getElements().contains(child));
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;
 root.addElement(child1);
 root.insertElement(child2, 0);
 assertEquals(2, root.count());
 assertTrue(root.getElements().get(0).equals(child2));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");;
 XmlElement clone = original.clone();
 assertNotSame(original, clone);
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 Hashtable<String, String> attributes1 = original.getAttributes();
 Hashtable<String, String> attributes2 = clone.getAttributes();
 assertEquals(attributes1.size(), attributes2.size());
 for (String key : attributes1.keySet()) {
 assertEquals(attributes1.get(key), attributes2.get(key));
 }
 List<XmlElement> children1 = original.getElements();
 List<XmlElement> children2 = clone.getElements();
 assertEquals(children1.size(), children2.size());
 for (XmlElement child : children1) {
 XmlElement childClone = null;
 for (XmlElement childCloneCandidate : children2) {
 if (child.equals(childCloneCandidate)) {
 childClone = childCloneCandidate;
 break;
 }
 }
 assertTrue(childClone != null);
 }
 }

 @Test
 public void testAddSubElement() {
 XmlElement parent = root;
 XmlElement child1 = parent.addSubElement("child1/child2");
 XmlElement child2 = parent.addSubElement("child1/child3");
 XmlElement child3 = parent.addSubElement("child3/child4");
 assertEquals(3, root.count());
 assertEquals(child1, root.getElements().get(0));
 assertEquals(child2, root.getElements().get(1));
 assertEquals(child3, root.getElements().get(2));
 }

 @Test
 public void testAddSubElementWithNullPath() {
 XmlElement parent = root;
 assertNull(parent.addSubElement(null));
 }

 @Test
 public void testAddSubElementWithEmptyPath() {
 XmlElement parent = root;
 assertNull(parent.addSubElement(""));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddSubElementWithInvalidIndex() {
 XmlElement parent = new XmlElement("parent");;
 parent.addSubElement("child", 0);
 assertNotNull(parent.getElements().get(0));
 parent.addSubElement("child", 1);
 assertNotNull(parent.getElements().get(1));
 parent.addSubElement("child", 2);
 assertNotNull(parent.getElements().get(2));
 parent.addSubElement("child", -1);
 }

 @Test(expected = NullPointerException.class)
 public void testAddSubElementWithNullChild() {
 XmlElement parent = new XmlElement("parent");;
 assertNotNull(parent.getElements().get(0));
 parent.addSubElement(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSubElementWithInvalidName() {
 XmlElement parent = new XmlElement("parent");;
 parent.addSubElement("child", "name");
 }
}