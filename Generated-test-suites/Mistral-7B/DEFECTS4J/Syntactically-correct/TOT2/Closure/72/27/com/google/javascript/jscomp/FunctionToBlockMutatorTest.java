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
 compiler = new AbstractCompiler();
 functionToBlockMutator = new FunctionToBlockMutator(compiler, () -> "temp_id_" + System.currentTimeMillis());
 }

 @Test
 public void testMutateFunctionWithNoArgumentsAndNoResult() {
 Node fnNode = Node.newFunction("testFunction", null, null, null, null,
 NodeUtil.newBlock(
 Node.newText("return 1;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), null);
 String labelName = "testLabel";
 String resultName = "testResult";
 Node result = functionToBlockMutator.mutate(null, fnNode, callNode, resultName, false, false);

 assertEquals("1", result.toString());
 }

 @Test
 public void testMutateFunctionWithNoArgumentsAndResult() {
 Node fnNode = Node.newFunction("testFunction", null, null, null, null,
 NodeUtil.newBlock(
 Node.newText("result = 1;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), null);
 String labelName = "testLabel";
 String resultName = "testResult";
 Node result = functionToBlockMutator.mutate(null, fnNode, callNode, resultName, true, false);

 assertEquals("undefined", result.toString());
 assertEquals("1", ((Node) result.getFirstChild()).toString());
 }

 @Test
 public void testMutateFunctionWithArgumentsAndNoResult() {
 Node fnNode = Node.newFunction("testFunction", new String[]{"arg1"}, null, null, null,
 NodeUtil.newBlock(
 Node.newText("arg1 = arg1 + 1;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), new Object[]{Node.newNumber(1)});
 String labelName = "testLabel";
 String resultName = null;
 Node result = functionToBlockMutator.mutate(null, fnNode, callNode, resultName, false, false);

 assertEquals("1", ((Node) callNode.getFirstChild()).toString());
 assertEquals("2", result.toString());
 }

 @Test
 public void testMutateFunctionWithArgumentsAndResult() {
 Node fnNode = Node.newFunction("testFunction", new String[]{"arg1"}, null, null, null,
 NodeUtil.newBlock(
 Node.newText("result = arg1 + 1;"),
 Node.newText("return result;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), new Object[]{Node.newNumber(1)});
 String labelName = "testLabel";
 String resultName = "testResult";
 Node result = functionToBlockMutator.mutate(null, fnNode, callNode, resultName, true, false);

 assertEquals("1", ((Node) callNode.getFirstChild()).toString());
 assertEquals("2", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateFunctionWithInvalidArguments() {
 Node fnNode = Node.newFunction("testFunction", null, null, null, null,
 NodeUtil.newBlock(
 Node.newText("return 1;")
 ));
 Node callNode = null;
 String labelName = "testLabel";
 String resultName = "testResult";
 try {
 functionToBlockMutator.mutate(null, fnNode, callNode, resultName, false, false);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateFunctionWithNullResultName() {
 Node fnNode = Node.newFunction("testFunction", null, null, null, null,
 NodeUtil.newBlock(
 Node.newText("return 1;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), null);
 String labelName = "testLabel";
 String resultName = null;
 try {
 functionToBlockMutator.mutate(null, fnNode, callNode, resultName, false, false);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testMutateFunctionWithCallInLoop() {
 Node fnNode = Node.newFunction("testFunction", null, null, null, null,
 NodeUtil.newBlock(
 Node.newText("result = arg1 + 1;"),
 Node.newText("return result;")
 ));
 Node callNode = Node.newCall(Node.newText("testFunction"), new Object[]{Node.newNumber(1)});
 String labelName = "testLabel";
 String resultName = "testResult";
 Node result = functionToBlockMutator.mutate(null, fnNode, callNode, resultName, true, true);

 assertEquals("1", ((Node) callNode.getFirstChild()).toString());
 assertEquals("2", result.toString());
 }

 @Test
 public void testAliasAndInlineArguments() {
 Node fnNode = Node.newFunction("testFunction", new String[]{"arg1"}, null, null, null,
 NodeUtil.newBlock(
 Node.newText("unsafe_arg1 = arg1;"),
 Node.newText("return arg1;")
 ));
 Set<String> namesToAlias = Set.of("unsafe_arg1");
 LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
 argMap.put("arg1", Node.newNumber(1));
 Node result = functionToBlockMutator.aliasAndInlineArguments(NodeUtil.getFunctionBody(fnNode), argMap, namesToAlias);
 assertEquals(fnNode.toString(), result.toString());
 }

 @Test
 public void testReplaceReturns() {
 Node block = NodeUtil.newBlock(
 Node.newText("return a;"),
 Node.newText("return b;"),
 Node.newText("return c;")
 );
 String labelName = "testLabel";
 String resultName = "testResult";
 Node result = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, true);

 Node resultNode = NodeUtil.newText("testResult");
 Node labelNode = Node.newLabel(labelName);
 Node firstStatement = NodeUtil.newText("a");
 Node secondStatement = NodeUtil.newText("b");
 Node thirdStatement = NodeUtil.newText("c");
 Node breakNode = Node.newBreak(labelName);
 Node newBlock = NodeUtil.newBlock(firstStatement, secondStatement, thirdStatement, breakNode);

 assertEquals(newBlock.toString(), result.toString());
 }

}