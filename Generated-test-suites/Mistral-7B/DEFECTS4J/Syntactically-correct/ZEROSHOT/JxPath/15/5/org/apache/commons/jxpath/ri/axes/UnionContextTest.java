import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 public void testUnionContextEmptyContexts() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 assertTrue(unionContext.getNodeSet().isEmpty());
 }

 @Test
 public void testUnionContextOneContext() {
 UnionContext unionContext = new UnionContext(null, contextA);
 assertTrue(unionContext.getNodeSet().isEmpty());
 }

 @Test
 public void testUnionContextTwoContextsEmptyNodes() {
 contextA.nextSet();
 contextB.nextSet();
 UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 assertTrue(unionContext.getNodeSet().isEmpty());
 }

 @Test
 public void testUnionContextTwoContextsSameNodes() {
 contextA.nextSet();
 contextB.nextSet();
 contextA.nextNode();
 contextB.nextNode();
 contextA.nextNode();
 unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer unionNodeA = ((BasicNodeSet) unionContext.getNodeSet()).get(0);
 NodePointer unionNodeB = ((BasicNodeSet) unionContext.getNodeSet()).get(1);
 assertTrue(unionNodeA.equals(nodeA));
 assertTrue(unionNodeB.equals(nodeB));
 }

 @Test
 public void testUnionContextTwoContextsWithDifferentNodes() {
 contextA.nextSet();
 contextA.nextNode();
 contextB.nextSet();
 contextB.nextNode();
 unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer unionNodeA = ((BasicNodeSet) unionContext.getNodeSet()).get(0);
 NodePointer unionNodeB = ((BasicNodeSet) unionContext.getNodeSet()).get(1);
 assertFalse(unionNodeA.equals(nodeB));
 }

 @Test
 public void testUnionContextTwoContextsWithCommonNodes() {
 contextA.nextSet();
 contextA.nextNode();
 contextB.nextSet();
 contextB.nextNode();
 contextA.nextNode();
 unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer unionNodeA = ((BasicNodeSet) unionContext.getNodeSet()).get(0);
 NodePointer unionNodeB = ((BasicNodeSet) unionContext.getNodeSet()).get(1);
 NodePointer commonNode = contextA.getCurrentNodePointer();
 NodePointer commonUnionNode = ((BasicNodeSet) unionContext.getNodeSet()).get(2);
 assertTrue(unionNodeA.equals(nodeA));
 assertTrue(unionNodeB.equals(nodeB));
 assertTrue(commonNode.equals(commonUnionNode));
 }

 @Test
 public void testUnionContextPosition() {
 contextA.nextSet();
 contextA.nextNode();
 contextB.nextSet();
 contextB.nextNode();
 contextA.nextNode();
 unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 // Position 0 should be the pointer from the first context
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer unionNodeA = ((BasicNodeSet) unionContext.getNodeSet()).get(0);
 assertTrue(unionNodeA.equals(nodeA));
 // Position 1 should be the pointer from the second context
 unionContext.setPosition(1);
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer unionNodeB = ((BasicNodeSet) unionContext.getNodeSet()).get(1);
 assertTrue(unionNodeB.equals(nodeB));
 // Position 2 should be the common pointer
 unionContext.setPosition(2);
 NodePointer commonNode = contextA.getCurrentNodePointer();
 NodePointer commonUnionNode = ((BasicNodeSet) unionContext.getNodeSet()).get(2);
 assertTrue(commonNode.equals(commonUnionNode));
 }
}