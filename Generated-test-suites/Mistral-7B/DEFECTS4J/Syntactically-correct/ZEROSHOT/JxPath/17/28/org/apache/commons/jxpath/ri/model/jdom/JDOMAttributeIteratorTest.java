import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingletonAttribute() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("name", "value", "namespace");
 element.addAttribute(attribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("namespace", "name"));
 NodePointer nodePointer = iterator.next();
 assertEquals(nodePointer.getNode(), attribute);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", "namespace1");
 Attribute attribute2 = new Attribute("name2", "value2", "namespace2");
 Attribute attribute3 = new Attribute("name3", "value3", "namespace3");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("namespace1", "*"));
 NodePointer nodePointer1 = iterator.next();
 assertEquals(nodePointer1.getNode(), attribute1);
 NodePointer nodePointer2 = iterator.next();
 assertEquals(nodePointer2.getNode(), attribute2);
 NodePointer nodePointer3 = iterator.next();
 assertEquals(nodePointer3.getNode(), attribute3);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoMatchingAttributes() {
 Element element = new Element("root");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("namespaceX", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithParentNodeChange() {
 Element root = new Element("root");
 Attribute attribute = new Attribute("name", "value", "namespace");
 root.addAttribute(attribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("namespace", "name"));
 NodePointer nodePointer = iterator.next();
 assertEquals(nodePointer.getNode(), attribute);
 Element child = new Element("child");
 NodePointer childPointer = new NodePointer(child);
 iterator.setParent(childPointer);
 assertFalse(iterator.hasNext());
 }
}