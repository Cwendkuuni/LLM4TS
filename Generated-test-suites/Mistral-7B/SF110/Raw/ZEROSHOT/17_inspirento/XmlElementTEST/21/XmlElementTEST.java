import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement("element1");;
 XmlElement element2 = new XmlElement("element2", new Hashtable<String, String>());
 XmlElement element3 = new XmlElement("element3", "data");

 assertEquals("element1", element1.getName());
 assertEquals(0, element1.count());
 assertEquals(0, element1.size());

 assertEquals("element2", element2.getName());
 assertEquals(0, element2.count());
 assertEquals(0, element2.size());

 assertEquals("element3", element3.getName());
 assertEquals("data", element3.getData());
 assertEquals(0, element3.count());
 assertEquals(0, element3.size());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");

 assertEquals("value1", root.getAttribute("attribute1"));
 assertEquals("value2", root.getAttribute("attribute2"));
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");

 assertEquals("value1", root.getAttribute("attribute1"));
 assertEquals("value2", root.getAttribute("attribute2"));
 assertEquals(null, root.getAttribute("nonExistentAttribute"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");

 Map<String, String> attributes = root.getAttributes();

 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("attribute1"));
 assertEquals("value2", attributes.get("attribute2"));
 }

 @Test
 public void testSetAttributes() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");

 Map<String, String> attributes = new HashMap<String, String>() {{
 put("attribute3", "value3");
 put("attribute4", "value4");
 }};
 root.setAttributes(attributes);

 assertEquals(4, root.count());
 assertEquals("value3", root.getAttribute("attribute3"));
 assertEquals("value4", root.getAttribute("attribute4"));
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;

 root.addElement(child1);
 root.addElement(child2);

 assertEquals(2, root.count());
 assertEquals(child1, root.getElement(0));
 assertEquals(child2, root.getElement(1));
 }

 @Test
 public void testRemoveElement() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;

 root.addElement(child1);
 root.addElement(child2);

 root.removeElement(child1);

 assertEquals(1, root.count());
 assertNull(root.getElement(0));
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");;
 XmlElement child2 = new XmlElement("child2");;

 root.addElement(child1);
 root.addElement(child2);

 root.removeAllElements();

 assertEquals(0, root.count());
 assertNull(root.getElements());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XmlElement element = new XmlElement("element");;
 element.addAttribute("attribute1", "value1");
 element.addAttribute("attribute2", "value2");
 element.addElement(new XmlElement("child1"));
 element.addElement(new XmlElement("child2"));

 XmlElement clonedElement = (XmlElement)element.clone();

 assertEquals(element.getName(), clonedElement.getName());
 assertEquals(element.getData(), clonedElement.getData());
 assertEquals(element.getAttributes().size(), clonedElement.getAttributes().size());
 assertEquals(element.count(), clonedElement.count());

 for (int i = 0; i < element.count(); i++) {
 XmlElement child = element.getElements().get(i);
 XmlElement clonedChild = clonedElement.getElements().get(i);
 assertEquals(child.getName(), clonedChild.getName());
 assertEquals(child.getData(), clonedChild.getData());
 assertEquals(child.getAttributes().size(), clonedChild.getAttributes().size());
 }
 }

 @Test
 public void testXmlElementWithParent() {
 XmlElement parent = new XmlElement("parent");;
 XmlElement child = new XmlElement("child") {
 @Override
 public void setParent(XmlElement parent) {
 super.setParent(parent);
 }
 };

 parent.addElement(child);
 assertSame(parent, child.getParent());
 }

 @Test
 public void testPrintNode() {
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 root.addElement(new XmlElement("child3") {{
 setData("data");
 addAttribute("attribute", "value");
 addElement(new XmlElement("grandchild"));
 }});

 StringBuilder output = new StringBuilder();
 XmlElement.printNode(root, "");

 String expected = ""
 + "child3 = 'data'\n"
 + "attribute:value\n"
 + "grandchild\n"
 + "child2\n"
 + "child1\n"
 + "root\n";

 assertEquals(expected.toString(), output.toString());
 }
 }