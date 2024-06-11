import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.jscomp.CompilerOptions.CheckLevel.ERROR;
import static com.google.javascript.jscomp.CompilerOptions.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.setCompilerOptions(
        new CompilerOptions()
            .setExterns(true)
            .setWarningsAsErrors(false)
            .setErrorLevel(UNDEFINED_VAR_ERROR, ERROR)
            .setErrorLevel(VIOLATED_MODULE_DEP_ERROR, ERROR)
            .setErrorLevel(MISSING_MODULE_DEP_ERROR, WARNING)
            .setErrorLevel(NAME_REFERENCE_IN_EXTERNS_ERROR, WARNING)
            .setErrorLevel(UNDEFINED_EXTERN_VAR_ERROR, WARNING)
            .setErrorLevel(STRICT_MODULE_DEP_ERROR, WARNING)
    );
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testUndefinedVariableInLocalScope() {
    String code = "function foo() {" +
        "  var x;" +
        "  x = 1;" +
        "}";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);

    NodeTraversal.traverse(compiler, input, root, varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testUndefinedVariableInGlobalScope() {
    String code = "var x;";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));

    Scope globalScope = compiler.getGlobalScope();
    Scope.Var var = globalScope.getVar("x");
    assertNotNull("Variable x declared in globals", var);
  }

  @Test
  public void testUndefinedVariableInExterns() {
    String code = "//@externs\n" +
        "var x;";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));

    Scope globalScope = compiler.getGlobalScope();
    Scope.Var var = globalScope.getVar("x");
    assertNull("Variable x undefined in externs", var);
  }

  @Test
  public void testUndefinedVariableInExternsAfterDeclaration() {
    String code = "//@externs\n" +
        "var x;// Declare x\n" +
        "function foo() {" +
        "  var x;" + // Re-declare x
        "}";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));

    // The 'x' in the function 'foo' should be declared in the globals.
    Scope globalScope = compiler.getGlobalScope();
    Scope.Var var = globalScope.getVar("x");
    assertNotNull("Variable x declared in globals", var);
  }

  @Test
  public void testUndefinedVariableInExternsDuringTraversal() {
    String code = "//@externs\n" +
        "var x;// Declare x" +
        "\n" +
        "function foo() {" +
        "  var x;" + // Re-declare x
        "}";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);
    Node externs = input.getExterns();

    // Traverse the externs before the root to ensure 'x' is declared.
    NodeTraversal.traverse(compiler, externs, null);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));

    // The 'x' in the function 'foo' should be declared in the globals.
    Scope globalScope = compiler.getGlobalScope();
    Scope.Var var = globalScope.getVar("x");
    assertNotNull("Variable x declared in globals", var);
  }

  @Test
  public void testUndefinedVariableInExternsAfterSynthesizedDeclaration() {
    String code = "//@externs\n" +
        "var x;// Declare x" +
        "\n" +
        "function foo() {" +
        "  var x;" + // Re-declare x
        "}";

    input = new CompilerInput("test.js", code);

    // First traversal to populate 'varsToDeclareInExterns'.
    Node root = NodeOperations.parseJs(code);
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    // Second traversal to create synthesized externs.
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    assertFalse("No undefined variable error reported",
        input.getDiagnostics().hasError(UNDEFINED_VAR_ERROR));

    // The 'x' in the function 'foo' should be declared in the globals.
    Scope globalScope = compiler.getGlobalScope();
    Scope.Var var = globalScope.getVar("x");
    assertNotNull("Variable x declared in globals", var);
  }

  @Test
  public void testUndefinedVariableInExternsDuringSynthesizedDeclaration() {
    String code = "//@externs\n" +
        "var x;// Declare x" +
        "\n" +
        "function foo() {" +
        "  var x;" + // Re-declare x
        "}";

    input = new CompilerInput("test.js", code);

    Node root = NodeOperations.parseJs(code);

    // First traversal to populate 'varsToDeclareInExterns'.
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    // Second traversal to create synthesized externs.
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    // Third traversal to check if the 'x' was created.
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), new AbstractPostOrderCallback() {
          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.getType() == Token.VAR && n.getString().equals("x")) {
              assertNotNull("Variable 'x' found in the synthesized externs", n);
            }
          }
        });
  }

  @Test
  public void testModuleDependency() {
    String module1Code = "//@externs\n" +
        "function foo() {}" +
        "\n" +
        "export function bar() {}";

    String module2Code = "//@externs\n" +
        "import foo from './module1.js';" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}";

    input = new CompilerInput("module1.js", module1Code);
    CompilerInput module1Externs = input.getExterns();

    input = new CompilerInput("test.js", module2Code);
    Node root = NodeOperations.parseJs(module2Code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(module1Externs, root), varCheck);

    assertTrue("No module dependency error reported",
        input.getDiagnostics().isEmpty());
  }

  @Test
  public void testMissingModuleDependency() {
    String module1Code = "//@externs\n" +
        "function foo() {}" +
        "\n" +
        "export function bar() {}";

    String module2Code = "//@externs\n" +
        "import foo from './module1.js';" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}";

    input = new CompilerInput("module1.js", module1Code);
    CompilerInput module1Externs = input.getExterns();

    String missingModuleDependencyCode = "//@externs\n" +
        "import foo from 'module3.js';" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}";

    input = new CompilerInput("test.js", missingModuleDependencyCode);
    Node root = NodeOperations.parseJs(missingModuleDependencyCode);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(module1Externs, root), varCheck);

    assertEquals("One missing module dependency warning reported",
        1, input.getDiagnostics().getWarningCount(MISSING_MODULE_DEP_ERROR));
  }

  @Test
  public void testStrictModuleDependency() {
    String module1Code = "//@externs\n" +
        "function foo() {}" +
        "\n" +
        "export function bar() {}";

    String module2Code = "//@externs\n" +
        "import foo from './module1.js';" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}";

    input = new CompilerInput("module1.js", module1Code);
    CompilerInput module1Externs = input.getExterns();

    input = new CompilerInput("test.js", module2Code);
    Node root = NodeOperations.parseJs(module2Code);

    varCheck = new VarCheck(compiler, true);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(module1Externs, root), varCheck);

    assertFalse("No strict module dependency error reported",
        input.getDiagnostics().hasError(STRICT_MODULE_DEP_ERROR));

    String stricModuleDependencyCode = "//@externs\n" +
        "import foo from './module1.js';" + // Expected to fail due to STRICT_MODULE_DEP_ERROR
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}";

    input = new CompilerInput("test.js", stricModuleDependencyCode);
    root = NodeOperations.parseJs(stricModuleDependencyCode);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(module1Externs, root), varCheck);

    assertTrue("One strict module dependency error reported",
        input.getDiagnostics().hasError(STRICT_MODULE_DEP_ERROR));
  }

  @Test
  public void testStrictModuleDependencyWithCircularDependency() {
    String module1Code = "//@externs\n" +
        "function foo() {}" +
        "\n" +
        "export function bar() {}";

    String module2Code = "//@externs\n" +
        "import foo from './module1.js';" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" +
        "}" +
        "\n" +
        "export function qux() {}";

    String module3Code = "//@externs\n" +
        "import foo from './module1.js';" +
        "import qux from './module2.js';" +
        "\n" +
        "function quux() {" +
        "  var x = qux.qux();" +
        "  var y = foo.bar();" +
        "}" +
        "\n" +
        "export function quuz() {}";

    input = new CompilerInput("module1.js", module1Code);
    CompilerInput module1Externs = input.getExterns();

    input = new CompilerInput("module2.js", module2Code);
    Node module2Root = NodeOperations.parseJs(module2Code);

    input = new CompilerInput("module3.js", module3Code);
    Node module3Root = NodeOperations.parseJs(module3Code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(module1Externs, module2Root, module3Root), varCheck);

    assertTrue("No strict module dependency error reported",
        input.getDiagnostics().isEmpty());
  }

  @Test
  public void testNameReferenceInExterns() {
    String code = "//@externs\n" +
        "function foo() {}" +
        "\n" +
        "export function bar() {" +
        "  var x = foo.bar();" + // Name reference to 'foo' in 'bar'
        "}" +
        "\n" +
        "function baz() {" +
        "  var x = foo.bar();" + // Name reference to 'foo' in 'baz'
        "}" +
        "\n";

    input = new CompilerInput("test.js", code);
    Node root = NodeOperations.parseJs(code);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    // Only one warning reported for 'foo' name reference in 'bar'.
    assertEquals("One name reference warning reported",
        1, input.getDiagnostics().getWarningCount(NAME_REFERENCE_IN_EXTERNS_ERROR));
  }
}