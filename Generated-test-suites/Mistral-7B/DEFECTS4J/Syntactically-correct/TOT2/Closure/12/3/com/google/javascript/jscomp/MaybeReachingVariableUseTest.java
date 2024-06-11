import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

  @org.junit.Test
  public void testMaybeReachingVariableUse() {
    // Given
    String jsCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    x = 2;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(jsScope, controlFlowGraph);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());

    // When - Basic usage
    Collection<Node> uses = maybeReachingVariableUse.getUses("x", functionNode);
    assertNotNull(uses);
    assertEquals(2, uses.size());
    Node useNode1 = uses.iterator().next();
    assertNotNull(useNode1);
    assertEquals(Token.ASSIGN, useNode1.getType());
    Node useNode2 = uses.iterator().next();
    assertNotNull(useNode2);
    assertEquals(Token.RETURN, useNode2.getType());

    // When - Edge cases

    // No definition
    jsCode = "function foo() {\n" +
        "  return x;\n" +
        "}";
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
    functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());
    uses = maybeReachingVariableUse.getUses("x", functionNode);
    assertNull(uses);

    // Multiple definitions
    jsCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "  return x;\n" +
        "}";
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
    functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());
    uses = maybeReachingVariableUse.getUsages();
    assertEquals(3, uses.size());

    // Exception handling
    jsCode = "function foo() {\n" +
        "  try {\n" +
        "    var x = 1;\n" +
        "  } catch (e) {\n" +
        "    x = 2;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
    functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());
    uses = maybeReachingVariableUse.getUses("x", functionNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    useNode1 = uses.iterator().next();
    assertNotNull(useNode1);
    assertEquals(Token.ASSIGN, useNode1.getType());

    // When - Error scenarios

    // Misspelled variable name
    jsCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return y;\n" +
        "}";
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
    functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());
    uses = maybeReachingVariableUse.getUses("y", functionNode);
    assertNull(uses);

    // Escaped variable
    jsCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
    functionNode = controlFlowGraph.getNode(entryNode.getFirstChild());
    maybeReachingVariableUse.escaped.add(jsScope.getVar("x"));
    maybeReachingVariableUse.process(functionNode, maybeReachingVariableUse.createEntryLattice());
    uses = maybeReachingVariableUse.getUses("x", functionNode);
    assertNull(uses);
  }
}