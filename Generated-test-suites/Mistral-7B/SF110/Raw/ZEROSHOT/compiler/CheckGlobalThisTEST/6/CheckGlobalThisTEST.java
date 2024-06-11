import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
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
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newBlock();
    when(functionParent.getJSDocInfo()).thenReturn(null);
    when(functionParent.getParent()).thenReturn(functionParent);
    when(functionParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(functionParent);
    when(compiler.getTraversal(functionNode)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(NodeTraversal.NO_TRAVERSAL, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = Node.newFunction("constructorFunction", null, null, null, null);
    Node constructorParent = Node.newBlock();
    when(constructorParent.getJSDocInfo()).thenReturn(Node.newJSDocInfo("constructor"));
    when(constructorParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(constructorParent);
    when(compiler.getTraversal(constructorNode)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(NodeTraversal.NO_TRAVERSAL, constructorNode, constructorParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithAnnotation() {
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node functionParent = Node.newBlock();
    when(functionParent.getJSDocInfo()).thenReturn(Node.newJSDocInfo("@this"));
    when(functionParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(functionParent);
    when(compiler.getTraversal(functionNode)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(NodeTraversal.NO_TRAVERSAL, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutLogicalPlaceForAnnotation() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newBlock();
    when(functionParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(functionParent);
    when(compiler.getTraversal(functionNode)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(NodeTraversal.NO_TRAVERSAL, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newBlock();
    Node prototypeNode = Node.newBlock();
    Node prototypeParent = Node.newBlock();
    prototypeParent.setProperty("prototype", prototypeNode);
    functionParent.setProperty("__proto__", prototypeParent);
    when(functionParent.getJSDocInfo()).thenReturn(null);
    when(functionParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(functionParent);
    when(compiler.getTraversal(functionNode)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(NodeTraversal.NO_TRAVERSAL, functionNode, functionParent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newThis(), Node.newIdentifier("property"));
    Node assignmentParent = Node.newBlock();
    when(compiler.getTraversal(assignmentNode)).thenReturn(traversal);

    assertTrue(checker.shouldReportThis(assignmentNode.getFirstChild()));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignmentNode = Node.newAssign(Node.newIdentifier("prototype"), Node.newThis());
    Node assignmentParent = Node.newBlock();
    when(assignmentParent.getJSDocInfo()).thenReturn(null);
    when(assignmentParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(assignmentParent);
    when(compiler.getTraversal(assignmentNode)).thenReturn(traversal);

    assertFalse(checker.shouldReportThis(assignmentNode.getLastChild()));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Node assignmentNode = Node.newAssign(Node.newGetProp(Node.newIdentifier("prototype"), "subProperty"), Node.newThis());
    Node assignmentParent = Node.newBlock();
    when(assignmentParent.getJSDocInfo()).thenReturn(null);
    when(assignmentParent.getType()).thenReturn(Token.SCRIPT);
    when(traversal.getCurrentScope()).thenReturn(assignmentParent);
    when(compiler.getTraversal(assignmentNode)).thenReturn(traversal);

    assertFalse(checker.shouldReportThis(assignmentNode.getLastChild()));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newThis(), Node.newIdentifier("property"));
    Node assignmentParent = Node.newBlock();
    when(compiler.getTraversal(assignmentNode)).thenReturn(traversal);

    assertTrue(checker.shouldReportThis(assignmentNode.getFirstChild()));
  }
}