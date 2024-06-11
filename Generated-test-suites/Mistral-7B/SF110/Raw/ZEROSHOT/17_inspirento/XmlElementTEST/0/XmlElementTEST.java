import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElementConstruction() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 XmlElement element3 = new XmlElement("element3", "data3");
 XmlElement element4 = new XmlElement("element4", null);
 XmlElement element5 = new XmlElement("element5", new Hashtable<>());

 assertEquals("element1", element1.getName());
 assertEquals(0, element1.count());
 assertEquals(null, element1.getData());
 assertEquals(0, element1.subElements.size());
 assertEquals(null, element1.getParent());

 assertEquals("element2", element2.getName());
 assertEquals(0, element2.count());
 assertEquals(null, element2.getData());
 assertEquals(0, element2.subElements.size());
 assertEquals(null, element2.getParent());

 assertEquals("element3", element3.getName());
 assertEquals(0, element3.count());
 assertEquals("data3", element3.getData());
 assertEquals(0, element3.subElements.size());
 assertEquals(null, element3.getParent());

 assertEquals("element4", element4.getName());
 assertEquals(0, element4.count());
 assertEquals(null, element4.getData());
 assertEquals(0, element4.subElements.size());
 assertEquals(null, element4.getParent());

 assertEquals("element5", element5.getName());
 assertEquals(0, element5.count());
 assertEquals(null, element5.getData());
 assertEquals(0, element5.subElements.size());
 assertEquals(null, element5.getParent());
 }

 @Test
 public void testAddingAndRemovingAttributes() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 root.removeAttribute("test");
 assertEquals(null, root.getAttribute("test"));
 }

 @Test
 public void testGettingAttributes() {
 root.addAttribute("test1", "testValue1");
 root.addAttribute("test2", "testValue2");
 assertNotNull(root.getAttributes());
 assertEquals(2, root.getAttributes().size());
 assertEquals("testValue1", root.getAttribute("test1"));
 assertEquals("testValue2", root.getAttribute("test2"));
 }

 @Test
 public void testGettingAttributeWithDefaultValue() {
 root.addAttribute("test3", null);
 assertEquals(null, root.getAttribute("test3", "defaultValue"));
 root.addAttribute("test3", "newValue");
 assertEquals("newValue", root.getAttribute("test3", "defaultValue"));
 }

 @Test
 public void testAddingAndRemovingElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 assertEquals(child1, root.getElement(0));
 assertEquals(child2, root.getElement(1));

 root.removeElement(child1);
 assertEquals(1, root.count());
 assertEquals(child2, root.getElement(0));
 }

 @Test
 public void testGettingElements() {
 root.addElement(child1);
 root.addElement(child2);
 List<XmlElement> elements = root.getElements();
 assertEquals(2, elements.size());
 assertEquals(child1, elements.get(0));
 assertEquals(child2, elements.get(1));
 }

 @Test
 public void testGettingElementByPath() {
 XmlElement child3 = new XmlElement("child3");
 XmlElement child4 = new XmlElement("child4");
 root.addElement(child1);
 root.addElement(child2);
 child1.addElement(child3);
 child2.addElement(child4);
 XmlElement elementByPath = root.getElement("root/child1/child3");
 assertEquals(child3, elementByPath);
 }

 @Test
 public void testAddingSubElements() {
 XmlElement newChild1 = root.addSubElement("child1");
 XmlElement newChild2 = root.addSubElement("child2", "data2");
 XmlElement newChild3 = root.addSubElement("child3", "data3", newChild1);
 assertEquals(3, root.count());
 assertEquals(newChild1, root.getElement("child1"));
 assertEquals(newChild2, root.getElement("child2"));
 assertEquals(newChild3, root.getElement("child1/child3"));
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 original.addAttribute("attribute1", "value1");
 original.addElement(new XmlElement("element1"));
 original.addElement(new XmlElement("element2").addSubElement("element3"));
 XmlElement cloned = (XmlElement)original.clone();
 assertNotSame(original, cloned);
 assertEquals(original, cloned.getParent());
 assertEquals("attribute1", cloned.getAttribute("attribute1"));
 assertEquals(2, cloned.count());
 assertEquals(original.getElement(0), cloned.getElement(0));
 assertEquals(original.getElement(1), cloned.getElement(1));
 }

 @Test
 public void testEqualsAndHashcode() {
 XmlElement xmlElement1 = new XmlElement("element1");
 XmlElement xmlElement2 = new XmlElement("element1");
 XmlElement xmlElement3 = new XmlElement("element2");

 assertTrue(xmlElement1.equals(xmlElement1));
 assertTrue(xmlElement1.equals(xmlElement2));
 assertTrue(xmlElement1.hashCode() == xmlElement2.hashCode());

 assertFalse(xmlElement1.equals(xmlElement3));
 assertFalse(xmlElement1.hashCode() == xmlElement3.hashCode());
 }
}