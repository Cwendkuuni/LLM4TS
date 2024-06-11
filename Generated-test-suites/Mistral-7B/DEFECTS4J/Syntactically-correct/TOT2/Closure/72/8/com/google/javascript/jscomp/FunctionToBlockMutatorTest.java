import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 safeNameIdSupplier = () -> "test_id";
 }

 @Test
 public void testMutateSimpleFunction() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.newArrayList("a"),
 Lists.newArrayList(
 NodeUtil.newExpr(Token.ASSIGN, NodeUtil.newString("result"), NodeUtil.newNumber(10))),
 NodeUtil.newBlock(
 NodeUtil.newVarNode("a", NodeUtil.newNumber(5))
 )
 );
 Node callNode = NodeUtil.newCallNode("testFunction");

 Node result = mutator.mutate("testFunction", fnNode, callNode, "result", false, false);

 assertEquals(NodeUtil.newNumber(10), result);
 }

 @Test
 public void testMutateFunctionWithArguments() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.newArrayList("a", "b"),
 Lists.newArrayList(
 NodeUtil.newExpr(Token.ASSIGN, NodeUtil.newString("result"), NodeUtil.newBinaryOperator(
 Token.PLUS,
 NodeUtil.newVarNode("a"),
 NodeUtil.newVarNode("b")
 ))),
 NodeUtil.newBlock(
 NodeUtil.newVarNode("a", NodeUtil.newNumber(5)),
 NodeUtil.newVarNode("b", NodeUtil.newNumber(3))
 )
 );
 Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newNumberList(NodeUtil.newNumber(1), NodeUtil.newNumber(2)));

 LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
 argMap.put("a", NodeUtil.newNumber(10));
 argMap.put("b", NodeUtil.newNumber(20));
 Node result = mutator.mutate("testFunction", fnNode, callNode, "result", false, false);

 assertEquals(NodeUtil.newNumber(30), result);
 }

 @Test
 public void testMutateFunctionWithLabel() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.<String>newArrayList(),
 Lists.<Node>newArrayList(
 NodeUtil.newLabel("foo"),
 NodeUtil.newReturn(NodeUtil.newNumber(10)),
 NodeUtil.newBlock(NodeUtil.newVarNode("a", NodeUtil.newNumber(5)))
 ),
 NodeUtil.newBlock()
 );
 Node callNode = NodeUtil.newCallNode("testFunction");

 Node result = mutator.mutate("testFunction", fnNode, callNode, "result", false, false);

 assertNotNull(result);
 assertEquals("JSCompiler_inline_label_testFunction_" + safeNameIdSupplier.get(), result.getClass().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateNullFunction() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node result = mutator.mutate(null, null, null, "result", false, false);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateNullCallNode() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node result = mutator.mutate("testFunction", NodeUtil.newFunctionNode("testFunction", Lists.<String>newArrayList(), Lists.<Node>newArrayList(), NodeUtil.newBlock()), null, "result", false, false);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateNullResultName() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node result = mutator.mutate("testFunction", NodeUtil.newFunctionNode("testFunction", Lists.<String>newArrayList(), Lists.<Node>newArrayList(), NodeUtil.newBlock()), NodeUtil.newCallNode("testFunction"), null, false, false);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testMutateFunctionInLoop() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.<String>newArrayList(),
 Lists.<Node>newArrayList(
 NodeUtil.newLabel("foo"),
 NodeUtil.newReturn(NodeUtil.newNumber(10)),
 NodeUtil.newVarNode("a", NodeUtil.newNumber(5)),
 NodeUtil.newExpressionStatement(Token.BREAK, NodeUtil.newLabel("foo")),
 NodeUtil.newBlock()
 ),
 NodeUtil.newBlock()
 );
 Node callNode = NodeUtil.newCallNode("testFunction");

 Node result = mutator.mutate("testFunction", fnNode, callNode, "result", true, false);

 assertNotNull(result);
 assertEquals(NodeUtil.newNumber(10), result);
 }

 @Test(expected = RuntimeException.class)
 public void testMutateFunctionWithUninitializedVariable() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.<String>newArrayList(),
 Lists.<Node>newArrayList(
 NodeUtil.newVarNode("a"),
 NodeUtil.newReturn(NodeUtil.newNumber(10)),
 NodeUtil.newBlock()
 ),
 NodeUtil.newBlock()
 );
 Node callNode = NodeUtil.newCallNode("testFunction");

 Node result = mutator.mutate("testFunction", fnNode, callNode, "result", false, false);
 fail("Expected RuntimeException");
 }

 @Test
 public void testMutateFunctionWithModifiedParameter() {
 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

 Node fnNode = NodeUtil.newFunctionNode(
 "testFunction",
 Lists.<String>newArrayList("a"),
 Lists.<Node>newArrayList(
 NodeUtil.newReturn(NodeUtil.newBinaryOperator(Token.PLUS, NodeUtil.newVarNode("a"), NodeUtil.newNumber(1))),
 NodeUtil.newBlock(NodeUtil.newVarNode("a", NodeUtil.newNumber(5)))
 ),
 NodeUtil.newBlock()
 );
 Set<String> namesToAlias = Sets.newHashSet("a");
 LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
 argMap.put("a", NodeUtil.newNumber(10));
 Node result = mutator.mutate("testFunction", fnNode, NodeUtil.newCallNode("testFunction", NodeUtil.newNumberList(NodeUtil.newNumber(5))), "result", false, false);

 assertEquals(NodeUtil.newNumber(16), result);
 }
}