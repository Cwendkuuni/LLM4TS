import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private NodeIterator iterator;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com'><child attr1='value1' attr2='value2' attr3='value3'/></root>";
 JDOMContext context = new JDOMContext();
 Document document = context.build(new StringReader(xml));
 Element root = document.getRootElement();

 nodePointer = new NodePointer(root);
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(nodePointer, null);
 assertTrue(emptyIterator.hasNext());
 assertNull(emptyIterator.next());
 assertFalse(emptyIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 JDOMAttributeIterator oneAttributeIterator = new JDOMAttributeIterator(nodePointer, new QName("x", "attr1"));
 assertTrue(oneAttributeIterator.hasNext());
 NodePointer next = oneAttributeIterator.next();
 assertEquals("value1", next.getValue());
 assertNull(oneAttributeIterator.next());
 assertFalse(oneAttributeIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 JDOMAttributeIterator multipleAttributesIterator = new JDOMAttributeIterator(nodePointer, new QName("*", ""));
 assertTrue(multipleAttributesIterator.hasNext());
 NodePointer next = multipleAttributesIterator.next();
 assertEquals("value1", next.getValue().getAttributeValue("local-name"));
 next = multipleAttributesIterator.next();
 assertEquals("value2", next.getValue().getAttributeValue("local-name"));
 next = multipleAttributesIterator.next();
 assertEquals("value3", next.getValue().getAttributeValue("local-name"));
 assertNull(multipleAttributesIterator.next());
 assertFalse(multipleAttributesIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithEmptyNamespace() {
 JDOMAttributeIterator emptyNamespaceIterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr1"));
 assertTrue(emptyNamespaceIterator.hasNext());
 NodePointer next = emptyNamespaceIterator.next();
 assertEquals("value1", next.getValue().getAttributeValue("local-name"));
 next = emptyNamespaceIterator.next();
 assertNull(next);
 assertFalse(emptyNamespaceIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithNamespace() {
 JDOMAttributeIterator namespaceIterator = new JDOMAttributeIterator(nodePointer, new QName("x", "attr1"));
 assertTrue(namespaceIterator.hasNext());
 NodePointer next = namespaceIterator.next();
 assertEquals("value1", next.getValue().getAttributeValue("local-name"));
 next = namespaceIterator.next();
 assertNull(next);
 assertFalse(namespaceIterator.hasPrevious());
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorWithWrongParent() {
 NodePointer wrongParent = new NodePointer(nodePointer.getNode().getChild(1));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(wrongParent, new QName("", "attr1"));
 iterator.next();
 }
}