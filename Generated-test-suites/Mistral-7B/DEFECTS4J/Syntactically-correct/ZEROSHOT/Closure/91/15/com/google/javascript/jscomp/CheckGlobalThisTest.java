import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticType getDefaultDiagnosticsType() {
        return null;
      }
    };

    Node ast = ParserUtil.parseJsDocLess(jsCode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("One diagnostic expected", 1, numDiagnostics);

    DiagnosticType diagnostic = compiler.getDiagnosticsList().get(0);
    assertEquals("Warning diagnostic expected", DiagnosticType.WARNING, diagnostic.getType());
    assertEquals("JSC_USED_GLOBAL_THIS diagnostic expected", "JSC_USED_GLOBAL_THIS", diagnostic.getMessage());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String jsCode = "function EvilConstructor() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticType getDefaultDiagnosticsType() {
        return null;
      }
    };

    Node ast = ParserUtil.parseJsDocLess(jsCode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("No diagnostics expected", 0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String jsCode = "var EvilProto = function() {}; EvilProto.prototype.evil = function() { this.useful = undefined; };";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticType getDefaultDiagnosticsType() {
        return null;
      }
    };

    Node ast = ParserUtil.parseJsDocLess(jsCode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("No diagnostics expected", 0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoThisAnnotation() {
    String jsCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticType getDefaultDiagnosticsType() {
        return WARNING;
      }
    };

    Node ast = ParserUtil.parseJsDocLess(jsCode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("One diagnostic expected", 1, numDiagnostics);

    DiagnosticType diagnostic = compiler.getDiagnosticsList().get(0);
    assertEquals("Warning diagnostic expected", DiagnosticType.WARNING, diagnostic.getType());
    assertEquals("JSC_USED_GLOBAL_THIS diagnostic expected", "JSC_USED_GLOBAL_THIS", diagnostic.getMessage());
  }
}