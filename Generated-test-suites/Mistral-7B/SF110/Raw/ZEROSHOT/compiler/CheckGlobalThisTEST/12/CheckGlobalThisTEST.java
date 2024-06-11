import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);
    rootNode = Node.newProgram(Token.PROGRAM, null);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    when(traversal.getCurrentNode()).thenReturn(rootNode);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node nonGlobalContext = Node.newExpressionStatement(Token.ASSIGN,
        Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "a"),
            Node.newNumber(1)));

    when(traversal.getCurrentNode()).thenReturn(nonGlobalContext);
    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node constructorFunction = Node.newFunctionDeclaration(
        Token.FUNCTION, null, null, null, null,
        Node.newBlock(Token.BLOCK, null, null));

    when(traversal.getCurrentNode()).thenReturn(constructorFunction);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node thisFunction = Node.newFunctionDeclaration(
        Token.FUNCTION, null, null, null, null,
        Node.newBlock(Token.BLOCK,
            Node.newExpressionStatement(Token.ASSIGN, Node.newName(
                Token.NAME, "this"), Node.newString("myThis")),
            Node.newExpressionStatement(Token.ASSIGN, Node.newName(
                Token.NAME, "x"), Node.newNumber(1))));

    when(traversal.getCurrentNode()).thenReturn(thisFunction);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionWhereNoThisAnnotation() {
    Node noThisFunction = Node.newFunctionDeclaration(
        Token.FUNCTION, null, null, null, null,
        Node.newBlock(Token.BLOCK, Node.newExpressionStatement(
            Token.ASSIGN, Node.newName(Token.NAME, "x"), Node.newNumber(1))));

    when(traversal.getCurrentNode()).thenReturn(noThisFunction);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeMethod() {
    Node prototypeMethodFunction = Node.newFunctionDeclaration(
        Token.FUNCTION, null, null, null, null,
        Node.newBlock(Token.BLOCK, Node.newIdentifier("prototype"),
            Node.newExpressionStatement(Token.ASSIGN, Node.newName(
                Token.NAME, "myFunction"), Node.newFunctionDeclaration(
                    Token.FUNCTION, null, null, null, null,
                    Node.newBlock(Token.BLOCK, Node.newIdentifier("prototype"),
                        Node.newExpressionStatement(
                            Token.ASSIGN, Node.newName(Token.NAME, "x"),
                            Node.newNumber(1)))))));

    when(traversal.getCurrentNode()).thenReturn(prototypeMethodFunction);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeProperty() {
    Node prototypePropertyFunction = Node.newFunctionDeclaration(
        Token.FUNCTION, null, null, null, null,
        Node.newBlock(Token.BLOCK, Node.newIdentifier("prototype"),
            Node.newExpressionStatement(Token.ASSIGN, Node.newName(
                Token.NAME, "prototypeProperty"), Node.newString("myString"))));

    when(traversal.getCurrentNode()).thenReturn(prototypePropertyFunction);
    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignment = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "this"), Node.newNumber(1));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newName(Token.NAME, "b"));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, assignment)));

    when(traversal.getCurrentNode()).thenReturn(assignment);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verify(compiler).report(eq(traversal.makeError(assignment, GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignment = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newName(Token.NAME, "this"));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newNumber(1));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, assignment)));

    when(traversal.getCurrentNode()).thenReturn(assignment);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testShouldNotReportThisKeywordOnLeftSideOfPropertyAccess() {
    Node propertyAccess = Node.newGetProperty(Node.newName(Token.NAME, "this"),
        Node.newString("x"));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newName(Token.NAME, "b"));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, propertyAccess)));

    when(traversal.getCurrentNode()).thenReturn(propertyAccess);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfPropertyAccess() {
    Node propertyAccess = Node.newGetProperty(Node.newName(Token.NAME, "a"),
        Node.newString("this.x"));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newNumber(1));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, propertyAccess)));

    when(traversal.getCurrentNode()).thenReturn(propertyAccess);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPrototypePropertyAccess() {
    Node prototypePropertyAccess = Node.newGetProperty(Node.newName(
        Token.NAME, "prototype"), Node.newString("this.x"));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newName(Token.NAME, "b"));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, prototypePropertyAccess)));

    when(traversal.getCurrentNode()).thenReturn(prototypePropertyAccess);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verify(compiler).report(eq(traversal.makeError(prototypePropertyAccess,
        GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisKeywordOnLeftSideOfPrototypeMethodCall() {
    Node prototypeMethodCall = Node.newCall(Node.newName(Token.NAME, "prototype"),
        Token.getNameIdentifier("myFunction"), Node.newList(Node.newNumber(1)));

    Node parent = Node.newExpressionStatement(Token.ASSIGN,
        Node.newName(Token.NAME, "a"), Node.newName(Token.NAME, "b"));

    Node tree = Node.newBlock(Token.BLOCK, null,
        Node.newStatementList(Node.newStatementList(parent, prototypeMethodCall)));

    when(traversal.getCurrentNode()).thenReturn(prototypeMethodCall);
    checker.visit(traversal, tree, Node.newProgram(Token.PROGRAM, null));

    verifyNoMoreInteractions(compiler);
  }

  private void assertTrue(boolean condition) {
    assertTrue("Expected: " + condition, condition);
  }

  private void assertFalse(boolean condition) {
    assertFalse("Expected: " + !condition, condition);
  }

  private void verify(Node node, DiagnosticType expectedDiagnosticType) {
    verify(compiler).report(eq(traversal.makeError(node, expectedDiagnosticType)));
  }

  private void verifyNoMoreInteractions(AbstractCompiler compiler) {
    verifyNoMoreInteractions(compiler, traversal);
  }

  private void verifyNoMoreInteractions(AbstractCompiler compiler,
      NodeTraversal traversal) {
    verify(compiler, times(0)).report(any());
    verify(traversal, times(0)).makeError(any());
  }
}