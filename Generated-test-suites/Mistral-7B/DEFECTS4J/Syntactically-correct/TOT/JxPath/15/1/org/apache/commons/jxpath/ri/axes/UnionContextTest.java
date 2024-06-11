import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;
 private NodePointer nodeA;
 private NodePointer nodeB;
 private NodePointer nodeC;
 private NodePointer nodeD;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());
 nodeA = new NodePointer("nodeA", 1);
 nodeB = new NodePointer("nodeB", 2);
 nodeC = new NodePointer("nodeC", 3);
 nodeD = new NodePointer("nodeD", 4);

 contextA.setCurrentNodePointer(nodeA);
 contextB.setCurrentNodePointer(nodeB);
 }

 @Test
 public void testUnionContext_singleNodeInEachContext() {
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet();
 unionContext.nextNode();
 assertEquals(nodeA.getName(), unionContext.getCurrentNodePointer().getName());
 unionContext.nextSet();
 unionContext.nextNode();
 assertEquals(nodeB.getName(), unionContext.getCurrentNodePointer().getName());
 }

 @Test
 public void testUnionContext_sameNodeInBothContexts() {
 contextA.add(nodeA);
 contextB.add(nodeB);
 contextA.add(nodeB);
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet();
 unionContext.nextNode();
 assertEquals(nodeA.getName(), unionContext.getCurrentNodePointer().getName());
 unionContext.nextSet();
 unionContext.nextNode();
 assertEquals(nodeB.getName(), unionContext.getCurrentNodePointer().getName());
 }

 @Test
 public void testUnionContext_noCommonNodes() {
 contextA.add(nodeA);
 contextB.add(nodeC);
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet();
 assertNull(unionContext.getCurrentNodePointer());
 unionContext.nextSet();
 assertEquals(nodeC.getName(), unionContext.getCurrentNodePointer().getName());
 }

 @Test
 public void testUnionContext_multipleNodesInEachContext() {
 contextA.add(nodeA);
 contextA.add(nodeD);
 contextB.add(nodeB);
 contextB.add(nodeC);
 contextB.add(nodeD);
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet();
 assertEquals(nodeA.getName(), unionContext.getCurrentNodePointer().getName());
 unionContext.nextSet();
 assertEquals(nodeB.getName(), unionContext.getCurrentNodePointer().getName());
 unionContext.nextSet();
 assertEquals(nodeC.getName(), unionContext.getCurrentNodePointer().getName());
 unionContext.nextSet();
 assertEquals(nodeD.getName(), unionContext.getCurrentNodePointer().getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContext_contextsCannotBeNull() {
 unionContext = new UnionContext(null, contextB);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContext_parentContextCannotBeNull() {
 unionContext = new UnionContext(contextA, null);
 }
}