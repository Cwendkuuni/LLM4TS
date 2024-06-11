import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.RITestCase;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.apache.commons.jxpath.ri.model.NodeSetContextTest;

public class UnionContextTest extends RITestCase {

 /**
 * Test constructor
 */
 public void testUnionContextConstructor() {
 EvalContext context1 = new NodeSetContextTest.ContextWithSingleNode();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 assertNotNull(unionContext);
 }

 /**
 * Test getDocumentOrder()
 */
 public void testGetDocumentOrder() {
 EvalContext context1 = new NodeSetContextTest.ContextWithSingleNode();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 assertEquals(1, unionContext.getDocumentOrder());
 }

 /**
 * Test setPosition() with one context
 */
 public void testSetPositionWithOneContext() {
 EvalContext context = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context});
 unionContext.setPosition(0);
 NodePointer expectedNodePointer = context.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();
 assertSame(expectedNodePointer, actualNodePointer);
 }

 /**
 * Test setPosition() with multiple contexts and position within the first context
 */
 public void testSetPositionWithMultipleContextsAndFirstContext() {
 EvalContext context1 = new NodeSetContextTest.ContextWithTwoNodes();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 unionContext.setPosition(0);
 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer1.getNode(), actualNodePointer.getNode());
 assertNull(actualNodePointer.getIndex());
 }

 /**
 * Test setPosition() with multiple contexts and position within the second context
 */
 public void testSetPositionWithMultipleContextsAndSecondContext() {
 EvalContext context1 = new NodeSetContextTest.ContextWithTwoNodes();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 unionContext.setPosition(1);
 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer2.getNode(), actualNodePointer.getNode());
 assertNotNull(actualNodePointer.getIndex());
 }

 /**
 * Test setPosition() with multiple contexts and position out of bounds
 */
 public void testSetPositionWithMultipleContextsAndOutOfBounds() {
 EvalContext context1 = new NodeSetContextTest.ContextWithTwoNodes();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 unionContext.setPosition(3);
 assertFalse(unionContext.isPositionValid());
 }

 /**
 * Test setPosition() with multiple contexts and position within the union context
 */
 public void testSetPositionWithMultipleContextsAndWithinUnionContext() {
 EvalContext context1 = new NodeSetContextTest.ContextWithTwoNodes();
 EvalContext context2 = new NodeSetContextTest.ContextWithSingleNode();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.setPosition(3);
 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();
 assertNull(expectedNodePointer1.getIndex());
 assertNull(expectedNodePointer2.getIndex());
 assertNull(actualNodePointer.getIndex());
 NodePointer nodePointer1 = context1.getNodeSet().getNode(0);
 NodePointer nodePointer2 = context1.getNodeSet().getNode(1);
 NodePointer nodePointer3 = context2.getNodeSet().getNode(0);
 assertEquals(nodePointer3.getNode(), actualNodePointer.getNode());
 }
}