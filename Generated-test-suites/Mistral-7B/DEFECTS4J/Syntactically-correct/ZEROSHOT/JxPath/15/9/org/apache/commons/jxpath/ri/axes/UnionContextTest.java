import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private NodeSetContext unionContext;
 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext parentContext;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());
 parentContext = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
 }

 @Test
 public void testGetDocumentOrder() {
 int documentOrder = unionContext.getDocumentOrder();
 assertEquals(1, documentOrder);
 }

 @Test
 public void testSetPosition_whenContextsAreNotPrepared_shouldSetPositionCorrectly() {
 int initialPosition = unionContext.getSize();
 unionContext.setPosition(initialPosition);
 int position = initialPosition + 1;
 assertTrue(unionContext.setPosition(position));

 int currentPosition = unionContext.getCurrentPosition();
 assertEquals(position, currentPosition);
 }

 @Test
 public void testSetPosition_whenContextsArePrepared_shouldThrowException() {
 unionContext.setPosition(0); // prepare contexts
 try {
 unionContext.setPosition(0);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
}

 @Test
 public void testNextSet_whenContextsArePrepared_shouldReturnTrue() {
 unionContext.setPosition(0); // prepare contexts
 assertTrue(unionContext.nextSet());
}

 @Test
 public void testNextNode_whenContextsArePrepared_shouldReturnNodesFromBothContexts() {
 unionContext.setPosition(0); // prepare contexts
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 // check nodes from contextA
 while (unionContext.nextNode()) {
 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNotSame(nodeA, currentNode);
 nodeA = currentNode;
 }

 // check nodes from contextB
 unionContext.nextSet();
 while (unionContext.nextNode()) {
 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNotSame(nodeB, currentNode);
 nodeB = currentNode;
 }

 // check that all nodes are unique
 assertEquals(unionContext.getSize(), unionContext.getPointerCount());
}

}