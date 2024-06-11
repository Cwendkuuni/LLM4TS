import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {

 @Test
 public void testUnionContext() {
 // Given: Two EvalContexts with different nodes
 EvalContext contextA = new EvalContext(new JXPathContext(new Object[] { new Object(), new Object(), new Object() }), new BasicNodeSet());
 contextA.addNode(new NodePointer(0, 0));
 contextA.addNode(new NodePointer(0, 1));

 EvalContext contextB = new EvalContext(new JXPathContext(new Object[] { new Object(), new Object(), new Object(), new Object() }), new BasicNodeSet());
 contextB.addNode(new NodePointer(0, 2));
 contextB.addNode(new NodePointer(0, 3));

 // When: Creating a UnionContext with those EvalContexts
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });

 // Then: The UnionContext should contain all nodes from both contexts
 NodeSet nodeSet = unionContext.getNodeSet();
 NodePointer[] nodes = nodeSet.toArray(new NodePointer[nodeSet.size()]);

 // Checking nodes are in the correct order
 Assert.assertEquals("First node should be from context A", 0, nodes[0].getDocumentIndex());
 Assert.assertEquals("Second node should be from context A", 1, nodes[1].getDocumentIndex());
 Assert.assertEquals("Third node should be from context B", 2, nodes[2].getDocumentIndex());
 Assert.assertEquals("Fourth node should be from context B", 3, nodes[3].getDocumentIndex());
 }

 @Test
 public void testUnionContext_emptyContexts() {
 // Given: Two empty EvalContexts
 EvalContext contextA = new EvalContext(new JXPathContext(new Object[0]), new BasicNodeSet());
 EvalContext contextB = new EvalContext(new JXPathContext(new Object[0]), new BasicNodeSet());

 // When: Creating a UnionContext with those EvalContexts
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });

 // Then: The UnionContext should be empty
 NodeSet nodeSet = unionContext.getNodeSet();
 Assert.assertEquals("UnionContext should be empty", 0, nodeSet.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContext_nullContexts() {
 // Given: One or more null EvalContexts
 EvalContext contextA = null;
 EvalContext contextB = new EvalContext(new JXPathContext(new Object[0]), new BasicNodeSet());
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContext_noContexts() {
 // Given: No EvalContexts
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 }
}