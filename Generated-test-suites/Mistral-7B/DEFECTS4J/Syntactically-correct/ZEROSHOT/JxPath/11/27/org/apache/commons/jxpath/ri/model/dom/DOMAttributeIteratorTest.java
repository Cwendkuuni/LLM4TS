import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DOMAttributeIteratorTest {

 private DOMAttributeIterator iterator;
 @Mock
 private NodePointer parent;
 @Mock
 private Element element;
 @Mock
 private Attr attr1;
 @Mock
 private Attr attr2;

 @Before
 public void setUp() {
 Mockito.when(parent.getNode()).thenReturn(element);
 Mockito.when(element.getAttributeNode(Mockito.eq("id"))).thenReturn(attr1);
 Mockito.when(element.getAttributeNodeNS(Mockito.eq("http://www.w3.org/2000/xmlns/"), Mockito.eq("xmlns:x"))).thenReturn(attr2);
 }

 @Test
 public void testConstructorWithElementAndQName() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testConstructorWithElementAndNonWildcardQName() {
 QName name = new QName("http://www.w3.org/2000/xmlns/", "xmlns:x");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testConstructorWithElementAndWrongQName() {
 QName name = new QName("http://wrongnamespace/", "xmlns:x");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNull(iterator);
 }

 @Test
 public void testConstructorWithElementAndNullQName() {
 iterator = new DOMAttributeIterator(parent, null);
 Assert.assertNull(iterator);
 }

 @Test
 public void testConstructorWithNullParent() {
 iterator = new DOMAttributeIterator(null, new QName("*"));
 Assert.assertNull(iterator);
 }

 @Test
 public void testHasNext() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 Mockito.when(parent.getNode()).thenReturn(element);
 Mockito.when(element.getAttributes()).thenReturn(Mockito.mock(NamedNodeMap.class));
 Mockito.when(element.getAttributes().getLength()).thenReturn(2);
 iterator.next();
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextAfterEnd() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Mockito.verify(parent).getNode();
 Mockito.verify(element.getAttributes()).size();
 Mockito.verify(element.getAttributes()).getLength();
 Attr nextAttr = iterator.next();
 Mockito.verify(element.getAttributeNode(nextAttr.getNodeName())).thenReturn(nextAttr);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextAfterEnd() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test
 public void testGetPosition() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testSetPositionToZero() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.setPosition(0);
 Assert.assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testSetPositionToOne() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.setPosition(1);
 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionToNegativeNumber() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionToNumberGreaterThanSize() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.setPosition(3);
 }

 @Test
 public void testGetNodePointer() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertNotNull(nodePointer);
 Assert.assertEquals(iterator.getClass(), nodePointer.getIterator().getClass());
 Assert.assertNotNull(nodePointer.getNode());
 Assert.assertNotNull(nodePointer.getNode().getNodeValue());
 }
}