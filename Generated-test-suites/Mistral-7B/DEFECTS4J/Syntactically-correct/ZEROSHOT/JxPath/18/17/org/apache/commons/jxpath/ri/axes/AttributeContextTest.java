import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.XPathTest;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Test
 public void testAttributeContextConstruction() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("testAttribute");
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);
 Assert.assertNotNull(ac);
 }

 @Test
 public void testGetCurrentNodePointer() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("testAttribute");
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);

 QName name = new QName("testns:testElement", "testns:testAttribute");
 NodePointer nodePointer = new NodePointer(new XPathTest(new QName("testElement"), null), null);
 nodePointer.setNodeType(NodeIterator.ATTRIBUTE_NODE);
 nodePointer.setNodeValue("testValue");
 ac.setCurrentNodePointer(nodePointer);

 NodePointer expected = nodePointer;
 NodePointer actual = attributeContext.getCurrentNodePointer();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testReset() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("testAttribute");
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);
 ac.setCurrentNodePointer(null);
 ac.reset();
 Assert.assertNull(ac.getCurrentNodePointer());
 }

 @Test
 public void testSetPosition() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("testAttribute");
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);
 ac.setCurrentNodePointer(null);

 QName name = new QName("testns:testElement", "testns:testAttribute");
 NodePointer nodePointer = new NodePointer(new XPathTest(new QName("testElement"), null), null);
 nodePointer.setNodeType(NodeIterator.ATTRIBUTE_NODE);
 nodePointer.setNodeValue("testValue");
 ac.setCurrentNodePointer(nodePointer);

 // Position is 0, nothing happens
 ac.setPosition(0);
 Assert.assertEquals(nodePointer, ac.getCurrentNodePointer());

 // Position is 1, the next node should be returned
 ac.nextNode();
 ac.setPosition(1);
 NodePointer nextNodePointer = new NodePointer(new XPathTest(new QName("testElement"), null), null);
 nextNodePointer.setNodeType(NodeIterator.ATTRIBUTE_NODE);
 nextNodePointer.setNodeValue("testNextValue");
 Assert.assertFalse(ac.nextNode());
 Assert.assertEquals(nodePointer, ac.getCurrentNodePointer());

 // Position is greater than current position, nothing happens
 ac.setPosition(2);
 Assert.assertEquals(nodePointer, ac.getCurrentNodePointer());
 }

 @Test
 public void testNextNode() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("testAttribute");
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);
 ac.setCurrentNodePointer(null);

 QName name = new QName("testns:testElement", "testns:testAttribute");
 NodePointer nodePointer1 = new NodePointer(new XPathTest(new QName("testElement"), null), null);
 nodePointer1.setNodeType(NodeIterator.ATTRIBUTE_NODE);
 nodePointer1.setNodeValue("testValue1");
 ac.setCurrentNodePointer(nodePointer1);

 NodePointer nodePointer2 = new NodePointer(new XPathTest(new QName("testElement"), null), null);
 nodePointer2.setNodeType(NodeIterator.ATTRIBUTE_NODE);
 nodePointer2.setNodeValue("testValue2");

 // First call to nextNode should return next node
 Assert.assertTrue(ac.nextNode());
 NodePointer actualNextNode = ac.getCurrentNodePointer();
 NodePointer expectedNextNode = nodePointer2;
 Assert.assertEquals(expectedNextNode, actualNextNode);

 // Second call to nextNode should return null, since there is no next node
 Assert.assertFalse(ac.nextNode());
 }
}