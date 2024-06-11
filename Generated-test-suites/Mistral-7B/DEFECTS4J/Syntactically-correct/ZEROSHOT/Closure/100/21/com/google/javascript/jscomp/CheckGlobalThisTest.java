import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = new CompilerOptions();
    traversal = new NodeTraversal(compiler, options);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    Node node = Node.newFunction(Node.EMPTY_ARGS, null, null, null);
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node node = Node.newFunction(Node.EMPTY_ARGS, null, Node.CONSTRUCTOR, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = createJsDocInfoWithThisAnnotation();
    Node node = Node.newFunction(Node.EMPTY_ARGS, jsDoc, null, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnyAnnotation() {
    Node node = Node.newFunction(Node.EMPTY_ARGS, null, null, null);
    assertFalse(node.hasThisAnnotation());
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotationButNoThisKeyword() {
    JSDocInfo jsDoc = createJsDocInfoWithThisAnnotation();
    Node node = Node.newFunction(Node.EMPTY_ARGS, jsDoc, null, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    Node parent = Node.newAssign(Node.newThis(), Node.newName("useful"));
    Node child = Node.newName("unknown");
    Node node = Node.newAssign(parent, child);
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototype() {
    Node parent = Node.newAssign(Node.newGetprop(Node.newThis(), "prototype"), Node.newName("property"));
    Node child = Node.newName("unknown");
    Node node = Node.newAssign(parent, child);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseAssignmentToPrototypeDeep() {
    Node parent = Node.newAssign(
        Node.newGetprop(Node.newGetprop(Node.newThis(), "prototype"), "prototype"), Node.newName("property"));
    Node child = Node.newName("unknown");
    Node node = Node.newAssign(parent, child);
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node parent = Node.newAssign(Node.newThis(), Node.newName("property"));
    Node node = Node.newAssign(parent, Node.newName("value"));
    when(compiler.report(Mockito.any(NodeTraversal.ErrorReporter.class),
        Mockito.eq(CheckLevel.WARNING), Mockito.eq(DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenAnswer(new Answer<Void>() {
          @Override
          public Void answer(InvocationOnMock invocation) throws Throwable {
            // Do nothing
            return null;
          }
        });
    checker.visit(traversal, node, parent);
    assertTrue(compiler.reportWasCalledOnce());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node parent = Node.newAssign(Node.newThis(), Node.newName("property"));
    Node node = Node.newAssign(Node.newName("left"), parent);
    checker.visit(traversal, node, null);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionCall() {
    Node node = Node.newCall(Node.newName("functionWithThis"), Node.newArray(), Node.newThis());
    checker.visit(traversal, node, null);
    assertFalse(compiler.reportWasCalled());
  }

  private JSDocInfo createJsDocInfoWithThisAnnotation() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);
    return jsDoc;
  }
}