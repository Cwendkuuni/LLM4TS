import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

 private AbstractCompiler compiler;
 private FunctionToBlockMutator functionToBlockMutator;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler(null, null);
 functionToBlockMutator = new FunctionToBlockMutator(compiler, () -> null);
 }

 @Test
 public void testMutateFunctionWithNoArguments() {
 Node fnNode = Node.newFunction(0, null, null);
 String resultName = "result";
 Node callNode = Node.newCall(fnNode);
 Node injectableBlock = functionToBlockMutator.mutate(
 "functionName", fnNode, callNode, resultName, false, false);

 assertEquals("functionName: {\n" +
 "  result = functionName();\n" +
 "}", injectableBlock.toString());
 }

 @Test
 public void testMutateFunctionWithArguments() {
 Node fnNode = Node.newFunction(3, null, null);
 String resultName = "result";
 Node callNode = Node.newCall(fnNode, "arg1", "arg2", "arg3");
 LinkedHashMap<String, Node> args = new LinkedHashMap<>();
 args.put("arg1", Node.newNumber(1));
 args.put("arg2", Node.newNumber(2));
 args.put("arg3", Node.newNumber(3));
 Node injectableBlock = functionToBlockMutator.mutate(
 "functionName", fnNode, callNode, resultName, false, false);

 assertEquals("functionName: {\n" +
 "  result = functionName(arg1, arg2, arg3);\n" +
 "}", injectableBlock.toString());
 }

 @Test
 public void testMutateFunctionWithNeedDefaultResult() {
 Node fnNode = Node.newFunction(0, null, null);
 String resultName = "result";
 Node callNode = Node.newCall(fnNode);
 Node injectableBlock = functionToBlockMutator.mutate(
 "functionName", fnNode, callNode, resultName, true, false);

 assertEquals("functionName: {\n" +
 "  result = functionName();\n" +
 "}", injectableBlock.toString());
 }

 @Test
 public void testMutateFunctionWithCallInLoop() {
 Node fnNode = Node.newFunction(0, null, null);
 String resultName = "result";
 Node callNode = Node.newCall(fnNode);
 Node injectableBlock = functionToBlockMutator.mutate(
 "functionName", fnNode, callNode, resultName, false, true);

 assertEquals("functionName: {\n" +
 "  result = functionName();\n" +
 "}", injectableBlock.toString());

 // Check that the variables are initialized properly
 Node block = injectableBlock.getFirstChild();
 Node variable = block.getLastChild();
 Preconditions.checkState(variable.getType() == Token.VAR);
 Preconditions.checkState(variable.getFirstChild().getType() == Token.NAME);
 Preconditions.checkState(variable.getFirstChild().getFirstChild().getType() == Token.ASSIGN);
 Preconditions.checkState(variable.getFirstChild().getFirstChild().getLastChild().getType() == Token.UNDEFINED);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateNullFunction() {
 Node callNode = Node.newCall(null);
 String resultName = "result";
 functionToBlockMutator.mutate("functionName", null, callNode, resultName, false, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateNullCallNode() {
 Node fnNode = Node.newFunction(0, null, null);
 String resultName = "result";
 functionToBlockMutator.mutate("functionName", fnNode, null, resultName, false, false);
 }

 @Test(expected = NullPointerException.class)
 public void testReplaceReturnsNullBlock() {
 replaceReturns(null, "resultName", "labelName", false);
 }

 @Test(expected = NullPointerException.class)
 public void testReplaceReturnsNullLabelName() {
 replaceReturns(
 Node.newBlock(
 Node.newLabel("labelName"),
 Node.newReturn(Node.newNumber(1))),
 "resultName", null, false);
 }

 @Test
 public void testReplaceReturns() {
 Node block = Node.newBlock(
 Node.newLabel("labelName"),
 Node.newReturn(Node.newNumber(1)));
 String resultName = "result";
 replaceReturns(block, resultName, "labelName", false);

 assertEquals("labelName: {\n" +
 "  result = 1;\n" +
 "  break labelName;\n" +
 "}", block.toString());
 }

 @Test
 public void testReplaceReturnsWithResultName() {
 Node block = Node.newBlock(
 Node.newLabel("labelName"),
 Node.newReturn(Node.newNumber(1)));
 String resultName = "resultName";
 replaceReturns(block, resultName, "labelName", true);

 assertEquals("labelName: {\n" +
 "  resultName = 1;\n" +
 "  break labelName;\n" +
 "}", block.toString());
 }

 private static Node replaceReturns(Node block, String resultName,
 String labelName, boolean resultMustBeSet) {
 return functionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);
 }

}