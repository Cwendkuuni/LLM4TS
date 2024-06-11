import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jxpath.JXPathContext;
import org.jxpath.dom.SimpleNodeIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Node root;

 @Before
 public void setup() {
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 Element rootElement = doc.createElement("root");
 root = doc.appendChild(rootElement);

 rootElement.setAttribute("attr1", "value1");
 rootElement.setAttribute("xmlns:x", "http://www.example.com");
 rootElement.setAttributeNS("http://www.example.com", "attr2:name", "value2");
 rootElement.setAttribute("attr3", "value3");
 }

 @Test
 public void testIteration() {
 iterator = new DOMAttributeIterator(new DOMNodePointer(root, "root"), new QName("*"));
 SimpleNodeIterator actual = new SimpleNodeIterator(iterator.getNodePointer().getNode());

 SimpleNodeIterator expected = new SimpleNodeIterator(root.getAttributes());

 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testFiltering() {
 iterator = new DOMAttributeIterator(new DOMNodePointer(root, "root"), new QName("xmlns:x", "attr2:name"));
 DOMNodePointer actual = iterator.getNodePointer();
 Assert.assertNotNull(actual);
 DOMNodePointer expected = new DOMNodePointer(root, "root", "xmlns:x", "attr2:name");
 Assert.assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(new DOMNodePointer(root, "root"), new QName("nonexistent", "*"));
 iterator.getNodePointer();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testEndOfIterator() {
 iterator = new DOMAttributeIterator(new DOMNodePointer(root, "root"), new QName("*"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.getNodePointer();
 }
}