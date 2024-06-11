import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.List;

public class MustBeReachingVariableDefTest {

  private static ControlFlowGraph<Node> createSimpleControlFlowGraph() {
    // Assuming we have a simple control flow graph for testing.
    // Replace it with the actual control flow graph for your class.
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null);
    n1.setString("x");
    Node x = new Node(Token.NAME, n1);
    Node n2 = new Node(Token.ASSIGN, null);
    Node assignX = new Node(Token.BINARY_OP, null);
    assignX.setType(Token.ASSIGN);
    assignX.setLeft(x);
    assignX.setRight(new Node(Token.INT_LITERAL, 5));
    n2.setLeft(x);
    n2.setRight(assignX);

    Node cond = new Node(Token.IF, null);
    Node condition = new Node(Token.BINARY_OP, null);
    condition.setType(Token.REL_OP);
    condition.setLeft(new Node(Token.INT_LITERAL, 5));
    condition.setRight(new Node(Token.INT_LITERAL, 10));
    cond.setCondition(condition);
    Node trueBranch = new Node(Token.BLOCK, null);
    Node n3 = new Node(Token.ASSIGN, null);
    Node y = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    n3.setLeft(y);
    n3.setRight(new Node(Token.INT_LITERAL, 7));
    trueBranch.setStatements(Arrays.asList(n3));
    cond.setTrueBranch(trueBranch);

    Node falseBranch = new Node(Token.BLOCK, null);
    Node n4 = new Node(Token.ASSIGN, null);
    Node z = new Node(Token.NAME, new Node(Token.VAR, null).setString("z"));
    n4.setLeft(z);
    n4.setRight(new Node(Token.INT_LITERAL, 8));
    falseBranch.setStatements(Arrays.asList(n4));
    cond.setFalseBranch(falseBranch);

