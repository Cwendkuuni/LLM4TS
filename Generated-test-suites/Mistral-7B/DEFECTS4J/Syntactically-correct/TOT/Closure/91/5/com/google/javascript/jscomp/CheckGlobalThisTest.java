import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestBase;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest extends TestBase {

  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = ""
        + "function evil() { this.useful = undefined; }"
        + "function safe() { return this.useful; }"
        + "function safeConstructor() {}"
        + "function safePrototype() { Object.defineProperty(this, 'useful', {value: undefined}); }"
        + "function safeFunctionWithThisAnnotation() { @this 'Global'; }"
        + "function safeFunctionWithNoThisAnnotation() { }"
        + "function safeFunctionWithPrototypeAssignment() { function p() { this.useful = undefined; } }"
        + "var obj = {useful: undefined}; function objFunction() {}";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for safe cases", compiler.hasErrors());
    assertTrue("One warning should be produced for the unsafe case", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  public void testCheckGlobalThisWithNoCompiler() {
    String jsCode = ""
        + "function evil() { this.useful = undefined; }"
        + "function safe() { return this.useful; }"
        + "function safeConstructor() {}"
        + "function safePrototype() { Object.defineProperty(this, 'useful', {value: undefined}); }"
        + "function safeFunctionWithThisAnnotation() { @this 'Global'; }"
        + "function safeFunctionWithNoThisAnnotation() { }"
        + "function safeFunctionWithPrototypeAssignment() { function p() { this.useful = undefined; } }"
        + "var obj = {useful: undefined}; function objFunction() {}";

    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeFactory nodeFactory = new NodeFactory();
    Node ast = nodeFactory.parse(jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, null);
    traversal.traverse(checker);

    assertFalse("No errors should be produced for safe cases", checker.hasErrors());
    assertTrue("One error should be produced for the unsafe case", checker.hasErrors());
  }

  public void testCheckGlobalThisWithAssignLhsChild() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = "var a = (function() { this.useful = undefined; })();";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for the safe case with assignLhsChild", compiler.hasErrors());
    assertTrue("One warning should be produced for the unsafe case with assignLhsChild", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  public void testCheckGlobalThisWithPrototypeAccess() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = "function prototypeFunction() { return this.prototype.useful; }";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for the safe case with prototype access", compiler.hasErrors());
    assertTrue("One warning should be produced for the unsafe case with prototype access", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  public void testCheckGlobalThisWithFunctionAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = "function @this('Global') unsafeFunction() { }";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for the safe case with function annotation", compiler.hasErrors());
    assertFalse("No warnings should be produced for the unsafe case with function annotation", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  public void testCheckGlobalThisWithNoFunctionAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = "function safeFunction() { }";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for the safe case with no function annotation", compiler.hasErrors());
    assertFalse("No warnings should be produced for the unsafe case with no function annotation", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  public void testCheckGlobalThisWithPrototypeAssignment() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, CheckLevel.WARNING);
    AbstractCompiler compiler = new AbstractCompiler(options, getSourceFile("CheckGlobalThisTest"));

    String jsCode = "var obj = {}; function objFunction() { Object.defineProperty(this, 'useful', {value: undefined}); }";

    Node ast = compile(jsCode, compiler);
    NodeTraversal traversal = new NodeTraversal(ast, compiler);
    traversal.traverse(new CheckGlobalThis(compiler, CheckLevel.WARNING));

    assertFalse("No warnings should be produced for the safe case with prototype assignment", compiler.hasErrors());
    assertFalse("No warnings should be produced for the unsafe case with prototype assignment", compiler.hasErrors(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }
}