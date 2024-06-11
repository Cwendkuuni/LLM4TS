import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DeadAssignmentsEliminationTest.createNode;
import static com.google.javascript.rhino.NodeOps.newScope;
import static com.google.javascript.rhino.NodeOps.newTopLevelScope;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeOperations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Scope globalScope;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    globalScope = newTopLevelScope(compiler);
  }

  @After
  public void tearDown() throws Exception {
    compiler = null;
  }

  @Test
  public void testBasicDeadAssignment() {
    String code = "var x = 1;\n" +
        "x = 2;";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    assertEquals(3, cfg.getNodes().size());

    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInConditional() {
    String code = "var x = 1;\n" +
        "if (false) {\n" +
        "  x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInLoop() {
    String code = "var x = 1;\n" +
        "for (var i = 0; i < 10; i++) {\n" +
        "  x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInFunction() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInFunctionWithReturn() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNotNull(xAssignment);
  }

  @Test(expected = RuntimeException.class)
  public void testDeadAssignmentInFunctionWithException() {
    String code = "function foo() {\n" +
        "  try {\n" +
        "    var x = 1;\n" +
        "    throw new Error();\n" +
        "  } catch (e) {\n" +
        "    x = 2;\n" +
        "  }";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);
  }

  @Test
  public void testDeadAssignmentInFunctionWithReturnAndException() {
    String code = "function foo() {\n" +
        "  try {\n" +
        "    var x = 1;\n" +
        "    return x = 2;\n" +
        "  } catch (e) {\n" +
        "  }";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNotNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithFunction() {
    String code = "var x = 1;\n" +
        "function foo() {\n" +
        "  x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithFunctionCall() {
    String code = "var x = 1;\n" +
        "foo();\n" +
        "function foo() {\n" +
        "  x = 2;\n" +
        "}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithExpressionStatement() {
    String code = "var x = 1;\n" +
        "x = 2;\n";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithExpressionStatementAndFunction() {
    String code = "var x = 1;\n" +
        "x = 2;\n" +
        "function foo() {}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithExpressionStatementAndFunctionCall() {
    String code = "var x = 1;\n" +
        "x = 2;\n" +
        "foo();\n" +
        "function foo() {}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithVariableDeclaration() {
    String code = "var x = 1;\n" +
        "var x = 2;\n";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithVariableDeclarationAndFunction() {
    String code = "var x = 1;\n" +
        "var x = 2;\n" +
        "function foo() {}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  @Test
  public void testDeadAssignmentInGlobalWithVariableDeclarationAndFunctionCall() {
    String code = "var x = 1;\n" +
        "var x = 2;\n" +
        "foo();\n" +
        "function foo() {}";
    Node root = createNode(code, globalScope);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(globalScope, root);

    ControlFlowGraph cfg = eliminator.getControlFlowGraph();
    Node xAssignment = GraphUtil.findNodeByChild(cfg.getNodes().get(1),
        matchRemovableAssigns);
    assertNull(xAssignment);
  }

  private static Node createNode(String code, Scope scope) {
    Node root = NodeOperations.parse(code);
    NodeOperations.setSource(root, code);
    NodeOperations.setScope(root, scope);
    return root;
  }
}