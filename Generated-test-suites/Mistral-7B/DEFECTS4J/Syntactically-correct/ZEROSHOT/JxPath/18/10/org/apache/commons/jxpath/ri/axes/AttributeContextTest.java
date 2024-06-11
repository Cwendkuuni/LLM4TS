import org.apache.commons.jxpath.ri.AXIS;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.TestAttributeContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private NodeTest nodeTest;

 @Before
 public void setUp() {
 parentContext = new TestAttributeContext(null);
 nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testConstructor() {
 Assert.assertNotNull(attributeContext);
 Assert.assertNotNull(attributeContext.getNodeTest());
 Assert.assertNotNull(attributeContext.getParentContext());
 }

 @Test
 public void testGetCurrentNodePointer() {
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertNull(currentNodePointer);
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 Assert.assertFalse(attributeContext.isStarted());
 Assert.assertNull(attributeContext.getIterator());
 }

 @Test
 public void testSetPosition() {
 attributeContext.setPosition(0);
 Assert.assertTrue(attributeContext.isStarted());
 Assert.assertEquals(0, attributeContext.getCurrentPosition());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionNegative() {
 attributeContext.setPosition(-1);
 }

 @Test
 public void testNextNode() {
 attributeContext.nextNode();
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertNotNull(currentNodePointer);
 Assert.assertEquals(AXIS.ATTRIBUTE, attributeContext.getAxis());
 }

 @Test
 public void testNextNodeNoMoreNodes() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 Assert.assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeSameNode() {
 attributeContext.nextNode();
 NodePointer currentNodePointer1 = attributeContext.getCurrentNodePointer();
 Assert.assertNotNull(currentNodePointer1);
 Assert.assertFalse(attributeContext.nextNode());
 NodePointer currentNodePointer2 = attributeContext.getCurrentNodePointer();
 Assert.assertEquals(currentNodePointer1, currentNodePointer2);
 }
}