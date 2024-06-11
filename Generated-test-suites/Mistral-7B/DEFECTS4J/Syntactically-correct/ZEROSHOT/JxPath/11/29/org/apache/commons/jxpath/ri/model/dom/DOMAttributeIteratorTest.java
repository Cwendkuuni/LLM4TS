import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;

 @Test
 public void testEmptyIterator() {
 parent = new NodePointer(null, null, 0, null, null);
 name = new QName("", "*");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 parent = new NodePointer(null, new ElementImpl(new QName("root"), "root"), 0, null, null);
 name = new QName("", "testAttribute");
 Attr attr = parent.getNode().setAttributeNode(new AttrImpl("testAttribute", "testValue"));
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 DOMAttributePointer next = (DOMAttributePointer) iterator.next();
 assertEquals("testAttribute", next.getNodeName().getLocalPart());
 assertEquals("testValue", next.getNodeValue());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 parent = new NodePointer(null, new ElementImpl(new QName("root"), "root"), 0, null, null);
 name = new QName("", "testAttribute");
 Attr attr1 = parent.getNode().setAttributeNode(new AttrImpl("testAttribute1", "testValue1"));
 Attr attr2 = parent.getNode().setAttributeNode(new AttrImpl("testAttribute2", "testValue2"));
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 DOMAttributePointer next = (DOMAttributePointer) iterator.next();
 assertEquals("testAttribute1", next.getNodeName().getLocalPart());
 assertEquals("testValue1", next.getNodeValue());
 assertTrue(iterator.hasNext());
 DOMAttributePointer next2 = (DOMAttributePointer) iterator.next();
 assertEquals("testAttribute2", next2.getNodeName().getLocalPart());
 assertEquals("testValue2", next2.getNodeValue());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithDifferentNS() {
 parent = new NodePointer(null, new ElementImpl(new QName("root", "http://test.com"), "root"), 0, null, null);
 name = new QName("http://test.com", "testAttribute");
 Attr attr = parent.getNode().setAttributeNode(new AttrImplNS("testAttribute", "http://test.com", "testValue"));
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 DOMAttributePointer next = (DOMAttributePointer) iterator.next();
 assertEquals("testAttribute", next.getNodeName().getLocalPart());
 assertEquals("testValue", next.getNodeValue());
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullName() {
 parent = new NodePointer(null, new ElementImpl(new QName("root"), "root"), 0, null, null);
 iterator = new DOMAttributeIterator(parent, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParent() {
 name = new QName("", "testAttribute");
 iterator = new DOMAttributeIterator(null, name);
 }

}