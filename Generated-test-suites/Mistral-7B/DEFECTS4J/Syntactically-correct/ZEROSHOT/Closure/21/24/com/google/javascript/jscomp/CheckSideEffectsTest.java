import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions(Checker.Type.ECMASCRIPT5);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function foo() {}"
        + "var s = \"this string is \"
          + \"continued on the next line but you forgot the +\";"
        + "x == foo();"
        + "foo();;"
        + "function bar() {}"
        + "for (var i = 0; i < 10; i++) {"
        + "  x == foo();"
        + "}";

    Node root = NodeFactory.createRoot(sourceCode);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue(checkSideEffects.problemNodes.isEmpty());

    compiler.process(root, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertTrue(numDiagnostics > 0);

    for (int i = 0; i < numDiagnostics; i++) {
      Node diagnosticNode = compiler.getDiagnostics().get(i).getSourceNode();
      Node expressionNode = diagnosticNode.getFirstChild();
      assertFalse(expressionNode.isQualifiedName());
      assertFalse(expressionNode.isCall());
      assertFalse(expressionNode.isExprResult());
      assertFalse(expressionNode.isBlock());
      int parentType = expressionNode.getParent().getType();
      if (parentType != Token.COMMA) {
        assertTrue(parentType == Token.EXPR_RESULT || parentType == Token.BLOCK);
      }
    }

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);
    compiler.process(root, null);

    numDiagnostics = compiler.getDiagnostics().size();
    assertTrue(numDiagnostics == 0);
  }
}