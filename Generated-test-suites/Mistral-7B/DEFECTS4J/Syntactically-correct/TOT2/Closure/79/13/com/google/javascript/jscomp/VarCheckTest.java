import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassStatus;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CommonCodes;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSFunction;
import com.google.javascript.jscomp.JSScope;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class VarCheckTest {

  private static CompilerPassManager cpManager;
  private static AbstractCompiler compiler;
  private static CompilerOptions options;
  private static ErrorReporter errorReporter;

  @org.junit.BeforeClass
  public static void setup() {
    cpManager = new CompilerPassManager();
    compiler = new AbstractCompiler() {
      @Override
      public void report(DiagnosticType diagnosticType, int line, int column, String message,
          Object... args) {
        errorReporter.report(diagnosticType, line, column, message, args);
      }
    };
    options = new CompilerOptions();
    errorReporter = new ErrorReporter() {};
  }

  @org.junit.Test
  public void testBasicFunctionDeclaration() {
    String code = "function foo() {}";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.OK, statuses.get(0).getStatus());
    assertTrue(result.isEmpty());
  }

  @org.junit.Test
  public void testBasicVariableDeclaration() {
    String code = "var x = 5;";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.OK, statuses.get(0).getStatus());
    assertTrue(result.isEmpty());
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    String code = "var x = 5;\nvar y = x;";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.ERROR, statuses.get(0).getStatus());
    List<JSError> errors = result.getErrors();
    assertEquals(1, errors.size());
    JSError error = errors.get(0);
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getDiagnosticType().getName());
    assertEquals("x is undefined", error.getMessage());
  }

  @org.junit.Test
  public void testDuplicateVariableDeclaration() {
    String code = "function foo() {\nvar x = 5;\nvar x = 10;\n}";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.ERROR, statuses.get(0).getStatus());
    List<JSError> errors = result.getErrors();
    assertEquals(1, errors.size());
    JSError error = errors.get(0);
    assertEquals("JSC_DUPLICATE_VAR_DECL", error.getDiagnosticType().getName());
    assertEquals("Duplicate variable declaration for 'x'", error.getMessage());
  }

  @org.junit.Test
  public void testModuleDependency() {
    String codeModule1 = "var x = module.exports.y;";
    String codeModule2 = "exports.y = 5;";
    String code = "var x = require('./module2');";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(
            Arrays.asList(pass),
            new String[] {"module1.js", "module2.js", "app.js"},
            options,
            null,
            result);
    assertEquals(CompilerPassStatus.ERROR, statuses.get(0).getStatus());
    List<JSError> errors = result.getErrors();
    assertEquals(1, errors.size());
    JSError error = errors.get(0);
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", error.getDiagnosticType().getName());
    assertEquals(
        "module 'module1.js' cannot reference 'module2.js', defined in "
            + "module 'app.js', since 'app.js' loads after 'module2.js'",
        error.getMessage());
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    String codeModule1 = "var x = module.exports.y;";
    String codeModule2 = "exports.y = 5;";
    String code = "var x = require('./module2');";
    CompilerPass pass = new VarCheck(compiler, true);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(
            Arrays.asList(pass),
            new String[] {"module1.js", "module2.js", "app.js"},
            options,
            null,
            result);
    assertEquals(CompilerPassStatus.SKIPPED, statuses.get(0).getStatus());
    assertTrue(result.isEmpty());
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    String codeModule1 = "var x = module.exports.y;";
    String codeModule2 = "exports.y = 5;";
    String code = "var x = require('./module2');";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(
            Arrays.asList(pass),
            new String[] {"module1.js", "app.js"},
            options,
            null,
            result);
    assertEquals(CompilerPassStatus.WARNING, statuses.get(0).getStatus());
    List<JSError> warnings = result.getWarnings();
    assertEquals(1, warnings.size());
    JSError warning = warnings.get(0);
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", warning.getDiagnosticType().getName());
    assertEquals(
        "missing module dependency; module 'app.js' should depend "
            + "on module 'module2.js' because it references 'module2.js'",
        warning.getMessage());
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    String code = "function() {}";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.ERROR, statuses.get(0).getStatus());
    List<JSError> errors = result.getErrors();
    assertEquals(1, errors.size());
    JSError error = errors.get(0);
    assertEquals("JSC_INVALID_FUNCTION_DECL", error.getDiagnosticType().getName());
    assertEquals("Syntax error: function declaration must have a name", error.getMessage());
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    String code = "var x = exports.y;";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.WARNING, statuses.get(0).getStatus());
    List<JSError> warnings = result.getWarnings();
    assertEquals(1, warnings.size());
    JSError warning = warnings.get(0);
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", warning.getDiagnosticType().getName());
    assertEquals("accessing name 'y' in externs has no effect", warning.getMessage());
  }

  @org.junit.Test
  public void testUndefinedExternalVariable() {
    String code = "var x = exports.y;";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.WARNING, statuses.get(0).getStatus());
    List<JSError> warnings = result.getWarnings();
    assertEquals(1, warnings.size());
    JSError warning = warnings.get(0);
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", warning.getDiagnosticType().getName());
    assertEquals("name 'y' is not undefined in the externs.", warning.getMessage());
  }

  @org.junit.Test
  public void testConstantVariableDeclaration() {
    String code = "const x = 5;";
    CompilerPass pass = new VarCheck(compiler);
    Diagnostics result = new Diagnostics();
    List<CompilerPassStatus> statuses =
        cpManager.runPasses(Arrays.asList(pass), code, options, null, result);
    assertEquals(CompilerPassStatus.OK, statuses.get(0).getStatus());
    assertTrue(result.isEmpty());
  }
}