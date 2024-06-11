import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CompilerOptions;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private CompilerOptions options;

  @Mock
  private NodeTraversal traversal;

  private CheckGlobalThis checker;

  @Before
  public void setup() {
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node node = Node.newFunctionNode(0, null, null, null, null, null, null, null);
    assertNotNull(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node node = Node.newFunctionNode(0, null, null, null, "constructor", null, null, null);
    when(compiler.getOptions()).thenReturn(options);
    when(options.getCheckLevel(CheckGlobalThis.class)).thenReturn(CheckLevel.ERROR);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);
    Node node = Node.newFunctionNode(0, null, null, null, null, null, null, null);
    node.setJSDocInfo(jsDoc);
    when(compiler.getOptions()).thenReturn(options);
    when(options.getCheckLevel(CheckGlobalThis.class)).thenReturn(CheckLevel.ERROR);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceToPutThis() {
    Node node = Node.newFunctionNode(0, null, null, null, null, null, null, null);
    Node parent = mock(Node.class);
    Node parentParent = mock(Node.class);
    when(parent.getParent()).thenReturn(parentParent);
    when(parentParent.getType()).thenReturn(Token.BLOCK);
    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node parent = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node left = Node.newThisNode(Token.THIS, null, null);
    Node right = Node.newStringLiteralNode("test");
    parent.setFirstChild(left);
    parent.setLastChild(right);

    Node node = Node.newIdentifierNode("a");
    when(compiler.getOptions()).thenReturn(options);
    when(options.getCheckLevel(CheckGlobalThis.class)).thenReturn(CheckLevel.ERROR);
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parent = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node left = Node.newThisNode(Token.THIS, null, null);
    Node right = Node.newStringLiteralNode("test");
    parent.setFirstChild(left);
    parent.setLastChild(right);

    Node node = Node.newIdentifierNode("a");
    when(compiler.report).thenAnswer(invocation -> {
      Node node = invocation.getArgument(0);
      DiagnosticType diagnosticType = invocation.getArgument(1);
      CheckLevel level = invocation.getArgument(2);
      assertNotNull(node);
      assertNotNull(diagnosticType);
      assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
      assertTrue(level.isWarning());
    });
    checker.visit(traversal, node, parent);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentWithPrototypeAccess() {
    Node parent = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node left = Node.newGetPropNode(Token.GETPROP, Node.newIdentifierNode("obj"), new Node[] { Node.newIdentifierNode("prototype") });
    Node right = Node.newStringLiteralNode("test");
    parent.setFirstChild(left);
    parent.setLastChild(right);

    Node node = Node.newIdentifierNode("a");
    checker.visit(traversal, node, parent);
    verify(compiler, never()).report(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypeAccessDeep() {
    Node parent = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node leftParent = Node.newGetPropNode(Token.GETPROP, Node.newIdentifierNode("obj"), new Node[] { Node.newIdentifierNode("prototype") });
    Node left = Node.newGetPropNode(Token.GETPROP, leftParent, new Node[] { new Node[] { Node.newIdentifierNode("subPrototype") } } );
    Node right = Node.newStringLiteralNode("test");
    parent.setFirstChild(left);
    parent.setLastChild(right);

    Node node = Node.newIdentifierNode("a");
    when(compiler.report).thenAnswer(invocation -> {
      Node node = invocation.getArgument(0);
      DiagnosticType diagnosticType = invocation.getArgument(1);
      CheckLevel level = invocation.getArgument(2);
      assertNotNull(node);
      assertNotNull(diagnosticType);
      assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
      assertTrue(level.isWarning());
    });
    checker.visit(traversal, node, parent);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parent = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node left = Node.newIdentifierNode("a");
    Node rightThis = Node.newThisNode(Token.THIS, null, null);
    Node right = Node.newIdentifierNode("test");
    parent.setFirstChild(left);
    parent.setLastChild(rightThis);

    Node node = Node.newIdentifierNode("a");
    checker.visit(traversal, node, parent);
    verify(compiler, never()).report(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class));
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccess() {
    Node parent = Node.newGetPropNode(Token.GETPROP, Node.newIdentifierNode("parent"), null);
    Node nodeThis = Node.newThisNode(Token.THIS, null, null);
    Node node = Node.newIdentifierNode("child");
    parent.setFirstChild(nodeThis);
    parent.setLastChild(node);

    checker.visit(traversal, node, parent);
    verify(compiler, never()).report(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class));
  }

  @Test
  public void testShouldReportThisOnPropertyAccessDeep() {
    Node parent = Node.newGetPropNode(Token.GETPROP, Node.newIdentifierNode("parent"), null);
    Node parentTHIS = Node.newGetPropNode(Token.GETPROP, Node.newIdentifierNode("parent"), null);
    Node leftParentThis = Node.newThisNode(Token.THIS, null, null);
    Node leftParent = Node.newIdentifierNode("prototype");
    Node left = Node.newGetPropNode(Token.GETPROP, leftParentThis, new Node[] { Node.newIdentifierNode("prototype") });
    Node node = Node.newIdentifierNode("child");
    parent.setFirstChild(leftParentThis);
    parent.setLastChild(parentTHIS);
    leftParentThis.setFirstChild(leftParent);
    leftParent.setFirstChild(left);
    parentTHIS.setLastChild(node);

    when(compiler.report).thenAnswer(invocation -> {
      Node node = invocation.getArgument(0);
      DiagnosticType diagnosticType = invocation.getArgument(1);
      CheckLevel level = invocation.getArgument(2);
      assertNotNull(node);
      assertNotNull(diagnosticType);
      assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
      assertTrue(level.isWarning());
    });
    checker.visit(traversal, node, parent);
  }
}