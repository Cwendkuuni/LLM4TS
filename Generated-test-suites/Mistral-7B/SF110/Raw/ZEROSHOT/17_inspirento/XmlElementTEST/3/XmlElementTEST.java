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
 public void testXmlElementCreation() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2", "data1");
 XmlElement element3 = new XmlElement("element3", new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});

 assertEquals("element1", element1.getName());
 assertEquals(null, element1.getData());
 assertEquals(null, element1.getAttributes());
 assertEquals(0, element1.count());

 assertEquals("element2", element2.getName());
 assertEquals("data1", element2.getData());
 assertEquals(null, element2.getAttributes());
 assertEquals(0, element2.count());

 assertEquals("element3", element3.getName());
 assertEquals(null, element3.getData());
 assertEquals(element3.getAttributes(), new Hashtable<String, String>() {{
 put("attr1", "value1");
 put("attr2", "value2");
 }});
 assertEquals(0, element3.count());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullName() {
 root.addAttribute(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAddAttributeNullValue() {
 root.addAttribute("test", null);
 }

 @Test
 public void testGetAttribute() {
 root.addAttribute("test", "testValue");
 assertEquals("testValue", root.getAttribute("test"));
 }

 @Test
 public void testGetAttributeNull() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttributeDefaultValue() {
 root.addAttribute("test", null);
 assertEquals("defaultValue", root.getAttribute("test", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("test1", "testValue1");
 root.addAttribute("test2", "testValue2");
 Hashtable<String, String> attributes = root.getAttributes();
 assertEquals("testValue1", attributes.get("test1"));
 assertEquals("testValue2", attributes.get("test2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attributes = new Hashtable<String, String>() {{
 put("test1", "testValue1");
 put("test2", "testValue2");
 }};
 root.setAttributes(attributes);
 assertEquals("testValue1", root.getAttribute("test1"));
 assertEquals("testValue2", root.getAttribute("test2"));
 }

 @Test
 public void testGetAttributeNames() {
 root.addAttribute("test1", "testValue1");
 root.addAttribute("test2", "testValue2");
 Enumeration<String> names = root.getAttributeNames();
 assertTrue(names.hasMoreElements());
 assertEquals("test1", names.nextElement());
 assertTrue(names.hasMoreElements());
 assertEquals("test2", names.nextElement());
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 assertEquals(1, root.count());
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child2);
 assertEquals(2, root.count());
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullElement() {
 root.addElement(null);
 }

 @Test
 public void testRemoveElement() {
 root.addElement(root.getElement("child1"));
 assertEquals(1, root.count());
 root.removeElement(root.getElement("child1"));
 assertEquals(0, root.count());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveElementInvalidIndex() {
 root.removeElement(-1);
 }

 @Test
 public void testRemoveAllElements() {
 root.addElement(root.getElement("child1"));
 root.addElement(root.getElement("child2"));
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testRemoveFromParent() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 child.removeFromParent();
 assertEquals(0, root.count());
 }

 @Test
 public void testAppend() {
 XmlElement child1 = new XmlElement("child1");
 root.append(child1);
 assertEquals(1, root.count());
 }

 @Test
 public void testInsertElement() {
 XmlElement child1 = new XmlElement("child1");
 root.insertElement(child1, 0);
 assertEquals(1, root.count());
 XmlElement child2 = new XmlElement("child2");
 root.insertElement(child2, 1);
 assertEquals(2, root.count());
 }

 @Test
 public void testGetElements() {
 root.addElement(root.getElement("child1"));
 root.addElement(root.getElement("child2"));
 List<XmlElement> children = root.getElements();
 assertEquals(2, children.size());
 assertEquals("child1", children.get(0).getName());
 assertEquals("child2", children.get(1).getName());
 }

 @Test
 public void testCount() {
 root.addElement(root.getElement("child1"));
 root.addElement(root.getElement("child2"));
 assertEquals(2, root.count());
 }

 @Test
 public void testGetElement() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 XmlElement child3 = root.getElement("child1/child3");
 assertNull(child3);
 XmlElement child4 = root.getElement("child1/child4");
 child4 = new XmlElement("child4");
 root.addElement(child4);
 assertEquals("child4", child4.getName());
 }

 @Test
 public void testGetElementInvalidPath() {
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 assertNull(root.getElement("child1/child5"));
 }

 @Test
 public void testAddSubElement() {
 XmlElement child1 = root.addSubElement("child1");
 assertEquals("child1", child1.getName());
 XmlElement child2 = root.addSubElement("child1/child2");
 assertEquals("child2", child2.getName());
 }

 @Test
 public void testAddSubElementNullName() {
 root.addSubElement(null);
 }

 @Test
 public void testAddSubElementEmptyName() {
 root.addSubElement("");
 }

 @Test
 public void testAddSubElementWithData() {
 XmlElement child1 = root.addSubElement("child1");
 XmlElement child2 = root.addSubElement("child1/child2", "childData");
 assertEquals("child2", child2.getName());
 assertEquals("childData", child2.getData());
 }

 @Test
 public void testGetParent() {
 XmlElement child1 = new XmlElement("child1");
 root.addElement(child1);
 assertEquals(root, child1.getParent());
 }

 @Test
 public void testSetParent() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement parent = new XmlElement("parent");
 child1.setParent(parent);
 assertEquals(parent, child1.getParent());
 }

 @Test
 public void testSetData() {
 XmlElement element = new XmlElement("element");
 element.setData("testData");
 assertEquals("testData", element.getData());
 }

 @Test
 public void testPrintNode() {
 root.addAttribute("test", "testValue");
 root.addElement(new XmlElement("child1"));
 root.addElement(new XmlElement("child2"));
 root.printNode(0);
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 original.addAttribute("test", "testValue");
 original.addElement(new XmlElement("child1"));
 XmlElement cloned = (XmlElement)original.clone();
 assertEquals(original.getName(), cloned.getName());
 assertEquals(original.getData(), cloned.getData());
 assertEquals(original.getAttributes(), cloned.getAttributes());
 assertEquals(original.count(), cloned.count());
 }

 @Test
 public void testEquals() {
 XmlElement xml1 = new XmlElement("xml1");
 XmlElement xml2 = new XmlElement("xml2");
 XmlElement xml3 = new XmlElement("xml1");
 assertTrue(xml1.equals(xml1));
 assertFalse(xml1.equals(xml2));
 assertFalse(xml1.equals(xml3));
 }

 @Test
 public void testHashCode() {
 XmlElement xml1 = new XmlElement("xml1");
 XmlElement xml2 = new XmlElement("xml2");
 assertEquals(xml1.hashCode(), xml1.hashCode());
 assertNotEquals(xml1.hashCode(), xml2.hashCode());
 }
}