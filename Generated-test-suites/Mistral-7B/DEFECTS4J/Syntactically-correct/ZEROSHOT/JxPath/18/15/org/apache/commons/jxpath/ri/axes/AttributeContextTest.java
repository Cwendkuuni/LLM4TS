import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 private AttributeContext attributeContext;
 private EvalContext parentContext;
 private NodeNameTest nodeNameTest;
 private NodeTest nodeTest;
 private NodePointer nodePointer;

 @Before
 public void setUp() {
 parentContext = new EvalContext(null);
 nodeNameTest = new NodeNameTest(new QName("testAttribute"));
 nodeTest = new NodeNameTest(nodeNameTest.getNodeName());
 attributeContext = new AttributeContext(parentContext, nodeTest);
 nodePointer = new NodePointer(new Object());
 parentContext.setCurrentNodePointer(nodePointer);
 }

 @Test
 public void testAttributeContextConstructor() {
 AttributeContext ac = new AttributeContext(parentContext, nodeTest);
 assertEquals("Unexpected parentContext", parentContext, ac.getParentContext());
 assertEquals("Unexpected nodeTest", nodeTest, ac.getNodeTest());
 assertFalse("Unexpected setStarted initialized", ac.isSetStarted());
 assertNull("Unexpected iterator initialized", ac.getIterator());
 assertNull("Unexpected currentNodePointer initialized", ac.getCurrentNodePointer());
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 assertFalse("setStarted not reset", attributeContext.isSetStarted());
 assertNull("iterator not reset", attributeContext.getIterator());
 assertNull("currentNodePointer not reset", attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testSetPosition() {
 attributeContext.setPosition(0);
 assertFalse("Unexpected position set to negative number", attributeContext.isSetPosition());
 attributeContext.setPosition(1);
 assertTrue("Unexpected position not set to 1", attributeContext.isSetPosition());
 }

 @Test
 public void testNextNode() {
 attributeContext.nextNode();
 assertFalse("Unexpected nextNode returned false", attributeContext.isSetStarted());
 assertNull("Unexpected currentNodePointer set before nextNode", attributeContext.getCurrentNodePointer());

 NodeIterator it = parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest);
 it.setPosition(1);
 nodePointer = it.getNodePointer();
 attributeContext.nextNode();
 assertFalse("Unexpected nextNode returned false after first one", !attributeContext.isSetStarted());
 assertEquals("Unexpected currentNodePointer not set", nodePointer, attributeContext.getCurrentNodePointer());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextNodeNegativePosition() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(-1);
 attributeContext.nextNode();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionNegativePosition() {
 attributeContext.setPosition(-1);
 }

 @Test
 public void testNextNodeAfterLastNode() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 assertTrue("Unexpected nextNode did not return false after last node", !attributeContext.nextNode());
 }

 @Test
 public void testNextNodeAfterLastNodeReset() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.reset();
 attributeContext.nextNode();
 assertFalse("Unexpected nextNode did not return false after last node and reset", !attributeContext.nextNode());
 }
}