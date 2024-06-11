package com.example.junit;

import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.NodeTraversal.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertNotNull(checkGlobalThis);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullAbstractCompiler() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithAbstractCompilerThatDoesntImplementNodeTraversal() {
    class DummyCompiler implements AbstractCompiler {
      // doesn't implement NodeTraversal
    }
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new DummyCompiler(), CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullCheckLevel() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), null);
  }

  @Test
  public void testShouldTraverseFunctionNotConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node function = new Node(Token.FUNCTION, new int[]{0}, null, null, new int[]{Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN, Token.LCURLY});
    checkGlobalThis.shouldTraverse(traversal, function, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node function = new Node(Token.FUNCTION, new int[]{0}, new JSDocInfo(new String[]{"@this"}, new String[]{}, null, null), null, new int[]{Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN, Token.LCURLY});
    checkGlobalThis.shouldTraverse(traversal, function, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node function = new Node(Token.FUNCTION, new int[]{0}, null, null, new int[]{Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN, Token.LCURLY});
    checkGlobalThis.shouldTraverse(traversal, function, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node function = new Node(Token.FUNCTION, new int[]{0}, null, null, new int[]{Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN, Token.LCURLY});
    checkGlobalThis.shouldTraverse(traversal, function, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, function, null)); // should report an error when no parent is provided
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsNotScriptOrBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node parent = new Node(Token.COMMENT, new int[]{0}, null, null, new int[]{});
    Node function = new Node(Token.FUNCTION, new int[]{0}, null, null, new int[]{Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN, Token.LCURLY});
    checkGlobalThis.shouldTraverse(traversal, function, parent);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, function, parent)); // should report an error when the parent is not a script or block
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node assign = new Node(Token.ASSIGN, new int[]{0}, null, null, new int[]{Node.THIS, new Node(Token.NAME, new int[]{0}, null, null, new int[]{Token.NAME, Token.EQUAL})});
    Node lhs = assign.getFirstChild();
    checkGlobalThis.shouldReportThis(assign, null);
    assertTrue(checkGlobalThis.shouldReportThis(assign, null));
  }

  @Test
  public void testShouldReportThisWithPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node getProp = new Node(Token.GETPROP, new int[]{0}, null, null, new int[]{Node.THIS, new Node(Token.NAME, new int[]{0}, null, null, new int[]{Token.IDENT})});
    Node lhs = getProp.getFirstChild();
    checkGlobalThis.shouldReportThis(getProp, null);
    assertTrue(checkGlobalThis.shouldReportThis(getProp, null));
  }

  @Test
  public void testVisitNodeThatIsNotThisType() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node node = new Node(Token.NAME, new int[]{0}, null, null, new int[]{});
    checkGlobalThis.visit(traversal, node, null);
  }

  @Test
  public void testVisitNodeWithNullParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node node = new Node(Token.NAME, new int[]{0}, null, null, new int[]{});
    checkGlobalThis.visit(traversal, node, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, null)); // should report an error when the parent is null
  }

  @Test
  public void testVisitNodeWithParentThatIsNotAssignOrFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING));
    Node parent = new Node(Token.COMMENT, new int[]{0}, null, null, new int[]{});
    Node node = new Node(Token.NAME, new int[]{0}, null, null, new int[]{});
    checkGlobalThis.visit(traversal, node, parent);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent)); // should report an error when the parent is not an assign or function
  }
}