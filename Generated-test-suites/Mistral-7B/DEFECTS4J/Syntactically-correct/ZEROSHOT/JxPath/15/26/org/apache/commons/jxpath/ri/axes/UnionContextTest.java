import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;
 private BasicNodeSet nodeSet;

 @Before
 public void setup() {
 // Create EvalContexts A and B
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());

 // Prepare some nodes for testing
 NodePointer nodeA1 = new NodePointer("docA:1");
 NodePointer nodeA2 = new NodePointer("docA:2");
 NodePointer nodeB1 = new NodePointer("docB:1");
 NodePointer nodeB2 = new NodePointer("docB:2");

 // Set some nodes for EvalContexts A and B
 contextA.setCurrentNodePointer(nodeA1);
 contextB.setCurrentNodePointer(nodeB1);

 // Create UnionContext with EvalContexts A and B
 unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

 // Set NodeSet for UnionContext
 nodeSet = (BasicNodeSet) unionContext.getNodeSet();
 }

 @Test
 public void testUnionContext_getDocumentOrder() {
 int documentOrder = unionContext.getDocumentOrder();
 assertTrue(documentOrder == 1);
 }

 @Test
 public void testUnionContext_setPosition_whenPositionIsWithinBothContexts() {
 int position = 0;
 assertTrue(unionContext.setPosition(position));

 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNotNull(currentNode);

 // Check that current node is from the first context (contextA)
 assertTrue(contextA.getCurrentNodePointer().equals(currentNode));
 }

 @Test
 public void testUnionContext_setPosition_whenPositionIsOutsideBothContexts() {
 int position = 2;
 assertFalse(unionContext.setPosition(position));
 }

 @Test
 public void testUnionContext_setPosition_whenPositionIsWithinFirstContext() {
 int position = 1;
 contextA.nextSet();
 contextA.nextNode();

 assertTrue(unionContext.setPosition(position));

 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNotNull(currentNode);

 // Check that current node is from the first context (contextA)
 assertTrue(contextA.getCurrentNodePointer().equals(currentNode));
 }

 @Test
 public void testUnionContext_setPosition_whenPositionIsWithinSecondContext() {
 int position = 1;
 contextB.nextSet();
 contextB.nextNode();

 assertTrue(unionContext.setPosition(position));

 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNotNull(currentNode);

 // Check that current node is from the second context (contextB)
 assertTrue(contextB.getCurrentNodePointer().equals(currentNode));
 }

 @Test
 public void testUnionContext_setPosition_whenPositionIsOutsideBothContextsAndNextSet() {
 int position = 2;
 unionContext.nextSet();

 assertTrue(unionContext.setPosition(position));

 NodePointer currentNode = unionContext.getCurrentNodePointer();
 assertNull(currentNode);
 }
}