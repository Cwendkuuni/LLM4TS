import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Compiler compiler = new Compiler();
    CheckerFactory checkerFactory = new CheckerFactory(compiler);
    CheckGlobalThis checker = checkerFactory.getCheckGlobalThis();

    String code = "function evil() { this.useful = undefined; }";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Diagnostics diagnostics = compiler.checkSource(new DiagnosticGroup(), sourceFile);

    assertTrue(diagnostics.getNumberOfErrors() == 0);

    code = "var a = function() { this.useful = undefined; }";
    sourceFile = new SourceFile("Test.js", code);
    diagnostics = compiler.checkSource(new DiagnosticGroup(), sourceFile);

    assertFalse(diagnostics.getNumberOfErrors() > 0);

    code = "function evil() { this.useful = undefined; } function safe() {}";
    sourceFile = new SourceFile("Test.js", code);
    diagnostics = compiler.checkSource(new DiagnosticGroup(), sourceFile);

    assertTrue(diagnostics.getNumberOfErrors() == 1);

    code = "function MyClass() {} MyClass.prototype.method = function() { this.useful = undefined; }";
    sourceFile = new SourceFile("Test.js", code);
    diagnostics = compiler.checkSource(new DiagnosticGroup(), sourceFile);

    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }
}