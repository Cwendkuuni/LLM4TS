import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private EvalContext contextA, contextB, unionContext;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

 contextA = new EvalContext(null, document);
 contextA.setNodeSet(new BasicNodeSet(document));
 contextA.pushContext(document);

 contextB = new EvalContext(null, document);
 contextB.setNodeSet(new BasicNodeSet(document));
 contextB.pushContext(document);

 unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, null);
 }

 @Test
 public void testUnionContext_EmptyContexts() {
 assertTrue(unionContext.nextSet());
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testUnionContext_SingleNodesInEachContext() {
 Element rootA = document.createElement("rootA");
 Element rootB = document.createElement("rootB");
 document.appendChild(rootA);
 document.appendChild(rootB);

 contextA.setCurrentNodePointer(new NodePointer(rootA));
 contextB.setCurrentNodePointer(new NodePointer(rootB));

 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertTrue(unionContext.getCurrentNodePointer().getNode() == rootA.getNode());
 unionContext.nextNode();
 assertTrue(unionContext.getCurrentNodePointer().getNode() == rootB.getNode());
 }

 @Test
 public void testUnionContext_NodesWithSamePositionInBothContexts() {
 Element a1 = document.createElement("a1");
 Element a2 = document.createElement("a2");
 Element b1 = document.createElement("b1");
 Element b2 = document.createElement("b2");
 document.appendChild(a1);
 document.appendChild(a2);
 document.appendChild(b1);
 document.appendChild(b2);

 contextA.setCurrentNodePointer(new NodePointer(a1));
 contextB.setCurrentNodePointer(new NodePointer(a1));

 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertTrue(unionContext.getCurrentNodePointer().getNode() == a1.getNode());
 }

 @Test
 public void testUnionContext_NodesWithDifferentPositionsInBothContexts() {
 Element a1 = document.createElement("a1");
 Element a2 = document.createElement("a2");
 Element b1 = document.createElement("b1");
 Element b2 = document.createElement("b2");
 document.appendChild(a1);
 document.appendChild(a2);
 document.appendChild(b1);
 document.appendChild(b2);

 contextA.setCurrentNodePointer(new NodePointer(a1));
 contextB.setCurrentNodePointer(new NodePointer(a2));

 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertTrue(unionContext.getCurrentNodePointer().getNode() == a1.getNode());
 unionContext.nextNode();
 assertTrue(unionContext.getCurrentNodePointer().getNode() == a2.getNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContext_MixedNodesAndEndOfSet() {
 Element a1 = document.createElement("a1");
 Element a2 = document.createElement("a2");
 Element b1 = document.createElement("b1");
 Element b2 = document.createElement("b2");
 document.appendChild(a1);
 document.appendChild(a2);
 document.appendChild(b1);
 document.appendChild(b2);

 contextA.setCurrentNodePointer(new NodePointer(a1));
 contextB.setCurrentNodePointer(new NodePointer(b2));

 assertTrue(unionContext.nextSet());
 assertTrue(unionContext.nextNode());
 assertTrue(unionContext.getCurrentNodePointer().getNode() == a1.getNode());
 unionContext.nextNode();
 unionContext.nextNode(); // should throw an exception
 }

 @Test
 public void testUnionContext_NodesFromDifferentContextsWithSameDocumentOrder() {
 Element a1 = document.createElement("a1");
 Element a2 = document.createElement("a2");
 Element b1 = document.createElement("b1");
 Element b2 = document.createElement("b2");
 document.appendChild(a1);
 document.appendChild(a2);
 document.appendChild(b1);
 document.appendChild(b2);

 contextA.setCurrentNodePointer(new NodePointer(a1));
 contextB.setCurrentNodePointer(new NodePointer(b1));
 List<NodePointer> nodePointersA = new ArrayList<>();
 List<NodePointer> nodePointersB = new ArrayList<>();
 while (contextA.nextSet()) {
 nodePointersA.add(contextA.getCurrentNodePointer());
 }
 while (contextB.nextSet()) {
 nodePointersB.add(contextB.getCurrentNodePointer());
 }
 unionContext.setDocumentOrder(1);

 List<NodePointer> expectedNodePointers = new ArrayList<>();
 expectedNodePointers.addAll(nodePointersA);
 expectedNodePointers.addAll(nodePointersB);

 int i = 0;
 while (unionContext.nextSet()) {
 List<NodePointer> currentNodePointers = new ArrayList<>();
 while (unionContext.nextNode()) {
 currentNodePointers.add(unionContext.getCurrentNodePointer());
 i++;
 }
 assertTrue(currentNodePointers.size() == expectedNodePointers.size());
 for (int j = 0; j < currentNodePointers.size(); j++) {
 assertTrue(currentNodePointers.get(j).getNode() == expectedNodePointers.get(j).getNode());
 }
 }
 }

 @Test
 public void testUnionContext_NodesFromDifferentContextsWithDifferentDocumentOrder() {
 Element a1 = document.createElement("a1");
 Element a2 = document.createElement("a2");
 Element b1 = document.createElement("b1");
 Element b2 = document.createElement("b2");
 document.appendChild(a1);
 document.appendChild(a2);
 document.appendChild(b1);
 document.appendChild(b2);

 contextA.setCurrentNodePointer(new NodePointer(a2));
 contextB.setCurrentNodePointer(new NodePointer(b1));
 List<NodePointer> nodePointersA = new ArrayList<>();
 List<NodePointer> nodePointersB = new ArrayList<>();
 while (contextA.nextSet()) {
 nodePointersA.add(contextA.getCurrentNodePointer());
 }
 while (contextB.nextSet()) {
 nodePointersB.add(contextB.getCurrentNodePointer());
 }
 unionContext.setDocumentOrder(0);

 List<NodePointer> expectedNodePointers = new ArrayList<>();
 expectedNodePointers.addAll(nodePointersB);
 expectedNodePointers.addAll(nodePointersA);

 int i = 0;
 while (unionContext.nextSet()) {
 List<NodePointer> currentNodePointers = new ArrayList<>();
 while (unionContext.nextNode()) {
 currentNodePointers.add(unionContext.getCurrentNodePointer());
 i++;
 }
 assertTrue(currentNodePointers.size() == expectedNodePointers.size());
 for (int j = 0; j < currentNodePointers.size(); j++) {
 assertTrue(currentNodePointers.get(j).getNode() == expectedNodePointers.get(j).getNode());
 }
 }
 }
}