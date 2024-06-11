import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 @Test
 public void testEmptyContextsUnion() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 JXPathContext jxPathContext = JXPathContext.newContext(unionContext);

 assertFalse(jxPathContext.nextSet());
 }

 @Test
 public void testSingleContextUnion() {
 EvalContext contextA = new EvalContext(new BasicNodeSet(), null);
 contextA.setCurrentNodePointer(new NodePointer(1));
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA });
 JXPathContext jxPathContext = JXPathContext.newContext(unionContext);

 assertTrue(jxPathContext.nextSet());
 assertTrue(jxPathContext.nextNode());
 }

 @Test
 public void testMultipleContextsWithDuplicateNodesUnion() {
 EvalContext contextA = new EvalContext(new BasicNodeSet(), null);
 contextA.setCurrentNodePointer(new NodePointer(1));
 EvalContext contextB = new EvalContext(new BasicNodeSet(), null);
 contextB.setCurrentNodePointer(new NodePointer(1));
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 JXPathContext jxPathContext = JXPathContext.newContext(unionContext);

 assertTrue(jxPathContext.nextSet());
 assertTrue(jxPathContext.nextNode()); // first node from contextA
 assertTrue(jxPathContext.nextNode()); // first node from contextB
 assertTrue(jxPathContext.nextNode()); // next node from contextB (not duplicated)
 assertFalse(jxPathContext.nextNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testMultipleContextsWithDuplicateNodesAndInvalidPositionUnion() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { createContextWithOnlyOneNode(), createContextWithOnlyOneNode() });
 unionContext.setPosition(1);
 }

 @Test
 public void testMultipleContextsWithNoDuplicateNodesUnion() {
 EvalContext contextA = new EvalContext(new BasicNodeSet(), null);
 contextA.setCurrentNodePointer(new NodePointer(1));
 EvalContext contextB = new EvalContext(new BasicNodeSet(), null);
 contextB.setCurrentNodePointer(new NodePointer(2));
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 JXPathContext jxPathContext = JXPathContext.newContext(unionContext);

 assertTrue(jxPathContext.nextSet());
 assertTrue(jxPathContext.nextNode()); // first node from contextA
 assertTrue(jxPathContext.nextNode()); // first node from contextB
 assertFalse(jxPathContext.nextNode());
 }

 private EvalContext createContextWithOnlyOneNode() {
 EvalContext context = new EvalContext(new BasicNodeSet(), null);
 context.setCurrentNodePointer(new NodePointer(1));
 context.nextNode();
 return context;
 }
}