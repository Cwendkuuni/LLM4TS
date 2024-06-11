import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

 private FunctionToBlockMutator functionToBlockMutator;
 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setUp() {
 compiler = new NoOpCompiler();
 safeNameIdSupplier = () -> "test";
 functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 }

 @Test
 public void testMutateFunctionWithNoArguments() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction", Lists.newArrayList(),
 NodeUtil.newBlock("return 1;"));
 Node callNode = NodeUtil.newCallNode("testFunction");
 String resultName = "result";
 Node resultNode = NodeUtil.newVarNode(resultName);
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 assertEquals("1", NodeUtil.getRawText(injectableBlock));
 }

 @Test
 public void testMutateFunctionWithOneArgument() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction",
 Lists.newArrayList(NodeUtil.newParameterNode("arg")),
 NodeUtil.newBlock("return arg;"));
 Node callNode = NodeUtil.newCallNode("testFunction", Lists.newArrayList(
 NodeUtil.newNumberNode(1)));
 String resultName = "result";
 Node resultNode = NodeUtil.newVarNode(resultName);
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 assertEquals("1", NodeUtil.getRawText(injectableBlock));
 }

 @Test
 public void testMutateFunctionWithMultipleArguments() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction",
 Lists.newArrayList(NodeUtil.newParameterNode("arg1"),
 NodeUtil.newParameterNode("arg2")),
 NodeUtil.newBlock("return arg1 + arg2;"));
 Node callNode = NodeUtil.newCallNode("testFunction",
 Lists.newArrayList(NodeUtil.newNumberNode(1), NodeUtil.newNumberNode(2)));
 String resultName = "result";
 Node resultNode = NodeUtil.newVarNode(resultName);
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 assertEquals("3", NodeUtil.getRawText(injectableBlock));
 }

 @Test
 public void testMutateFunctionWithInlineArguments() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction",
 Lists.newArrayList(NodeUtil.newParameterNode("arg")),
 NodeUtil.newBlock("return arg;"));
 Node callNode = NodeUtil.newCallNode("testFunction",
 Lists.newArrayList(NodeUtil.newIdentifierNode("argValue")));
 String resultName = "result";
 Node resultNode = NodeUtil.newVarNode(resultName);
 LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
 argMap.put("arg", NodeUtil.newIdentifierNode("argValue"));
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 assertEquals("argValue", NodeUtil.getRawText(
 injectableBlock.getFirstChild().getFirstChild()));
 }

 @Test
 public void testMutateFunctionWithException() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction",
 Lists.newArrayList(), NodeUtil.newBlock("throw new Error('test')"));
 Node callNode = NodeUtil.newCallNode("testFunction");
 String resultName = "result";
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 try {
 NodeUtil.evaluateToNormal(injectableBlock);
 fail("Expected an error to be thrown");
 } catch (Error e) {
 assertEquals("test", e.getMessage());
 }
 }

 @Test
 public void testMutateFunctionWithCallInLoop() {
 Node fnNode = NodeUtil.newFunctionNode("testFunction",
 Lists.newArrayList(), NodeUtil.newBlock("return 1;"));
 Node callNode = NodeUtil.newCallNode("testFunction");
 String resultName = "result";
 Node resultNode = NodeUtil.newVarNode(resultName);
 Node injectableBlock = functionToBlockMutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, true);
 Node loopNode = NodeUtil.newForStatementNode(
 NodeUtil.newIdentifierNode("i"), NodeUtil.newNumberNode(0),
 NodeUtil.newNumberNode(10), NodeUtil.newBlock(
 NodeUtil.newAssignStatementNode("i", NodeUtil.newIncrementNode(
 NodeUtil.newVariableNode("i"))),
 NodeUtil.newCallNode("testFunction"),
 NodeUtil.newAssignStatementNode(resultName,
 NodeUtil.newGetValueNode(resultName))),
 null);
 Node block = NodeUtil.newBlock(loopNode);
 Node root = NodeUtil.newBlock(block);
 Node result = NodeUtil.evaluateToNormal(root);

 assertEquals(55, ((Long) result).longValue());
 }
}

class NoOpCompiler implements AbstractCompiler {
 @Override
 public void reportCode(Node node, int lineNumber, int columnNumber, String message) {}

 @Override
 public void reportWarning(String message, Node node, int lineNumber, int columnNumber) {}

 @Override
 public void reportError(String message, Node node, int lineNumber, int columnNumber) {}

 @Override
 public void reportFatalError(String message) {
 throw new RuntimeException(message);
 }

 @Override
 public Node getUniqueNameIdSupplier() {
 return new Supplier<String>() {
 @Override
 public String get() {
 return "test";
 }
 };
 }
}