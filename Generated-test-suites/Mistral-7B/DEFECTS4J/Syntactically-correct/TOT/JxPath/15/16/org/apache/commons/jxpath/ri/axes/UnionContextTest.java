import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;
 private Document document;

 @Before
 public void setup() {
 // Create two contexts to be united in the UnionContext
 contextA = new EvalContext(null, new BasicNodeSet());
 contextB = new EvalContext(null, new BasicNodeSet());

 // Set up some nodes for testing
 document = createDocument();
 NodePointer nodeA = new NodePointer(document, createElement("A", "a"));
 NodePointer nodeB = new NodePointer(document, createElement("B", "b"));
 NodePointer nodeC = new NodePointer(document, createElement("C", "c"));
 NodePointer nodeD = new NodePointer(document, createElement("D", "d"));

 contextA.setNodeSet(new BasicNodeSet(nodeA, nodeB));
 contextB.setNodeSet(new BasicNodeSet(nodeC, nodeD));
 }

 @Test
 public void testUnionContext_EmptyContexts() {
 unionContext = new UnionContext(null, new EvalContext[0]);
 assertEquals(0, unionContext.getPosition());
 assertTrue(unionContext.nextSet());
 assertEquals(0, unionContext.getPosition());
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testUnionContext_SingleContext() {
 unionContext = new UnionContext(null, new EvalContext[] { contextA });
 assertEquals(contextA.getPosition(), unionContext.getPosition());
 assertTrue(unionContext.nextSet());
 int pos = unionContext.getPosition();
 assertFalse(unionContext.nextNode());
 assertFalse(unionContext.nextSet());
 assertEquals(pos, unionContext.getPosition());
 }

 @Test
 public void testUnionContext_MultipleContexts() {
 unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 assertEquals(contextA.getPosition(), unionContext.getPosition());
 assertTrue(unionContext.nextSet());
 int pos = unionContext.getPosition();
 assertTrue(unionContext.nextNode()); // should return nodeA from contextA
 assertEquals(contextB.getNodeSet().size(), unionContext.getNodeSet().size());
 assertContains(unionContext.getNodeSet(), nodeA);
 assertContains(unionContext.getNodeSet(), nodeB);
 assertContains(unionContext.getNodeSet(), nodeC);
 assertContains(unionContext.getNodeSet(), nodeD);

 // Test that we can iterate through all nodes in both contexts
 while (unionContext.nextNode()) {
 // Do something with the current node
 }

 // Test that we've returned to the starting position
 assertEquals(pos, unionContext.getPosition());
 }

 private Document createDocument() {
 // Create a simple XML document for testing
 return new DocumentImpl(); // Replace this with a valid Document implementation
 }

 private void assertContains(BasicNodeSet nodeSet, NodePointer node) {
 assertTrue("Expected to find node " + node, nodeSet.contains(node));
 }
}