import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 @Test
 public void testUnionContext() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 assertEquals("UnionContext should have empty node set", new BasicNodeSet(), unionContext.getNodeSet());

 EvalContext context1 = new EvalContext(new BasicNodeSet(), null);
 context1.add(new NodePointer("context1-node1"));
 context1.add(new NodePointer("context1-node2"));

 EvalContext context2 = new EvalContext(new BasicNodeSet(), null);
 context2.add(new NodePointer("context2-node1"));
 context2.add(new NodePointer("context2-node2"));
 context2.add(new NodePointer("context2-node1")); // duplicate node

 UnionContext unionContext1 = new UnionContext(null, new EvalContext[]{context1, context2});
 assertEquals("UnionContext should have 4 nodes in node set", 4, unionContext1.getNodeSet().size());

 // check nodes
 NodePointer node1 = unionContext1.getNodeSet().get(0);
 assertEquals("UnionContext node1 should be context1-node1", "context1-node1", node1.getNodeName());
 NodePointer node2 = unionContext1.getNodeSet().get(1);
 assertEquals("UnionContext node2 should be context1-node2", "context1-node2", node2.getNodeName());
 NodePointer node3 = unionContext1.getNodeSet().get(2);
 assertEquals("UnionContext node3 should be context2-node1", "context2-node1", node3.getNodeName());
 NodePointer node4 = unionContext1.getNodeSet().get(3);
 assertEquals("UnionContext node4 should be context2-node2", "context2-node2", node4.getNodeName());

 // check document order
 assertTrue("UnionContext document order should be set to 1", unionContext1.getDocumentOrder() == 1);

 // test setPosition
 unionContext1.setPosition(0);
 assertTrue("UnionContext position 0 should be context1-node1", node1.equals(unionContext1.getCurrentNode()));
 unionContext1.setPosition(1);
 assertTrue("UnionContext position 1 should be context1-node2", node2.equals(unionContext1.getCurrentNode()));
 unionContext1.setPosition(2);
 assertTrue("UnionContext position 2 should be context2-node1", node3.equals(unionContext1.getCurrentNode()));
 unionContext1.setPosition(3);
 assertTrue("UnionContext position 3 should be context2-node2", node4.equals(unionContext1.getCurrentNode()));
 unionContext1.setPosition(4); // out of bound
 assertNull("UnionContext position 4 should return null", unionContext1.getCurrentNode());

 // test nextNode
 assertFalse("UnionContext nextNode before first node should return false", unionContext1.nextNode());
 unionContext1.setPosition(0);
 assertTrue("UnionContext nextNode after first node should return true", unionContext1.nextNode());
 unionContext1.setPosition(3);
 assertTrue("UnionContext nextNode after last node should return false", !unionContext1.nextNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextWithEmptyContexts() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextWithNullContexts() {
 UnionContext unionContext = new UnionContext(null, null);
 }
}