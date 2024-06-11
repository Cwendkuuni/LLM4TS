import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());
 }

 @Test
 public void testUnionContext_emptyContexts() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 assertEquals(0, unionContext.getDocumentOrder());
 assertFalse(unionContext.nextSet());
 }

 @Test
 public void testUnionContext_oneContext() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA });
 assertEquals(0, unionContext.getDocumentOrder());
 assertTrue(unionContext.nextSet());
 assertFalse(unionContext.nextSet());
 }

 @Test
 public void testUnionContext_twoContexts() {
 NodePointer nodeA = contextA.addNode("A");
 NodePointer nodeB = contextB.addNode("B");
 NodePointer nodeC = contextB.addNode("C");

 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 assertEquals(1, unionContext.getDocumentOrder());
 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeA, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeB, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeC, unionContext.getCurrentNodePointer());
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testUnionContext_sameNodes() {
 NodePointer nodeA = contextA.addNode("A");
 NodePointer nodeB = contextB.addNode("B");
 NodePointer nodeC = contextB.addNode("B");

 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 assertEquals(1, unionContext.getDocumentOrder());
 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeA, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeB, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeC, unionContext.getCurrentNodePointer());
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testUnionContext_sameNodesDifferentOrder() {
 NodePointer nodeA = contextA.addNode("A");
 NodePointer nodeB = contextB.addNode("B");
 NodePointer nodeC = contextB.addNode("B");

 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextB, contextA });
 assertEquals(1, unionContext.getDocumentOrder());
 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeB, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeA, unionContext.getCurrentNodePointer());
 assertTrue(unionContext.nextNode());
 assertEquals(nodeC, unionContext.getCurrentNodePointer());
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testUnionContext_setPosition() {
 NodePointer nodeA = contextA.addNode("A");
 NodePointer nodeB = contextB.addNode("B");
 NodePointer nodeC = contextB.addNode("C");
 NodePointer nodeD = contextA.addNode("D");

 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.setPosition(3);

 assertTrue(unionContext.nextNode());
 assertEquals(nodeD, unionContext.getCurrentNodePointer());
 assertFalse(unionContext.nextNode());
 }
}