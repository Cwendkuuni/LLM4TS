import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Element rootElement;

 @Before
 public void setUp() throws JDOMException {
 rootElement = new Element("root");
 rootElement.addAttribute("attr1", "value1");
 rootElement.addAttribute("attr2", "value2", "ns1:prefix");
 rootElement.addNamespace("ns1", "namespace-uri");
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "*"));
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttributeIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertEquals("value1", nodePointer.getNode().getValue());
 Assert.assertEquals("attr1", nodePointer.getNode().getName());
 Assert.assertEquals("", nodePointer.getNode().getNamespaceURI());
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorByPrefix() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("ns1:prefix", "attr2"));
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertEquals("value2", nodePointer.getNode().getValue());
 Assert.assertEquals("attr2", nodePointer.getNode().getName());
 Assert.assertEquals("namespace-uri", nodePointer.getNode().getNamespaceURI());
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorByPrefixWithWildcard() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("ns1:prefix", "*"));
 NodePointer nodePointer = null;
 while ((nodePointer = iterator.getNodePointer()) != null) {
 Assert.assertEquals("value1", nodePointer.getNode().getValue());
 Assert.assertEquals("attr1", nodePointer.getNode().getName());
 Assert.assertEquals("", nodePointer.getNode().getNamespaceURI());
 iterator.setPosition(iterator.getPosition() + 1);
 }
 Assert.assertEquals(1, iterator.getPosition());
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorWithNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "attr2"));
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAttributeIteratorOutOfBounds() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "attr1"));
 iterator.setPosition(2);
 }

}