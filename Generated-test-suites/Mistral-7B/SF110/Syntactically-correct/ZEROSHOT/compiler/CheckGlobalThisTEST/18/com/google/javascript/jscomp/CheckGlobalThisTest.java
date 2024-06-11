import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);

    Node nonGlobalContext = Node.newLiteral(42);

    when(traversal.getCurrentNode()).thenReturn(nonGlobalContext);

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctions() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName");

    when(traversal.getCurrentNode()).thenReturn(functionNode);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseConstructors() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node constructorNode = Node.newFunction("constructorName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});

    when(traversal.getCurrentNode()).thenReturn(constructorNode);

    JSDocInfo jsDoc = Node.newJsDoc("constructor", null, null, null, null, null, null, null, null, null, null, null, null);

    constructorNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithAtConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});

    when(traversal.getCurrentNode()).thenReturn(functionNode);

    JSDocInfo jsDoc = Node.newJsDoc("constructor", null, null, null, null, null, null, null, null, null, null, null, null);

    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithAtthis() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});

    when(traversal.getCurrentNode()).thenReturn(functionNode);

    JSDocInfo jsDoc = Node.newJsDoc(null, null, null, null, "@this", null, null, null, null, null, null, null, null);

    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});

    when(traversal.getCurrentNode()).thenReturn(functionNode);

    // An empty JSDocInfo
    JSDocInfo jsDoc = Node.newJsDoc(null, null, null, null, null, null, null, null, null, null, null, null, null);

    functionNode.setJSDocInfo(jsDoc);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();

    when(traversal.getCurrentNode()).thenReturn(assignmentNode);
    when(assignmentNode.getFirstChild()).thenReturn(leftSideNode);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();
    Node rightSideNode = Node.newGetProp("prototype");

    when(traversal.getCurrentNode()).thenReturn(assignmentNode);
    when(assignmentNode.getFirstChild()).thenReturn(leftSideNode);
    when(leftSideNode.getParent()).thenReturn(Node.newAssign(Token.ASSIGN, null, rightSideNode, null, null));

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();
    Node subPropertyNode = Node.newGetProp("prototype");
    Node propertyNameNode = Node.newString("propertyName");
    Node rightSideNode = Node.newGetProp(propertyNameNode);

    when(traversal.getCurrentNode()).thenReturn(assignmentNode);
    when(assignmentNode.getFirstChild()).thenReturn(leftSideNode);
    when(leftSideNode.getParent()).thenReturn(Node.newGetProp(subPropertyNode));

    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();
    Node rightSideNode = Node.newNumber(42);

    when(traversal.getCurrentNode()).thenReturn(assignmentNode);
    when(assignmentNode.getFirstChild()).thenReturn(leftSideNode);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();

    when(traversal.makeError(Node.newThis(), CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.newError("error message"));

    checker.visit(traversal, null, null);

    Node leftSideOfAssignment = traversal.getCurrentNode();

    assertTrue(leftSideOfAssignment.equals(leftSideNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssign(Token.ASSIGN, null, null, null, null);
    Node leftSideNode = Node.newThis();
    Node rightSideNode = Node.newNumber(42);

    when(traversal.getCurrentNode()).thenReturn(assignmentNode);
    when(assignmentNode.getFirstChild()).thenReturn(leftSideNode);

    checker.visit(traversal, null, null);

    assertFalse(checker.shouldReportThis(rightSideNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getNode = Node.newGet(Token.GETPROPNAM, null, null, Node.newThis(), null);

    when(traversal.makeError(getNode, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.newError("error message"));

    checker.visit(traversal, null, null);

    Node nodeToCheck = traversal.getCurrentNode();

    assertTrue(nodeToCheck.equals(getNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getPropertyNameNode = Node.newString("getPropertyName");
    Node getNode = Node.newGetProp(getPropertyNameNode);
    Node nodeToCheck = Node.newThis();

    when(traversal.getCurrentNode()).thenReturn(getNode);

    checker.visit(traversal, null, null);

    assertFalse(checker.shouldReportThis(nodeToCheck));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});

    when(traversal.getCurrentNode()).thenReturn(functionNode);

    checker.visit(traversal, null, null);

    Node nodeToCheck = traversal.getCurrentNode();

    assertTrue(nodeToCheck.equals(functionNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", Token.FUNCTION_DECL, null, new int[] {Token.VOID});
    Node nodeToCheck = Node.newThis();

    when(traversal.getCurrentNode()).thenReturn(nodeToCheck);

    checker.visit(traversal, null, null);

    assertFalse(checker.shouldReportThis(functionNode));
  }
}