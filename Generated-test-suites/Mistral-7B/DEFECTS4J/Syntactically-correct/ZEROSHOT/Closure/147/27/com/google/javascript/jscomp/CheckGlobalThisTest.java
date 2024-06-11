import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionAnnotatedWithConstructor = new Node(Token.FUNCTION);
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setIsConstructor(true);
    functionAnnotatedWithConstructor.setJSDocInfo(constructorJsDoc);

    Node parent = new Node(Token.SCRIPT);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionAnnotatedWithConstructor, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionAnnotatedWithThis = new Node(Token.FUNCTION);
    JSDocInfo thisJsDoc = new JSDocInfo();
    thisJsDoc.setHasThisType(true);
    functionAnnotatedWithThis.setJSDocInfo(thisJsDoc);

    Node parent = new Node(Token.SCRIPT);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionAnnotatedWithThis, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionAnnotatedWithInterface = new Node(Token.FUNCTION);
    JSDocInfo interfaceJsDoc = new JSDocInfo();
    interfaceJsDoc.setIsInterface(true);
    functionAnnotatedWithInterface.setJSDocInfo(interfaceJsDoc);

    Node parent = new Node(Token.SCRIPT);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionAnnotatedWithInterface, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionAnnotatedWithOverride = new Node(Token.FUNCTION);
    JSDocInfo overrideJsDoc = new JSDocInfo();
    overrideJsDoc.setIsOverride(true);
    functionAnnotatedWithOverride.setJSDocInfo(overrideJsDoc);

    Node parent = new Node(Token.SCRIPT);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionAnnotatedWithOverride, parent));
  }

  @Test
  public void testShouldTraverseFunctionsInsideScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionInsideScript = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionInsideScript, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionInsideBlock = new Node(Token.FUNCTION);

    Node parent = new Node(Token.BLOCK);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionInsideBlock, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsidePrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionInsidePrototypeProperty = new Node(Token.FUNCTION);

    Node parentPrototype = new Node(Token.GETPROP);
    parentPrototype.setLastChild(new Node("prototype"));
    Node parentPrototypeProperty = new Node(Token.GETPROP);
    parentPrototypeProperty.setLastChild(functionInsidePrototypeProperty);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToEnd(parentPrototypeProperty);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionInsidePrototypeProperty, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsidePrototypeSubProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionInsidePrototypeSubProperty = new Node(Token.FUNCTION);

    Node parentPrototype = new Node(Token.GETPROP);
    parentPrototype.setLastChild(new Node("prototype"));
    Node parentPrototypeProperty = new Node(Token.GETPROP);
    parentPrototypeProperty.setLastChild(new Node("property"));
    Node parentPrototypePropertySubProperty = new Node(Token.GETPROP);
    parentPrototypePropertySubProperty.setLastChild(functionInsidePrototypeSubProperty);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToEnd(parentPrototypePropertySubProperty);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionInsidePrototypeSubProperty, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node leftSideOfAssignment = new Node(Token.THIS);

    Node parentAssignment = new Node(Token.ASSIGN);
    parentAssignment.addChildToEnd(leftSideOfAssignment);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToEnd(parentAssignment);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldReportThis(leftSideOfAssignment, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node leftSideOfPropertyAccess = new Node(Token.THIS);

    Node parentPropertyAccess = new Node(Token.GETPROP);
    parentPropertyAccess.setLastChild(leftSideOfPropertyAccess);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToEnd(parentPropertyAccess);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);

    assertTrue(checker.shouldReportThis(leftSideOfPropertyAccess, parent));
  }
}