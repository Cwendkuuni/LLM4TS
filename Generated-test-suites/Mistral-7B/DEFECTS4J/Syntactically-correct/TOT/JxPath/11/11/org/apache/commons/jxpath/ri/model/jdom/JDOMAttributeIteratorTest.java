import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null, null), new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr", "value");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr"));
 assertEquals(nodePointer, iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
 assertFalse(iterator.hasNext());
 assertEquals(0, iterator.getPosition());

 for (int i = 0; i < 5; i++) {
 iterator.next();
 }

 assertEquals(5, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalStateException.class)
 public void testPreviousOnEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null, null), new QName("", "*"));
 iterator.previous();
 }

 @Test
 public void testPreviousOnSingleAttributeIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr", "value");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr"));
 iterator.next();
 iterator.next();

 assertFalse(iterator.hasPrevious());
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testSetPositionOnEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null, null), new QName("", "*"));
 assertFalse(iterator.setPosition(1));
 }

 @Test
 public void testSetPositionOnSingleAttributeIterator() {
 Element root = new Element("root");
 root.addAttribute("attr", "value");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr"));
 iterator.next();

 assertTrue(iterator.setPosition(1));
 assertFalse(iterator.hasPrevious());
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testSetPositionOnEndOfIterator() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));

 int position = iterator.getPosition();
 for (int i = 0; i < iterator.size(); i++) {
 iterator.next();
 }

 assertFalse(iterator.setPosition(position + 1));
 }

 @Test
 public void testSetPositionOnInvalidPosition() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));

 int position = iterator.getPosition();
 for (int i = 0; i < iterator.size(); i++) {
 iterator.next();
 }

 assertFalse(iterator.setPosition(position - 1));
 }

 @Test
 public void testSetPositionToFirstAttribute() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));

 assertTrue(iterator.setPosition(1));
 }

 @Test
 public void testSetPositionToLastAttribute() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 NodePointer nodePointer = new NodePointer(root, root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));

 int size = iterator.size();
 iterator.next();
 iterator.next();

 assertTrue(iterator.setPosition(size));
 }
}