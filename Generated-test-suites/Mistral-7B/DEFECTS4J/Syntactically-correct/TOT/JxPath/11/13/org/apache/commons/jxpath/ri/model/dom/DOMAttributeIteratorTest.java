import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private Node testNode;
 private Element testElement;

 @org.junit.Before
 public void setup() {
 testNode = createTestNode();
 testElement = (Element) testNode;
 }

 @org.junit.Test
 public void testIterationOverSingleAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("test:attr"));
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertNotNull(nodePointer);
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr")).toString(), nodePointer.toString());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testIterationOverMultipleAttributesWithSamePrefix() {
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test", "attr1"), "attr1");
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test", "attr2"), "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("test:attr", "test"));
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr1")).toString(), iterator.getNodePointer().toString());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr2")).toString(), iterator.getNodePointer().toString());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testIterationOverMultipleAttributesWithDifferentPrefixes() {
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test1", "attr1"), "attr1");
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test2", "attr2"), "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("attr1"));
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr1")).toString(), iterator.getNodePointer().toString());
 Assert.assertFalse(iterator.hasNext());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());

 iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("attr2"));
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr2")).toString(), iterator.getNodePointer().toString());
 Assert.assertFalse(iterator.hasNext());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testIterationOverEmptyList() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("nonExistentAttr"));
 Assert.assertFalse(iterator.hasNext());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testIterationInReverseOrder() {
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test", "attr1"), "attr1");
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test", "attr2"), "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(testNode), new QName("test:attr", "test"));
 List<NodePointer> nodePointers = new ArrayList<>();
 while (iterator.hasNext()) {
 nodePointers.add(iterator.getNodePointer());
 iterator.next();
 }
 iterator.setPosition(nodePointers.size());
 Assert.assertTrue(iterator.hasPrevious());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr2")).toString(), iterator.getNodePointer().toString());
 Assert.assertTrue(iterator.hasPrevious());
 Assert.assertEquals(new DOMAttributePointer(new DOMPointNodePointer(testNode), testElement.getAttributeNodeByName("attr1")).toString(), iterator.getNodePointer().toString());
 Assert.assertFalse(iterator.hasPrevious());
 Assert.assertNull(iterator.getNodePointer());
 }

 @org.junit.After
 public void teardown() {
 // no clean up needed
 }

 private Node createTestNode() {
 Node testNode = testDocument.createProcessingInstruction("xml", "version=\"1.0\"");
 testNode = testDocument.createElement("root");
 testNode = testDocument.appendChild(testNode);
 testNode = testDocument.createElement("child");
 testNode = testDocument.appendChild(testNode);
 testElement = (Element) testNode;
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test1", "attr1"), "attr1");
 testElement.setAttributeNodeNS(testElement.createAttributeNS("test2", "attr2"), "attr2");
 NodeList children = testNode.getChildNodes();
 for (int i = 0; i < children.getLength(); i++) {
 if (children.item(i) instanceof Element) {
 children.item(i).setNodeValue("value" + i);
 }
 }
 return testNode;
}
}