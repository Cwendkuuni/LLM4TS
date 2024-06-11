import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SANKHOST);
    Checker checker = new Checker(options, new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.method = function() {}\n" +
        "function Bar() { this.property = 42; }\n" +
        "var Baz = function() {}\n" +
        "Baz.prototype.property = function() {}\n";

    checker.checkSource(sourceCode, "TestFile.js");
    Node ast = checker.getSourceAST();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);

    assertTrue("No warnings expected", checker.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testUnsafeUseOfThisAsAssignmentLhs() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SANKHOST);
    Checker checker = new Checker(options, new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evil() { this.property = 42; }";

    checker.checkSource(sourceCode, "TestFile.js");
    Node ast = checker.getSourceAST();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);

    DiagnosticType expectedDiagnostic = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object"
    );

    assertTrue("One warning expected", checker.getDiagnostics().size() == 1);
    assertTrue("Unexpected diagnostic", checker.getDiagnostics().get(0).getType() == expectedDiagnostic);
  }

  @org.junit.Test
  public void testUnsafeUseOfThisAsPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SANKHOST);
    Checker checker = new Checker(options, new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evil() { return this.property; }";

    checker.checkSource(sourceCode, "TestFile.js");
    Node ast = checker.getSourceAST();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);

    DiagnosticType expectedDiagnostic = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object"
    );

    assertTrue("One warning expected", checker.getDiagnostics().size() == 1);
    assertTrue("Unexpected diagnostic", checker.getDiagnostics().get(0).getType() == expectedDiagnostic);
  }

  @org.junit.Test
  public void testSafeUseOfThisAsPrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SANKHOST);
    Checker checker = new Checker(options, new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.method = function() { this.property = 42; }";

    checker.checkSource(sourceCode, "TestFile.js");
    Node ast = checker.getSourceAST();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);

    assertTrue("No warnings expected", checker.getDiagnostics().isEmpty());
  }
}