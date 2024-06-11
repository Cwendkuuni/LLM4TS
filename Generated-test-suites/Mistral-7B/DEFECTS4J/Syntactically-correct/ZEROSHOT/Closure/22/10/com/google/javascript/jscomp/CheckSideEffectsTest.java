import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.IR;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    nodeTraversal = new NodeTraversal(compiler, null);
  }

  @Test
  public void testNoSideEffectWarning() {
    Node root = createNode("var x = 1 + 2;");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), messageCaptor.capture()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(1, nodeCaptor.getAllValues().size());
    Node node = nodeCaptor.getValue();
    assertEquals(Token.VAR, node.getType());
    assertEquals(node.getLastChild(), nodeCaptor.getValue());

    assertEquals(CheckLevel.WARNING, levelCaptor.getValue());
    assertEquals(DiagnosticType.warning, diagnosticTypeCaptor.getValue());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", messageCaptor.getValue());
  }

  @Test
  public void testSimpleOperatorWarning() {
    Node root = createNode("1 + 2 + 3;");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), messageCaptor.capture()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(1, nodeCaptor.getAllValues().size());
    Node node = nodeCaptor.getValue();
    assertEquals(Token.PLUS, node.getType());
    assertEquals(node.getNext(), nodeCaptor.getValue());

    assertEquals(CheckLevel.WARNING, levelCaptor.getValue());
    assertEquals(DiagnosticType.warning, diagnosticTypeCaptor.getValue());
    assertEquals("The result of the '+' operator is not being used.", messageCaptor.getValue());
  }

  @Test
  public void testStringConcatenationWarning() {
    Node root = createNode("var s = 'this string is " + ' ' + "continued on the next line but you forgot the +';");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), messageCaptor.capture()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(1, nodeCaptor.getAllValues().size());
    Node node = nodeCaptor.getValue();
    assertEquals(Token.VAR, node.getType());
    assertEquals(node.getLastChild(), node.getLastChild().getLastChild());
    assertEquals(node.getLastChild().getLastChild().getNext(), nodeCaptor.getValue());

    assertEquals(CheckLevel.WARNING, levelCaptor.getValue());
    assertEquals(DiagnosticType.warning, diagnosticTypeCaptor.getValue());
    assertEquals("Is there a missing '+' on the previous line?", messageCaptor.getValue());
  }

  @Test
  public void testNoWarningForVoidExpression() {
    Node root = createNode(";");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), anyString()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(0, nodeCaptor.getAllValues().size());
  }

  @Test
  public void testNoWarningForCallExpressionWithEval() {
    Node root = createNode("eval('');");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), anyString()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(0, nodeCaptor.getAllValues().size());
  }

  @Test
  public void testNoWarningForForStatement() {
    Node root = createNode("for (var i = 0; i < 10; i++) {}");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), anyString()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(0, nodeCaptor.getAllValues().size());
  }

  @Test
  public void testNoWarningForStatementInsideBlock() {
    Node root = createNode("if (true) {} var x = 1;");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), anyString()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(0, nodeCaptor.getAllValues().size());
  }

  @Test
  public void testNoWarningForStatementAfterExpressionResult() {
    Node root = createNode("var x = 1; x;");
    checkSideEffects.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture(), anyString()))
        .thenReturn(null);

    checkSideEffects.visit(nodeTraversal, null, root);

    assertEquals(0, nodeCaptor.getAllValues().size());
  }

  private Node createNode(String jsCode) {
    CompilerInput input = new CompilerInput();
    Node node = IR.parseTree(input, jsCode, null, null);
    return node;
  }

  private static DiagnosticType anyDiagnosticType() {
    return ArgumentCaptor.forClass(DiagnosticType.class).capture();
  }
}