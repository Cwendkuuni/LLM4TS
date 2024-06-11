import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.forEachChild;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.ScopeType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tests for Scope class.
 */
public class ScopeTest {
  private static final ErrorReporter REPORTER = new ErrorReporter() {};

  /**
   * Creates a sample node tree for testing.
   *
   * @return a sample node tree for testing
   */
  private Node createSampleNodeTree() {
    Node root = new Node(Token.SCRIPT);
    Node funcNode = new Node(Token.FUNCTION, "sampleFunction");
    Node paramNode1 = new Node(Token.VAR, "a");
    Node paramNode2 = new Node(Token.VAR, "b");
    Node bodyNode = new Node(Token.BLOCK);
    Node assignmentNode1 = new Node(Token.ASSIGN, "c", new Node(Token.NAME, "a"));
    Node assignmentNode2 = new Node(Token.ASSIGN, Node.NULL, new Node(Token.NAME, "b"));
    bodyNode.addChild(assignmentNode1);
    bodyNode.addChild(assignmentNode2);
    funcNode.addChild(new Node(Token.VAR, "c")); // define "c" in local scope
    funcNode.addChild(paramNode1);
    funcNode.addChild(paramNode2);
    funcNode.addChild(bodyNode);
    root.addChild(funcNode);
    return root;
  }

  /**
   * Test for Scope constructor with rootNode and parent.
   */
  @org.junit.Test
  public void testScopeConstructionWithRootNodeAndParent() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    assertEquals(ScopeType.LOCAL, scope.getType());
    assertEquals(1, scope.getDepth());
  }

  /**
   * Test for Scope constructor with rootNode and thisType.
   */
  @org.junit.Test
  public void testScopeConstructionWithRootNodeAndThisType() {
    Node rootNode = createSampleNodeTree();
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(rootNode, thisType);
    assertEquals(ScopeType.GLOBAL, scope.getType());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for Scope constructor with no arguments.
   */
  @org.junit.Test
  public void testScopeConstructionWithNoArguments() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, null);
    assertEquals(ScopeType.GLOBAL, scope.getType());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for Scope.getParent() method.
   */
  @org.junit.Test
  public void testScopeGetParent() {
    Node rootNode = createSampleNodeTree();
    Scope parentScope = new Scope(new Node(Token.SCRIPT), null);
    Scope childScope = new Scope(rootNode, parentScope);
    assertEquals(parentScope, childScope.getParent());
  }

  /**
   * Test for Scope.getTypeOfThis() method.
   */
  @org.junit.Test
  public void testScopeTypeOfThis() {
    Node rootNode = createSampleNodeTree();
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(rootNode, thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  /**
   * Test for Scope.getDepth() method.
   */
  @org.junit.Test
  public void testScopeGetDepth() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    assertEquals(1, scope.getDepth());
  }

  /**
   * Test for Scope.isBottom() method.
   */
  @org.junit.Test
  public void testScopeIsBottom() {
    Node rootNode = new Node(Token.SCRIPT);
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(rootNode, thisType);
    assertFalse(scope.isBottom());

    rootNode = null;
    scope = new Scope(rootNode, thisType);
    assertTrue(scope.isBottom());
  }

  /**
   * Test for Scope.getRootNode() method.
   */
  @org.junit.Test
  public void testScopeGetRootNode() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    assertEquals(rootNode, scope.getRootNode());
  }

  /**
   * Test for Scope.isGlobal() method.
   */
  @org.junit.Test
  public void testScopeIsGlobal() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    assertTrue(scope.isGlobal());

    rootNode = new Node(Token.FUNCTION, "sampleFunction");
    scope = new Scope(rootNode, RootScope.instance());
    assertFalse(scope.isGlobal());
  }

  /**
   * Test for Scope.isLocal() method.
   */
  @org.junit.Test
  public void testScopeIsLocal() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    assertFalse(scope.isLocal());

    rootNode = new Node(Token.FUNCTION, "sampleFunction");
    scope = new Scope(rootNode, RootScope.instance());
    assertTrue(scope.isLocal());
  }

  /**
   * Test for Scope.getParentScope() method.
   */
  @org.junit.Test
  public void testScopeGetParentScope() {
    Node rootNode = createSampleNodeTree();
    Scope parentScope = new Scope(new Node(Token.SCRIPT), null);
    Scope childScope = new Scope(rootNode, parentScope);
    assertEquals(parentScope, childScope.getParentScope());
  }

  /**
   * Test for Scope.getVar() method.
   */
  @org.junit.Test
  public void testScopeGetVar() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);
    Var varC = scope.getVar("c");

    assertEquals(varA, scope.getVar("a"));
    assertEquals(varB, scope.getVar("b"));
    assertNull(scope.getVar("d"));
    assertEquals(varC, scope.getVar("c"));
  }

  /**
   * Test for Scope.getOwnSlot() method.
   */
  @org.junit.Test
  public void testScopeGetOwnSlot() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    assertEquals(varA, scope.getOwnSlot("a"));
    assertEquals(varB, scope.getOwnSlot("b"));
    assertNull(scope.getOwnSlot("c"));
  }

  /**
   * Test for Scope.getSlot() method.
   */
  @org.junit.Test
  public void testScopeGetSlot() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    assertEquals(varA, scope.getSlot("a"));
    assertEquals(varB, scope.getSlot("b"));
    assertNull(scope.getSlot("c"));
  }

  /**
   * Test for Scope.isDeclared() method.
   */
  @org.junit.Test
  public void testScopeIsDeclared() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    assertTrue(scope.isDeclared("a"));
    assertTrue(scope.isDeclared("b"));
    assertFalse(scope.isDeclared("c"));
  }

  /**
   * Test for Scope.getVars() method.
   */
  @org.junit.Test
  public void testScopeGetVars() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    List<Var> vars = new ArrayList<>();
    vars.add(varA);
    vars.add(varB);

    List<Var> varsInScope = new ArrayList<>();
    forEachChild(scope.getRootNode(), new NodeVisitor<Node>() {
      @Override
      public void visit(Node node) {
        if (node instanceof Scope) {
          List<Var> varsInCurrentScope = ((Scope) node).getVars();
          varsInScope.addAll(varsInCurrentScope);
        }
      }

      @Override
      public void visitEnd() {
        assertEquals(vars, varsInScope);
      }
    });
  }

  /**
   * Test for Scope.getVarCount() method.
   */
  @org.junit.Test
  public void testScopeGetVarCount() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    assertEquals(2, scope.getVarCount());
  }

  /**
   * Test for DataFlowAnalysis.compute() method.
   */
  @org.junit.Test
  public void testDataFlowAnalysisCompute() {
    Node rootNode = createSampleNodeTree();
    Scope scope = new Scope(rootNode, RootScope.instance());
    Var varA = scope.declare("a", new Node(Token.NAME, "a"), null, null);
    Var varB = scope.declare("b", new Node(Token.NAME, "b"), null, null);

    DataFlowAnalysis analysis = new DataFlowAnalysis(REPORTER, scope);
    analysis.compute();

    Var resultVarA = scope.getVar("a");
    Var resultVarB = scope.getVar("b");

    JSType expectedTypeA = new IntType();
    JSType expectedTypeB = new StringType();

    assertEquals(expectedTypeA, resultVarA.getType());
    assertEquals(expectedTypeB, resultVarB.getType());
  }
}