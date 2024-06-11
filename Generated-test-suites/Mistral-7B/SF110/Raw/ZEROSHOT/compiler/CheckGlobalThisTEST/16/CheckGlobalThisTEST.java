import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.Callback.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerPass checkGlobalThis = new CheckGlobalThis(new NoOpCompiler());

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("JSC_USED_GLOBAL_THIS", WarningLevel.ERROR);

    Parser parser = new Parser(null);
    SourceFile src = parser.parseJavaScript("function evil() { this.useful = undefined; }", null, null);

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticType getFactory() {
        return CheckerFactory.getInstance();
      }

      @Override
      public void process(SourceFile src, Node externs, boolean inCheck) {
        NodeTraversal traversal = new NodeTraversal(src, new NodeTraversal.RootVisitor(checkGlobalThis), null, null);
        checkGlobalThis.visit(traversal, null, null);
        traversal.traverse();
      }
    };

    CompilerPass compilerPass = new CompilerPass(checkGlobalThis, options);
    compilerPass.process(src, null, true);
    assertTrue(compiler.getDiagnostics().isEmpty());

    src.setTree(compilerPass.getProgramNode());
    Node n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.useful = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype.useful = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype.useful = this.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { var a = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.x = function() {}; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { var x = function() {}; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { var x = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.x = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype.x = this; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype.x = this.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { var a = {x: function() {}}; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.x = {useful: function() {}}; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.x = {useful: function() {}}.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.x = {useful: function() {}}.prototype.useful; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { function foo() {}}.prototype = this.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype = {foo: function() {}}; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype = {foo: function() {}}.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype = {foo: function() {}}.foo; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype = {foo: function() {}}.foo(); }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertFalse(CheckGlobalThis.shouldReportThis(n));

    src.setTree(parser.parseJavaScript("function evil() { this.prototype = {foo: function() {}}.prototype; }", null, null).getTree());
    n = src.getTree().findFirstDescendantOfType(Node.FUNCTION);
    assertTrue(CheckGlobalThis.shouldReportThis(n));
  }

  private static class NoOpCompiler extends AbstractCompiler {
    @Override
    public void process(SourceFile src, Node externs, boolean inCheck) {
      // NOOP
    }

    @Override
    public DiagnosticType getFactory() {
      return null;
    }
  }
}