import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setCheckSideEffects(true);
    options.setWarningLevel(WARNING);
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), false);
  }

  @Test
  public void testSimpleStatement() {
    String source = "var x = 1;";
    Node ast = Parser.parseJavaScript(source, null, null);
    checkSideEffects.process(null, ast);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testVoidStatement() {
    String source = ";;";
    Node ast = Parser.parseJavaScript(source, null, null);
    checkSideEffects.process(null, ast);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStatementWithSideEffect() {
    String source = "var x = 1;\n" +
        "document.getElementById(' foo ').style.backgroundColor = 'red';";
    Node ast = Parser.parseJavaScript(source, null, null);
    checkSideEffects.process(null, ast);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStatementWithSimpleOperator() {
    String source = "var x = 1 + 1;";
    Node ast = Parser.parseJavaScript(source, null, null);
    checkSideEffects.process(null, ast);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStatementWithSimpleOperatorNotUsed() {
    String source = "var x = 1 + 1;";
    Node ast = Parser.parseJavaScript(source, null, null);
    Node utilityNode = new Node(Token.OP_ADD);
    Node nodeToReplace = ast.getFirstChild().getNext();
    nodeToReplace.getParent().replaceChild(nodeToReplace, utilityNode);
    checkSideEffects.process(null, ast);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStatementInsideBlock() {
    String source = "{\n" +
        "  var x = 1;\n" +
        "  document.getElementById(' foo ').style.backgroundColor = 'red';\n" +
        "}";
    Node ast = Parser.parseJavaScript(source, null, null);
    NodeTraversal.traverse(checkSideEffects, ast, null);
    checkSideEffects.protectSideEffects();
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testStatementInsideForLoop() {
    String source = "for (var i = 0; i < 10; i++) {\n" +
        "  document.getElementById(' foo ').style.backgroundColor = 'red';\n" +
        "}";
    Node ast = Parser.parseJavaScript(source, null, null);
    NodeTraversal.traverse(checkSideEffects, ast, null);
    checkSideEffects.protectSideEffects();
  }
}