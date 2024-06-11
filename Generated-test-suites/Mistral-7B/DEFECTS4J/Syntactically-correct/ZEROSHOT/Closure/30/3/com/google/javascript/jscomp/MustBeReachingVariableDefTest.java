import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDefTest.createNode;
import static com.google.javascript.jscomp.MustBeReachingVariableDefTest.createScope;
import static com.google.javascript.jscomp.MustBeReachingVariableDefTest.createVariable;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    Scope scope = createScope();
    AbstractCompiler compiler = new NoOpCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node functionNode = createNode(Token.FUNCTION, "myFunction", null, null);
    Node varA = createNode(Token.VAR, "a", null, null);
    Node varB = createNode(Token.VAR, "b", null, null);
    Node varC = createNode(Token.VAR, "c", null, null);
    Node varD = createNode(Token.VAR, "d", null, null);
    Node assignmentA = createNode(Token.ASSIGN, "=", null, null, varA, createNode(Token.NAME, "a", null, null));
    Node assignmentB = createNode(Token.ASSIGN, "=", null, null, varB, createNode(Token.NAME, "b", null, null));
    Node assignmentC = createNode(Token.ASSIGN, "=", null, null, varC, createNode(Token.NAME, "c", null, null));
    Node ifNode = createNode(Token.IF, null, null, null, assignmentC, createNode(Token.NAME, "alert", null, null, createNode(Token.NAME, "a", null, null)));
    Node elseNode = createNode(Token.ELSE, null, null, null);
    Node assignmentD = createNode(Token.ASSIGN, "=", null, null, varD, createNode(Token.NAME, "d", null, null));
    Node assignementE = createNode(Token.ASSIGN, "=", null, null, varC, createNode(Token.NAME, "e", null, null));

    functionNode.addChild(varA);
    functionNode.addChild(varB);
    functionNode.addChild(varC);
    functionNode.addChild(varD);
    functionNode.addChild(assignmentA);
    functionNode.addChild(assignmentB);
    functionNode.addChild(assignmentC);
    functionNode.addChild(ifNode);
    functionNode.addChild(elseNode);
    functionNode.addChild(assignmentD);
    functionNode.addChild(assignementE);

    cfg.addNode(functionNode);
    cfg.addEdge(assignmentC, ifNode, Branch.CONSTANT(null, true));
    cfg.addEdge(assignmentC, elseNode, Branch.CONSTANT(null, false));

    analyzer.computeMustDef(functionNode, functionNode, analyzer.createEntryLattice(), false);

    MustDef expected = new MustDef();
    MustDef actual = analyzer.getAnnotation(functionNode).getOut();

    assertEquals(expected, actual);

    assertEquals(4, actual.reachingDef.size());
    assertEquals(varA, actual.reachingDef.get(createVariable("a")).node);
    assertEquals(varB, actual.reachingDef.get(createVariable("b")).node);
    assertEquals(varC, actual.reachingDef.get(createVariable("c")).node);
    assertEquals(varD, actual.reachingDef.get(createVariable("d")).node);

    assertEquals(Sets.newHashSet(varA, varB, varC),
        Sets.difference(Sets.newHashSet(varA, varB, varC, varD),
            Sets.newHashSet(createVariable("e"))));

    assertEquals(Sets.newHashSet(createVariable("a")),
        Sets.difference(Sets.newHashSet(createVariable("a"), createVariable("b"), createVariable("c"), createVariable("d")),
            Sets.newHashSet(createVariable("e"))));

    assertTrue(analyzer.dependsOnOuterScopeVars("a", assignmentA));
    assertFalse(analyzer.dependsOnOuterScopeVars("a", varA));

    assertTrue(analyzer.dependsOnOuterScopeVars("e", assignmentE));
    assertFalse(analyzer.dependsOnOuterScopeVars("e", varC));

    Node useA = createNode(Token.NAME, "a", null, null);
    assertEquals(varA, analyzer.getDef("a", useA));
    assertEquals(varA, analyzer.getDef("a", assignmentA));
    assertEquals(null, analyzer.getDef("e", assignmentA));
    assertEquals(null, analyzer.getDef("e", useA));

    Node useB = createNode(Token.NAME, "b", null, null);
    assertEquals(varB, analyzer.getDef("b", useB));
    assertEquals(varB, analyzer.getDef("b", assignmentB));
    assertEquals(null, analyzer.getDef("a", useB));
    assertEquals(null, analyzer.getDef("a", assignmentB));

    Node useC = createNode(Token.NAME, "c", null, null);
    assertEquals(varC, analyzer.getDef("c", useC));
    assertEquals(varC, analyzer.getDef("c", assignmentC));
    assertEquals(varD, analyzer.getDef("d", useC));
    assertEquals(null, analyzer.getDef("a", useC));

    Node useD = createNode(Token.NAME, "d", null, null);
    assertEquals(varD, analyzer.getDef("d", useD));
    assertEquals(varD, analyzer.getDef("d", assignmentD));
    assertEquals(null, analyzer.getDef("a", useD));
    assertEquals(null, analyzer.getDef("a", assignmentD));

    Node useE = createNode(Token.NAME, "e", null, null);
    assertEquals(null, analyzer.getDef("c", useE));
    assertEquals(null, analyzer.getDef("e", useE));
  }

  Node createNode(int type, String name, @Nullable Node... siblings) {
    Node node = NodeFactory.createNode(type, name);
    for (Node sibling : siblings) {
      node.addChild(sibling);
    }
    return node;
  }

  Scope createScope() {
    return new Scope(new Node(Token.BLOCK, null), null, null, null);
  }

  Var createVariable(String name) {
    Var var = new Var();
    var.setName(name);
    var.setParentNode(createScope());
    return var;
  }

  static class NoOpCompiler extends AbstractCompiler {
    @Override
    public void process(Node rootNode, CompilerContext context) {}
  }
}