import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    CheckerFactory checkerFactory = mock(CheckerFactory.class);
    when(checkerFactory.create(compiler)).thenAnswer(new Answer<Callback>() {
      @Override
      public Callback answer(InvocationOnMock invocation) throws Throwable {
        return new CheckGlobalThis(compiler);
      }
    });
    Compiler jsCompiler = mock(Compiler.class);
    when(compiler.getOptions()).thenReturn(jsCompiler.getOptions());
    when(compiler.getCheckerFactory()).thenReturn(checkerFactory);
    checker = new CheckGlobalThis(compiler);
    traversal = NodeTraversal.create(compiler, null, checker, null);
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDocInfo() {
    Node node = Node.newFunction("functionName", new Node[0], new Node[0]);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = Node.newFunction("constructorFunction", new Node[0], new Node[0]);
    constructorNode.setJSDocInfo(Node.makeJSDocInfo("@constructor"));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, constructorNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisType() {
    Node functionNode = Node.newFunction("functionWithThisType", new Node[0], new Node[0]);
    functionNode.setJSDocInfo(Node.makeJSDocInfo("@this {type: 'Foo'}"));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node functionNode = Node.newFunction("functionWithInterfaceAnnotation", new Node[0], new Node[0]);
    functionNode.setJSDocInfo(Node.makeJSDocInfo("@interface"));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunction("functionWithOverrideAnnotation", new Node[0], new Node[0]);
    functionNode.setJSDocInfo(Node.makeJSDocInfo("@override"));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideOfScriptOrBlock() {
    Node functionNode = Node.newFunction("functionOutsideOfScriptOrBlock", new Node[0], new Node[0]);
    Node parent = Node.newName("parentNode", Token.NAME);
    parent.setType(Token.SCRIPT);
    Node grandParent = Node.newBlock("grandParentNode", null, null, null);
    parent.setParent(grandParent);
    functionNode.setParent(parent);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node functionNode = Node.newFunction("functionLentToPrototype", new Node[0], new Node[0]);
    Node parent = Node.newName("parentNode", Token.NAME);
    Node gramps = Node.newBlock("grandParentNode", null, null, null);
    parent.setParent(gramps);
    Node prototype = Node.newObjectLiteral("prototypeObject", new Node[0], new Node[0], null);
    Node prototypeProperty = Node.newName("prototypePropertyName", Token.STRING_KEY);
    prototype.setFirstChild(prototypeProperty);
    prototypeProperty.setLastChild(functionNode);
    parent.setFirstChild(prototype);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoJsDocInfoNorParentWithJsDocInfo() {
    Node functionNode = Node.newFunction("functionWithNoJsDocInfoNorParentWithJsDocInfo", new Node[0], new Node[0]);
    Node parent = Node.newBlock("parentNode", null, null, null);
    functionNode.setParent(parent);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testVisitThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(null, Node.newThis(), Node.newName("a", Token.NAME));
    Node node = Node.newName("thisKeyword", Token.THIS);
    assignNode.setFirstChild(node);
    Node parent = Node.newBlock("parentNode", null, null, null);
    parent.setParent(null);
    assignNode.setParent(parent);
    checker.visit(NodeTraversal.EMPTY, node, parent);
    assertNotNull(compiler.getLatestError());
  }

  @Test
  public void testVisitThisKeywordOnLeftSideOfPropertyAccess() {
    Node getNode = Node.newGetProp(Node.newName("parentNode", Token.NAME), Node.newName("prototype", Token.STRING_KEY));
    Node node = Node.newThis();
    getNode.setLastChild(node);
    Node parent = Node.newBlock("parentNode", null, null, null);
    parent.setParent(null);
    getNode.setParent(parent);
    checker.visit(NodeTraversal.EMPTY, node, parent);
    assertNotNull(compiler.getLatestError());
  }

  @Test
  public void testVisitThisKeywordOnLeftSideOfAssignmentWithLhsChild() {
    Node assignNode = Node.newAssign(Node.newName("a", Token.NAME), Node.newThis(), Node.newName("b", Token.NAME));
    Node lhs = assignNode.getFirstChild();
    checker.visit(NodeTraversal.EMPTY, lhs, Node.newBlock("parentNode", null, null, null));
    assertNull(compiler.getLatestError());

    assignNode.setFirstChild(Node.newThis());
    checker.visit(NodeTraversal.EMPTY, lhs, Node.newBlock("parentNode", null, null, null));
    assertNotNull(compiler.getLatestError());
  }

  @Test
  public void testVisitThisKeywordOnLeftSideOfPropertyAccessWithLhsChild() {
    Node getNode = Node.newGetProp(Node.newName("a", Token.NAME), Node.newName("prototype", Token.STRING_KEY));
    Node lhs = getNode.getFirstChild();
    checker.visit(NodeTraversal.EMPTY, lhs, Node.newBlock("parentNode", null, null, null));
    assertNull(compiler.getLatestError());

    getNode.setFirstChild(Node.newThis());
    checker.visit(NodeTraversal.EMPTY, lhs, Node.newBlock("parentNode", null, null, null));
    assertNotNull(compiler.getLatestError());
  }

  @Test
  public void testShouldNotReportThisKeywordInsidePrototypeMethod() {
    Node prototypeNode = Node.newObjectLiteral("prototypeObject", new Node[0], new Node[0], null);
    Node prototypeMethodNode = Node.newFunction("prototypeMethod", new Node[0], new Node[0]);
    prototypeMethodNode.setJSDocInfo(Node.makeJSDocInfo("@method"));
    Node prototypeProperty = Node.newName("prototypeProperty", Token.STRING_KEY);
    prototypeNode.setFirstChild(prototypeProperty);
    prototypeProperty.setLastChild(prototypeMethodNode);

    Node methodCallNode = Node.newCall(prototypeMethodNode, Node.newArrayLiteral(Node.newUndefined(), Node.newUndefined()));
    Node parent = Node.newBlock("parentNode", null, null, null);
    parent.setParent(null);
    methodCallNode.setParent(parent);

    checker.visit(NodeTraversal.EMPTY, methodCallNode, parent);
    assertNull(compiler.getLatestError());
  }

  @Test
  public void testShouldReportThisKeywordInsideContructorFunction() {
    Node constructorNode = Node.newFunction("constructorFunction", new Node[0], new Node[0]);
    constructorNode.setJSDocInfo(Node.makeJSDocInfo("@constructor"));

    Node node = Node.newThis();
    Node parent = Node.newBlock("parentNode", null, null, null);
    parent.setParent(null);
    node.setParent(parent);

    checker.visit(NodeTraversal.EMPTY, node, parent);
    assertNull(compiler.getLatestError());

    constructorNode.setFirstChild(node);
    Node error = compiler.getLatestError();
    assertNotNull(error);
    assertEquals("function constructorFunction() {dangerous use of the global 'this' object}", error.getMessage());
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithNoAnnotation() {
    Node functionNode = Node.newFunction("functionWithNoAnnotation", new Node[0], new Node[0]);
    Node node = Node.newThis();
    Node parent = Node.newBlock("parentNode", null, null, null);
    parent.setParent(null);
    node.setParent(parent);

    functionNode.setFirstChild(node);
    Node error = compiler.getLatestError();
    assertNotNull(error);
    assertEquals("function functionWithNoAnnotation() {dangerous use of the global 'this' object}", error.getMessage());
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithTooDeepNesting() {
    Node functionNode = Node.newFunction("functionWithTooDeepNesting", new Node[0], new Node[0]);
    Node node = Node.newThis();
    Node parent = Node.newBlock("parentNode1", null, null, null);
    parent.setParent(Node.newBlock("parentNode0", null, null, null));
    parent.setParent(null);
    node.setParent(parent);

    functionNode.setFirstChild(node);
    Node error = compiler.getLatestError();
    assertNull(error);

    checker.visit(NodeTraversal.EMPTY, node, Node.newBlock("parentNode0", null, null, null));
    assertNull(compiler.getLatestError());
  }

  private void assertTrue(boolean condition) {
    if (!condition) {
      fail("expected: true, but: " + condition);
    }
  }

  private void assertEquals(String expected, String actual) {
    if (!expected.equals(actual)) {
      fail("expected: " + expected + ", but: " + actual);
    }
  }

}