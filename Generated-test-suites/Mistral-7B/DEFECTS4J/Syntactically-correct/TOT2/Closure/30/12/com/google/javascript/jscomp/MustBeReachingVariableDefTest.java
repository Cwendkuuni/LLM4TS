import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testSimpleAssignment() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node node = Node.newCompoundStatement(
        Node.newVariableDeclaration("x", Node.newName("x")),
        Node.newAssignment(Node.newName("x"), Node.newNumber(5))
    );

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(node));

    Node useNode = Node.newName("x");

    MustDef result = analysis.process(node);

    assertEquals(expected, result);
    assertEquals(node, analysis.getDef("x", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("x", useNode));
  }

  @org.junit.Test
  public void testAssignmentWithMultiplePaths() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entry = Node.newBlock(
        Node.newVariableDeclaration("x", Node.newName("x")),
        Node.newAssignment(Node.newName("x"), Node.newNumber(5)),
        Node.newIf(Node.newComparison(Token.EQ, Node.newName("x"), Node.newNumber(5)), Node.newBlock(
            Node.newAssignment(Node.newName("x"), Node.newNumber(10)),
            Node.newReturnValue(Node.newName("x"))
        ), Node.newBlock(
            Node.newAssignment(Node.newName("x"), Node.newNumber(15)),
            Node.newReturnValue(Node.newName("x"))
        ))
    );

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), null);

    Node useNode1 = Node.newName("x");

    MustDef result1 = analysis.process(entry);

    assertNotNull(result1);
    assertNull(analysis.getDef("x", useNode1));
    assertEquals(expected, result1);

    MustDef expected1 = new MustDef();
    expected1.reachingDef.put(jsScope.getVar("x"), new Definition(Node.newNumber(5)));

    Node useNode2 = Node.newName("x");

    MustDef result2 = analysis.process(useNode1);
    MustDef result3 = analysis.process(result1.reachingDef.get(jsScope.getVar("x")));

    assertEquals(expected1, result3);
    assertEquals(Node.newNumber(5), analysis.getDef("x", useNode2));
    assertFalse(analysis.dependsOnOuterScopeVars("x", useNode2));
  }

  @org.junit.Test
  public void testAssignmentWithEscapedVariable() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.escaped.add(jsScope.getVar("x"));

    Node entry = Node.newBlock(
        Node.newVariableDeclaration("x", Node.newName("x")),
        Node.newAssignment(Node.newName("x"), Node.newName("y")),
        Node.newReturnValue(Node.newName("x"))
    );

    Node useNode = Node.newName("x");

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("y"), null);

    MustDef result = analysis.process(entry);

    assertNotNull(result);
    assertNull(analysis.getDef("x", useNode));
    assertEquals(expected, result);
    assertFalse(analysis.dependsOnOuterScopeVars("x", useNode));
  }

  @org.junit.Test
  public void testAssignmentWithParameter() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entry = Node.newFunction("foo", new Node[0], Node.newBlock(
        Node.newVariableDeclaration("x", Node.newName("x")),
        Node.newAssignment(Node.newName("x"), Node.newName(0)),
        Node.newReturnValue(Node.newName("x"))
    ));

    Node useNode = Node.newName("arguments[0]");

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("arguments[0]"), null);

    MustDef result = analysis.process(entry);

    assertNotNull(result);
    assertNull(analysis.getDef("x", useNode));
    assertEquals(expected, result);
    assertTrue(analysis.dependsOnOuterScopeVars("x", useNode));
  }
}