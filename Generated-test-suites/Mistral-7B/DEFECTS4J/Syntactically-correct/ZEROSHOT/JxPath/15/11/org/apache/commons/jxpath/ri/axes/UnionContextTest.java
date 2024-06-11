import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext createContext1() {
 return new EvalContext(new BasicNodeSet(), new Object[] {});
 }

 private EvalContext createContext2() {
 return new EvalContext(new BasicNodeSet(), new Object[] {});
 }

 @Test
 public void testUnionContextCreation() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext[] contexts = new EvalContext[] { createContext1(), createContext2() };
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 assertNotNull(unionContext.contexts);
 assertNotNull(unionContext.getNodeSet());
 }

 @Test
 public void testGetDocumentOrder() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context1 = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context2 = new EvalContext(new BasicNodeSet(), new Object[] {});
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context1 = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context2 = new EvalContext(new BasicNodeSet(), new Object[] {});
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

 context1.add("node1");
 context1.add("node2");
 context1.add("node3");
 context2.add("node3");
 context2.add("node4");
 context2.add("node5");

 unionContext.setPosition(3);
 NodePointer node = unionContext.getCurrentNodePointer();
 assertTrue(node.getNode().getClass().isInstance(context2.getCurrentNode()));
 }

 @Test
 public void testSetPositionOutOfBounds() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context1 = new EvalContext(new BasicNodeSet(), new Object[] {});
 EvalContext context2 = new EvalContext(new BasicNodeSet(), new Object[] {});
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

 context1.add("node1");
 context1.add("node2");
 context1.add("node3");
 context2.add("node3");
 context2.add("node4");
 context2.add("node5");

 unionContext.setPosition(10);
 assertFalse(unionContext.nextNode());
 }
}