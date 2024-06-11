import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private ErrorCollector diagnostics;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    diagnostics = mock(ErrorCollector.class);
    when(compiler.getErrorCollector()).thenReturn(diagnostics);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(options, checker, null);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node functionNode = Mockito.mock(Node.class);
    Node constructorNode = Mockito.mock(Node.class);
    Node prototypeNode = Mockito.mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(constructorNode.getType()).thenReturn(Token.FUNCTION);
    when(constructorNode.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getString()).thenReturn("prototype");

    // Test function annotated with @constructor
    when(functionNode.getJSDocInfo()).thenReturn(createConstructorJsDocInfo(constructorNode));
    Node functionTraversal = new NodeTraversal.TraversalResult(functionNode);
    checker.visit(traversal, functionNode, null);
    Mockito.verify(diagnostics, Mockito.never()).report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    // Test prototype method
    when(functionNode.getJSDocInfo()).thenReturn(createMethodJsDocInfo(constructorNode));
    functionTraversal = new NodeTraversal.TraversalResult(functionNode);
    checker.visit(traversal, functionNode, prototypeNode);
    Mockito.verify(diagnostics, Mockito.never()).report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    // Test function annotated with @this
    when(functionNode.getJSDocInfo()).thenReturn(createThisJsDocInfo(functionNode));
    functionTraversal = new NodeTraversal.TraversalResult(functionNode);
    checker.visit(traversal, functionNode, null);
    Mockito.verify(diagnostics, Mockito.never()).report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    // Test function where there's no logical place to put a this annotation
    when(functionNode.getJSDocInfo()).thenReturn(null);
    Node assignmentNode = Mockito.mock(Node.class);
    Node lhs = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);
    when(assignmentNode.getType()).thenReturn(Token.ASSIGN);
    when(lhs.getType()).thenReturn(Token.NAME);
    when(lhs.getString()).thenReturn("a");
    when(rhs.getType()).thenReturn(Token.THIS);
    Node lhsTraversal = new NodeTraversal.TraversalResult(lhs);
    Node rhsTraversal = new NodeTraversal.TraversalResult(rhs);
    Node assignmentTraversal = new NodeTraversal.TraversalResult(assignmentNode);
    Node parentTraversal = new NodeTraversal.TraversalResult(functionNode);
    Node traversal = new NodeTraversal.TraversalResult(parentTraversal, lhsTraversal, assignmentTraversal);
    checker.visit(traversal, rhs, null);
    Mockito.verify(diagnostics, Mockito.never()).report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    Node functionNode = Mockito.mock(Node.class);
    Node assignmentNode = Mockito.mock(Node.class);
    Node lhs = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(assignmentNode.getType()).thenReturn(Token.ASSIGN);
    when(lhs.getType()).thenReturn(Token.NAME);
    when(lhs.getString()).thenReturn("this");
    when(rhs.getType()).thenReturn(Token.NUMBER);

    Node functionTraversal = new NodeTraversal.TraversalResult(functionNode);
    Node lhsTraversal = new NodeTraversal.TraversalResult(lhs);
    Node rhsTraversal = new NodeTraversal.TraversalResult(rhs);
    Node assignmentTraversal = new NodeTraversal.TraversalResult(assignmentNode);
    Node parentTraversal = new NodeTraversal.TraversalResult(functionNode);
    Node traversal = new NodeTraversal.TraversalResult(parentTraversal, lhsTraversal, assignmentTraversal);
    checker.visit(traversal, rhs, null);
    Mockito.verify(diagnostics).report(Mockito.eq(CheckGlobalThis.GLOBAL_THIS), Mockito.eq(functionNode), Mockito.eq(0), Mockito.eq("JSC_USED_GLOBAL_THIS"), Mockito.eq("dangerous use of the global 'this' object"), Mockito.anyString());
  }

  @Test
  public void testUnsafeUsesOfThisWithPropertyAccess() {
    Node functionNode = Mockito.mock(Node.class);
    Node assignmentNode = Mockito.mock(Node.class);
    Node lhs = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);
    Node getNode = Mockito.mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(assignmentNode.getType()).thenReturn(Token.ASSIGN);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(getNode);
    when(getNode.getString()).thenReturn("prototype");
    when(rhs.getType()).thenReturn(Token.NUMBER);

    Node functionTraversal = new NodeTraversal.TraversalResult(functionNode);
    Node lhsTraversal = new NodeTraversal.TraversalResult(lhs);
    Node rhsTraversal = new NodeTraversal.TraversalResult(rhs);
    Node getTraversal = new NodeTraversal.TraversalResult(getNode);
    Node assignmentTraversal = new NodeTraversal.TraversalResult(assignmentNode);
    Node parentTraversal = new NodeTraversal.TraversalResult(functionNode);
    Node traversal = new NodeTraversal.TraversalResult(parentTraversal, lhsTraversal, assignmentTraversal);
    checker.visit(traversal, rhs, null);
    Mockito.verify(diagnostics).report(Mockito.eq(CheckGlobalThis.GLOBAL_THIS), Mockito.eq(functionNode), Mockito.eq(0), Mockito.eq("JSC_USED_GLOBAL_THIS"), Mockito.eq("dangerous use of the global 'this' object"), Mockito.anyString());
  }

  private JSDocInfo createConstructorJsDocInfo(Node constructorNode) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    return jsDoc;
  }

  private JSDocInfo createMethodJsDocInfo(Node constructorNode) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setThis(createThisJsDocInfo(constructorNode));
    return jsDoc;
  }

  private JSDocInfo createThisJsDocInfo(Node node) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThis(true);
    return jsDoc;
  }
}