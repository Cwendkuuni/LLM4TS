import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker);
  }

  @Test
  public void shouldTraverseFunctions() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null, null);
    Node parent = Node.newBlock();
    parent.addChild(functionNode);

    assertTrue(checker.shouldTraverse( NodeTraversal.create(checker), functionNode, parent));
  }

  @Test
  public void shouldNotTraverseConstructors() {
    Node constructorNode = Node.newFunction("constructorName", null, null, null, null, null);
    constructorNode.getFirstChild().setInteger(3, Token.CONSTRUCTOR);
    Node parent = Node.newBlock();

    assertFalse(checker.shouldTraverse( NodeTraversal.create(checker), constructorNode, parent));
  }

  @Test
  public void shouldNotTraverseFunctionsWithThisAnnotation() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null, null);
    Node jsDoc = Node.newText("@this");
    functionNode.setJSDocInfo(new JSDocInfo(null, null, jsDoc));
    Node parent = Node.newBlock();

    assertFalse(checker.shouldTraverse( NodeTraversal.create(checker), functionNode, parent));
  }

  @Test
  public void shouldNotTraverseFunctionsInNonGlobalContext() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null, null);
    Node parent = Node.newGetProp(null, "prototype");
    parent.setString("prototype", "functionName");

    assertFalse(checker.shouldTraverse( NodeTraversal.create(checker), functionNode, parent));
  }

  @Test
  public void shouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null, null);
    Node parent = Node.newBlock();

    assertTrue(checker.shouldTraverse( NodeTraversal.create(checker), functionNode, parent));
  }

  @Test
  public void shouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssign();
    Node lhs = Node.newThis();
    Node rhs = Node.newIdentifier("identifier");
    assignNode.addChildren(lhs, rhs);
    Node parent = Node.newBlock();

    assertTrue(checker.shouldTraverse( NodeTraversal.create(checker), lhs, parent));
  }

  @Test
  public void shouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign();
    Node lhs = Node.newIdentifier("identifier");
    Node rhs = Node.newGetProp(Node.newThis(), "prototype");
    assignNode.addChildren(lhs, rhs);
    Node parent = Node.newBlock();

    assertFalse(checker.shouldTraverse( NodeTraversal.create(checker), rhs, parent));
  }

  @Test
  public void shouldTraverseRightSideOfAssignment() {
    Node assignNode = Node.newAssign();
    Node lhs = Node.newIdentifier("identifier");
    Node rhs = Node.newThis();
    assignNode.addChildren(lhs, rhs);
    Node parent = Node.newBlock();

    assertTrue(checker.shouldTraverse( NodeTraversal.create(checker), rhs, parent));
  }

  @Test
  public void shouldReportUsageOfGlobalThis() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null, null);
    Node thisNode = Node.newThis();
    Node parent = Node.newBlock();

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(CheckLevel.WARNING), Mockito.eq(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(null);

    checker.visit(NodeTraversal.create(checker), thisNode, parent);

    assertTrue(compiler.reportWasCalled());
  }
}