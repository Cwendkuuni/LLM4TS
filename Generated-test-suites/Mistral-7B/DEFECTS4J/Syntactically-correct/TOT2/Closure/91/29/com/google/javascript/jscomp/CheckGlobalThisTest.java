import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node node = mock(Node.class);
    Node parent = null;
    boolean result = new CheckGlobalThis(null, null).shouldTraverse(null, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionThatIsConstructor() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    boolean result = new CheckGlobalThis(null, null).shouldTraverse(null, node, parent);
    assertFalse(result);
  }

  // ... (other test cases for shouldTraverse)

  // ... (test cases for visit, shouldReportThis, and getFunctionJsDocInfo)

  @Test
  public void testShouldTraverseFunctionThatIsGettingLentToPrototypeProperty() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node prototype = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    when(node.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");
    boolean result = new CheckGlobalThis(null, null).shouldTraverse(null, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionOnLeftSideOfAssignment() {
    Node assign = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    when(assign.getType()).thenReturn(Token.ASSIGN);
    when(assign.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.THIS);
    boolean result = new CheckGlobalThis(null, null).shouldTraverse(null, rhs, assign);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionOnRightSideOfAssignment() {
    Node assign = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    when(assign.getType()).thenReturn(Token.ASSIGN);
    when(assign.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.THIS);
    when(rhs.getType()).thenReturn(Token.FUNCTION);
    boolean result = new CheckGlobalThis(null, null).shouldTraverse(null, rhs, assign);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assign = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    when(assign.getType()).thenReturn(Token.ASSIGN);
    when(assign.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.THIS);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(lhs, assign);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node getProp = mock(Node.class);
    Node parent = mock(Node.class);
    when(getProp.getType()).thenReturn(Token.GETPROP);
    when(getProp.getLastChild()).thenReturn(mock(Node.class));
    when(parent.getType()).thenReturn(Token.THIS);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(getProp, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldNotReportThisOnPrototypePropertyAccess() {
    Node getProp = mock(Node.class);
    Node parent = mock(Node.class);
    Node prototype = mock(Node.class);
    when(getProp.getType()).thenReturn(Token.GETPROP);
    when(getProp.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(getProp, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldNotReportThisOnFunctionWithNoParent() {
    Node node = mock(Node.class);
    Node parent = null;
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThisOnFunctionWithPropertyAccessParent() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.GETPROP);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisOnFunctionWithFunctionParent() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.FUNCTION);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisOnFunctionWithVariableParent() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.VAR);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisOnFunctionWithObjectLiteralParent() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.OBJECT);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldNotReportThisOnFunctionAnnotatedWithConstructor() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldNotReportThisOnFunctionAnnotatedWithThis() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldNotReportThisOnFunctionAnnotatedWithOverride() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldNotReportThisOnFunctionWithNoLogicalPlaceForAnnotation() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    boolean result = new CheckGlobalThis(null, null).shouldReportThis(node, parent);
    assertFalse(result);
  }

  @Test
  public void testVisitFunctionWithNoParent() {
    Node node = mock(Node.class);
    Node parent = null;
    new CheckGlobalThis(null, null).visit(null, node, parent);
  }

  @Test
  public void testVisitFunctionThatIsConstructor() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    new CheckGlobalThis(null, null).visit(null, node, parent);
  }

  @Test
  public void testVisitFunctionAnnotatedWithThis() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);
    new CheckGlobalThis(null, null).visit(null, node, parent);
  }

  @Test
  public void testVisitFunctionAnnotatedWithOverride() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);
    new CheckGlobalThis(null, null).visit(null, node, parent);
  }

  @Test
  public void testVisitFunctionWithNoLogicalPlaceForAnnotation() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    new CheckGlobalThis(null, null).visit(null, node, parent);
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithNoJSDocInfo() {
    Node node = mock(Node.class);
    Node parent = null;
    JSDocInfo result = new CheckGlobalThis(null, null).getFunctionJsDocInfo(node);
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithJSDocInfo() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    JSDocInfo result = new CheckGlobalThis(null, null).getFunctionJsDocInfo(node);
    assertEquals(jsDoc, result);
  }

}