import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsConstructor(true);

    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotated() {
    Node functionNode = new Node(Token.FUNCTION);

    JSDocInfo thisAnnotation = new JSDocInfo();
    thisAnnotation.setThisType(true);

    functionNode.setJSDocInfo(thisAnnotation);

    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = new Node(Token.FUNCTION);

    JSDocInfo overrideAnnotation = new JSDocInfo();
    overrideAnnotation.setOverride(true);

    functionNode.setJSDocInfo(overrideAnnotation);

    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    Node functionNode = new Node(Token.FUNCTION);

    JSDocInfo interfaceAnnotation = new JSDocInfo();
    interfaceAnnotation.setInterface(true);

    functionNode.setJSDocInfo(interfaceAnnotation);

    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parent = null;

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);

    Node prototypeProperty = new Node(Token.GETPROP);
    prototypeProperty.setString("prototype");
    prototypeProperty.setLastChild(new Node(Token.IDENT, "prototype"));

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(prototypeProperty);
    parent.addChild(functionNode);

    when(traversal.getCurrentNode()).thenReturn(prototypeProperty);
    when(traversal.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyAccessThroughChain() {
    Node functionNode = new Node(Token.FUNCTION);

    Node prototypeProperty = new Node(Token.GETPROP);
    prototypeProperty.setString("prototype");
    prototypeProperty.setLastChild(new Node(Token.IDENT, "prototype"));

    Node prototypePrototypeProperty = new Node(Token.GETPROP);
    prototypePrototypeProperty.setString("prototype");
    prototypePrototypeProperty.setLastChild(new Node(Token.IDENT, "prototype"));

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(prototypePrototypeProperty);
    parent.addChild(prototypeProperty);
    parent.addChild(functionNode);

    when(traversal.getCurrentNode()).thenReturn(prototypePrototypeProperty);
    when(traversal.getParent()).thenReturn(prototypeProperty);
    when(prototypeProperty.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParentOrSiblingAssignment() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parent = null;

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithSiblingAssignmentToVariable() {
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setLeftChild(new Node(Token.VAR, "a"));
    assignNode.setRightChild(new Node(Token.THIS));

    Node functionNode = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);
    parent.addChild(functionNode);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithSiblingAssignmentToObject() {
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setLeftChild(new Node(Token.GETPROP, new Node(Token.IDENT, "a"), "x"));
    assignNode.setRightChild(new Node(Token.THIS));

    Node functionNode = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);
    parent.addChild(functionNode);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldReportGlobalThisInFunction() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    Node thisNode = new Node(Token.THIS);

    Node globalThisNode = new Node(Token.THIS);
    parent.addChild(globalThisNode);
    parent.addChild(functionNode);
    functionNode.addChild(thisNode);

    when(traversal.getParent()).thenReturn(parent);

    Node previousNode = new Node(Token.IDENT, "previous");

    checker.visit(traversal, traversal.getParent(), null);
    checker.visit(traversal, functionNode, parent);
    checker.visit(traversal, thisNode, functionNode);

    assertTrue(compiler.wasCalledWithError(traversal, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypeMethod() {
    Node methodNode = new Node(Token.FUNCTION, Token.FUNCTION);
    methodNode.setIsMethod(true);

    Node prototypeNode = new Node(Token.GETPROP, new Node(Token.IDENT, "prototype"), methodNode);

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChild(methodNode);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(prototypeNode);
    parent.addChild(functionNode);

    Node thisNode = new Node(Token.THIS);

    checker.visit(traversal, traversal.getParent(), null);
    checker.visit(traversal, functionNode, parent);
    checker.visit(traversal, thisNode, functionNode);

    assertFalse(compiler.wasCalledWithError(traversal, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInConstructorFunction() {
    Node constructorNode = new Node(Token.FUNCTION, Token.FUNCTION);
    constructorNode.setIsConstructor(true);

    Node thisNode = new Node(Token.THIS);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(constructorNode);
    constructorNode.addChild(thisNode);

    checker.visit(traversal, traversal.getParent(), null);
    checker.visit(traversal, constructorNode, parent);
    checker.visit(traversal, thisNode, constructorNode);

    assertFalse(compiler.wasCalledWithError(traversal, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInFunctionAnnotatedWithThis() {
    Node functionNode = new Node(Token.FUNCTION);

    JSDocInfo thisAnnotation = new JSDocInfo();
    thisAnnotation.setThisType(true);

    functionNode.setJSDocInfo(thisAnnotation);

    Node thisNode = new Node(Token.THIS);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);
    functionNode.addChild(thisNode);

    checker.visit(traversal, traversal.getParent(), null);
    checker.visit(traversal, functionNode, parent);
    checker.visit(traversal, thisNode, functionNode);

    assertFalse(compiler.wasCalledWithError(traversal, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInFunctionWhereThisAnnotationCanBeAdded() {
    Node functionNode = new Node(Token.FUNCTION);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setLeftChild(new Node(Token.IDENT, "a"));
    assignNode.setRightChild(new Node(Token.FUNCTION, Token.FUNCTION));
    functionNode.addChild(assignNode);

    Node thisNode = new Node(Token.THIS);

    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);
    functionNode.addChild(thisNode);

    checker.visit(traversal, traversal.getParent(), null);
    checker.visit(traversal, functionNode, parent);
    checker.visit(traversal, thisNode, functionNode);

    assertFalse(compiler.wasCalledWithError(traversal, CheckGlobalThis.GLOBAL_THIS));
  }

  private void checkCondition(boolean expectedResult, Node n, Node parent) {
    boolean result = checker.shouldTraverse(traversal, n, parent);

    if (expectedResult) {
      assertTrue(result);
    } else {
      assertFalse(result);
    }
  }

  private void checkFunctionJsDocInfo(Node functionNode, JSDocInfo expectedJsDocInfo, Node expectedParent) {
    JSDocInfo actualJsDocInfo = functionNode.getJSDocInfo();
    Node actualParent = functionNode.getParent();

    assertEquals(expectedJsDocInfo, actualJsDocInfo);
    assertEquals(expectedParent, actualParent);
  }
}