    Node exit = new Node(Token.RETURN, null);
    entry.setStatements(Arrays.asList(x, assignX, cond, exit));
    cfg.setEntryNode(entry);
    return cfg;
  }

  @org.junit.Test
  public void testSimpleMustBeReachingVariableDef() {
    // Given
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(createSimpleControlFlowGraph(), jsScope, compiler);

    // When
    MustDef mustDef = mustBeReachingVariableDef.compute(null);

    // Then
    Node xDef = mustBeReachingVariableDef.getDef("x", mustBeReachingVariableDef.getEntryNode());
    assertNotNull(xDef);
    assertEquals(x, xDef);

    Node yDef = mustBeReachingVariableDef.getDef("y", mustBeReachingVariableDef.getEntryNode());
    assertNull(yDef);

    Node zDef = mustBeReachingVariableDef.getDef("z", mustBeReachingVariableDef.getEntryNode());
    assertNull(zDef);

    // When x is used, it should reach the definition of x.
    Node useX = new Node(Token.NAME, x);
    Node mustDefX = mustBeReachingVariableDef.getDef("x", useX);
    assertEquals(x, mustDefX);

    // When y is used, it should not reach any definition
    Node useY = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefY = mustBeReachingVariableDef.getDef("y", useY);
    assertNull(mustDefY);

    // When z is used, it should not reach any definition
    Node useZ = new Node(Token.NAME, new Node(Token.VAR, null).setString("z"));
    Node mustDefZ = mustBeReachingVariableDef.getDef("z", useZ);
    assertNull(mustDefZ);

    // Given x is defined in the false branch
    MustDef mustDefFalseBranch = new MustDef();
    for (Var var : jsScope.getVars()) {
      if (var.isDeclared()) {
        if (var.getName().equals("x")) {
          mustDefFalseBranch.reachingDef.put(var, new Definition(falseBranch));
        }
      }
    }
    MustDef mustDefTrueBranch = mustBeReachingVariableDef.compute(mustDefFalseBranch);

    // When x is used, it should reach the definition of x in the false branch.
    Node useXInFalseBranch = new Node(Token.NAME, x);
    Node mustDefXInFalseBranch = mustBeReachingVariableDef.getDef("x", useXInFalseBranch);
    assertEquals(falseBranch, mustDefXInFalseBranch.node);

    // When y is used, it should not reach any definition
    Node useYInFalseBranch = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranch = mustBeReachingVariableDef.getDef("y", useYInFalseBranch);
    assertNull(mustDefYInFalseBranch);
  }

  @org.junit.Test
  public void testSimpleMustBeReachingVariableDefWithDependsOnOuterScopeVars() {
    // Given
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(createSimpleControlFlowGraph(), jsScope, compiler);

    // When
    MustDef mustDef = mustBeReachingVariableDef.compute(null);

    // Then
    Node xDef = mustBeReachingVariableDef.getDef("x", mustBeReachingVariableDef.getEntryNode());
    assertNotNull(xDef);
    assertEquals(x, xDef);

    Node yDef = mustBeReachingVariableDef.getDef("y", mustBeReachingVariableDef.getEntryNode());
    assertNull(yDef);

    Node zDef = mustBeReachingVariableDef.getDef("z", mustBeReachingVariableDef.getEntryNode());
    assertNull(zDef);

    // Given x is defined in the false branch
    MustDef mustDefFalseBranch = new MustDef();
    for (Var var : jsScope.getVars()) {
      if (var.isDeclared()) {
        if (var.getName().equals("x")) {
          mustDefFalseBranch.reachingDef.put(var, new Definition(falseBranch));
        }
      }
    }
    MustDef mustDefTrueBranch = mustBeReachingVariableDef.compute(mustDefFalseBranch);

    // Given y is defined in the outer scope
    Scope outerScope = new Scope();
    outerScope.addVar(new Var("y", null, null, null, null, null));
    MustDef mustDefOuterScope = new MustDef();
    for (Var var : outerScope.getVars()) {
      if (var.isDeclared()) {
        mustDefOuterScope.reachingDef.put(var, new Definition(null));
      }
    }

    // When x is used, it should reach the definition of x in the false branch.
    Node useXInFalseBranch = new Node(Token.NAME, x);
    Node mustDefXInFalseBranch = mustBeReachingVariableDef.getDef("x", useXInFalseBranch);
    assertEquals(falseBranch, mustDefXInFalseBranch.node);

    // When y is used, it should reach the definition of y in the outer scope.
    Node useYInFalseBranch = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranch = mustBeReachingVariableDef.getDef("y", useYInFalseBranch);
    assertEquals(null, mustDefYInFalseBranch.node);
    assertTrue(mustDefYInFalseBranch.reachingDef.containsKey(outerScope.getVar("y")));
  }

  @org.junit.Test
  public void testSimpleMustBeReachingVariableDefWithEscapedVariables() {
    // Given
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(createSimpleControlFlowGraph(), jsScope, compiler);

    // When
    MustDef mustDef = mustBeReachingVariableDef.compute(null);

    // Then
    Node xDef = mustBeReachingVariableDef.getDef("x", mustBeReachingVariableDef.getEntryNode());
    assertNotNull(xDef);
    assertEquals(x, xDef);

    Node yDef = mustBeReachingVariableDef.getDef("y", mustBeReachingVariableDef.getEntryNode());
    assertNull(yDef);

    Node zDef = mustBeReachingVariableDef.getDef("z", mustBeReachingVariableDef.getEntryNode());
    assertNull(zDef);

    // Given x is defined in the false branch and is an escaped variable
    MustDef mustDefFalseBranch = new MustDef();
    for (Var var : jsScope.getVars()) {
      if (var.isDeclared()) {
        if (var.getName().equals("x")) {
          mustDefFalseBranch.reachingDef.put(var, new Definition(falseBranch));
          mustBeReachingVariableDef.escaped.add(var);
        }
      }
    }
    MustDef mustDefTrueBranch = mustBeReachingVariableDef.compute(mustDefFalseBranch);

    // When x is used, it should not reach the definition of x in the false branch.
    Node useXInFalseBranch = new Node(Token.NAME, x);
    Node mustDefXInFalseBranch = mustBeReachingVariableDef.getDef("x", useXInFalseBranch);
    assertNull(mustDefXInFalseBranch);

    // When y is used, it should not reach any definition
    Node useYInFalseBranch = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranch = mustBeReachingVariableDef.getDef("y", useYInFalseBranch);
    assertNull(mustDefYInFalseBranch);

    // Given y is defined in the outer scope
    Scope outerScope = new Scope();
    outerScope.addVar(new Var("y", null, null, null, null, null));
    MustDef mustDefOuterScope = new MustDef();
    for (Var var : outerScope.getVars()) {
      if (var.isDeclared()) {
        mustDefOuterScope.reachingDef.put(var, new Definition(null));
      }
    }

    // When x is used, it should not reach any definition
    Node useXInFalseBranchEscaped = new Node(Token.NAME, x);
    Node mustDefXInFalseBranchEscaped = mustBeReachingVariableDef.getDef("x", useXInFalseBranchEscaped);
    assertNull(mustDefXInFalseBranchEscaped);

    // When y is used, it should reach the definition of y in the outer scope.
    Node useYInFalseBranchEscaped = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranchEscaped = mustBeReachingVariableDef.getDef("y", useYInFalseBranchEscaped);
    assertEquals(null, mustDefYInFalseBranchEscaped.node);
    assertTrue(mustDefYInFalseBranchEscaped.reachingDef.containsKey(outerScope.getVar("y")));
  }

  @org.junit.Test
  public void testSimpleMustBeReachingVariableDefWithEscapeParameters() {
    // Given
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(createSimpleControlFlowGraph(), jsScope, compiler);

    // When
    MustDef mustDef = mustBeReachingVariableDef.compute(null);

    // Then
    Node xDef = mustBeReachingVariableDef.getDef("x", mustBeReachingVariableDef.getEntryNode());
    assertNotNull(xDef);
    assertEquals(x, xDef);

    Node yDef = mustBeReachingVariableDef.getDef("y", mustBeReachingVariableDef.getEntryNode());
    assertNull(yDef);

    Node zDef = mustBeReachingVariableDef.getDef("z", mustBeReachingVariableDef.getEntryNode());
    assertNull(zDef);

    // Given x is defined in the false branch and is an escaped parameter
    MustDef mustDefFalseBranch = new MustDef();
    for (Var var : jsScope.getVars()) {
      if (var.isDeclared()) {
        if (var.getName().equals("arguments")) {
          MustDef args = new MustDef();
          MustDef argsDef = new MustDef();
          argsDef.reachingDef.put(var, new Definition(falseBranch));
          args.reachingDef = ImmutableSet.of(argsDef);
          mustDefFalseBranch.reachingDef = ImmutableSet.of(args);
        }
      }
    }
    MustDef mustDefTrueBranch = mustBeReachingVariableDef.compute(mustDefFalseBranch);

    // When x[0] is used, it should not reach the definition of x[0] in the false branch.
    Node useXInFalseBranch = new Node(Token.GET_PROP, new Node(Token.NAME, new Node(Token.NAME, x)));
    Node mustDefXInFalseBranch = mustBeReachingVariableDef.getDef("arguments[0]", useXInFalseBranch);
    assertNull(mustDefXInFalseBranch);

    // When y is used, it should not reach any definition
    Node useYInFalseBranch = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranch = mustBeReachingVariableDef.getDef("y", useYInFalseBranch);
    assertNull(mustDefYInFalseBranch);

    // Given y is defined in the outer scope
    Scope outerScope = new Scope();
    outerScope.addVar(new Var("y", null, null, null, null, null));
    MustDef mustDefOuterScope = new MustDef();
    for (Var var : outerScope.getVars()) {
      if (var.isDeclared()) {
        mustDefOuterScope.reachingDef.put(var, new Definition(null));
      }
    }

    // When x[0] is used, it should not reach any definition
    Node useXInFalseBranchEscaped = new Node(Token.GET_PROP, new Node(Token.NAME, x));
    Node mustDefXInFalseBranchEscaped = mustBeReachingVariableDef.getDef("arguments[0]", useXInFalseBranchEscaped);
    assertNull(mustDefXInFalseBranchEscaped);

    // When y is used, it should reach the definition of y in the outer scope.
    Node useYInFalseBranchEscaped = new Node(Token.NAME, new Node(Token.VAR, null).setString("y"));
    Node mustDefYInFalseBranchEscaped = mustBeReachingVariableDef.getDef("y", useYInFalseBranchEscaped);
    assertEquals(null, mustDefYInFalseBranchEscaped.node);
    assertTrue(mustDefYInFalseBranchEscaped.reachingDef.containsKey(outerScope.getVar("y")));
  }
}