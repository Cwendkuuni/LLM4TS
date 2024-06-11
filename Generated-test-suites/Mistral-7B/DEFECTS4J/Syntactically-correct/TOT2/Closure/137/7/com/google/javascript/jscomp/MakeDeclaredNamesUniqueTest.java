import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

 private Node createTestNode() {
 Node testNode = new Node();
 Node functionNode = Node.newFunctionNode(
 "myFunction", null, ImmutableSet.of(), Lists.newArrayList(
 new Node("param1"), new Node("param2")), null, null);
 Node bodyNode = Node.newBlockNode(null);
 bodyNode.addChild(Node.newNameNode("result", Node.NAME));
 bodyNode.addChild(Node.newAssignNode(
 "result", Node.NAME, Node.newNameNode("param1"), Node.op_PLUS,
 Node.newNameNode("param2"), null));
 functionNode.setFirstChild(bodyNode);
 testNode.setFirstChild(functionNode);
 return testNode;
 }

 @Test
 public void testMakeDeclaredNamesUniqueEmpty() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 nodeTraversal.traverse(testNode, null);
 Iterator<Map.Entry<String, String>> iterator =
 makeDeclaredNamesUnique.nameStack.iterator();
 assertEquals(0, iterator.hasNext());
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionWithTwoParams() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 nodeTraversal.traverse(testNode, null);
 Iterator<Map.Entry<String, String>> iterator =
 makeDeclaredNamesUnique.nameStack.iterator();
 Map.Entry<String, String> entry = iterator.next();
 assertEquals("param1$$0", entry.getKey());
 assertEquals("param1$$1", entry.getValue());
 assertEquals(0, iterator.hasNext());
 }

 @Test(expected = AssertionError.class)
 public void testMakeDeclaredNamesUniqueDuplicateFunctionName() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 Node duplicateFunctionNode = Node.newFunctionNode(
 "myFunction", null, ImmutableSet.of(), Lists.newArrayList(),
 null, null);
 testNode.addChild(functionNode);
 testNode.addChild(duplicateFunctionNode);
 nodeTraversal.traverse(testNode, null);
 }

 @Test(expected = AssertionError.class)
 public void testMakeDeclaredNamesUniqueDuplicateVariableName() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 Node varNode1 = Node.newVarNode(
 "result", Node.VAR_DECLARE, Node.NO_INIT, Node.NAME, null);
 Node varNode2 = Node.newVarNode(
 "result", Node.VAR_DECLARE, Node.NO_INIT, Node.NAME, null);
 testNode.addChild(varNode1);
 testNode.addChild(varNode2);
 nodeTraversal.traverse(testNode, null);
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionWithSameNameParameter() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 Node sameNameParamNode1 = Node.newNameNode("param1");
 Node sameNameParamNode2 = Node.newNameNode("param1");
 testNode.getFirstChild().getFirstChild().setFirstChild(sameNameParamNode1);
 testNode.getFirstChild().getLastChild().getFirstChild().setFirstChild(sameNameParamNode2);
 nodeTraversal.traverse(testNode, null);
 Iterator<Map.Entry<String, String>> iterator =
 makeDeclaredNamesUnique.nameStack.iterator();
 Map.Entry<String, String> entry1 = iterator.next();
 assertEquals("param1$$0", entry1.getKey());
 Map.Entry<String, String> entry2 = iterator.next();
 assertEquals("param1$$1", entry2.getKey());
 assertEquals(0, iterator.hasNext());
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionWithGlobalVariable() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 Node globalVarNode = Node.newVarNode(
 "result", Node.VAR_DECLARE, Node.NO_INIT, Node.NAME, null);
 testNode.setFirstChild(globalVarNode);
 Node functionNode = Node.newFunctionNode(
 "myFunction", null, ImmutableSet.of(),
 Lists.newArrayList(new Node("param1")), null, null);
 Node bodyNode = Node.newBlockNode(null);
 bodyNode.addChild(Node.newAssignNode(
 "result", Node.NAME, Node.newNameNode("param1"), Node.op_PLUS,
 Node.newNameNode("param1"), null));
 functionNode.setFirstChild(bodyNode);
 testNode.addChild(functionNode);
 NodeTraversal testNodeTraversal = new NodeTraversal();
 testNodeTraversal.addCallback(makeDeclaredNamesUnique);
 testNodeTraversal.traverse(testNode, null);
 Iterator<Map.Entry<String, String>> iterator =
 makeDeclaredNamesUnique.nameStack.iterator();
 Map.Entry<String, String> entry1 = iterator.next();
 assertEquals("result$$0", entry1.getKey());
 Map.Entry<String, String> entry2 = iterator.next();
 assertEquals("param1$$0", entry2.getKey());
 assertEquals(0, iterator.hasNext());
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionWithGlobalVariableRenamed() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal();
 nodeTraversal.addCallback(makeDeclaredNamesUnique);
 Node testNode = createTestNode();
 Node globalVarNode = Node.newVarNode(
 "result", Node.VAR_DECLARE, Node.NO_INIT, Node.NAME, null);
 testNode.setFirstChild(globalVarNode);
 Node functionNode = Node.newFunctionNode(
 "myFunction", null, ImmutableSet.of(),
 Lists.newArrayList(new Node("param1")), null, null);
 Node bodyNode = Node.newBlockNode(null);
 bodyNode.addChild(Node.newAssignNode(
 "result$$1", Node.NAME, Node.newNameNode("param1"), Node.op_PLUS,
 Node.newNameNode("param1"), null));
 functionNode.setFirstChild(bodyNode);
 testNode.addChild(functionNode);
 NodeTraversal testNodeTraversal = new NodeTraversal();
 testNodeTraversal.addCallback(makeDeclaredNamesUnique);
 testNodeTraversal.traverse(testNode, null);
 Iterator<Map.Entry<String, String>> iterator =
 makeDeclaredNamesUnique.nameStack.iterator();
 Map.Entry<String, String> entry1 = iterator.next();
 assertEquals("result$$1", entry1.getKey());
 Map.Entry<String, String> entry2 = iterator.next();
 assertEquals("param1$$0", entry2.getKey());
 assertEquals(0, iterator.hasNext());
 }

}