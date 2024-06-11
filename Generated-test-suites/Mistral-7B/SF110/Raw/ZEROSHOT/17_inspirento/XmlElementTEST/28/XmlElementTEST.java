import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

 private XmlElement root;

 @Before
 public void setUp() {
 root = new XmlElement("root");;
 }

 @Test
 public void testXmlElement_noArgs() {
 XmlElement e = new XmlElement();
 assertEquals("", e.getData());
 assertEquals(null, e.getName());
 assertEquals(new Hashtable<String, String>(10), e.getAttributes());
 assertEquals(new Vector<XmlElement>(), e.getElements());
 }

 @Test
 public void testXmlElement_oneArg() {
 XmlElement e = new XmlElement("elementName");
 assertEquals("elementName", e.getName());
 assertEquals(new Hashtable<String, String>(10), e.getAttributes());
 assertEquals(new Vector<XmlElement>(), e.getElements());
 }

 @Test
 public void testXmlElement_twoArgs() {
 XmlElement e = new XmlElement("elementName", new Hashtable<String, String>() {{
 put("attribute1", "value1");
 put("attribute2", "value2");
 }});
 assertEquals("elementName", e.getName());
 assertEquals(new Hashtable<String, String>() {{
 put("attribute1", "value1");
 put("attribute2", "value2");
 }}, e.getAttributes());
 assertEquals(new Vector<XmlElement>(), e.getElements());
 }

 @Test
 public void testXmlElement_threeArgs() {
 XmlElement e = new XmlElement("elementName", "data");
 assertEquals("elementName", e.getName());
 assertEquals("data", e.getData());
 assertEquals(new Hashtable<String, String>(10), e.getAttributes());
 assertEquals(new Vector<XmlElement>(), e.getElements());
 }

 @Test
 public void testAddAttribute() {
 root.addAttribute("attr1", "val1");
 assertEquals("val1", root.getAttribute("attr1"));
 }

 @Test
 public void testGetAttribute_null() {
 assertEquals(null, root.getAttribute(null));
 }

 @Test
 public void testGetAttribute_defaultValue() {
 root.addAttribute("attr1", "val1");
 assertEquals("val1", root.getAttribute("attr1"));
 assertEquals("defaultValue", root.getAttribute("attr2", "defaultValue"));
 }

 @Test
 public void testGetAttributes() {
 root.addAttribute("attr1", "val1");
 root.addAttribute("attr2", "val2");
 Hashtable<String, String> attrs = root.getAttributes();
 assertEquals("val1", attrs.get("attr1"));
 assertEquals("val2", attrs.get("attr2"));
 }

 @Test
 public void testSetAttributes() {
 Hashtable<String, String> attrs = new Hashtable<String, String>() {{
 put("attr1", "val1");
 put("attr2", "val2");
 }};
 root.setAttributes(attrs);
 assertEquals("val1", root.getAttribute("attr1"));
 assertEquals("val2", root.getAttribute("attr2"));
 }

 @Test
 public void testAttributeNames() {
 root.addAttribute("attr1", "val1");
 root.addAttribute("attr2", "val2");
 Enumeration<String> names = root.getAttributeNames();
 String name;
 while (names.hasMoreElements()) {
 name = names.nextElement();
 assertEquals("attr1", name);
 assertEquals("attr2", name);
 }
 }

 @Test
 public void testAddElement() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertTrue(root.getElements().contains(child1));
 assertTrue(root.getElements().contains(child2));
 assertSame(root, child1.getParent());
 assertSame(root, child2.getParent());
 }

 @Test
 public void testRemoveElement() {
 XmlElement child = new XmlElement("child");
 root.addElement(child);
 assertEquals(1, root.count());
 root.removeElement(child);
 assertEquals(0, root.count());
 assertNull(child.getParent());
 }

 @Test
 public void testRemoveAllElements() {
 XmlElement child1 = new XmlElement("child1");
 XmlElement child2 = new XmlElement("child2");
 root.addElement(child1);
 root.addElement(child2);
 assertEquals(2, root.count());
 root.removeAllElements();
 assertEquals(0, root.count());
 }

 @Test
 public void testClone() {
 XmlElement original = new XmlElement("original");
 original.addAttribute("attr1", "val1");
 XmlElement clone = (XmlElement)original.clone();
 assertEquals(original.getName(), clone.getName());
 assertEquals(original.getData(), clone.getData());
 assertEquals(original.getAttributes(), clone.getAttributes());
 assertEquals(original.getElements(), clone.getElements());
 }

 @Test
 public void testEquals() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element1");
 XmlElement element3 = new XmlElement("element2");
 XmlElement element4 = new XmlElement("element1");
 assertTrue(element1.equals(element1));
 assertTrue(element1.equals(element4));
 assertFalse(element1.equals(element2));
 assertFalse(element1.equals(element3));
 }

 @Test
 public void testHashCode() {
 XmlElement element1 = new XmlElement("element1");
 XmlElement element2 = new XmlElement("element2");
 int hashCode1 = element1.hashCode();
 int hashCode2 = element2.hashCode();
 assertFalse(hashCode1 == hashCode2);
 }
}