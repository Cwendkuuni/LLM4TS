import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOption("--compilation_level", CompilerOptions.CompilationLevel.SIMPLE);
  }

  @Test
  public void testSimpleDeadAssignment() {
    String jsCode = "function f() { var x = 1; x = 2; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInSimpleIfStatement() {
    String jsCode = "function f() { if (true) { var x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInComplexIfStatement() {
    String jsCode = "function f() { if (a > 1) { var x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInForStatement() {
    String jsCode = "function f() { for (var i = 0; i < 10; i++) { var x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInFunctionCall() {
    String jsCode = "function f() { g(); var x = 1; x = 2; } function g() {}";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInVariableDeclaration() {
    String jsCode = "function f() { var x, y = 1; x = 2; y = 3; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInConditionalExpression() {
    String jsCode = "function f() { return a > 1 ? (var x = 1; x = 2) : 1; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInAssignmentExpression() {
    String jsCode = "function f() { a = b = 1; a = 2; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "a = 2"));
  }

  @Test
  public void testDeadAssignmentInFunctionDeclaration() {
    String jsCode = "function f() { function g() { var x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInVariableDeclarationWithAssignment() {
    String jsCode = "function f() { var x = 1, y = 2; x = 3; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 3"));
  }

  @Test
  public void testDeadAssignmentInConditionalExpressionWithAssignment() {
    String jsCode = "function f() { return a > 1 ? (var x = 1, x = 2) : 1; }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclaration() {
    String jsCode = "function f() { for (var i = 0; i < 10; i++) { var x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclarationAndAssignment() {
    String jsCode = "function f() { for (var i = 0, j = 0; i < 10; i++) { var x = 1, y = 2; x = 3, y = 4; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 3"));
    assertNull(findNodeByCode(root, "y = 4"));
  }

  @Test
  public void testDeadAssignmentInLoopWithAssignment() {
    String jsCode = "function f() { for (var i = 0; i < 10; i++) { x = 1; x = 2; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 2"));
  }

  @Test
  public void testDeadAssignmentInLoopWithAssignmentAndVariableDeclaration() {
    String jsCode = "function f() { for (var i = 0; i < 10; i++) { x = 1, y = 2; x = 3; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 3"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclarationAndAssignmentAndVariableDeclaration() {
    String jsCode = "function f() { for (var i = 0, j = 0; i < 10; i++) { x = 1, y = 2, z = 3; x = 4, y = 5; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 4"));
    assertNull(findNodeByCode(root, "y = 5"));
    assertNull(findNodeByCode(root, "z = 3"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclarationAndAssignmentAndVariableDeclarationAndAssignment() {
    String jsCode = "function f() { for (var i = 0, j = 0; i < 10; i++) { x = 1, y = 2, z = 3, w = 4; x = 5, y = 6, z = 7, w = 8; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 5"));
    assertNull(findNodeByCode(root, "y = 6"));
    assertNull(findNodeByCode(root, "z = 7"));
    assertNull(findNodeByCode(root, "w = 8"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclarationAndAssignmentAndVariableDeclarationAndAssignmentAndVariableDeclaration() {
    String jsCode = "function f() { for (var i = 0, j = 0, k = 0; i < 10; i++) { x = 1, y = 2, z = 3, w = 4, v = 5; x = 6, y = 7, z = 8, w = 9, v = 10; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 6"));
    assertNull(findNodeByCode(root, "y = 7"));
    assertNull(findNodeByCode(root, "z = 8"));
    assertNull(findNodeByCode(root, "w = 9"));
    assertNull(findNodeByCode(root, "v = 10"));
  }

  @Test
  public void testDeadAssignmentInLoopWithVariableDeclarationAndAssignmentAndVariableDeclarationAndAssignmentAndVariableDeclarationAndAssignment() {
    String jsCode = "function f() { for (var i = 0, j = 0, k = 0, l = 0, m = 0; i < 10; i++) { x = 1, y = 2, z = 3, w = 4, v = 5, u = 6; x = 7, y = 8, z = 9, w = 10, v = 11, u = 12; } }";
    Node root = parseAndCompile(compiler, jsCode);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);
    assertNull(findNodeByCode(root, "x = 7"));
    assertNull(findNodeByCode(root, "y = 8"));
    assertNull(findNodeByCode(root, "z = 9"));
    assertNull(findNodeByCode(root, "w = 10"));
    assertNull(findNodeByCode(root, "v = 11"));
    assertNull(findNodeByCode(root, "u = 12"));
  }

  private Node parseAndCompile(AbstractCompiler compiler, String code) {
    Node root = Parser.parseAndCompile(compiler, code);
    return root;
  }

  private Node findNodeByCode(Node node, Node codeNode) {
    for (Node n = node; n != null; n = n.getParent()) {
      if (isNodeEqual(codeNode, n)) {
        return n;
      }
    }
    return null;
  }

  private boolean isNodeEqual(Node a, Node b) {
    if (a == b) {
      return true;
    }

    if (a == null || b == null) {
      return false;
    }

    return a.getType() == b.getType() &&
        a.getFirstChild() == b.getFirstChild() &&
        a.getLastChild() == b.getLastChild();
  }
}