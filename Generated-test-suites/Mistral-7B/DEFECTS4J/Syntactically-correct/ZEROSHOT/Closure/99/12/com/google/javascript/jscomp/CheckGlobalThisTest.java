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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test class for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeTraversal traversal;

  @InjectMocks
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    when(compiler.getTraversal(CheckLevel.WARNING)).thenReturn(traversal);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    // Arrange:
    Node context = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, null, context);

    // Assert:
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseGlobalContexts() {
    // Arrange:
    Node context = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, null, null);

    // Assert:
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctions() {
    // Arrange:
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo functionJsDoc = mock(JSDocInfo.class);
    when(functionJsDoc.isConstructor()).thenReturn(false);
    when(functionJsDoc.hasThisType()).thenReturn(false);
    when(functionJsDoc.isOverride()).thenReturn(false);
    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);
    Node parent = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    // Assert:
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    // Arrange:
    Node constructorFunctionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo constructorFunctionJsDoc = mock(JSDocInfo.class);
    when(constructorFunctionJsDoc.isConstructor()).thenReturn(true);
    when(constructorFunctionNode.getJSDocInfo()).thenReturn(constructorFunctionJsDoc);
    Node parent = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, constructorFunctionNode, parent);

    // Assert:
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithThis() {
    // Arrange:
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo functionJsDoc = mock(JSDocInfo.class);
    when(functionJsDoc.hasThisType()).thenReturn(true);
    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);
    Node parent = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    // Assert:
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithOverride() {
    // Arrange:
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo functionJsDoc = mock(JSDocInfo.class);
    when(functionJsDoc.isOverride()).thenReturn(true);
    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);
    Node parent = Node.newNode(Token.SCRIPT);

    // Act:
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    // Assert:
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionsWithoutParent() {
    // Arrange:
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node parent = null;

    // Act:
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);

    // Assert:
    assertFalse(result);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    // Arrange:
    Node parentAssignment = Node.newNode(Token.ASSIGN);
    Node leftSideAssignment = Node.newNode(Token.THIS);
    Node rightSideAssignment = Node.newNode(Token.NUM, 1);

    when(traversal.makeError(leftSideAssignment, checker.level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))
        .thenReturn(mock(Throwable.class));

    // Act:
    checker.visit(traversal, null, parentAssignment);
    checker.visit(traversal, leftSideAssignment, parentAssignment);

    // Assert:
    // No error should be thrown since leftSideAssignment is reported.
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    // Arrange:
    Node parentAssignment = Node.newNode(Token.ASSIGN);
    Node leftSideAssignment = Node.newNode(Token.NAME, "x");
    Node rightSideAssignment = Node.newNode(Token.THIS);

    when(traversal.makeError(rightSideAssignment, checker.level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))
        .thenAnswer(new Answer<Throwable>() {
          @Override
          public Throwable answer(InvocationOnMock invocation) throws Throwable {
            throw invocation.getArgumentAt(0, Throwable.class);
          }
        });

    // Act:
    checker.visit(traversal, null, parentAssignment);
    checker.visit(traversal, leftSideAssignment, parentAssignment);
    checker.visit(traversal, rightSideAssignment, parentAssignment);

    // Assert:
    // An error should be thrown since rightSideAssignment is not reported.
  }

  @Test
  public void testShouldNotReportThisOnPrototypePropertyAccess() {
    // Arrange:
    Node parentAssignment = Node.newNode(Token.ASSIGN);
    Node leftSideAssignment = Node.newNode(Token.THIS);
    Node prototypePropertyAccess = Node.newNode(Token.GETPROP, new Node[] { leftSideAssignment,
        Node.newNode(Token.IDENTIFIER, "prototype") });

    when(traversal.makeError(prototypePropertyAccess, checker.level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))
        .thenAnswer(new Answer<Throwable>() {
          @Override
          public Throwable answer(InvocationOnMock invocation) throws Throwable {
            throw invocation.getArgumentAt(0, Throwable.class);
          }
        });

    // Act:
    checker.visit(traversal, null, parentAssignment);
    checker.visit(traversal, leftSideAssignment, parentAssignment);
    checker.visit(traversal, prototypePropertyAccess, parentAssignment);

    // Assert:
    // An error should be thrown since prototypePropertyAccess is not reported.
  }

  @Test
  public void testShouldReportThisOnPrototypeSubPropertyAccess() {
    // Arrange:
    Node parentAssignment = Node.newNode(Token.ASSIGN);
    Node leftSideAssignment = Node.newNode(Token.THIS);
    Node prototypePropertyAccess = Node.newNode(Token.GETPROP, new Node[] { leftSideAssignment,
        Node.newNode(Token.IDENTIFIER, "prototype"), Node.newNode(Token.IDENTIFIER, "subProperty") });

    when(traversal.makeError(prototypePropertyAccess, checker.level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))
        .thenAnswer(new Answer<Throwable>() {
          @Override
          public Throwable answer(InvocationOnMock invocation) throws Throwable {
            throw invocation.getArgumentAt(0, Throwable.class);
          }
        });

    // Act:
    checker.visit(traversal, null, parentAssignment);
    checker.visit(traversal, leftSideAssignment, parentAssignment);
    checker.visit(traversal, prototypePropertyAccess, parentAssignment);

    // Assert:
    // An error should be thrown since prototypePropertyAccess is not reported.
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToPrototypeProperty() {
    // Arrange:
    Node parentAssignment = Node.newNode(Token.ASSIGN);
    Node leftSideAssignment = Node.newNode(Token.THIS);
    Node prototypePropertyAssignment = Node.newNode(Token.ASSIGN, new Node[] { Node.newNode(Token.GETPROP,
        new Node[] { leftSideAssignment, Node.newNode(Token.IDENTIFIER, "prototype") }), leftSideAssignment,
        Node.newNode(Token.NUM, 1) });

    // Act:
    checker.visit(traversal, null, parentAssignment);
    checker.visit(traversal, leftSideAssignment, parentAssignment);
    checker.visit(traversal, prototypePropertyAssignment, parentAssignment);

    // Assert:
    // No error should be thrown since leftSideAssignment is not reported.
  }
}