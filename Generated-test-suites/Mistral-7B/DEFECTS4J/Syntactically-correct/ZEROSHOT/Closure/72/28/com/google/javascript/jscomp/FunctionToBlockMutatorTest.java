import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isStatementBlock;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilationLevel;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversalType;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link FunctionToBlockMutator} class.
 */
public class FunctionToBlockMutatorTest extends Test {

  private static AbstractCompiler compiler;
  private static Supplier<String> safeNameIdSupplier;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // Initialize the compiler, but do not set the printing handler.
    compiler = new AbstractCompiler(CompilationLevel.ADVANCED);
    safeNameIdSupplier = () -> compiler.getUniqueNameIdSupplier().get();
  }

  public static Test suite() {
    return new TestSuite(FunctionToBlockMutatorTest.class);
  }

  /**
   * Test basic inlining of a simple function.
   */
  public void testSimpleFunctionInlining() {
    Node fnNode = NodeOperations.newFunctionNode(
        "add", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("a", INT_TYPE),
            NodeOperations.newParameterNode("b", INT_TYPE)));

    Node callNode = NodeOperations.newCallNode("add", Lists.newArrayList(
        NodeOperations.newIntNode(1), NodeOperations.newIntNode(2)));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("add", fnNode, callNode, "result", false, false);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains the correct statements.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(2, statements.size());

    Node a = statements.get(0);
    Node b = statements.get(1);
    Node sum = statements.get(2);

    // Verify that 'a' is assigned the first argument value.
    assertEquals(NodeOperations.newIntNode(1), a.getFirstChild());

    // Verify that 'b' is assigned the second argument value.
    assertEquals(NodeOperations.newIntNode(2), b.getFirstChild());

    // Verify that 'sum' is assigned the sum of the arguments.
    Node aValue = a.getFirstChild();
    Node bValue = b.getFirstChild();
    Node addNode = statements.get(1).getFirstChild();
    assertEquals(Token.PLUS, addNode.getType());
    assertEquals(aValue, addNode.getFirstChild());
    assertEquals(bValue, addNode.getLastChild());
    Node resultNode = statements.get(2).getFirstChild();
    assertEquals("result", resultNode.getName());
    assertEquals(INT_TYPE, resultNode.getType());
    assertEquals(addNode, resultNode.getFirstChild());
  }

  /**
   * Test inlining of a function with modified parameters.
   */
  public void testModifiedParametersInlining() {
    Node fnNode = NodeOperations.newFunctionNode(
        "add", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("a", INT_TYPE),
            NodeOperations.newParameterNode("b", INT_TYPE)));

    Node callNode = NodeOperations.newCallNode("add", Lists.newArrayList(
        NodeOperations.newIntNode(1), NodeOperations.newIntNode(2)));

    Map<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("a", NodeOperations.newIntNode(3));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("add", fnNode, callNode, "result", false, false);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains the correct statements.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(3, statements.size());

    Node a = statements.get(0);
    Node b = statements.get(1);
    Node sum = statements.get(2);

    // Verify that 'a' is assigned the first argument value.
    assertEquals(NodeOperations.newIntNode(3), a.getFirstChild());

    // Verify that 'b' is assigned the second argument value.
    assertEquals(NodeOperations.newIntNode(2), b.getFirstChild());

    // Verify that 'sum' is assigned the sum of the arguments.
    Node aValue = a.getFirstChild();
    Node bValue = b.getFirstChild();
    Node addNode = statements.get(1).getFirstChild();
    assertEquals(Token.PLUS, addNode.getType());
    assertEquals(aValue, addNode.getFirstChild());
    assertEquals(bValue, addNode.getLastChild());
    Node resultNode = statements.get(2).getFirstChild();
    assertEquals("result", resultNode.getName());
    assertEquals(INT_TYPE, resultNode.getType());
    assertEquals(addNode, resultNode.getFirstChild());
  }

  /**
   * Test inlining of a function with modified parameters and aliases.
   */
  public void testModifiedParametersWithAliasesInlining() {
    Node fnNode = NodeOperations.newFunctionNode(
        "add", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("x", INT_TYPE),
            NodeOperations.newParameterNode("y", INT_TYPE)));

    Set<String> namesToAlias = Sets.newHashSet("x");

    Map<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("x", NodeOperations.newIntNode(3));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("add", fnNode, null, "result", false, false, namesToAlias, argMap);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains the correct statements.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(4, statements.size());

    Node a = statements.get(0);
    Node b = statements.get(1);
    Node sum = statements.get(2);
    Node alias = statements.get(3);

    // Verify that 'a' is assigned the value of the parameter 'x'.
    assertEquals(NodeOperations.newIntNode(3), a.getFirstChild());
    assertEquals("a", alias.getName());

    // Verify that 'b' is assigned the second argument value.
    assertEquals(NodeOperations.newIntNode(2), b.getFirstChild());

    // Verify that 'sum' is assigned the sum of the arguments.
    Node aValue = a.getFirstChild();
    Node bValue = b.getFirstChild();
    Node addNode = statements.get(1).getFirstChild();
    assertEquals(Token.PLUS, addNode.getType());
    assertEquals(aValue, addNode.getFirstChild());
    assertEquals(bValue, addNode.getLastChild());
    Node resultNode = statements.get(2).getFirstChild();
    assertEquals("result", resultNode.getName());
    assertEquals(INT_TYPE, resultNode.getType());
    assertEquals(addNode, resultNode.getFirstChild());
  }

  /**
   * Test inlining of a function with modified parameters and aliases,
   * with the result assigned to a variable.
   */
  public void testModifiedParametersWithAliasesResultInlining() {
    Node fnNode = NodeOperations.newFunctionNode(
        "add", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("x", INT_TYPE),
            NodeOperations.newParameterNode("y", INT_TYPE)));

    Set<String> namesToAlias = Sets.newHashSet("x");

    Map<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("x", NodeOperations.newIntNode(3));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("add", fnNode, null, "result", true, false, namesToAlias, argMap);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains the correct statements.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(4, statements.size());

    Node a = statements.get(0);
    Node b = statements.get(1);
    Node sum = statements.get(2);
    Node alias = statements.get(3);

    // Verify that 'a' is assigned the value of the parameter 'x'.
    assertEquals(NodeOperations.newIntNode(3), a.getFirstChild());
    assertEquals("a", alias.getName());

    // Verify that 'b' is assigned the second argument value.
    assertEquals(NodeOperations.newIntNode(2), b.getFirstChild());

    // Verify that 'sum' is assigned the sum of the arguments.
    Node aValue = a.getFirstChild();
    Node bValue = b.getFirstChild();
    Node addNode = statements.get(1).getFirstChild();
    assertEquals(Token.PLUS, addNode.getType());
    assertEquals(aValue, addNode.getFirstChild());
    assertEquals(bValue, addNode.getLastChild());
    Node resultNode = statements.get(3).getFirstChild();
    assertEquals("result", resultNode.getName());
    assertEquals(INT_TYPE, resultNode.getType());
    assertEquals(addNode, resultNode.getFirstChild());
  }

  /**
   * Test inlining of a function called in a loop.
   */
  public void testFunctionInliningInLoop() {
    Node fnNode = NodeOperations.newFunctionNode(
        "add", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("a", INT_TYPE),
            NodeOperations.newParameterNode("b", INT_TYPE)));

    Set<String> namesToAlias = Sets.newHashSet();

    Map<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("a", NodeOperations.newIntNode(3));
    argMap.put("b", NodeOperations.newIntNode(2));

    Node loopStart = NodeOperations.newForInitNode(null);
    Node loopCondition = NodeOperations.newIntNode(5);
    Node loopUpdate = NodeOperations.newIntNode(1);

    Node callNode = NodeOperations.newCallNode("add", Lists.newArrayList(
        NodeOperations.newIntNode(1), NodeOperations.newIntNode(2)));

    Node loopBody = NodeOperations.newBlockNode();
    Node resultNode = NodeOperations.newIntNode(0);

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("add", fnNode, callNode, "result", false, true, namesToAlias, argMap);
    Node loop = NodeOperations.newForNode(loopStart, loopCondition, loopUpdate, loopBody);

    Node body = NodeOperations.newBlockNode();
    Node assignment = NodeOperations.newAssignNode("result", result);
    Node addCall = NodeOperations.newCallNode("add", Lists.newArrayList(
        NodeOperations.newIntNode(1), NodeOperations.newIntNode(2)));
    Node bodyStatements = NodeUtil.newStatementsList(
        assignment,
        NodeOperations.newReturnNode(resultNode)
    );
    Node bodyBlock = NodeOperations.newBlockNode(bodyStatements);

    // Replace the function call with the inlined block.
    loopBody.replaceChild(0, result);

    // Replace the loop body with the body of the transformed function.
    loop.replaceChild(2, bodyBlock);

    // Perform the loop execution.
    NodeOperations.runCode(compiler, loop);

    // Verify that the loop has executed the transformed function 5 times.
    assertEquals(15, ((NodeOperations.getIntValue(resultNode))));
  }

  /**
   * Test that the function body does not contain any uninitialized VAR nodes.
   */
  public void testFunctionWithoutUninitializedVars() {
    Node fnNode = NodeOperations.newFunctionNode(
        "identity", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("x", INT_TYPE)));
    Node body = NodeOperations.newBlockNode();
    body.addChildrenToBack(NodeOperations.newReturnNode(NodeOperations.newIntNode(1)));
    fnNode.setBody(body);

    Node callNode = NodeOperations.newCallNode("identity", Lists.newArrayList(
        NodeOperations.newIntNode(1)));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("identity", fnNode, callNode, "result", false, false);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains no uninitialized VAR nodes.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(1, statements.size());
    assertTrue(result instanceof Node);
    assertEquals(NodeOperations.RETURN_TYPE, result.getType());
  }

  /**
   * Test that the function body contains an uninitialized VAR node, and it is
   * initialized after inlining.
   */
  public void testFunctionWithUninitializedVars() {
    Node fnNode = NodeOperations.newFunctionNode(
        "identity", Token.FUNCTION, Lists.newArrayList(
            NodeOperations.newParameterNode("x", INT_TYPE)));
    Node body = NodeOperations.newBlockNode();
    body.addChildrenToBack(
        NodeOperations.newVarNode("result", INT_TYPE),
        NodeOperations.newReturnNode(NodeOperations.newVarNode("result")));
    fnNode.setBody(body);

    Node callNode = NodeOperations.newCallNode("identity", Lists.newArrayList(
        NodeOperations.newIntNode(1)));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate("identity", fnNode, callNode, "result", false, false);

    // Assert that the result is a valid block node.
    assertNotNull(result);
    assertTrue(isStatementBlock(result));

    // Assert that the block contains an initialized VAR node for 'result'.
    List<Node> statements = NodeUtil.getStatements(result);
    assertEquals(2, statements.size());
    assertEquals(NodeOperations.ASSIGN, statements.get(0).getType());
    assertEquals(NodeOperations.VAR_DECLARATION, statements.get(1).getType());
    assertEquals("result", statements.get(1).getFirstChild().getName());
    assertEquals(INT_TYPE, statements.get(1).getFirstChild().getType());
  }
}