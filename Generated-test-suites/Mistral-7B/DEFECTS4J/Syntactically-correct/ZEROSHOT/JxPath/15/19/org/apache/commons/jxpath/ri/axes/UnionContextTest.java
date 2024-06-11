import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UnionContextTest {

 private UnionContext unionContext;
 private EvalContext context1;
 private EvalContext context2;
 private NodeSetContext nodeSetContext;

 @Parameterized.Parameters(name = "{index}: UnionContext with {0} and {1}")
 public static Object[] data() {
 return new Object[][]{
 new Object[]{new BasicNodeSet(), new BasicNodeSet(), new BasicNodeSet()},
 new Object[]{new BasicNodeSet(), new BasicNodeSet(), new BasicNodeSet(), new BasicNodeSet()},
 new Object[]{new BasicNodeSet(), new BasicNodeSet(), new ArrayList<>(), new BasicNodeSet()}
 };
 }

 @Parameterized.Constructor
 public UnionContextTest(NodeSetContext nodeSet, EvalContext context1, EvalContext context2) {
 this.nodeSetContext = nodeSet;
 this.context1 = context1;
 this.context2 = context2;
 }

 @Before
 public void setup() {
 unionContext = new UnionContext(context1, context2);
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition_whenPositionIsLessThanTotalNodes_thenReturnsTrue() {
 int totalNodes = context1.size() + context2.size();
 assertTrue(unionContext.setPosition(0));
 assertTrue(unionContext.setPosition(1));
 // ... and so on
 assertFalse(unionContext.setPosition(totalNodes + 1));
 }

 @Test
 public void testSetPosition_whenPositionIsGreaterThanTotalNodes_thenReturnsFalse() {
 int totalNodes = context1.size() + context2.size();
 assertFalse(unionContext.setPosition(totalNodes));
 }

 @Test
 public void testSetPosition_whenContextsArePrepared_thenReturnsTrue() {
 unionContext.setPosition(context1.size());
 context1.nextSet();
 context2.nextSet();
 NodePointer node1 = context1.getCurrentNodePointer();
 NodePointer node2 = context2.getCurrentNodePointer();
 unionContext.nextNode();
 NodePointer currentNodePointer = unionContext.getCurrentNodePointer();
 assertNotSame(node1, currentNodePointer);
 assertNotSame(node2, currentNodePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_whenContextsAreNotPrepared_thenThrowsException() {
 unionContext.nextNode();
 unionContext.setPosition(context1.size());
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextSet();
 unionContext.nextNode();
 }
}