import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerResponse;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node nonGlobalNode = Node.newNode(Token.NAME, "nonGlobal");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(nonGlobalNode);

    assertFalse(checker.shouldTraverse(traversal, Node.newNode(Token.FUNCTION), null));
  }

  @Test
  public void testShouldTraverseFunctions() {
    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(functionNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithConstructorsOrThisAnnotations() {
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    functionNode.setJSDocInfo(jsDoc);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(functionNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithoutParent() {
    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(functionNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithPrototypeAccess() {
    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    Node prototypeAccess = Node.newNode(Token.GETPROP, "prototype");
    prototypeAccess.setNext(functionNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(prototypeAccess);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    assignNode.setFirstChild(leftSide);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(assignNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, assignNode.getParent()));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    assignNode.setNext(leftSide);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    Node rightSide = Node.newNode(Token.NAME, "rightSide");
    assignNode.setLastChild(rightSide);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(rightSide);

    assertFalse(checker.shouldTraverse(traversal, functionNode, assignNode.getParent()));
  }

  @Test
  public void testShouldNotTraverseRightSideOfGetProperty() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, "getProperty");
    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    getPropertyNode.setLastChild(functionNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropertyNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseGetPropertyWithPrototypeAccess() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, "getProperty");
    Node prototypeAccess = Node.newNode(Token.GETPROP, "prototype");
    prototypeAccess.setNext(getPropertyNode);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(prototypeAccess);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    assignNode.setFirstChild(leftSide);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);

    CompilerResponse response = checker.visit(traversal, functionNode, assignNode.getParent());

    assertEquals(1, response.getDiagnostics().size());
    DiagnosticType diagnostic = response.getDiagnostics().get(0).getType();
    assertNotNull(diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getName());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    assignNode.setNext(leftSide);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    Node rightSide = Node.newNode(Token.NAME, "rightSide");
    assignNode.setLastChild(rightSide);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);

    checker.visit(traversal, functionNode, assignNode.getParent());

    assertEquals(0, checker.compiler.getDiagnosticList().size());
  }

  @Test
  public void testShouldNotReportThisOnGetProperty() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, "getProperty");
    Node functionNode = Node.newNode(Token.FUNCTION, "function");
    getPropertyNode.setLastChild(functionNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);

    checker.visit(traversal, functionNode, null);

    assertEquals(0, checker.compiler.getDiagnosticList().size());
  }

  @Test
  public void testShouldReportThisOnGetPropertyWithPrototypeAccess() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, "getProperty");
    Node prototypeAccess = Node.newNode(Token.GETPROP, "prototype");
    prototypeAccess.setNext(getPropertyNode);

    Node functionNode = Node.newNode(Token.FUNCTION, "function");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);

    checker.visit(traversal, functionNode, null);

    assertEquals(0, checker.compiler.getDiagnosticList().size());
  }
}