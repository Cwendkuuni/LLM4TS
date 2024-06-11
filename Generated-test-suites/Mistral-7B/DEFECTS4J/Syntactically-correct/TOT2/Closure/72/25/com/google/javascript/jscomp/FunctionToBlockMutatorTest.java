import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

 private FunctionToBlockMutator mutator;
 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 safeNameIdSupplier = () -> "test_id";
 mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 }

 @Test
 public void testMutateFunctionNoArguments() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node callNode = Node.newCallNode(functionNode, null);
 String resultName = "result";
 Node resultNode = Node.newIdentNode("result");
 Node mutatedNode = mutator.mutate(null, functionNode, callNode, resultName, false, false);

 assertEquals(Token.BLOCK, mutatedNode.getType());
 assertNotNull(mutatedNode.getFirstChild());
 assertNull(mutatedNode.getLastChild());
 }

 @Test
 public void testMutateFunctionWithArguments() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node arg1 = Node.newIdentNode("arg1");
 Node arg2 = Node.newIdentNode("arg2");
 Node callNode = Node.newCallNode(functionNode, new Node[]{arg1, arg2});
 String resultName = "result";
 Node resultNode = Node.newIdentNode("result");
 Node mutatedNode = mutator.mutate(null, functionNode, callNode, resultName, false, false);

 assertEquals(Token.BLOCK, mutatedNode.getType());
 assertNotNull(mutatedNode.getFirstChild());
 assertNull(mutatedNode.getLastChild());

 Map<String, Node> expectedArgs = Maps.newLinkedHashMap();
 expectedArgs.put("arg1", arg1);
 expectedArgs.put("arg2", arg2);
 Node expectedArgsNode = NodeUtil.newArgListNode(expectedArgs);
 Node actualArgsNode = (Node) mutatedNode.getFirstChild();
 assertEquals(expectedArgsNode, actualArgsNode);
 }

 @Test(expected = RuntimeException.class)
 public void testMutateFunctionWithException() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node callNode = Node.newCallNode(functionNode, null);
 String resultName = "result";
 Node resultNode = Node.newIdentNode("result");
 mutator.mutate(null, functionNode, callNode, resultName, false, true);
 }

 @Test(expected = RuntimeException.class)
 public void testMutateFunctionInLoop() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node callNode = Node.newCallNode(functionNode, null);
 String resultName = "result";
 Node resultNode = Node.newIdentNode("result");
 mutator.mutate(null, functionNode, callNode, resultName, true, false);
 }

 @Test
 public void testReplaceReturns() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node arg1 = Node.newIdentNode("arg1");
 Node arg2 = Node.newIdentNode("arg2");
 Node callNode = Node.newCallNode(functionNode, new Node[]{arg1, arg2});
 String resultName = "result";
 Node expectedResultNode = Node.newIdentNode("result");
 Node expectedReturnNode = Node.newReturnNode(expectedResultNode);
 Node mutatedNode = mutator.mutate(null, functionNode, callNode, resultName, false, false).getLastChild();
 assertEquals(Token.RETURN, mutatedNode.getType());
 assertEquals(expectedResultNode, mutatedNode.getFirstChild());
 }

 @Test
 public void testReplaceReturnsWithResult() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node arg1 = Node.newIdentNode("arg1");
 Node arg2 = Node.newIdentNode("arg2");
 Node callNode = Node.newCallNode(functionNode, new Node[]{arg1, arg2});
 String resultName = "result";
 Node expectedResultNode = Node.newIdentNode("result");
 Node expectedReturnNode = Node.newAssignNode(expectedResultNode, Node.newIdentNode("result"));
 Node mutatedNode = mutator.mutate(null, functionNode, callNode, resultName, true, false).getLastChild();
 assertEquals(Token.BREAK, mutatedNode.getType());
 assertEquals(Token.LABEL, mutatedNode.getFirstChild().getType());
 Node labelNode = (Node) mutatedNode.getFirstChild().getFirstChild();
 assertEquals("test_label", labelNode.getString(Token.LABEL_NAME));
 assertEquals(expectedResultNode, mutatedNode.getFirstChild().getLastChild());
 }

 @Test
 public void testReplaceReturnsWithLabel() {
 Node functionNode = Node.newFunctionNode(Token.FUNCTION, "testFunc", null, null, null);
 Node arg1 = Node.newIdentNode("arg1");
 Node arg2 = Node.newIdentNode("arg2");
 Node callNode = Node.newCallNode(functionNode, new Node[]{arg1, arg2});
 String resultName = "result";
 String labelName = "test_label";
 Node expectedLabelNode = Node.newLabelNode();
 Node expectedBreakNode = Node.newBreakNode(expectedLabelNode);
 Node mutatedNode = mutator.mutate(null, functionNode, callNode, resultName, false, false).getLastChild();
 assertEquals(Token.BLOCK, mutatedNode.getType());
 Node expectedBlockNode = NodeUtil.newBlockNode(new Node[]{expectedLabelNode, mutatedNode.getFirstChild()});
 assertEquals(expectedBlockNode, mutatedNode);
 }
}